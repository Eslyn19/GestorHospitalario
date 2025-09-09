package cr.ac.una;

import cr.ac.una.presentation_layer.Views.Registro;
import cr.ac.una.data_access_layer.AdminFileStore;
import cr.ac.una.service_layer.AdminService;
import cr.ac.una.service_layer.DoctorService;
import cr.ac.una.service_layer.FarmaceutaService;
import cr.ac.una.presentation_layer.Controller.AdminController;
import cr.ac.una.presentation_layer.Controller.DoctorController;
import cr.ac.una.presentation_layer.Controller.FarmaceutaController;
import cr.ac.una.utilities.FileManagement;

public class Main {
    public static void main(String[] args) {
        Registro registro = new Registro();

        // Configurar AdminController
        AdminFileStore adminFileStore = FileManagement.getAdminFileStore("admins.xml");
        AdminService adminService = new AdminService(adminFileStore);
        AdminController adminController = new AdminController(adminService);
        registro.setAdminController(adminController);

        // Configurar DoctorController
        DoctorService doctorService = new DoctorService(FileManagement.getDoctoresFileStore("doctores.xml"));
        DoctorController doctorController = new DoctorController(doctorService);
        registro.setDoctorController(doctorController);

        // Configurar FarmaceutaController
        FarmaceutaService farmaceutaService = new FarmaceutaService(FileManagement.getFarmaceutasFileStore("farmaceutas.xml"));
        FarmaceutaController farmaceutaController = new FarmaceutaController(farmaceutaService);
        registro.setFarmaceutaController(farmaceutaController);

        registro.setVisible(true);
    }
}