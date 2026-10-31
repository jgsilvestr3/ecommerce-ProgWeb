
// Recupera o tema salvo no navegador
const temaSalvo = localStorage.getItem("tema");

// Aplica o tema salvo ou utiliza o tema claro como padrão
document.documentElement.setAttribute(
    "data-tema",
    temaSalvo === "escuro" ? "escuro" : "claro"
);

// Localiza o botão de alternância
const botaoTema = document.getElementById("botao-tema");

// Atualiza o texto e o estado do botão
function atualizarBotaoTema() {
    const temaAtual = document.documentElement.getAttribute("data-tema");

    if (temaAtual === "escuro") {
        botaoTema.textContent = "☀️ Tema claro";
        botaoTema.setAttribute("aria-label", "Ativar tema claro");
        botaoTema.setAttribute("aria-pressed", "true");
    } else {
        botaoTema.textContent = "🌙 Tema escuro";
        botaoTema.setAttribute("aria-label", "Ativar tema escuro");
        botaoTema.setAttribute("aria-pressed", "false");
    }
}

// Atualiza o botão ao carregar a página
atualizarBotaoTema();

// Alterna o tema ao clicar no botão
botaoTema.addEventListener("click", function () {
    const temaAtual = document.documentElement.getAttribute("data-tema");

    const novoTema = temaAtual === "escuro" ? "claro" : "escuro";

    // Aplica o novo tema
    document.documentElement.setAttribute("data-tema", novoTema);

    // Salva a preferência no navegador
    localStorage.setItem("tema", novoTema);

    // Atualiza o botão
    atualizarBotaoTema();
});
