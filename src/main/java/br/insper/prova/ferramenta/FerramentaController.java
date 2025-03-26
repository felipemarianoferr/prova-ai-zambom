package br.insper.prova.ferramenta;

import br.insper.prova.usuario.Usuario;
import br.insper.prova.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/ferramentas")
public class FerramentaController {
    @Autowired
    private FerramentaRepository ferramentaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Ferramenta> criarFerramenta(
            @RequestBody Ferramenta ferramenta,
            @RequestHeader(name = "email") String email) {

        Usuario usuario = usuarioService.getUsuario(email);

        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        if (!usuario.getPapel().equalsIgnoreCase("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário sem permissão para criar");
        }

        ferramenta.setNomeUsuario(usuario.getNome());
        ferramenta.setEmailUsuario(usuario.getEmail());

        Ferramenta salva = ferramentaRepository.save(ferramenta);

        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFerramenta(
            @PathVariable String id,
            @RequestHeader(name = "email") String email) {

        Usuario usuario = usuarioService.getUsuario(email);

        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        if (!usuario.getPapel().equalsIgnoreCase("ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário sem permissão para deletar");
        }

        Ferramenta ferramenta = ferramentaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ferramenta não encontrada"));

        ferramentaRepository.delete(ferramenta);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Ferramenta>> listarFerramentas() {
        return ResponseEntity.ok(ferramentaRepository.findAll());
    }
}
