package com.usuario.service.controlador;

import com.usuario.service.entidades.Usuario;
import com.usuario.service.modelos.Carro;
import com.usuario.service.modelos.Moto;
import com.usuario.service.servicio.UsuarioService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        List<Usuario> usuarios = usuarioService.getAll();
        if(usuarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable("id") int id){
        Usuario usuario = usuarioService.getUsuarioById(id);
        if(usuario == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario){
        Usuario nuevoUsuario = usuarioService.save(usuario);
        return ResponseEntity.ok(usuario);
    }

    //INICIO DEL REST TEMPLATE
    @CircuitBreaker(name = "carrosCB",fallbackMethod = "fallBackGetCarros")
    @GetMapping("/carros/{usuarioId}")
    public ResponseEntity<List<Carro>> listarCarros(@PathVariable("usuarioId") int id){
        Usuario usuario = usuarioService.getUsuarioById(id);
        if(usuario == null){
            return ResponseEntity.notFound().build();
        }
        List<Carro> carros = usuarioService.getCarros(id);
        return ResponseEntity.ok(carros);
    }

    @CircuitBreaker(name = "motosCB",fallbackMethod = "fallBackGetMotos")
    @GetMapping("/motos/{usuarioId}")
    public ResponseEntity<List<Moto>> listarMotos(@PathVariable("usuarioId") int id){
        Usuario usuario = usuarioService.getUsuarioById(id);
        if(usuario == null){
            return ResponseEntity.notFound().build();
        }
        List<Moto> motos = usuarioService.getMotos(id);
        return ResponseEntity.ok(motos);
    }

//FIN DEL RESTEMPLATE

    //INICIO DEL FEIGN CARRO

    @CircuitBreaker(name = "carrosCB",fallbackMethod = "fallBackSaveCarro")
    @PostMapping("/carro/{usuarioId}")
    public ResponseEntity<Carro> guardarCarro(@PathVariable("usuarioId")int usuarioId,@RequestBody Carro carro){
        Carro nuevoCarro = usuarioService.saveCarro(usuarioId,carro);
        return ResponseEntity.ok(nuevoCarro);
    }


    //FIN DEL FEIGN CARRO

    //INICIO DEL FEIGN MOTO

    @CircuitBreaker(name = "motosCB",fallbackMethod = "fallBackSaveMoto")
    @PostMapping("/moto/{usuarioId}")
    public ResponseEntity<Moto> guardarMoto(@PathVariable("usuarioId")int usuarioId,@RequestBody Moto moto){
        Moto nuevaMoto = usuarioService.saveMoto(usuarioId,moto);
        return ResponseEntity.ok(nuevaMoto);
    }

    //FIN DEL FEIGN MOTO

    //INICIO FEIGN DE LA CONSULTA CONJUNTA DE MOTO Y CARRO
    //LISTADO DE AMBOS QUE PERTENECEN A ESE USUARIO

    @CircuitBreaker(name = "todosCB",fallbackMethod = "fallBackGetTodos")
    @GetMapping("/todos/{usuarioId}")
    public ResponseEntity<Map<String,Object>> listarTodosLosVehiculos(@PathVariable("usuarioId")int usuarioId){
        Map<String,Object> resultado = usuarioService.getUsuarioAndVehiculos(usuarioId);
        return ResponseEntity.ok(resultado);
    }

    //FIN MOTO CARRO LISTADO

    private ResponseEntity<List<Carro>> fallBackGetCarros(@PathVariable("usuarioId") int id,RuntimeException excepcion){
        return new ResponseEntity("El usuario : "+id+" tiene los carros en el taller", HttpStatus.OK);
    }

    private ResponseEntity<List<Carro>> fallBackSaveCarro(@PathVariable("usuarioId") int id,@RequestBody Carro carro,RuntimeException excepcion){
        return new ResponseEntity("El usuario : "+id+" no tiene dinero para los carros", HttpStatus.OK);
    }

    private ResponseEntity<List<Moto>> fallBackGetMotos(@PathVariable("usuarioId") int id,RuntimeException excepcion){
        return new ResponseEntity("El usuario : "+id+" tiene las motos en el taller", HttpStatus.OK);
    }

    private ResponseEntity<List<Moto>> fallBackSaveMoto(@PathVariable("usuarioId") int id,@RequestBody Moto moto,RuntimeException excepcion){
        return new ResponseEntity("El usuario : "+id+" no tiene dinero para las motos", HttpStatus.OK);
    }

    private ResponseEntity<Map<String,Object>> fallBackGetTodos(@PathVariable("usuarioId") int id,RuntimeException excepcion){
        return new ResponseEntity("El usuario : "+id+" tiene los VEHICULOS en el taller", HttpStatus.OK);
    }

}
