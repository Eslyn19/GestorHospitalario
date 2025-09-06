package cr.ac.una.domain_layer;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "prescripcionMedicamento")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrescripcionMedicamento {
    private String medicamento;
    private int cantidad;
    private String indicaciones;
    private String duracion;

    public PrescripcionMedicamento() {
        medicamento = "";
        cantidad = 0;
        indicaciones = "";
        duracion = "";
    }

    public PrescripcionMedicamento(String _medicamento, int _cantidad, String _indicaciones, String _duracion) {
        this.medicamento = _medicamento;
        this.cantidad = _cantidad;
        this.indicaciones = _indicaciones;
        this.duracion = _duracion;
    }

    // Setters & getters
    public String getMedicamento() {
        return medicamento;
    }
    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getIndicaciones() {
        return indicaciones;
    }
    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public String getDuracion() {
        return duracion;
    }
    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    @Override
    public String toString() {
        return "PrescripcionMedicamento{" +
                "medicamento='" + medicamento + '\'' +
                ", cantidad=" + cantidad +
                ", indicaciones='" + indicaciones + '\'' +
                ", duracion='" + duracion + '\'' +
                '}';
    }
}
