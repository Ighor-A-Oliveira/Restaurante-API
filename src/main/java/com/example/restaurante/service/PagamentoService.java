package com.example.restaurante.service;

import com.example.restaurante.client.PagamentoClient;
import com.example.restaurante.domain.entity.FechamentoConta;
import com.example.restaurante.domain.entity.Mesa;
import com.example.restaurante.domain.entity.Pagamento;
import com.example.restaurante.domain.entity.Pedido;
import com.example.restaurante.domain.enums.FormaPagamento;
import com.example.restaurante.domain.enums.StatusMesa;
import com.example.restaurante.domain.enums.StatusPagamento;
import com.example.restaurante.domain.enums.StatusPedido;
import com.example.restaurante.dto.PagamentoRequest;
import com.example.restaurante.dto.PagamentoResponse;
import com.example.restaurante.exception.RegraNegocioException;
import com.example.restaurante.repository.FechamentoContaRepo;
import com.example.restaurante.repository.MesaRepo;
import com.example.restaurante.repository.PagamentoRepo;
import com.example.restaurante.repository.PedidoRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PagamentoService {
    private final PagamentoRepo pagamentoRepo;
    private final PagamentoClient pagamentoClient;
    private final FechamentoContaRepo fechamentoContaRepo;
    private final PedidoRepo pedidoRepo;
    private final MesaRepo mesaRepo;

    public PagamentoService(PagamentoRepo pagamentoRepo, PagamentoClient pagamentoClient, FechamentoContaRepo fechamentoContaRepo, PedidoRepo pedidoRepo, MesaRepo mesaRepo) {
        this.pagamentoRepo = pagamentoRepo;
        this.pagamentoClient = pagamentoClient;
        this.fechamentoContaRepo = fechamentoContaRepo;
        this.pedidoRepo = pedidoRepo;
        this.mesaRepo = mesaRepo;
    }

    @Transactional
    public void pagar(Long pedidoId, String formaPagamento){
        FechamentoConta fechamento = fechamentoContaRepo.findByPedidoId(pedidoId).orElseThrow(()->new RegraNegocioException("Conta nao encontrada"));

        PagamentoResponse response = pagamentoClient.processar(
                new PagamentoRequest(
                    fechamento.getTotal(),
                    formaPagamento
                )
        );

        if ("APROVADO".equals(response.status())){
            Pedido pedido = fechamento.getPedido();
            pedido.setStatus(StatusPedido.FECHADO);

            Mesa mesa = pedido.getMesa();
            mesa.setStatus(StatusMesa.LIVRE);

            Pagamento pagamento = new Pagamento();
            pagamento.setPedido(pedido);
            pagamento.setFormaPagamento(FormaPagamento.valueOf(formaPagamento));
            pagamento.setStatus(StatusPagamento.APROVADO);
            pagamento.setValor(fechamento.getTotal());
            pagamento.setDataPagamento(fechamento.getDataFechamento());

            pedidoRepo.save(pedido);
            mesaRepo.save(mesa);
            pagamentoRepo.save(pagamento);
        }


    }

}
