(function () {
  'use strict';

    // Carrinho separado por usuário: cada login tem a sua chave no localStorage.
  // Visitante (sem login) usa a chave "loja_carrinho", como antes.
  var CHAVE_VISITANTE = 'loja_carrinho';
  var usuario = (document.body && document.body.dataset.usuario) || '';
  var CHAVE = usuario ? CHAVE_VISITANTE + ':' + usuario : CHAVE_VISITANTE;
  
  var moeda = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });

  // ---------- ler e gravar no localStorage ----------

  function lerDe(chave) {
    try {
      var dados = JSON.parse(localStorage.getItem(chave));
      if (!Array.isArray(dados)) return [];
      // descarta itens inválidos (ex.: alguém editou o localStorage na mão)
      return dados.filter(function (i) {
        return i && Number.isInteger(i.id) && typeof i.nome === 'string'
            && typeof i.preco === 'number' && Number.isInteger(i.qtd) && i.qtd > 0;
      });
    } catch (e) {
      return [];
    }
  }

  function ler() {
    return lerDe(CHAVE);
  }

  function salvar(itens) {
    localStorage.setItem(CHAVE, JSON.stringify(itens));
    atualizarContador();
  }

  function limiteDe(item) {
    return item.estoque > 0 ? item.estoque : 99;
  }

  // Ao entrar numa conta, os itens que a pessoa juntou como visitante passam para o carrinho dela
  function migrarCarrinhoDeVisitante() {
    if (!usuario) return;
    var doVisitante = lerDe(CHAVE_VISITANTE);
    if (doVisitante.length === 0) return;
    var itens = ler();
    doVisitante.forEach(function (novo) {
      var existente = itens.find(function (i) { return i.id === novo.id; });
      if (existente) {
        existente.qtd = Math.min(existente.qtd + novo.qtd, limiteDe(existente));
      } else {
        itens.push(novo);
      }
    });
    salvar(itens);
    localStorage.removeItem(CHAVE_VISITANTE);
  }

  // ---------- operações do carrinho ----------

  function adicionar(produto) {
    var itens = ler();
    var existente = itens.find(function (i) { return i.id === produto.id; });
    if (existente) {
      if (existente.qtd >= limiteDe(existente)) return false;
      existente.qtd += 1;
    } else {
      itens.push({ id: produto.id, nome: produto.nome, preco: produto.preco,
                   estoque: produto.estoque, qtd: 1 });
    }
    salvar(itens);
    return true;
  }

  function alterarQtd(id, delta) {
    var itens = ler();
    var item = itens.find(function (i) { return i.id === id; });
    if (!item) return;
    var nova = item.qtd + delta;
    if (nova < 1 || nova > limiteDe(item)) return;
    item.qtd = nova;
    salvar(itens);
  }

  function remover(id) {
    salvar(ler().filter(function (i) { return i.id !== id; }));
  }

  function limpar() {
    salvar([]);
  }

  // soma em centavos (inteiros) para evitar erro de arredondamento de ponto flutuante
  function total(itens) {
    var centavos = itens.reduce(function (soma, i) {
      return soma + Math.round(i.preco * 100) * i.qtd;
    }, 0);
    return centavos / 100;
  }

  // ---------- contador no menu ----------

  function atualizarContador() {
    var el = document.getElementById('contador-carrinho');
    if (!el) return;
    el.textContent = ler().reduce(function (s, i) { return s + i.qtd; }, 0);
  }

  // ---------- aviso para leitores de tela ----------

  var aviso = null;
  function anunciar(texto) {
    if (!aviso) {
      aviso = document.createElement('div');
      aviso.className = 'visually-hidden';
      aviso.setAttribute('role', 'status');
      document.body.appendChild(aviso);
    }
    aviso.textContent = texto;
  }

  // ---------- botões "Adicionar ao carrinho" (páginas de produtos) ----------

  document.addEventListener('click', function (e) {
    var btn = e.target.closest('.btn-add-carrinho');
    if (!btn) return;

    var ok = adicionar({
      id: parseInt(btn.dataset.id, 10),
      nome: btn.dataset.nome,
      preco: parseFloat(btn.dataset.preco),
      estoque: parseInt(btn.dataset.estoque, 10)
    });

    var textoOriginal = btn.dataset.textoOriginal || btn.textContent;
    btn.dataset.textoOriginal = textoOriginal;
    btn.textContent = ok ? 'Adicionado!' : 'Limite do estoque';
    anunciar(ok ? btn.dataset.nome + ' adicionado ao carrinho.'
                : 'Você já colocou todas as unidades em estoque.');
    setTimeout(function () { btn.textContent = textoOriginal; }, 1200);
  });

  // ---------- página do carrinho ----------

  function celula(texto) {
    var td = document.createElement('td');
    td.textContent = texto;
    return td;
  }

  function botao(rotulo, acao, item, descricao, desabilitado) {
    var b = document.createElement('button');
    b.type = 'button';
    b.className = 'btn btn-sm btn-outline-secondary';
    b.textContent = rotulo;
    b.dataset.acao = acao;
    b.dataset.id = item.id;
    b.setAttribute('aria-label', descricao + ' ' + item.nome);
    b.disabled = !!desabilitado;
    return b;
  }

  function renderizar() {
    var corpo = document.getElementById('carrinho-itens');
    if (!corpo) return; // não estamos na página do carrinho

    var itens = ler();
    document.getElementById('carrinho-vazio').classList.toggle('d-none', itens.length > 0);
    document.getElementById('carrinho-conteudo').classList.toggle('d-none', itens.length === 0);

    corpo.textContent = '';
    itens.forEach(function (item) {
      var tr = document.createElement('tr');
      tr.appendChild(celula(item.nome));
      tr.appendChild(celula(moeda.format(item.preco)));

      var tdQtd = document.createElement('td');
      var grupo = document.createElement('div');
      grupo.className = 'd-flex align-items-center gap-2';
      var numero = document.createElement('span');
      numero.textContent = item.qtd;
      grupo.appendChild(botao('−', 'menos', item, 'Diminuir quantidade de', item.qtd <= 1));
      grupo.appendChild(numero);
      grupo.appendChild(botao('+', 'mais', item, 'Aumentar quantidade de', item.qtd >= limiteDe(item)));
      tdQtd.appendChild(grupo);
      tr.appendChild(tdQtd);

      tr.appendChild(celula(moeda.format(item.preco * item.qtd)));

      var tdAcao = document.createElement('td');
      var rem = botao('Remover', 'remover', item, 'Remover');
      rem.className = 'btn btn-sm btn-outline-danger';
      tdAcao.appendChild(rem);
      tr.appendChild(tdAcao);

      corpo.appendChild(tr);
    });

    document.getElementById('carrinho-total').textContent = moeda.format(total(itens));
  }

  var corpoTabela = document.getElementById('carrinho-itens');
  if (corpoTabela) {
    corpoTabela.addEventListener('click', function (e) {
      var btn = e.target.closest('button[data-acao]');
      if (!btn) return;
      var id = parseInt(btn.dataset.id, 10);
      if (btn.dataset.acao === 'mais') alterarQtd(id, 1);
      if (btn.dataset.acao === 'menos') alterarQtd(id, -1);
      if (btn.dataset.acao === 'remover') remover(id);
      renderizar();
    });
    document.getElementById('btn-limpar').addEventListener('click', function () {
      if (confirm('Esvaziar o carrinho?')) {
        limpar();
        renderizar();
      }
    });
  }

  // se o carrinho mudar em outra aba, esta aba acompanha
  window.addEventListener('storage', function (e) {
    if (e.key === CHAVE) {
      atualizarContador();
      renderizar();
    }
  });

  migrarCarrinhoDeVisitante();
  atualizarContador();
  renderizar();

  // usado pela página de pagamento (próximo passo)
  window.Carrinho = { ler: ler, limpar: limpar, total: total, moeda: moeda };
})();