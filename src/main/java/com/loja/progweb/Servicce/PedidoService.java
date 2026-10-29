package com.loja.progweb.Servicce;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loja.progweb.Model.ItemPedido;
import com.loja.progweb.Model.Pagamento;
import com.loja.progweb.Model.PagamentoForm;
import com.loja.progweb.Model.Pedido;
import com.loja.progweb.Model.Produto;
import com.loja.progweb.Model.StatusPedido;
import com.loja.progweb.Model.Usuario;
import com.loja.progweb.Repository.PedidoRepository;
import com.loja.progweb.Repository.ProdutoRepository;
import com.loja.progweb.Repository.UsuarioRepository;
import com.loja.progweb.Util.CartaoUtil;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PagamentoSimuladoService pagamentoSimulado;
    private final CriptografiaService criptografia;

    public PedidoService(PedidoRepository pedidoRepository,
                         ProdutoRepository produtoRepository,
                         UsuarioRepository usuarioRepository,
                         PagamentoSimuladoService pagamentoSimulado,
                         CriptografiaService criptografia) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.pagamentoSimulado = pagamentoSimulado;
        this.criptografia = criptografia;
    }

    /**
     * Fecha a compra. Tudo acontece em UMA transação: se qualquer passo falhar
     * (exceção), o banco volta ao estado anterior e nada fica pela metade.
     *
     * O navegador só informa "produto X, quantidade Y". Nome, preço e estoque
     * vêm SEMPRE do banco, porque o carrinho (localStorage) pode ser adulterado.
     */
    @Transactional
    public Pedido finalizarCompra(String emailUsuario, List<Long> ids, List<Integer> quantidades,
                                  PagamentoForm form) {

        Map<Long, Integer> carrinho = consolidar(ids, quantidades);
        if (carrinho.isEmpty()) {
            throw new CompraInvalidaException("Seu carrinho está vazio.");
        }

        validarCartao(form);
        String numero = CartaoUtil.somenteDigitos(form.getNumeroCartao());

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new CompraInvalidaException("Usuário não encontrado. Entre novamente."));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDataHora(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> entrada : carrinho.entrySet()) {
            Produto produto = produtoRepository.findById(entrada.getKey())
                    .orElseThrow(() -> new CompraInvalidaException(
                            "Um produto do carrinho não existe mais. Remova-o e tente de novo."));

            int quantidade = entrada.getValue();
            int estoque = (Integer)produto.getEstoque() == null ? 0 : produto.getEstoque();
            if (quantidade > estoque) {
                throw new CompraInvalidaException("Estoque insuficiente para \"" + produto.getNome()
                        + "\" (disponível: " + estoque + ").");
            }

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setNomeProduto(produto.getNome());
            item.setPrecoUnitario(produto.getPreco());
            item.setQuantidade(quantidade);
            pedido.adicionarItem(item);
            total = total.add(item.getSubtotal());
        }
        pedido.setTotal(total);

        // pagamento simulado + dados do cartão (criptografados)
        PagamentoSimuladoService.Resultado resultado = pagamentoSimulado.processar(numero, total);

        Pagamento pagamento = new Pagamento();
        pagamento.setTitular(form.getTitular().trim());
        pagamento.setBandeira(CartaoUtil.bandeira(numero));
        pagamento.setFinalCartao(CartaoUtil.ultimosQuatro(numero));
        pagamento.setNumeroCriptografado(criptografia.criptografar(numero)); // nunca o número puro
        pagamento.setCodigoTransacao(resultado.codigoTransacao());
        pagamento.setAprovado(resultado.aprovado());
        pagamento.setMensagem(resultado.mensagem());
        pedido.setPagamento(pagamento);

        pedido.setStatus(resultado.aprovado() ? StatusPedido.PAGO : StatusPedido.RECUSADO);

        // só baixa o estoque se o pagamento foi aprovado
        if (resultado.aprovado()) {
            for (ItemPedido item : pedido.getItens()) {
                Produto produto = item.getProduto();
                produto.setEstoque(produto.getEstoque() - item.getQuantidade());
                produtoRepository.save(produto);
            }
        }

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarDoUsuario(String email) {
        return pedidoRepository.findByUsuarioEmailOrderByDataHoraDesc(email);
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    private void validarCartao(PagamentoForm form) {
        if (form.getTitular() == null || form.getTitular().isBlank()) {
            throw new CompraInvalidaException("Informe o nome impresso no cartão.");
        }
        String numero = CartaoUtil.somenteDigitos(form.getNumeroCartao());
        if (!CartaoUtil.numeroValido(numero)) {
            throw new CompraInvalidaException("Número de cartão inválido.");
        }
        if (!CartaoUtil.validadeValida(form.getValidade(), YearMonth.now())) {
            throw new CompraInvalidaException("Validade inválida ou cartão vencido (use MM/AA).");
        }
        if (!CartaoUtil.cvvValido(form.getCvv())) {
            throw new CompraInvalidaException("CVV inválido (3 ou 4 dígitos).");
        }
    }

    /** Junta repetidos (mesmo produto duas vezes vira uma linha) e rejeita quantidades absurdas. */
    private Map<Long, Integer> consolidar(List<Long> ids, List<Integer> quantidades) {
        Map<Long, Integer> mapa = new LinkedHashMap<>();
        if (ids == null || quantidades == null || ids.size() != quantidades.size()) {
            return mapa;
        }
        for (int i = 0; i < ids.size(); i++) {
            Long id = ids.get(i);
            Integer quantidade = quantidades.get(i);
            if (id == null || quantidade == null || quantidade < 1 || quantidade > 99) {
                throw new CompraInvalidaException("Quantidade inválida no carrinho.");
            }
            int novaQuantidade = mapa.merge(id, quantidade, Integer::sum);
            if (novaQuantidade > 99) {
                throw new CompraInvalidaException("Quantidade inválida no carrinho.");
            }
        }
        return mapa;
    }
}
