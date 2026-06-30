package com.example.restaurante.service;

import com.example.restaurante.FechamentoContaResponse;
import com.example.restaurante.domain.entity.FechamentoConta;
import com.example.restaurante.domain.entity.Mesa;
import com.example.restaurante.domain.entity.Pedido;
import com.example.restaurante.domain.entity.PedidoItem;
import com.example.restaurante.domain.enums.StatusItemPedido;
import com.example.restaurante.domain.enums.StatusMesa;
import com.example.restaurante.domain.enums.StatusPedido;
import com.example.restaurante.dto.FechamentoContaRequest;
import com.example.restaurante.exception.RegraNegocioException;
import com.example.restaurante.repository.FechamentoContaRepo;
import com.example.restaurante.repository.MesaRepo;
import com.example.restaurante.repository.PedidoItemRepo;
import com.example.restaurante.repository.PedidoRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FechamentoContaService {

    private final PedidoRepo pedidoRepo;
    private final PedidoItemRepo pedidoItemRepo;
    private final FechamentoContaRepo fechamentoContaRepo;
    private final MesaRepo mesaRepo;


    public FechamentoContaService(PedidoRepo pedidoRepo, PedidoItemRepo pedidoItemRepo, FechamentoContaRepo fechamentoContaRepo, MesaRepo mesaRepo) {
        this.pedidoRepo = pedidoRepo;
        this.pedidoItemRepo = pedidoItemRepo;
        this.fechamentoContaRepo = fechamentoContaRepo;
        this.mesaRepo = mesaRepo;
    }

    public FechamentoContaResponse fecharConta(Long pedidoId, FechamentoContaRequest request){
        Pedido pedido = buscarPedidoPorId(pedidoId);

        if (pedido.getStatus() == StatusPedido.FECHADO){
            throw new RegraNegocioException("Pedido ja esta fechado");
        }

        if (pedido.getStatus() == StatusPedido.CANCELADO){
            throw new RegraNegocioException("Pedido esta cancelado, nao pode ser fechado");
        }

        if (fechamentoContaRepo.existsByPedidoId(pedidoId)){
            throw new RegraNegocioException("Ja existe fechamento para este pedido");
        }

        List<PedidoItem> items = pedidoItemRepo.findByPedidoId(pedidoId);

        if (items.isEmpty()){
            throw new RegraNegocioException("Nao eh possivel fechar uma conta sem itens");
        }

        List<PedidoItem> itensNaoEntregues = pedidoItemRepo.findByPedidoIdAndStatusNot(pedidoId, StatusItemPedido.ENTREGUE);

        if (!itensNaoEntregues.isEmpty()){
            throw new RegraNegocioException("Todos os itens precisam estar entregues antes de poder fechar a conta");
        }


        BigDecimal subtotal = items.stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxaService = request.taxaServico() != null ? request.taxaServico() : BigDecimal.ZERO;
        BigDecimal desconto = request.desconto() != null ? request.desconto() : BigDecimal.ZERO;

        if (taxaService.compareTo(BigDecimal.ZERO) < 0){
            throw new RegraNegocioException("Taxa de servico nao pode ser negativa");
        }

        if (desconto.compareTo(BigDecimal.ZERO) < 0){
            throw new RegraNegocioException("Desconto nao pode ser negativo");
        }

        BigDecimal total = subtotal.add(taxaService).subtract(desconto);

        if (total.compareTo(BigDecimal.ZERO) < 0){
            throw new RegraNegocioException("Total da conta nao pode ser negativo");
        }

        FechamentoConta fechamento = new FechamentoConta();
        fechamento.setPedido(pedido);
        fechamento.setSubtotal(subtotal);
        fechamento.setTaxaServico(taxaService);
        fechamento.setDesconto(desconto);
        fechamento.setTotal(total);

        pedido.setStatus(StatusPedido.FECHADO);
        pedido.setDataFechamento(LocalDateTime.now());

        FechamentoConta fechamentoSalvo = fechamentoContaRepo.save(fechamento);
        pedidoRepo.save(pedido);

        return FechamentoContaResponse.fromEntity(fechamentoSalvo);
    }

    public FechamentoContaResponse buscarPorFechamento(Long pedidoId){
        FechamentoConta fechamento = fechamentoContaRepo.findByPedidoId(pedidoId).orElseThrow(() -> new RuntimeException(""));
        return FechamentoContaResponse.fromEntity(fechamento);
    }


    private Pedido buscarPedidoPorId(Long id){
        return pedidoRepo.findById(id).orElseThrow(() -> new RuntimeException("Fechamento"));
    }
}
