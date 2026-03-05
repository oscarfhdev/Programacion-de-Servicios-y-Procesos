package psp.af5.Gestion_Empleados.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// Clase de configuracion de Spring Security
// Aqui definimos los usuarios, las contrasenas cifradas y los permisos de cada rol
@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    // Creamos el bean para cifrar contrasenas con BCrypt
    // Asi las contrasenas nunca se guardan en texto plano
    @Bean
    public PasswordEncoder codificadorContrasenas() {
        return new BCryptPasswordEncoder();
    }

    // Creamos los dos usuarios en memoria con sus roles
    // Las contrasenas se cifran con el codificador de arriba
    @Bean
    public UserDetailsService servicioDetallesUsuario(PasswordEncoder codificadorContrasenas) {

        // Usuario administrador: tiene acceso a todo
        UserDetails admin = User.builder()
                .username("admin")
                .password(codificadorContrasenas.encode("admin123"))
                .roles("ADMIN")
                .build();

        // Usuario normal: solo puede consultar (GET)
        UserDetails user = User.builder()
                .username("user")
                .password(codificadorContrasenas.encode("user123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    // Configuramos las reglas de seguridad de la aplicacion
    @Bean
    public SecurityFilterChain cadenaFiltrosSeguridad(HttpSecurity http) throws Exception {
        http
                // Desactivamos CSRF porque es una API REST y no usamos cookies de sesion
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Dejamos la consola H2 abierta para poder ver la base de datos
                        .requestMatchers("/h2-console/**").permitAll()

                        // Los GET los puede hacer cualquier usuario autenticado (USER o ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/empleados", "/api/empleados/**")
                        .hasAnyRole("USER", "ADMIN")

                        // Para crear, modificar o borrar hace falta ser ADMIN
                        // Si un USER lo intenta le salta un 403 Forbidden
                        .requestMatchers(HttpMethod.POST, "/api/empleados").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/empleados/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/empleados/**").hasRole("ADMIN")

                        // Todo lo demas requiere estar autenticado
                        .anyRequest().authenticated())

                // Usamos Basic Auth, el cliente manda usuario y contrasena en cada peticion
                .httpBasic(Customizer.withDefaults())

                // Esto es para que la consola H2 se pueda cargar bien en un iframe
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}
