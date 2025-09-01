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
    
    public Admin(int ID, String clave) {
        this.ID = ID;
        this.clave = clave;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Admin)) return false;
        Admin admin = (Admin) o;
        return ID == admin.ID;
    }
    
    @Override
    public String toString() {
        return "Admin{" +
                "ID=" + ID +
                ", clave='" + clave + '\'' +
                '}';
    }
}
