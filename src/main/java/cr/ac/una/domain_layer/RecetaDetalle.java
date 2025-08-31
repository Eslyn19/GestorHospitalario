package cr.ac.una.domain_layer;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class RecetaDetalle {
    @XmlAttribute
    private String codigo; // codigo del medicamento

    @XmlAttribute
    private Integer cantidad; // cantidad prescrita

    public RecetaDetalle() {}

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public Integer getCantidad() { return cantidad == null ? 0 : cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
