package pl.szymanski.springfrontend;

import java.util.Locale;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import pl.szymanski.springfrontend.oauth2.RestTemplateInterceptor;

@SpringBootApplication
@EnableScheduling
public class SpringFrontendApplication extends SpringBootServletInitializer {

  Locale poland = new Locale("pl", "PL");

  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver slr = new SessionLocaleResolver();
    slr.setDefaultLocale(poland);
    return slr;
  }

  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Bean
  public LocalValidatorFactoryBean getValidator() {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource());
    return bean;
  }

	@Bean
	public RestTemplate restTemplate(OAuth2AuthorizedClientService clientService) {
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.getInterceptors().add(restTemplateInterceptor(clientService));
      return restTemplate;
	}


    @Bean public RestTemplateInterceptor restTemplateInterceptor(OAuth2AuthorizedClientService clientService) {
      return new RestTemplateInterceptor(clientService);
    }


  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(SpringFrontendApplication.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringFrontendApplication.class, args);
  }

}
