package com.sa.appexamelaboratorio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll() // acesso liberado as paginas
                .requestMatchers("/admin/**").hasRole("ADMIN") // Apenas ADMIN pode acessar URLs começando com /admin
                .requestMatchers("/**").hasAnyRole("USER", "ADMIN") // Usuários e Admins podem acessar URLs de usuário
                .anyRequest().authenticated() // todas as outcasdaras paginas precisa de autenticação
        )
                .formLogin(form -> form
                        .loginPage("/login") // pagina de login setada
                        .usernameParameter("email")
                        .passwordParameter("senha")
                        .defaultSuccessUrl("/main", true) // redireciona para home após login feito
                        .failureUrl("/login?error=true")
                        .permitAll() // permite voltar pra login
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // url de logout
                        .logoutSuccessUrl("/login") // manda pra login depois do logout
                        .permitAll())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
