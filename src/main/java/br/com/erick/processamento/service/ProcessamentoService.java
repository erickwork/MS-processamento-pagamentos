package br.com.erick.processamento.service;

import br.com.erick.processamento.domain.Pagamento;
import br.com.erick.processamento.domain.StatusPagamento;
import br.com.erick.processamento.domain.TipoPagamento;
import br.com.erick.processamento.domain.Usuario;
import br.com.erick.processamento.repository.PagamentoRepository;
import br.com.erick.processamento.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProcessamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void receberValor(Pagamento dadosPagamento){
        Pagamento pagamento = pagamentoRepository.findById(dadosPagamento.getId()).orElseThrow(EntityNotFoundException::new);
        Usuario usuario = pagamento.getUsuario();
        BigDecimal novoSaldo = usuario.getSaldo().add(pagamento.getValor());
        usuario.setSaldo(novoSaldo);
        pagamento.setStatus(StatusPagamento.RECEBIDO);
        usuarioRepository.save(usuario);
        pagamentoRepository.save(pagamento);
        rabbitTemplate.convertAndSend("processamento", pagamento);
    }

    public void debitarValor(Pagamento dadosPagamento){
        Pagamento pagamento = pagamentoRepository.findById(dadosPagamento.getId()).orElseThrow(EntityNotFoundException::new);
        Usuario usuario = pagamento.getUsuario();
        if(usuario.getSaldo().compareTo(pagamento.getValor()) >= 0) {
            BigDecimal novoSaldo = usuario.getSaldo().subtract(pagamento.getValor());
            pagamento.setStatus(StatusPagamento.PAGO);
            usuario.setSaldo(novoSaldo);
            usuarioRepository.save(usuario);
        }else{
            pagamento.setStatus(StatusPagamento.CANCELADO);
        }
        pagamentoRepository.save(pagamento);
        rabbitTemplate.convertAndSend("processamento", pagamento);

    }
}
