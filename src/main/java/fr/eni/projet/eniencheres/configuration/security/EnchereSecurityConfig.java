package fr.eni.projet.eniencheres.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity // Activer le débogage de sécurité
public class EnchereSecurityConfig {
    // Configuration de sécurité pour la filmothèque
    private static final String SELECT_USER = "SELECT pseudo AS username, mot_de_passe AS password, 1 AS enabled " +
            "FROM UTILISATEURS WHERE pseudo = ?";
    private static final String SELECT_ROLES = "SELECT pseudo AS username, CASE WHEN administrateur = 1 " +
            "THEN 'ROLE_ADMIN' ELSE 'ROLE_USER' END AS authority FROM utilisateurs WHERE pseudo = ?";

    @Bean
    UserDetailsManager userDetailsManager(DataSource dataSource)  {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(SELECT_USER);
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(SELECT_ROLES);

        return jdbcUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, HttpSecurity httpSecurity) throws Exception {

        http.authorizeHttpRequests(auth -> {
            auth
                   /* .requestMatchers("/").permitAll()
                    .requestMatchers("/css/**").permitAll()
                    .requestMatchers("/images/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/utilisateurs").permitAll()
                    .requestMatchers(HttpMethod.GET, "/utilisateurs/register").permitAll()
                    .requestMatchers(HttpMethod.POST, "/utilisateurs/register").permitAll()
                    .requestMatchers(HttpMethod.GET,"/utilisateurs/reset-password").permitAll()
                    .requestMatchers(HttpMethod.POST, "/utilisateurs/reset-password").permitAll()
                    .requestMatchers(HttpMethod.GET, "/profil").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/article/{id}/upload-image").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/article/{id}/edit").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/article/{id}/edit").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST, "/utilisateur/{pseudo}/supprimer").hasRole("ADMIN")
                    .anyRequest().authenticated();*/
                    .anyRequest().permitAll();
        });

        http.formLogin(form ->{
            form.loginPage("/login").permitAll();
            form.defaultSuccessUrl("/login/success", true);
        });

        http.logout(logout -> logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").permitAll()
        );
        http.sessionManagement(session -> {
            session
                .invalidSessionUrl("/login/session-timeout")
                .maximumSessions(1)
                .expiredUrl("/login/session-timeout")
                .maxSessionsPreventsLogin(true);
        });


        return http.build();
    }
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
