package cr.ac.una.domain_layer;


import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "receta")
@XmlAccessorType(XmlAccessType.FIELD)
public class Receta {
    @XmlAttribute
    private String id;

    @XmlAttribute
    private String fecha; // yyyy-MM-dd

    @XmlAttribute
    private String estado;

    // Wrapper <detalle> ... <medicamento .../> ...
    @XmlElementWrapper(name = "detalle")
    @XmlElement(name = "medicamento")
    private List<RecetaDetalle> detalle = new ArrayList<>();

    public Receta() {}

    // getters / setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<RecetaDetalle> getDetalle() { return detalle; }
    public void setDetalle(List<RecetaDetalle> detalle) { this.detalle = detalle; }
}