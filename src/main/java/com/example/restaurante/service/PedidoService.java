package com.example.restaurante.service;

import com.example.restaurante.domain.entity.Mesa;
import com.example.restaurante.domain.entity.Pedido;
import com.example.restaurante.domain.entity.PedidoItem;
import com.example.restaurante.domain.entity.Produto;
import com.example.restaurante.domain.enums.StatusItemPedido;
import com.example.restaurante.domain.enums.StatusMesa;
import com.example.restaurante.domain.enums.StatusPedido;
import com.example.restaurante.dto.PedidoItemRequest;
import com.example.restaurante.dto.PedidoItemResponse;
import com.example.restaurante.dto.PedidoRequest;
import com.example.restaurante.dto.PedidoResponse;
import com.example.restaurante.exception.RegraNegocioException;
import com.example.restaurante.repository.MesaRepo;
import com.example.restaurante.repository.PedidoItemRepo;
import com.example.restaurante.repository.PedidoRepo;
import com.example.restaurante.repository.ProdutoRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepo pedidoRepo;
    private final MesaRepo mesaRepo;
    private final ProdutoRepo produtoRepo;
    private final PedidoItemRepo pedidoItemRepo;



    public PedidoService(PedidoRepo pedidoRepo, MesaRepo mesaRepo, ProdutoRepo produtoRepo, PedidoItemRepo pedidoItemRepo) {
        this.pedidoRepo = pedidoRepo;
        this.mesaRepo = mesaRepo;
        this.produtoRepo = produtoRepo;
        this.pedidoItemRepo = pedidoItemRepo;
    }


    public PedidoResponse abrirPedido(PedidoRequest request){
        Mesa mesa = mesaRepo.findById(request.mesaId()).orElseThrow(() -> new RuntimeException("Mesa nao encontrada"));

        if(mesa.getStatus() != StatusMesa.LIVRE){
            throw new RuntimeException("Mesa nao esta livre para abertura de pedido");
        }

        Pedido pedido = new Pedido();
        pedido.setMesa(mesa);
        pedido.setStatus(StatusPedido.ABERTO);
        pedido.setObservacao(request.observacao());

        mesa.setStatus(StatusMesa.OCUPADA);

        Pedido pedidoSalvo = pedidoRepo.save(pedido);
        mesaRepo.save(mesa);

        return PedidoResponse.fromEntity(pedidoSalvo);
    }

    public Page<PedidoResponse> listar(Pageable pageable){
        return pedidoRepo.findAll(pageable).map(ped -> PedidoResponse.fromEntity(ped));
    }

    public PedidoResponse buscarPorId(Long id){
        Pedido pedido = buscarPedidoPorId(id);
        return PedidoResponse.fromEntity(pedido);
    }


    public PedidoItemResponse adicionarItem(Long pedidoId, PedidoItemRequest request){
        Pedido pedido = buscarPedidoPorId(pedidoId);

        if (pedido.getStatus() != StatusPedido.ABERTO){
            throw new RegraNegocioException("So eh possivel adicionar items a pedidos em aberto");
        }

        Produto prod = produtoRepo.findById(request.productId()).orElseThrow(() -> new RuntimeException("Nao foi possivel encontrar o produto"));

        if (!prod.getDisponivel()){
            throw new RegraNegocioException("Produto nao esta disponivel para venda");
        }

        if (request.quantidade() == null || request.quantidade() <= 0){
            throw  new RegraNegocioException("A quantidade precisa ser maior que zero");
        }

        PedidoItem pedidoItem = new PedidoItem();
        pedidoItem.setPedido(pedido);
        pedidoItem.setProduto(prod);
        pedidoItem.setQuantidade(request.quantidade());
        pedidoItem.setPrecoUnitario(prod.getPreco());
        pedidoItem.setObservacao(request.observacao());
        pedidoItem.setStatus(StatusItemPedido.PENDENTE);

        PedidoItem itemSalvo = pedidoItemRepo.save(pedidoItem);

        return PedidoItemResponse.fromEntity(itemSalvo);
    }

    public List<PedidoItemResponse> listarItens(Long pedidoId){
        return pedidoItemRepo.findByPedidoId(pedidoId).stream().map(item -> PedidoItemResponse.fromEntity(item)).collect(Collectors.toList());
    }






    private Pedido buscarPedidoPorId(Long id){
        return pedidoRepo.findById(id).orElseThrow(() -> new RuntimeException("Pedido nao encontrado"));
    }
}
