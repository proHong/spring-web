package com.define.web.security;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Log
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    UsersService usersService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("security config...................");

        http
                .authorizeRequests()
                .antMatchers("/boards/list").hasAnyRole("BASIC","MANAGER","ADMIN")
                .antMatchers("/boards/register").hasAnyRole("MANAGER","ADMIN")
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(new LoginSuccessHandler())
                .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true);

        http.exceptionHandling().accessDeniedPage("/accessDenied");


        http.rememberMe().key("key").userDetailsService(usersService).tokenRepository(getJDBCRepository()).tokenValiditySeconds(60*60*24);
    }

    private PersistentTokenRepository getJDBCRepository(){
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(usersService).passwordEncoder(passwordEncoder());

    }

}