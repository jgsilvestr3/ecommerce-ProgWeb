(function () {
  'use strict';

  var carrinho = window.Carrinho; // vem do carrinho.js (carregue ele ANTES deste)
  if (!carrinho) return;

  // Mesmo algoritmo de validação do servidor (Luhn); aqui serve só para avisar mais rápido.
  // A validação que vale é a do servidor.
  function luhnValido(digitos) {
    if (!/^\d{13,19}$/.test(digitos)) return false;
    var soma = 0, dobrar = false;
    for (var i = digitos.length - 1; i >= 0; i--) {
      var d = parseInt(digitos.charAt(i), 10);
      if (dobrar) { d *= 2; if (d > 9) d -= 9; }
      soma += d;
      dobrar = !dobrar;
    }
    return soma % 10 === 0;
  }

  function campoOculto(nome, valor) {
    var input = document.createElement('input');
    input.type = 'hidden';
    input.name = nome;
    input.value = valor;
    return input;
  }

  // ---------- página de pagamento ----------
  var form = document.getElementById('form-pagamento');
  if (form) {
    var itens = carrinho.ler();

    if (itens.length === 0) {
      document.getElementById('checkout-vazio').classList.remove('d-none');
      document.getElementById('checkout-conteudo').classList.add('d-none');
    } else {
      var lista = document.getElementById('resumo-itens');
      itens.forEach(function (item) {
        var li = document.createElement('li');
        li.className = 'list-group-item d-flex justify-content-between';
        var nome = document.createElement('span');
        nome.textContent = item.qtd + ' × ' + item.nome;
        var valor = document.createElement('span');
        valor.textContent = carrinho.moeda.format(item.preco * item.qtd);
        li.appendChild(nome);
        li.appendChild(valor);
        lista.appendChild(li);
      });
      document.getElementById('resumo-total').textContent = carrinho.moeda.format(carrinho.total(itens));
    }

    var numero = document.getElementById('numeroCartao');
    var validade = document.getElementById('validade');
    var cvv = document.getElementById('cvv');

    // máscaras de digitação
    numero.addEventListener('input', function () {
      numero.setCustomValidity('');
      var d = numero.value.replace(/\D/g, '').slice(0, 19);
      numero.value = d.replace(/(.{4})(?=.)/g, '$1 ');
    });
    validade.addEventListener('input', function () {
      var d = validade.value.replace(/\D/g, '').slice(0, 4);
      validade.value = d.length > 2 ? d.slice(0, 2) + '/' + d.slice(2) : d;
    });
    cvv.addEventListener('input', function () {
      cvv.value = cvv.value.replace(/\D/g, '').slice(0, 4);
    });

    form.addEventListener('submit', function (e) {
      var atuais = carrinho.ler();
      if (atuais.length === 0) {
        e.preventDefault();
        return;
      }
      if (!luhnValido(numero.value.replace(/\D/g, ''))) {
        e.preventDefault();
        numero.setCustomValidity('Número de cartão inválido.');
        numero.reportValidity();
        return;
      }

      // o navegador só manda "id + quantidade"; nome e preço o servidor busca no banco
      var area = document.getElementById('itens-hidden');
      area.textContent = '';
      atuais.forEach(function (item) {
        area.appendChild(campoOculto('produtoId', item.id));
        area.appendChild(campoOculto('quantidade', item.qtd));
      });

      var botao = document.getElementById('btn-pagar');
      botao.disabled = true; // evita clique duplo e cobrança em dobro
      botao.textContent = 'Processando…';
    });
  }

  // ---------- página do pedido: esvazia o carrinho só se o pagamento foi aprovado ----------
  if (document.querySelector('[data-limpar-carrinho="true"]')) {
    carrinho.limpar();
  }
})();
