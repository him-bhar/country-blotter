package com.himanshu.countryblotter.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class InMemAuthenticationProvider implements AuthenticationProvider {
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    if (authentication.getPrincipal().equals("user") && authentication.getCredentials().equals("password")) {
      return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), null, null);
    }
    return null;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return true;
  }
}
