package cr.ac.una.presentation_layer.Controller;

import cr.ac.una.domain_layer.Paciente;
import cr.ac.una.domain_layer.Receta;
import cr.ac.una.presentation_layer.Views.Despacho;
import cr.ac.una.presentation_layer.Model.DespachoTableModel;
import cr.ac.una.service_layer.PacienteService;
import cr.ac.una.service_layer.RecetaService;

import javax.swing.*;
import java.util.List;

public class DespachoController {
    private final Despacho vista;
    private final PacienteService pacienteService;
    private final RecetaService recetaService;
    private final DespachoTableModel despachoTableModel;
    private Paciente pacienteActual;
    private Receta recetaActual;

    public DespachoController(Despacho vista, PacienteService pacienteService, RecetaService recetaService, DespachoTableModel despachoTableModel) {
        String[] estados = {"Confeccionada", "En proceso", "Lista", "Entregada"};
        DefaultComboBoxModel<String> modeloEstados = new DefaultComboBoxModel<>(estados);
        vista.getEstadosCB().setModel(modeloEstados);

        this.vista = vista;
        this.pacienteService = pacienteService;
        this.recetaService = recetaService;
        this.despachoTableModel = despachoTableModel;

        vista.getBuscarPacienteBTN().addActionListener(e -> buscarPaciente());
        vista.getRecetasCB().addActionListener(e -> cargarEstadoReceta());
        vista.getActualizarRecetaBTN().addActionListener(e -> actualizarEstadoReceta());
        vista.getLimpiarBTN().addActionListener(e -> limpiarFormulario());
    }

    private void cargarEstadosDisponibles(String estadoActual) {
        DefaultComboBoxModel<String> modeloEstados = new DefaultComboBoxModel<>();
        
        switch (estadoActual) {
            case "Confeccionada":
                modeloEstados.addElement("Confeccionada");
                modeloEstados.addElement("En proceso");
                break;
                
            case "En proceso":
                modeloEstados.addElement("En proceso");
                modeloEstados.addElement("Lista");
                break;
                
            case "Lista":
                modeloEstados.addElement("Lista");
                modeloEstados.addElement("Entregada");
                break;
                
            case "Entregada":
                modeloEstados.addElement("Entregada");
                break;
                
            default:
                modeloEstados.addElement("Confeccionada");
                modeloEstados.addElement("En proceso");
                modeloEstados.addElement("Lista");
                modeloEstados.addElement("Entregada");
                break;
        }
        vista.getEstadosCB().setModel(modeloEstados);
    }

    private boolean esCambioEstadoValido(String estadoActual, String nuevoEstado) {
        if (estadoActual.equals(nuevoEstado)) {
            return true;
        }
        
        switch (estadoActual) {
            case "Confeccionada":
                return "En proceso".equals(nuevoEstado);
                
            case "En proceso":
                return "Lista".equals(nuevoEstado);
                
            case "Lista":
                return "Entregada".equals(nuevoEstado);
                
            case "Entregada":
                return false;
                
            default:
                return true;
        }
    }

    private void buscarPaciente() {
        try {
            String idTexto = vista.getBuscarPacienteTF().getText().trim();
            
            if (idTexto.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Por favor ingrese un ID de paciente", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idPaciente = Integer.parseInt(idTexto);
            pacienteActual = pacienteService.leerPorId(idPaciente);

            if (pacienteActual == null) {
                JOptionPane.showMessageDialog(vista, "No se encontró un paciente con el ID: " + idPaciente, "Paciente no encontrado", JOptionPane.WARNING_MESSAGE);
                limpiarRecetas();
                return;
            }

            cargarRecetasPaciente();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El ID debe ser un número válido", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al buscar paciente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarRecetasPaciente() {
        if (pacienteActual == null) return;

        // Buscar recetas por paciente
        String nombrePaciente = pacienteActual.getNombre() + " " + pacienteActual.getApellido();
        List<Receta> recetas = recetaService.leerPorPaciente(nombrePaciente);

        DefaultComboBoxModel<Receta> modeloRecetas = new DefaultComboBoxModel<>();
        
        if (recetas.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El paciente no tiene recetas registradas", "Sin recetas", JOptionPane.INFORMATION_MESSAGE);
        } else {
            java.time.LocalDate hoy = java.time.LocalDate.now();
            java.time.LocalDate fechaLimiteInferior = hoy.minusDays(3);
            java.time.LocalDate fechaLimiteSuperior = hoy.plusDays(3);
            
            for (Receta receta : recetas) {
                if ("Confeccionada".equals(receta.getEstado())) {
                    if (receta.getFechaRetiro() != null) {
                        if (receta.getFechaRetiro().isAfter(fechaLimiteInferior.minusDays(1)) && 
                            receta.getFechaRetiro().isBefore(fechaLimiteSuperior.plusDays(1))) {
                            modeloRecetas.addElement(receta);
                        }
                    } else {
                        modeloRecetas.addElement(receta);
                    }
                } else if ("En proceso".equals(receta.getEstado()) || "Lista".equals(receta.getEstado())) {
                    modeloRecetas.addElement(receta);
                }
            }
            
            if (modeloRecetas.getSize() == 0) {
                JOptionPane.showMessageDialog(vista, 
                    "El paciente no tiene recetas 'Confeccionadas' disponibles para despacho.\n" +
                    "Solo se pueden procesar recetas confeccionadas con fecha de retiro válida (hoy ± 3 días).", 
                    "Sin recetas disponibles", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        vista.getRecetasCB().setModel(modeloRecetas);
        vista.getRecetasCB().setSelectedIndex(-1);
    }

    private void cargarEstadoReceta() {
        Receta recetaSeleccionada = (Receta) vista.getRecetasCB().getSelectedItem();
        
        if (recetaSeleccionada == null) {
            vista.getEstadosCB().setSelectedIndex(-1);
            recetaActual = null;
            return;
        }

        recetaActual = recetaSeleccionada;
        String estadoActual = recetaActual.getEstado();
        
        cargarEstadosDisponibles(estadoActual);
        
        DefaultComboBoxModel<String> modeloEstados = (DefaultComboBoxModel<String>) vista.getEstadosCB().getModel();
        for (int i = 0; i < modeloEstados.getSize(); i++) {
            if (modeloEstados.getElementAt(i).equals(estadoActual)) {
                vista.getEstadosCB().setSelectedIndex(i);
                return;
            }
        }
        
        if (modeloEstados.getSize() > 0) {
            vista.getEstadosCB().setSelectedIndex(0);
        }
    }

    private void actualizarEstadoReceta() {
        if (recetaActual == null) {
            JOptionPane.showMessageDialog(vista, "Por favor seleccione una receta", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nuevoEstado = (String) vista.getEstadosCB().getSelectedItem();
        
        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor seleccione un estado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!esCambioEstadoValido(recetaActual.getEstado(), nuevoEstado)) {
            JOptionPane.showMessageDialog(vista, 
                "No se puede cambiar de '" + recetaActual.getEstado() + "' a '" + nuevoEstado + "'.\n" +
                "El flujo de despacho debe seguir: Confeccionada → En proceso → Lista → Entregada", 
                "Cambio de estado inválido", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            recetaService.cambiarEstado(recetaActual.getId(), nuevoEstado);
            
            recetaActual.setEstado(nuevoEstado);
            
            cargarEstadosDisponibles(nuevoEstado);
            vista.getEstadosCB().setSelectedItem(nuevoEstado);
            
            JOptionPane.showMessageDialog(vista, "Estado de la receta actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al actualizar el estado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        vista.getBuscarPacienteTF().setText("");
        limpiarRecetas();
        vista.getEstadosCB().setSelectedIndex(-1);
        pacienteActual = null;
        recetaActual = null;
    }

    private void limpiarRecetas() {
        DefaultComboBoxModel<Receta> modeloVacio = new DefaultComboBoxModel<>();
        vista.getRecetasCB().setModel(modeloVacio);
        vista.getRecetasCB().setSelectedIndex(-1);
    }
}
