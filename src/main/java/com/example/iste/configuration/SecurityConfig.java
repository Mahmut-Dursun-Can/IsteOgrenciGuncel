package com.example.iste.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
public class SecurityConfig {

    // Security configuration for user login (with /login-user)
    @Bean
    @Order(2)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/login-user", "/signup", "/public","/logo.png","/user-dashboard","/message-page","/api/random-word","/logout").authorizeHttpRequests(authz -> authz
                        .requestMatchers("/user-dashboard","/api/random-word").hasRole("USER")
                        .requestMatchers("/login-user", "/signup","/logo.png","/public").permitAll() // Public URLs
                        .anyRequest().authenticated()

                )
                .formLogin(login -> login
                        .loginPage("/login-user")
                        .usernameParameter("email") // Email as username
                        .passwordParameter("password") // Password field
                        .defaultSuccessUrl("/user-dashboard", true)
                        .failureUrl("/login-user?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // /logout URL'si ile çıkış yapacak
                        .logoutSuccessUrl("/login-user") // Çıkış başarılı olduktan sonra yönlendirecek URL
                        .invalidateHttpSession(true) // Oturumu geçersiz kılacak
                        .deleteCookies("JSESSIONID") // Çerezleri silmek için
                )  .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/logout") // Çıkış işlemi için CSRF'yi devre dışı bırakıyoruz
                );

        ;
        return http.build();
}

    // Security configuration for admin login (with /login-admin)
    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/login-admin","/logo.png", "/admin-dashboard").authorizeHttpRequests(authz -> authz
                        .requestMatchers("/admin-dashboard").hasRole("ADMIN")
                        .requestMatchers("/login-admin","/logo.png", "/public").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login-admin")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/admin-dashboard", true)
                        .failureUrl("/login-admin?error=true")
                        .permitAll()
                );


        return http.build();
    }


    // Bean for password encoder
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
