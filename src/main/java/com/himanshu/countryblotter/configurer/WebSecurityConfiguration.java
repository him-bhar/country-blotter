package com.himanshu.countryblotter.configurer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
@Order(2)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private AuthenticationProvider authenticationProvider;

  public WebSecurityConfiguration(@Autowired AuthenticationProvider authenticationProvider) {
    super();
    this.authenticationProvider = authenticationProvider;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.regexMatcher("^(/(swagger|webjars|login|logout)).*")
          .authorizeRequests()
          .antMatchers("/swagger-ui.html").fullyAuthenticated()
          .antMatchers("/login").permitAll()
          .antMatchers("/logout").permitAll()
          .and().formLogin().and().csrf().disable();
  }
}
