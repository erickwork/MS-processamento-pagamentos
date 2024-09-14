package br.com.erick.processamento.domain;

import br.com.erick.processamento.domain.StatusPagamento;
import br.com.erick.processamento.domain.TipoPagamento;
import br.com.erick.processamento.domain.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private TipoPagamento tipo;

    private BigDecimal valor;
    private LocalDate vencimento;

    @Enumerated(EnumType.STRING)
    private StatusPagamento status;

}
