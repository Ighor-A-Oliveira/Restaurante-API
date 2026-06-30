package com.example.restaurante.service;

import com.example.restaurante.domain.entity.PedidoItem;
import com.example.restaurante.domain.enums.StatusItemPedido;
import com.example.restaurante.dto.CozinhaItemResponse;
import com.example.restaurante.exception.RegraNegocioException;
import com.example.restaurante.repository.PedidoItemRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CozinhaService {
    private final PedidoItemRepo pedidoItemRepo;

    public CozinhaService(PedidoItemRepo pedidoItemRepo) {
        this.pedidoItemRepo = pedidoItemRepo;
    }

    public List<CozinhaItemResponse> listarItensPendentes(){
        return pedidoItemRepo.findByStatusOrderByIdAsc(StatusItemPedido.PENDENTE).stream().map(item -> CozinhaItemResponse.fromEntity(item)).toList();
    }

    public List<CozinhaItemResponse> listarItensEmPreparo(){
        return pedidoItemRepo.findByStatusOrderByIdAsc(StatusItemPedido.EM_PREPARO).stream().map(item -> CozinhaItemResponse.fromEntity(item)).toList();
    }

    public CozinhaItemResponse iniciarPreparo(Long itemId){
        PedidoItem item = buscarItemPorId(itemId);

        if (item.getStatus() != StatusItemPedido.PENDENTE){
            throw new RegraNegocioException("Apenas items pendentes podem entrar em preparo");
        }

        item.setStatus(StatusItemPedido.EM_PREPARO);
        item.setDataInicioPreparo(LocalDateTime.now());
        PedidoItem itemSalvo = pedidoItemRepo.save(item);

        return CozinhaItemResponse.fromEntity(itemSalvo);
    }

    public CozinhaItemResponse marcarComoPronto(Long itemId){
        PedidoItem item = buscarItemPorId(itemId);

        if (item.getStatus() != StatusItemPedido.EM_PREPARO){
            throw new RegraNegocioException("Apenas items em preparo podem ser listados como pronto");
        }

        item.setStatus(StatusItemPedido.PRONTO);
        item.setDataPronto(LocalDateTime.now());
        PedidoItem itemSalvo = pedidoItemRepo.save(item);

        return CozinhaItemResponse.fromEntity(itemSalvo);
    }

    public CozinhaItemResponse entregarItem(Long itemId){
        PedidoItem item = buscarItemPorId(itemId);

        if (item.getStatus() != StatusItemPedido.PRONTO){
            throw new RegraNegocioException("Apenas items prontos podem ser listados como entregue");
        }

        item.setStatus(StatusItemPedido.ENTREGUE);
        item.setDataEntrega(LocalDateTime.now());
        PedidoItem itemSalvo = pedidoItemRepo.save(item);

        return CozinhaItemResponse.fromEntity(itemSalvo);
    }


    public PedidoItem buscarItemPorId(Long itemId){
        return pedidoItemRepo.findById(itemId).orElseThrow(() -> new RuntimeException("PedidoItem não encontrado. Id = " + itemId));
    }
}
