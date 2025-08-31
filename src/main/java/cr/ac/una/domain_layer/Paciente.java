package cr.ac.una.domain_layer;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "paciente")
@XmlAccessorType(XmlAccessType.FIELD)
public class Paciente {
    
    @XmlAttribute(name = "ID")
    private int ID;
    
    @XmlAttribute(name = "nombre")
    private String nombre;
    
    @XmlAttribute(name = "apellido")
    private String apellido;
    
    @XmlAttribute(name = "fechaNacimiento")
    private String fechaNacimiento;
    
    @XmlAttribute(name = "telefono")
    private String telefono;

    // Constructores
    public Paciente() {}
    
    public Paciente(int ID, String nombre, String apellido, String fechaNacimiento, String telefono) {
        this.ID = ID;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paciente)) return false;
        Paciente paciente = (Paciente) o;
        return ID == paciente.ID;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "ID=" + ID +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
