package cr.ac.una.presentation_layer.Views;

import cr.ac.una.data_access_layer.AdminFileStore;
import cr.ac.una.presentation_layer.Controller.AdminController;
import cr.ac.una.presentation_layer.Controller.DoctorController;
import cr.ac.una.presentation_layer.Controller.FarmaceutaController;
import cr.ac.una.service_layer.AdminService;
import cr.ac.una.service_layer.DoctorService;
import cr.ac.una.service_layer.FarmaceutaService;
import cr.ac.una.utilities.FileManagement;

import javax.swing.*;
import java.awt.*;

public class BannerView extends JPanel{
    private JPanel PanelBase;
    private JLabel FondoLabel;
    private JLabel TituloLabel;
    private JButton LogOutBTN;
    private JFrame parentWindow;

    public JPanel getPanel() { return PanelBase; }

    public BannerView(){
        LogOutBTN.setPreferredSize(new Dimension(30, 30));
        Salir();
    }

    public BannerView(JFrame parentWindow){
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
            // Admin
            AdminFileStore adminFileStore = FileManagement.getAdminFileStore("admins.xml");
            AdminService adminService = new AdminService(adminFileStore);
            AdminController adminController = new AdminController(adminService);
            registro.setAdminController(adminController);

            // Doctor
            DoctorService doctorService = new cr.ac.una.service_layer.DoctorService(cr.ac.una.utilities.FileManagement.getDoctoresFileStore("doctores.xml"));
            DoctorController doctorController = new DoctorController(doctorService);
            registro.setDoctorController(doctorController);

            // Farmaceuta
            FarmaceutaService farmaceutaService = new cr.ac.una.service_layer.FarmaceutaService(cr.ac.una.utilities.FileManagement.getFarmaceutasFileStore("farmaceutas.xml"));
            FarmaceutaController farmaceutaController = new cr.ac.una.presentation_layer.Controller.FarmaceutaController(farmaceutaService);
            registro.setFarmaceutaController(farmaceutaController);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
