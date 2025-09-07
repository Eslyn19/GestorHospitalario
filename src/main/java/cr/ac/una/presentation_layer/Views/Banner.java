package cr.ac.una.presentation_layer.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Banner extends JPanel{
    private JPanel PanelBase;
    private JLabel FondoLabel;
    private JLabel TituloLabel;
    private JButton LogOutBTN;
    private JFrame parentWindow;

    public JPanel getPanel() { return PanelBase; }

    public Banner(){
        LogOutBTN.setPreferredSize(new Dimension(30, 30));
        Salir();
    }

    public Banner(JFrame parentWindow){
        this.parentWindow = parentWindow;
        Salir();
    }

    private void Salir() {
        LogOutBTN.addActionListener(e -> CerrarSeccion());
    }

    private void CerrarSeccion() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cerrar sesión?",
            "Cerrar Sesión",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            parentWindow.dispose();

            SwingUtilities.invokeLater(() -> {
                try {
                    Registro registro = new Registro();
                    ControladoresConfig(registro);
                    registro.setVisible(true);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                        "Error al abrir la ventana de login: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }
    
    private void ControladoresConfig(Registro registro) {
        try {
            // Configurar AdminController
            cr.ac.una.data_access_layer.AdminFileStore adminFileStore = cr.ac.una.utilities.FileManagement.getAdminFileStore("admins.xml");
            cr.ac.una.service_layer.AdminService adminService = new cr.ac.una.service_layer.AdminService(adminFileStore);
            cr.ac.una.presentation_layer.Controller.AdminController adminController = new cr.ac.una.presentation_layer.Controller.AdminController(adminService);
            registro.setAdminController(adminController);

            // Configurar DoctorController
            cr.ac.una.service_layer.DoctorService doctorService = new cr.ac.una.service_layer.DoctorService(cr.ac.una.utilities.FileManagement.getDoctoresFileStore("doctores.xml"));
            cr.ac.una.presentation_layer.Controller.DoctorController doctorController = new cr.ac.una.presentation_layer.Controller.DoctorController(doctorService);
            registro.setDoctorController(doctorController);

            // Configurar FarmaceutaController
            cr.ac.una.service_layer.FarmaceutaService farmaceutaService = new cr.ac.una.service_layer.FarmaceutaService(cr.ac.una.utilities.FileManagement.getFarmaceutasFileStore("farmaceutas.xml"));
            cr.ac.una.presentation_layer.Controller.FarmaceutaController farmaceutaController = new cr.ac.una.presentation_layer.Controller.FarmaceutaController(farmaceutaService);
            registro.setFarmaceutaController(farmaceutaController);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
