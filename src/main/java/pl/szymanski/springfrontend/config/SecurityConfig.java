package pl.szymanski.springfrontend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import pl.szymanski.springfrontend.oauth2.CustomOAuth2AuthorizationCodeGrantRequestEntityConverter;
import pl.szymanski.springfrontend.oauth2.KeycloakLogoutHandler;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String ROLES = "roles";
	@Resource(name = "userService")
	private UserDetailsService userDetailsService;

	@Resource
	private KeycloakLogoutHandler keycloakLogoutHandler;

	@Value("${keycloak.clientId}")
	private String clientId;

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(encoder());
	}

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
		return new MySimpleUrlAuthenticationSuccessHandler();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/anonymous*").anonymous()
				.antMatchers("/webjars/**", "/css/**", "/js/**", "/actuator/**", "/").permitAll()
				.anyRequest().authenticated();

		http.oauth2ResourceServer().jwt();
		http.oauth2Login().tokenEndpoint().accessTokenResponseClient(customAccessTokenRequestClient())
				.and()
				.successHandler(myAuthenticationSuccessHandler())
				.failureUrl("/?error=true")
				.and()
				.logout().addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/")
				.logoutUrl("/perform_logout")
				.deleteCookies("JSESSIONID");
	}

	@Bean
	public DefaultAuthorizationCodeTokenResponseClient customAccessTokenRequestClient() {
		DefaultAuthorizationCodeTokenResponseClient customAccessTokenRequestClient = new DefaultAuthorizationCodeTokenResponseClient();
		customAccessTokenRequestClient.setRequestEntityConverter(customOAuth2AuthorizationCodeGrantRequestEntityConverter() );
		return customAccessTokenRequestClient;
	}

	@Bean
	public CustomOAuth2AuthorizationCodeGrantRequestEntityConverter customOAuth2AuthorizationCodeGrantRequestEntityConverter() {
		return new CustomOAuth2AuthorizationCodeGrantRequestEntityConverter(clientId);
	}
	@Bean
	public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
		return authorities -> {
			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
			GrantedAuthority authority = authorities.iterator().next();

				OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;
				Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

				if (userAttributes.containsKey(ROLES)) {
					Collection<String> roles = (Collection<String>) userAttributes.get(ROLES);
					mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
				}

			return mappedAuthorities;
		};
	}

	Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
		return roles.stream().map(SimpleGrantedAuthority::new).collect(
				Collectors.toList());
	}
}
