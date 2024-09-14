package br.com.erick.processamento.amqp;

import br.com.erick.processamento.domain.Pagamento;
import br.com.erick.processamento.domain.TipoPagamento;
import br.com.erick.processamento.domain.Usuario;
import br.com.erick.processamento.service.ProcessamentoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PagamentoListener {

    @Autowired
    private ProcessamentoService service;

    @RabbitListener(queues = "pagamento")
    public void receberPagamento(Pagamento dadosPagamento){
        System.out.printf("""
                Pagamento solicitado
                Tipo: %s
                Valor: %s
                Usuario: %s
                Saldo: %s
                %n""", dadosPagamento.getTipo(), dadosPagamento.getValor(), dadosPagamento.getUsuario().getNome(), dadosPagamento.getUsuario().getSaldo());
        if (dadosPagamento.getTipo() == TipoPagamento.CONTA_RECEBER){
            service.receberValor(dadosPagamento);
        }
        if (dadosPagamento.getTipo() == TipoPagamento.CONTA_PAGAR){
            service.debitarValor(dadosPagamento);
        }


    }
}
