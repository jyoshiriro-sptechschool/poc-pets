package prosper.pets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Profile({"dev","hml"})
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class UsuariosConfig {

    @Bean
    public UserDetailsService criarUsuarios() {
        UserDetails usuario1 = User.builder()
                .username("usuario1")
                .password("{noop}s1")
                .roles("usuario")
                .build();

        UserDetails usuario2 = User.builder()
                .username("usuario2")
                .password("{noop}s2")
                .roles("usuario")
                .build();

        UserDetails usuario3 = User.builder()
                .username("admin")
                .password("{noop}admin")
                .roles("admin")
                .build();

        UserDetails usuario4 = User.builder()
                .username("hacker")
                .password("{noop}hacker")
                .roles("hacker")
                .build();

        return new InMemoryUserDetailsManager(usuario1, usuario2, usuario3, usuario4);
    }

}
