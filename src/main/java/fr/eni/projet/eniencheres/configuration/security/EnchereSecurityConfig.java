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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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
                    // Routes de ControllerUtilisateur
                    .requestMatchers(HttpMethod.GET, "/utilisateurs/register", "/utilisateurs/verify", "/utilisateurs/reset-password").permitAll()
                    .requestMatchers(HttpMethod.POST, "/utilisateurs/register", "/utilisateurs/reset-password").permitAll()
                    .requestMatchers(HttpMethod.GET, "/utilisateurs/reset-password/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/utilisateurs/reset-password/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/utilisateurs/{id}/anonymiser").authenticated()

                    // Routes de ControllerArticle
                    .requestMatchers(HttpMethod.GET, "/article/creer", "/article/details/**", "/article/{id}/edit").authenticated()
                    .requestMatchers(HttpMethod.POST, "/article/creer", "/article/{id}/edit", "/article/{id}/upload-image").authenticated()
                    .requestMatchers(HttpMethod.POST, "/article/encherir").authenticated()
                    .requestMatchers(HttpMethod.GET, "/article/{id}/delete", "/article/{id}/retrait").authenticated()

                    // Routes de ControllerAccueil
                    .requestMatchers(HttpMethod.GET, "/", "/search", "/mes-achats").authenticated()
                    .requestMatchers(HttpMethod.GET, "/").permitAll()

                    // Routes de AdminController
                    .requestMatchers("/admin/**").hasRole("ADMIN")

                    // Autoriser l'accès aux ressources statiques
                    .requestMatchers("/css/**", "/images/**").permitAll()

                    .anyRequest().authenticated();
                 /*   .anyRequest().permitAll();  */
        });

        http.formLogin(form ->{
            form.loginPage("/login").permitAll();
            form.defaultSuccessUrl("/login/success", true);
            form.failureUrl("/login?error=true");
            form.failureHandler(authenticationFailureHandler());
            form.permitAll();
        });

        http.logout(logout -> logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").permitAll()
                .permitAll()
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

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.sendRedirect("/login?error=true");
        };
    }
}
