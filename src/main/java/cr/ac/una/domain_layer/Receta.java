package cr.ac.una.domain_layer;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import cr.ac.una.utilities.LocalDateAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "receta")
@XmlAccessorType(XmlAccessType.FIELD)
public class Receta {
    private String id;
    private String paciente;
    private String medico;

    @XmlElementWrapper(name = "prescripciones")
    @XmlElement(name = "prescripcion")
    private List<PrescripcionMedicamento> prescripciones;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fechaConfeccion;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fechaRetiro;
    private String estado;

    public Receta() {
        this.prescripciones = new ArrayList<>();
    }

    public Receta(String id, String paciente, String medico, List<PrescripcionMedicamento> prescripciones, 
                  LocalDate fechaConfeccion, LocalDate fechaRetiro, String estado) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.prescripciones = prescripciones != null ? prescripciones : new ArrayList<>();
        this.fechaConfeccion = fechaConfeccion;
        this.fechaRetiro = fechaRetiro;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPaciente() {
        return paciente;
    }
    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getMedico() {
        return medico;
    }
    public void setMedico(String medico) {
        this.medico = medico;
    }

    public List<PrescripcionMedicamento> getPrescripciones() {
        return prescripciones;
    }
    public void setPrescripciones(List<PrescripcionMedicamento> prescripciones) {
        this.prescripciones = prescripciones != null ? prescripciones : new ArrayList<>();
    }

    public LocalDate getFechaConfeccion() {
        return fechaConfeccion;
    }
    public void setFechaConfeccion(LocalDate fechaConfeccion) {
        this.fechaConfeccion = fechaConfeccion;
    }

    public LocalDate getFechaRetiro() {
        return fechaRetiro;
    }
    public void setFechaRetiro(LocalDate fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public void agregarPrescripcion(PrescripcionMedicamento prescripcion) {
        if (prescripcion != null) {
            this.prescripciones.add(prescripcion);
        }
    }

    public void removerPrescripcion(PrescripcionMedicamento prescripcion) {
        this.prescripciones.remove(prescripcion);
    }

    @Override
    public String toString() {
        return "Receta{" +
                "id='" + id + '\'' +
                ", paciente='" + paciente + '\'' +
                ", medico='" + medico + '\'' +
                ", prescripciones=" + prescripciones.size() + " items" +
                ", fechaConfeccion=" + fechaConfeccion +
                ", fechaRetiro=" + fechaRetiro +
                ", estado='" + estado + '\'' +
                '}';
    }
}
