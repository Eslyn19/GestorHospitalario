package cr.ac.una.domain_layer;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "farmaceuta")
@XmlAccessorType(XmlAccessType.FIELD)
public class Farmaceuta extends Persona{

    public Farmaceuta(){ super (); }

    public Farmaceuta(int _id, String nombre, String apellido, String _clave){
        super(_id, nombre, apellido, _clave);
    }
}
