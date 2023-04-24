package ru.skypro.homework;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@CrossOrigin(value = "http://localhost:3000")
public class WebSecurityConfig {

  private final UserDetailsService userDetailsService;
  private static final String[] AUTH_WHITELIST = {
          "/swagger-resources/**",
          "/swagger-ui.html",
          "/v3/api-docs",
          "/webjars/**",
          "/login",
          "/register"
  };

  public WebSecurityConfig(@Qualifier("userDetailsServiceImp")UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf()
            .disable()
            .authorizeHttpRequests(
                    (authorization) ->
                            authorization
                                    .mvcMatchers(AUTH_WHITELIST)
                                    .permitAll()
                                    .mvcMatchers("/ads/**", "/users/**")
                                    .authenticated())
            .cors()
            .disable()
            .httpBasic(withDefaults())
            .userDetailsService(userDetailsService);
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
