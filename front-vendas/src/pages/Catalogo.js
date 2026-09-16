import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../services/api";

function Catalogo() {
  const [produtos, setProdutos] = useState([]);

  useEffect(() => {
    carregarProdutos();
  }, []);

  function carregarProdutos() {
    api.get("/produtos").then((resposta) => setProdutos(resposta.data));
  }

  function excluirProduto(id) {
    if (window.confirm("Tem certeza que deseja excluir este produto?")) {
      api.delete(`/produtos/${id}`).then(() => carregarProdutos());
    }
  }

  return (
    <div>
      <h2 className="mb-4">Nossos Produtos</h2>
      <div className="row g-4" aria-label="Lista de produtos">
        {produtos.map((p) => (
          <div className="col-12 col-sm-6 col-md-4 col-lg-3" key={p.id}>
            <div className="card h-100 shadow-sm">
              <img
                src={p.imagemUrl}
                className="card-img-top"
                alt={p.nome}
                style={{ height: "180px", objectFit: "cover" }}
              />
              <div className="card-body d-flex flex-column">
                <h3 className="card-title h5">{p.nome}</h3>
                <p className="card-text text-muted">{p.descricao}</p>
                <p className="card-text fw-bold">
                  {(p.preco ?? 0).toLocaleString("pt-BR", { style: "currency", currency: "BRL" })}
                </p>
                {localStorage.getItem("role") === "ADMIN" && (
                  <div className="mt-auto d-flex gap-2">
                    <Link to={`/produto/editar/${p.id}`} className="btn btn-primary btn-sm">
                      Editar
                    </Link>
                    <button
                      onClick={() => excluirProduto(p.id)}
                      className="btn btn-danger btn-sm"
                      aria-label={`Excluir ${p.nome}`}
                    >
                      Excluir
                    </button>
                  </div>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Catalogo;