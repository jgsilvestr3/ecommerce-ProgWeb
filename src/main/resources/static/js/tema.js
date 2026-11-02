
(function () {
    'use strict';

    const CHAVE_TEMA = 'shophub-tema';

    const botao = document.getElementById('btn-tema');

    // Lê o tema salvo no navegador.
    function obterTemaSalvo() {
        try {
            return localStorage.getItem(CHAVE_TEMA) === 'escuro'
                ? 'escuro'
                : 'claro';
        } catch (erro) {
            return 'claro';
        }
    }

    // Aplica o tema na página.
    function aplicarTema(tema) {
        document.documentElement.setAttribute('data-tema', tema);

        if (botao) {
            const temaEscuroAtivo = tema === 'escuro';

            botao.textContent = temaEscuroAtivo
                ? '☀️ Tema claro'
                : '🌙 Tema escuro';

            botao.setAttribute(
                'aria-label',
                temaEscuroAtivo ? 'Ativar tema claro' : 'Ativar tema escuro'
            );

            botao.setAttribute(
                'aria-pressed',
                String(temaEscuroAtivo)
            );
        }
    }

    // Salva a preferência global no navegador.
    function salvarTema(tema) {
        try {
            localStorage.setItem(CHAVE_TEMA, tema);
        } catch (erro) {
            console.warn('Não foi possível salvar a preferência de tema.');
        }
    }

    // Inicializa o tema.
    aplicarTema(obterTemaSalvo());

    // Alterna o tema quando o usuário clicar no botão.
    if (botao) {
        botao.addEventListener('click', function () {
            const temaAtual =
                document.documentElement.getAttribute('data-tema');

            const novoTema = temaAtual === 'escuro' ? 'claro' : 'escuro';

            aplicarTema(novoTema);
            salvarTema(novoTema);
        });
    }
})();
