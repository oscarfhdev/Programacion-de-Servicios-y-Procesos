package psp.af5.Gestion_Empleados.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import psp.af5.Gestion_Empleados.modelo.Empleado;

// Interfaz que se encarga de hablar con la base de datos
// Al extender JpaRepository ya nos da los metodos tipicos: buscar, guardar, borrar, etc.
// No hace falta escribir SQL ni nada, Spring lo hace solo
@Repository
public interface EmpleadoRepositorio extends JpaRepository<Empleado, Long> {
    // No necesitamos poner nada aqui,
    // JpaRepository ya trae findAll(), findById(), save() y deleteById()
}
