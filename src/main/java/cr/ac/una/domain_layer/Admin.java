package cr.ac.una.domain_layer;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "admin")
public class Admin {
    
    @XmlAttribute(name = "ID")
    private int ID;
    
    @XmlAttribute(name = "clave")
    private String clave;

    public Admin() {}

    public Admin(int _id, String _clave) {
        this.ID = _id;
        this.clave = _clave;
    }
    
    // Getters y Setters
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public String getClave() {
        return clave;
    }
    public void setClave(String clave) {
        this.clave = clave;
    }

}
