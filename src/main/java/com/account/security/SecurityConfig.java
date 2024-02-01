package com.account.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/api/**").hasRole("MANAGER")
            .antMatchers("/**").hasRole("CUSTOMER")
            .anyRequest().authenticated()
            .and()
            .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("Preeti")
            .password(passwordEncoder().encode("manager123"))
            .roles("MANAGER");

        auth.inMemoryAuthentication()
            .withUser("Niall")
            .password(passwordEncoder().encode("customer123"))
            .roles("CUSTOMER");
    }
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		//return NoOpPasswordEncoder.getInstance();
		return new BCryptPasswordEncoder(10);
	}
}
