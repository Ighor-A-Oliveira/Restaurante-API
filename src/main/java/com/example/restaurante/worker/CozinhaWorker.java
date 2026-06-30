package com.example.restaurante.worker;

import com.example.restaurante.domain.entity.PedidoItem;
import com.example.restaurante.domain.enums.StatusItemPedido;
import com.example.restaurante.repository.PedidoItemRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class CozinhaWorker {

    private final PedidoItemRepo pedidoItemRepo;

    //Criando uma virtual thread para o programa
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public CozinhaWorker(PedidoItemRepo pedidoItemRepo) {
        this.pedidoItemRepo = pedidoItemRepo;
    }

    @Scheduled(fixedRate = 60000)
    public void verificarItensAtrasados(){
        List<PedidoItem> itensEmPreparo = pedidoItemRepo.buscarItensComProdutoEPedido(StatusItemPedido.EM_PREPARO);

        for(PedidoItem pedidoItem : itensEmPreparo){
            executorService.submit(() -> verificarItem(pedidoItem));
        }
    }

    public void verificarItem(PedidoItem item){
        if(item.getDataInicioPreparo() == null){
            return;
        }

        Integer tempoPreparo = item.getProduto().getTempoPreparoMinutos();

        if(tempoPreparo == null || tempoPreparo <= 0){
            return;
        }


        Long minutosEmPreparo = Duration.between(item.getDataInicioPreparo(), LocalDateTime.now()).toMinutes();

        if (minutosEmPreparo > tempoPreparo){
            System.out.println(
                    """
                    [ALERTA COZINHA]
                    ITEM ATRASADO:
                    Pedido: %d
                    Mesa: %d
                    Produto: %s
                    Tempo Esperado: %d minutos
                    Tempo em Preparo: %d minutos
                    """.formatted(
                            item.getPedido().getId(),
                            item.getPedido().getMesa().getNumero(),
                            item.getProduto().getNome(),
                            tempoPreparo,
                            minutosEmPreparo
                    )
            );
        }
    }
}
