package com.example.demo.config;

import com.example.demo.security.UserLoginFailHandler;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserLoginFailHandler failHandler;

    private final MemberService memberService;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
//                .csrf().disable()
//                    .cors().disable()
//                        .headers().frameOptions().disable()
//
//                .and()
                    .authorizeRequests()
                        .antMatchers("/login/info/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                    .anyRequest().permitAll()

                .and()
                    .formLogin()
                        .loginPage("/login")
                        .loginProcessingUrl("/auth")
                        .defaultSuccessUrl("/")
                        .failureHandler(failHandler)

                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws  Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
