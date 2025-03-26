package br.insper.prova.controller;

import br.insper.prova.ferramenta.Ferramenta;
import br.insper.prova.ferramenta.FerramentaController;
import br.insper.prova.ferramenta.FerramentaRepository;
import br.insper.prova.usuario.Usuario;
import br.insper.prova.usuario.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FerramentaControllerTests {

    @InjectMocks
    private FerramentaController ferramentaController;

    @Mock
    private FerramentaRepository ferramentaRepository;

    @Mock
    private UsuarioService usuarioService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(ferramentaController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testListarFerramentas() throws Exception {
        Ferramenta ferramenta = new Ferramenta();
        ferramenta.setId("1");
        ferramenta.setNome("Parafusadeira");

        Mockito.when(ferramentaRepository.findAll()).thenReturn(List.of(ferramenta));

        mockMvc.perform(get("/ferramentas"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(ferramenta))));
    }

    @Test
    void testCriarFerramenta_comAdmin_deveCriar() throws Exception {
        Ferramenta nova = new Ferramenta();
        nova.setNome("Furadeira");
        nova.setDescricao("Ferramenta potente");
        nova.setCategoria("Eletrônica");

        Usuario admin = new Usuario();
        admin.setNome("Eduardo");
        admin.setEmail("eduardo@teste.com");
        admin.setPapel("ADMIN");

        Ferramenta salva = new Ferramenta();
        salva.setId("123");
        salva.setNome("Furadeira");
        salva.setDescricao("Ferramenta potente");
        salva.setCategoria("Eletrônica");
        salva.setNomeUsuario("Eduardo");
        salva.setEmailUsuario("eduardo@teste.com");

        Mockito.when(usuarioService.getUsuario("eduardo@teste.com")).thenReturn(admin);
        Mockito.when(ferramentaRepository.save(any(Ferramenta.class))).thenReturn(salva);

        mockMvc.perform(post("/ferramentas")
                        .header("email", "eduardo@teste.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nova)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(salva)));
    }

    @Test
    void testCriarFerramenta_comUser_deveRetornar403() throws Exception {
        Ferramenta nova = new Ferramenta();
        nova.setNome("Chave Inglesa");

        Usuario user = new Usuario();
        user.setNome("User");
        user.setEmail("user@teste.com");
        user.setPapel("USER");

        Mockito.when(usuarioService.getUsuario("user@teste.com")).thenReturn(user);

        mockMvc.perform(post("/ferramentas")
                        .header("email", "user@teste.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nova)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCriarFerramenta_usuarioNaoExiste_deveRetornar404() throws Exception {
        Ferramenta nova = new Ferramenta();
        nova.setNome("Martelo");

        Mockito.when(usuarioService.getUsuario("naoexiste@teste.com")).thenReturn(null);

        mockMvc.perform(post("/ferramentas")
                        .header("email", "naoexiste@teste.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nova)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletarFerramenta_comAdmin_deveDeletar() throws Exception {
        Usuario admin = new Usuario();
        admin.setNome("Eduardo");
        admin.setEmail("eduardo@teste.com");
        admin.setPapel("ADMIN");

        Ferramenta ferramenta = new Ferramenta();
        ferramenta.setId("abc123");

        Mockito.when(usuarioService.getUsuario("eduardo@teste.com")).thenReturn(admin);
        Mockito.when(ferramentaRepository.findById("abc123")).thenReturn(Optional.of(ferramenta));

        mockMvc.perform(delete("/ferramentas/abc123")
                        .header("email", "eduardo@teste.com"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeletarFerramenta_comUser_deveRetornar403() throws Exception {
        Usuario user = new Usuario();
        user.setNome("User");
        user.setEmail("user@teste.com");
        user.setPapel("USER");

        Mockito.when(usuarioService.getUsuario("user@teste.com")).thenReturn(user);

        mockMvc.perform(delete("/ferramentas/abc123")
                        .header("email", "user@teste.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeletarFerramenta_usuarioNaoExiste_deveRetornar404() throws Exception {
        Mockito.when(usuarioService.getUsuario("naoexiste@teste.com")).thenReturn(null);

        mockMvc.perform(delete("/ferramentas/abc123")
                        .header("email", "naoexiste@teste.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletarFerramenta_naoEncontrada_deveRetornar404() throws Exception {
        Usuario admin = new Usuario();
        admin.setNome("Eduardo");
        admin.setEmail("eduardo@teste.com");
        admin.setPapel("ADMIN");

        Mockito.when(usuarioService.getUsuario("eduardo@teste.com")).thenReturn(admin);
        Mockito.when(ferramentaRepository.findById("abc123")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/ferramentas/abc123")
                        .header("email", "eduardo@teste.com"))
                .andExpect(status().isNotFound());
    }
}
