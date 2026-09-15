import { useEffect, useState } from "react";
import api from "../services/api";

function Catalogo() {
  const [produtos, setProdutos] = useState([]);

  useEffect(() => {
    api.get("/produtos").then((resposta) => setProdutos(resposta.data));
  }, []);

  return (
    <main>
      <h1>Nossos Produtos</h1>
      <ul aria-label="Lista de produtos">
        {produtos.map((p) => (
          <li key={p.id}>
            <img src={p.imagemUrl} alt={p.nome} />
            <h2>{p.nome}</h2>
            <p>R$ {p.preco}</p>
          </li>
        ))}
      </ul>
    </main>
  );
}

export default Catalogo;