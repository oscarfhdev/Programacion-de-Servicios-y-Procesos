package psp.af5.Gestion_Empleados.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import psp.af5.Gestion_Empleados.modelo.Empleado;
import psp.af5.Gestion_Empleados.repositorio.EmpleadoRepositorio;

// Servicio que contiene la logica de negocio
// Se situa entre el controlador y el repositorio
// Asi separamos las responsabilidades de cada capa
@Service
public class EmpleadoServicio {

    // Inyectamos el repositorio para poder acceder a la base de datos
    private final EmpleadoRepositorio empleadoRepositorio;

    // Spring se encarga de pasarnos el repositorio automaticamente por el
    // constructor
    public EmpleadoServicio(EmpleadoRepositorio empleadoRepositorio) {
        this.empleadoRepositorio = empleadoRepositorio;
    }

    // Devuelve todos los empleados que hay en la base de datos
    public List<Empleado> listarTodos() {
        return empleadoRepositorio.findAll();
    }

    // Busca un empleado por su id, devuelve Optional por si no existe
    public Optional<Empleado> buscarPorId(Long id) {
        return empleadoRepositorio.findById(id);
    }

    // Guarda un empleado nuevo o actualiza uno existente
    public Empleado guardar(Empleado empleado) {
        return empleadoRepositorio.save(empleado);
    }

    // Elimina un empleado por su id
    public void eliminar(Long id) {
        empleadoRepositorio.deleteById(id);
    }
}
