package cr.ac.una.presentation_layer.Views;

import cr.ac.una.Main;
import cr.ac.una.domain_layer.Doctor;
import cr.ac.una.presentation_layer.Controller.DoctorController;
import cr.ac.una.presentation_layer.Model.DoctorTableModel;
import cr.ac.una.service_layer.DoctorService;
import cr.ac.una.service_layer.IService;
import cr.ac.una.utilities.FileManagement;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class PanelPrincipal extends JFrame {
    private JPanel PanelBase;
    private JTabbedPane PanelTabs;

    public PanelPrincipal() {
        IService<Doctor> doctorservice = new DoctorService(FileManagement.getDoctoresFileStore("doctores.xml"));
        DoctorController doctorcontroller = new DoctorController(doctorservice);
        DoctorTableModel doctormodel = new DoctorTableModel();
        DoctorView doctorview = new DoctorView(doctorcontroller, doctormodel, doctorcontroller.leerTodos());
        doctorservice.addObserver(doctormodel);

        Dictionary<String, JPanel> tabs = new Hashtable<>();
        tabs.put("Doctores", doctorview.getPanelBase());
    }

    private void initializePrincipalPanel() {
        // Configure main window
        setTitle("Sistema de Gestión Hospitalaria");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(PanelBase);
        setVisible(true);
    }
}

