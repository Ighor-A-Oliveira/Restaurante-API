package com.example.restaurante.controller;

import com.example.restaurante.dto.ProdutoRequest;
import com.example.restaurante.dto.ProdutoResponse;
import com.example.restaurante.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService prodServ;

    public ProdutoController(ProdutoService prodServ) {
        this.prodServ = prodServ;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse cadastrar(@RequestBody @Valid ProdutoRequest request){
        return prodServ.cadastrar(request);
    }

    @GetMapping
    public Page<ProdutoResponse> listar(Pageable pageable){
        return prodServ.listar(pageable);
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarPorId(@PathVariable @Valid Long id){
        return prodServ.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(@PathVariable @Valid Long id, @RequestBody @Valid ProdutoRequest request){
        return prodServ.atualizar(id,request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable @Valid Long id){
        prodServ.deletar(id);
    }
}
