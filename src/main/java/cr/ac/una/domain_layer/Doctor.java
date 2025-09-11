package cr.ac.una.domain_layer;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "doctor")
@XmlAccessorType(XmlAccessType.FIELD)
public class Doctor extends Persona{
    @XmlAttribute(name = "especialidad")
    String Especialidad;

    // Constructores
    public Doctor() {
        super();
    }
    
    public Doctor(int _ID, String _nombre, String _apellido, String _clave, String _especialidad) {
        super(_ID, _nombre, _apellido, _clave);
        Especialidad = _especialidad;
    }

    // Setter & getter
    public String getEspecialidad() { return Especialidad; }
    public void setEspecialidad(String especialidad) { Especialidad = especialidad; }
}
