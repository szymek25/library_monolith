package pl.szymanski.springfrontend.config;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import pl.szymanski.springfrontend.oauth2.CustomOAuth2AuthorizationCodeGrantRequestEntityConverter;

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

	private static final String GROUPS = "groups";
	private static final String REALM_ACCESS_CLAIM = "realm_access";
	private static final String ROLES_CLAIM = "roles";
	@Resource(name = "userService")
	private UserDetailsService userDetailsService;

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
				.antMatchers("/webjars/**", "/css/**", "/js/**", "/actuator/**").permitAll()
				.anyRequest().authenticated();

		http.oauth2ResourceServer().jwt();
		http.oauth2Login().tokenEndpoint().accessTokenResponseClient(customAccessTokenRequestClient())
				.and()
				.successHandler(myAuthenticationSuccessHandler())
				.failureUrl("/login?error=true")
				.and()
				.logout()
				.logoutUrl("/perform_logout")
				.deleteCookies("JSESSIONID");
	}

	@Bean
	public DefaultAuthorizationCodeTokenResponseClient customAccessTokenRequestClient() {
		DefaultAuthorizationCodeTokenResponseClient customAccessTokenRequestClient = new DefaultAuthorizationCodeTokenResponseClient();
		customAccessTokenRequestClient.setRequestEntityConverter(new CustomOAuth2AuthorizationCodeGrantRequestEntityConverter());
		return customAccessTokenRequestClient;
	}

//	@Bean
//	public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
//		return authorities -> {
//			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//			GrantedAuthority authority = authorities.iterator().next();
//			boolean isOidc = authority instanceof OidcUserAuthority;
//
//			if (isOidc) {
//				OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
//				ClaimAccessor userInfo = oidcUserAuthority.getUserInfo();
//
//				// Tokens can be configured to return roles under
//				// Groups or REALM ACCESS hence have to check both
//				if (userInfo.containsClaim(REALM_ACCESS_CLAIM)) {
//					Map<String, Object> realmAccess = userInfo.getClaimAsMap(REALM_ACCESS_CLAIM);
//					Collection<String> roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
//					mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
//				} else if (userInfo.containsClaim(GROUPS)) {
//					Collection<String> roles = (Collection<String>) userInfo.getClaimAsStringList(
//							GROUPS);
//					mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
//				}
//			} else {
//				OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;
//				Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();
//
//				if (userAttributes.containsKey(REALM_ACCESS_CLAIM)) {
//					Map<String, Object> realmAccess = (Map<String, Object>) userAttributes.get(
//							REALM_ACCESS_CLAIM);
//					Collection<String> roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
//					mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
//				}
//			}
//			return mappedAuthorities;
//		};
//	}
//
//	Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
//		return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(
//				Collectors.toList());
//	}
}
