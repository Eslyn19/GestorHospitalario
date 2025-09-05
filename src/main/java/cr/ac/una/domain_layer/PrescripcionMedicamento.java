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
    }

    public PrescripcionMedicamento(String medicamento, int cantidad, String indicaciones, String duracion) {
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.duracion = duracion;
    }

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
