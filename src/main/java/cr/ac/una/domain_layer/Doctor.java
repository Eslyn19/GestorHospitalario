package cr.ac.una.domain_layer;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "doctor")
@XmlAccessorType(XmlAccessType.FIELD)
public class Doctor extends Persona{
    @XmlAttribute(name = "especialidad")
    String Especialidad;

    // Constructores
    public Doctor() {
        super();
    }
    
    public Doctor(int _ID, String _nombre, String _apellido, String _clave, String especialidad) {
        super(_ID, _nombre, _apellido, _clave);
        Especialidad = especialidad;
    }

    public Doctor(String especialidad) { 
        super();
        Especialidad = especialidad; 
    }

    // Setter & getter
    public String getEspecialidad() { return Especialidad; }
    public void setEspecialidad(String especialidad) { Especialidad = especialidad; }

    @Override
    public String toString() {
        return "Doctor{" +
                "Especialidad='" + Especialidad + '\'' +
                ", ID=" + getID() +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() +
                '}';
    }
}
