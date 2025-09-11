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
    
    public Paciente(int _id, String _nombre, String _apellido, String _FN, String _telefono) {
        this.ID = _id;
        this.nombre = _nombre;
        this.apellido = _apellido;
        this.fechaNacimiento = _FN;
        this.telefono = _telefono;
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
}
