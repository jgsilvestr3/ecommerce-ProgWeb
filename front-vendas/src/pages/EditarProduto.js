import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../services/api";

function EditarProduto() {
  const { id } = useParams();
  const navegar = useNavigate();
  const [produto, setProduto] = useState(null);

  useEffect(() => {
    api.get(`/produtos/${id}`).then((resposta) => setProduto(resposta.data));
  }, [id]);

  function atualizarCampo(campo, valor) {
    setProduto({ ...produto, [campo]: valor });
  }

  function salvar(e) {
    e.preventDefault();
    api.put(`/produtos/${id}`, produto).then(() => navegar("/"));
  }

  if (!produto) return <p>Carregando...</p>;

  return (
    <div className="col-12 col-md-6">
      <h2 className="mb-4">Editar Produto</h2>
      <form onSubmit={salvar}>
        <div className="mb-3">
          <label htmlFor="nome" className="form-label">Nome</label>
          <input
            id="nome"
            type="text"
            className="form-control"
            required
            value={produto.nome}
            onChange={(e) => atualizarCampo("nome", e.target.value)}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="descricao" className="form-label">Descrição</label>
          <input
            id="descricao"
            type="text"
            className="form-control"
            value={produto.descricao}
            onChange={(e) => atualizarCampo("descricao", e.target.value)}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="preco" className="form-label">Preço</label>
          <input
            id="preco"
            type="number"
            step="0.01"
            className="form-control"
            required
            value={produto.preco}
            onChange={(e) => atualizarCampo("preco", e.target.value)}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="estoque" className="form-label">Estoque</label>
          <input
            id="estoque"
            type="number"
            className="form-control"
            required
            value={produto.estoque}
            onChange={(e) => atualizarCampo("estoque", e.target.value)}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="imagemUrl" className="form-label">URL da Imagem</label>
          <input
            id="imagemUrl"
            type="text"
            className="form-control"
            value={produto.imagemUrl}
            onChange={(e) => atualizarCampo("imagemUrl", e.target.value)}
          />
        </div>

        <button type="submit" className="btn btn-primary">Salvar</button>
      </form>
    </div>
  );
}

export default EditarProduto;