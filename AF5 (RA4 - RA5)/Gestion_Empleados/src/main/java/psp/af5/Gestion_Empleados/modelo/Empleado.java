package psp.af5.Gestion_Empleados.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

// Clase que representa un empleado en la base de datos
// Cada objeto Empleado se guarda como una fila en la tabla "empleados"
@Entity
@Table(name = "empleados")
public class Empleado {

    // Clave primaria, se genera sola al crear un empleado nuevo
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El nombre no puede estar vacio
    @NotBlank(message = "El nombre del empleado es obligatorio")
    private String nombre;

    // El puesto tampoco puede estar vacio
    @NotBlank(message = "El puesto del empleado es obligatorio")
    private String puesto;

    // El salario tiene que ser un numero positivo, no vale 0 ni negativos
    @Positive(message = "El salario debe ser un numero positivo mayor que cero")
    private Double salario;

    // Constructor vacio que necesita JPA para funcionar
    public Empleado() {
    }

    // Constructor con parametros para cuando queramos crear un empleado a mano
    public Empleado(String nombre, String puesto, Double salario) {
        this.nombre = nombre;
        this.puesto = puesto;
        this.salario = salario;
    }

    // Getters y setters de todos los campos

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }
}
