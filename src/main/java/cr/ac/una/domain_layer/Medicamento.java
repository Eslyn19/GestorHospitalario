package cr.ac.una.domain_layer;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "medicamento")
@XmlAccessorType(XmlAccessType.FIELD)
public class Medicamento {
    
    @XmlAttribute(name = "codigo")
    private String codigo;
    
    @XmlAttribute(name = "nombreMedic")
    private String nombreMedic;
    
    @XmlAttribute(name = "presentacion")
    private String presentacion;

    public Medicamento(){
        codigo = "";
        nombreMedic = "";
        presentacion = "";
    }

    public Medicamento(String _codigo, String _nombreMedic, String _presentacion) {
        this.codigo = _codigo;
        this.nombreMedic = _nombreMedic;
        this.presentacion = _presentacion;
    }

    // Setters & getters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombreMedic() { return nombreMedic; }
    public void setNombreMedic(String nombreMedic) { this.nombreMedic = nombreMedic; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }
}
