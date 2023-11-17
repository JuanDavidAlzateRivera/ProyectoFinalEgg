package ProyectoFinal.Final.controladores;

import ProyectoFinal.Final.entidades.Usuario;
import ProyectoFinal.Final.servicios.UsuarioService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/") //configura que url escuchara este controlador usando la / en este caso
public class PortalControlador {

    @Autowired
    UsuarioService usuServ;

    @GetMapping("/") //cuando en la url ponga la / se ejecutara este metodo en este caso, al principio apenas
    //abro la pagina
    public String index() {
        return "index.html";
    }

    @GetMapping("/listas")
    public String listas() {
        return "listas.html";
    }

    /*
    @GetMapping("/cliente/registrar")
    public String registrar() {
        return "registroCliente";
    }

     */
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Usuario o contraseña invalidos.");
        }
        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN' , 'ROLE_PROVEEDOR')")
    @GetMapping("/inicio")
    public String inicio(HttpSession sesion) {
        Usuario logeado = (Usuario) sesion.getAttribute("usuarioSesion");

        if (logeado.getRol().toString().equals("ADMIN")) {

            return "redirect:/admin/dashboard";
        }else if(logeado.getRol().toString().equals("PROVEEDOR")){
            return "redirect:/proveedor/inicio";
        }
        return "inicio.html"; //seria para el cliente
    }
    /*
    
    @PostMapping("/registro")
    public String registro(@RequestParam(required=false) String nombre,
            @RequestParam(required=false) String email,
            @RequestParam(required=false) String password,
            ModelMap modelo,
            MultipartFile archivo){
        
        try{
           usuServ.registrar(archivo, nombre, email, password);  
           modelo.put("exito", "Usuario registrado exitosamente.");
           
           return "index";
        }catch(miException e){
            modelo.put("error", e.getMessage());
            return "registro";
        }
    }
    
    */
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession sesion){
        Usuario user = (Usuario) sesion.getAttribute("usuarioSesion");
        
        modelo.put("user", user);
        
        return "usuario_Modificar";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @PostMapping("/perfil/{id}")
    public String actualizar(@PathVariable String id,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam Long dni,
            @RequestParam String correo,
             @RequestParam int telefono,
            @RequestParam String direccion,
            @RequestParam String password, ModelMap modelo){
        
        try {
            usuServ.actualizarUsuario(id,nombre,apellido,dni,correo,telefono,direccion,password);
            modelo.put("exito", "Usuario modificado correctamente.");
            return "inicio";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "usuario_Modificar";
        }
    }
     
}
