package br.insper.prova.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import insper_2025.demo.produto.Produto;
import insper_2025.demo.produto.ProdutoController;
import insper_2025.demo.produto.ProdutoService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProdutoControllerTests {

    @InjectMocks
    private ProdutoController produtoController;

    @Mock
    private ProdutoService produtoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(produtoController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void test_GetProdutos() throws Exception {
        Produto produto1 = new Produto();
        produto1.setId("1");
        produto1.setNome("Produto A");
        produto1.setPreco(29.99);
        produto1.setEstoque(10);

        Produto produto2 = new Produto();
        produto2.setId("2");
        produto2.setNome("Produto B");
        produto2.setPreco(49.99);
        produto2.setEstoque(20);

        List<Produto> produtos = Arrays.asList(produto1, produto2);

        Mockito.when(produtoService.listaProdutos()).thenReturn(produtos);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/produto"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(produtos)));
    }

    @Test
    void test_GetProdutoById() throws Exception {
        Produto produto = new Produto();
        produto.setId("123");
        produto.setNome("Produto A");
        produto.setPreco(29.99);
        produto.setEstoque(10);

        Mockito.when(produtoService.findProdutoById("123")).thenReturn(produto);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/produto/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(produto)));
    }

    @Test
    void test_PostProduto() throws Exception {
        Produto produto = new Produto();
        produto.setId("123");
        produto.setNome("Produto Teste");
        produto.setPreco(39.99);
        produto.setEstoque(15);

        Mockito.when(produtoService.cadastraProduto(Mockito.any())).thenReturn(produto);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/produto")
                                .content(objectMapper.writeValueAsString(produto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(produto)));
    }

    @Test
    void test_PutAtualizaEstoque() throws Exception {
        Mockito.doNothing().when(produtoService).atualizaEstoque("123", 20, "true");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/produto/estoque/123/20/true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Estoque Atualizado"));
    }
}
