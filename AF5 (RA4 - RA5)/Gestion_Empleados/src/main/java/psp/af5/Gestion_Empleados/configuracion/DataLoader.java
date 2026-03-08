package psp.af5.Gestion_Empleados.configuracion;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import psp.af5.Gestion_Empleados.modelo.Empleado;
import psp.af5.Gestion_Empleados.repositorio.EmpleadoRepositorio;

// Clase para inicializar datos en la base de datos nada mas arrancar la aplicacion
@Configuration
public class DataLoader {

    // Este bean se ejecuta automaticamente al arrancar Spring Boot
    @Bean
    public CommandLineRunner cargarDatos(EmpleadoRepositorio empleadoRepositorio) {
        return args -> {
            // Solo insertamos datos si la base de datos esta vacia
            if (empleadoRepositorio.count() == 0) {
                System.out.println("Cargando empleados iniciales en la base de datos...");

                empleadoRepositorio.save(new Empleado("Ana Martínez", "Directora de RRHH", 55000.0));
                empleadoRepositorio.save(new Empleado("Luis Gómez", "Desarrollador Backend", 32000.0));
                empleadoRepositorio.save(new Empleado("Sofía Ruiz", "Diseñadora UX/UI", 28000.0));
                empleadoRepositorio.save(new Empleado("Carlos Pérez", "Administrador de Sistemas", 35000.0));

                System.out.println("¡Datos cargados correctamente!");
            } else {
                System.out.println("La base de datos ya contiene empleados. No se cargan datos iniciales.");
            }
        };
    }
}
