package br.insper.prova.produto;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProdutoRepository extends MongoRepository<Produto, String> {
    Produto findByNome(String nome);
}
