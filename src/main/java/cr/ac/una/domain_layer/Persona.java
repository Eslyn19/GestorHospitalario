package cr.ac.una.domain_layer;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Doctor.class, Farmaceuta.class, Paciente.class})
public abstract class Persona {

    @XmlAttribute(name = "ID")
    private int ID;

    @XmlAttribute(name = "nombre")
    private String nombre;

    @XmlAttribute(name = "apellido")
    private String apellido;

    @XmlAttribute(name = "clave")
    private String clave;

    protected Persona(int _ID, String _nombre, String _apellido, String _clave) {
        this.ID = _ID;
        this.nombre = _nombre;
        this.apellido = _apellido;
        this.clave = _clave;
    }

    // Setters & getters
    public Persona(){}
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getClave() {
        return clave;
    }
    public void setClave(String clave) {
        this.clave = clave;
    }
}