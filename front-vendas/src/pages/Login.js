import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

function Login() {
  const navegar = useNavigate();
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");

  function entrar(e) {
    e.preventDefault();
    setErro("");

    api.post("/auth/login", { email, senha })
      .then((resposta) => {
        localStorage.setItem("token", resposta.data.token);
        localStorage.setItem("role", resposta.data.role);
        navegar("/");
        window.location.reload(); // atualiza a navbar para refletir o login
      })
      .catch(() => {
        setErro("Email ou senha incorretos.");
      });
  }

  return (
    <div className="col-12 col-md-5">
      <h2 className="mb-4">Entrar</h2>

      {erro && <div className="alert alert-danger" role="alert">{erro}</div>}

      <form onSubmit={entrar}>
        <div className="mb-3">
          <label htmlFor="email" className="form-label">Email</label>
          <input
            id="email"
            type="email"
            className="form-control"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>

        <div className="mb-3">
          <label htmlFor="senha" className="form-label">Senha</label>
          <input
            id="senha"
            type="password"
            className="form-control"
            required
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
          />
        </div>

        <button type="submit" className="btn btn-primary">Entrar</button>
      </form>
    </div>
  );
}

export default Login;