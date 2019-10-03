package com.baeldung.registration.configurations;

import com.baeldung.registration.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by uc on 10/2/2019
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(PUBLIC).permitAll()
            .anyRequest().authenticated();

        // not in production
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    private static final String[] PUBLIC = {
        "/",
        "/css/**",
        "/js/**",
        "/img/**",
        "/webjars/**",
        "/user/registration",
        "/h2-console/**"
    };
}
