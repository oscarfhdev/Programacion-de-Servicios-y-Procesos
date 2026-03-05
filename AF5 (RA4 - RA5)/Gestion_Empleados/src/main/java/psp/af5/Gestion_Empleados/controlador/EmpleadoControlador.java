package psp.af5.Gestion_Empleados.controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import psp.af5.Gestion_Empleados.modelo.Empleado;
import psp.af5.Gestion_Empleados.servicio.EmpleadoServicio;

// Controlador REST que gestiona todas las peticiones de /api/empleados
// Aqui definimos los endpoints para listar, crear, actualizar y borrar empleados
@RestController
@RequestMapping("/api/empleados")
public class EmpleadoControlador {

    // Inyectamos el servicio para llamar a la logica de negocio
    private final EmpleadoServicio empleadoServicio;

    public EmpleadoControlador(EmpleadoServicio empleadoServicio) {
        this.empleadoServicio = empleadoServicio;
    }

    // GET /api/empleados -> devuelve la lista completa de empleados
    @GetMapping
    public ResponseEntity<List<Empleado>> listarTodos() {
        List<Empleado> empleados = empleadoServicio.listarTodos();
        return ResponseEntity.ok(empleados);
    }

    // GET /api/empleados/{id} -> busca un empleado concreto por su id
    // Si no existe devolvemos 404
    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtenerPorId(@PathVariable Long id) {
        return empleadoServicio.buscarPorId(id)
                .map(empleado -> ResponseEntity.ok(empleado))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/empleados -> crea un empleado nuevo
    // Usamos @Valid para que compruebe las validaciones de la entidad antes de
    // guardar
    // Si algun campo no cumple, salta el manejador de errores de abajo y devuelve
    // 400
    @PostMapping
    public ResponseEntity<Empleado> crear(@Valid @RequestBody Empleado empleado) {
        Empleado nuevoEmpleado = empleadoServicio.guardar(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEmpleado);
    }

    // PUT /api/empleados/{id} -> actualiza un empleado existente
    // Primero miramos si existe, si no devolvemos 404
    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizar(@PathVariable Long id,
            @Valid @RequestBody Empleado empleado) {
        return empleadoServicio.buscarPorId(id)
                .map(empleadoExistente -> {
                    empleado.setId(id);
                    Empleado actualizado = empleadoServicio.guardar(empleado);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/empleados/{id} -> elimina un empleado
    // Comprobamos que exista antes de borrarlo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return empleadoServicio.buscarPorId(id)
                .map(empleado -> {
                    empleadoServicio.eliminar(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Este metodo captura los errores de validacion
    // Cuando @Valid detecta que algun campo no cumple las reglas (@NotBlank,
    // @Positive, etc)
    // recogemos los errores y los devolvemos como un JSON con el campo y el mensaje
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> manejarErroresDeValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            errores.put(campo, mensaje);
        });
        return errores;
    }
}
