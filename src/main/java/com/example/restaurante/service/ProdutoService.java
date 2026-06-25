package com.example.restaurante.service;

import com.example.restaurante.domain.entity.CategoriaProduto;
import com.example.restaurante.domain.entity.Produto;
import com.example.restaurante.dto.ProdutoRequest;
import com.example.restaurante.dto.ProdutoResponse;
import com.example.restaurante.repository.CategoriaProdutoRepo;
import com.example.restaurante.repository.ProdutoRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final ProdutoRepo prodRepo;
    private final CategoriaProdutoRepo catProdutoRepo;

    public ProdutoService(ProdutoRepo prodRepo, CategoriaProdutoRepo catProdutoRepo) {
        this.prodRepo = prodRepo;
        this.catProdutoRepo = catProdutoRepo;
    }



    public ProdutoResponse cadastrar(ProdutoRequest request) {
        CategoriaProduto categoriaProduto = buscarCategoriaPorId(request.categoriaId());
        Produto prod = request.toEntity(categoriaProduto);
        Produto prodSalvo = prodRepo.save(prod);

        return ProdutoResponse.fromEntity(prodSalvo);

    }


    public Page<ProdutoResponse> listar(Pageable pageable){
        return  prodRepo.findAll(pageable).map(ProdutoResponse::fromEntity);
    }


    public ProdutoResponse buscarPorId(Long id){
        Produto prod = buscarProdutoPorId(id);
        return ProdutoResponse.fromEntity(prod);
    }

    public ProdutoResponse atualizar(Long id, ProdutoRequest request){
        Produto prod = buscarProdutoPorId(id);
        CategoriaProduto catProd = buscarCategoriaPorId(request.categoriaId());

        request.preencher(prod, catProd);
        Produto prodAtualizado = prodRepo.save(prod);

        return ProdutoResponse.fromEntity(prodAtualizado);
    }

    public void deletar(Long id){
        Produto prod = buscarProdutoPorId(id);
        prodRepo.delete(prod);
    }

    private Produto buscarProdutoPorId(Long id){
        return prodRepo.findById(id).orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
    }
    private CategoriaProduto buscarCategoriaPorId(Long id){
        return catProdutoRepo.findById(id).orElseThrow(() -> new RuntimeException("Categoria nao encontrada"));
    }
}
