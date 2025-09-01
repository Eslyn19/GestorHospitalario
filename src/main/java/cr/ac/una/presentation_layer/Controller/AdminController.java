package cr.ac.una.presentation_layer.Controller;

import cr.ac.una.domain_layer.Admin;
import cr.ac.una.service_layer.AdminService;

import java.util.List;

public class AdminController {
    
    private AdminService adminService;
    
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    
    public void agregar(Admin admin) {
        adminService.agregar(admin);
    }
    
    public void borrar(int id) {
        adminService.borrar(id);
    }
    
    public void actualizar(Admin admin) {
        adminService.actualizar(admin);
    }
    
    public List<Admin> leerTodos() {
        return adminService.leerTodos();
    }
    
    public Admin leerPorId(int id) {
        return adminService.leerPorId(id);
    }
    
    public boolean autenticar(int id, String clave) {
        return adminService.autenticar(id, clave);
    }
}
