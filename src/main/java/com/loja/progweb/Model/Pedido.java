package com.loja.progweb.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Pedido {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Usuario usuario;

    private LocalDateTime dataHora;

    @Column(precision = 12, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Pagamento pagamento;

    public void adicionarItem(ItemPedido item) {
        item.setPedido(this);
        itens.add(item);
    }

    public boolean isPago() {
        return status == StatusPedido.PAGO;
    }

    public String getDataHoraFormatada() {
        return dataHora == null ? "" : dataHora.format(FORMATO);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
    public Pagamento getPagamento() { return pagamento; }
    public void setPagamento(Pagamento pagamento) { this.pagamento = pagamento; }
}
