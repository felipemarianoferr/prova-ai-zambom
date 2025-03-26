//package br.insper.prova.service;
//
//import br.insper.prova.ferramenta.Ferramenta;
//import lombok.Data;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Data
//@ExtendWith(MockitoExtension.class)
//public class ProdutoServiceTests {
//
//    @InjectMocks
//    private ProdutoService produtoService;
//
//    @Mock
//    private ProdutoRepository produtoRepository;
//
//    @Test
//    void test_listaProdutosQuandoNaoHaProdutos() {
//        Mockito.when(produtoRepository.findAll()).thenReturn(new ArrayList<>());
//
//        List<Ferramenta> produtos = produtoService.listaProdutos();
//
//        Assertions.assertTrue(produtos.isEmpty());
//    }
//
//    @Test
//    void test_cadastraProdutoComSucesso() {
//        Ferramenta produto = new Ferramenta();
//        produto.setNome("Produto Teste");
//        produto.setPreco(1.0);
//        produto.setEstoque(1);
//
//        Mockito.when(produtoRepository.save(Mockito.any())).thenReturn(produto);
//
//        Ferramenta produtoSalvo = produtoService.cadastraProduto(produto);
//
//        Assertions.assertEquals("Produto Teste", produtoSalvo.getNome());
//    }
//
//    @Test
//    void test_findProdutoByIdComSucesso() {
//        Ferramenta produto = new Ferramenta();
//        produto.setId("123");
//        produto.setNome("Produto A");
//
//        Mockito.when(produtoRepository.findById("123")).thenReturn(Optional.of(produto));
//
//        Ferramenta produtoRetornado = produtoService.findProdutoById("123");
//
//        Assertions.assertEquals("Produto A", produtoRetornado.getNome());
//    }
//
//    @Test
//    void test_atualizaEstoqueAdicionando() {
//        Ferramenta produto = new Ferramenta();
//        produto.setId("123");
//        produto.setNome("Produto A");
//        produto.setPreco(29.99);
//        produto.setEstoque(10);
//
//        Mockito.when(produtoRepository.findById("123")).thenReturn(Optional.of(produto));
//
//        produtoService.atualizaEstoque("123", 20, "true");
//
//        Assertions.assertEquals(20, produto.getEstoque());
//    }
//
//    @Test
//    void test_atualizaEstoqueRemovendo() {
//        Ferramenta produto = new Ferramenta();
//        produto.setId("123");
//        produto.setNome("Produto A");
//        produto.setPreco(29.99);
//        produto.setEstoque(30);  // Estoque inicial corrigido
//
//        Mockito.when(produtoRepository.findById("123")).thenReturn(Optional.of(produto));
//
//        produtoService.atualizaEstoque("123", 10, "false");
//
//        Assertions.assertEquals(20, produto.getEstoque());
//    }
//
//}
