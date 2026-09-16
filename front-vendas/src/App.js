import { BrowserRouter, Routes, Route, Link, useNavigate } from "react-router-dom";
import Catalogo from "./pages/Catalogo";
import NovoProduto from "./pages/NovoProduto";
import EditarProduto from "./pages/EditarProduto";
import Login from "./pages/Login";

function Navbar() {
  const navegar = useNavigate();
  const role = localStorage.getItem("role");
  const logado = !!localStorage.getItem("token");

  function sair() {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    navegar("/");
    window.location.reload();
  }

  return (
    <nav className="navbar navbar-dark bg-dark px-4 flex-wrap gap-2" aria-label="Navegação principal">
      <Link className="navbar-brand" to="/">Minha Loja</Link>
      <div className="d-flex gap-2">
        {role === "ADMIN" && (
          <Link className="btn btn-outline-light" to="/produto/novo">Novo Produto</Link>
        )}
        {logado ? (
          <button className="btn btn-outline-light" onClick={sair}>Sair</button>
        ) : (
          <Link className="btn btn-outline-light" to="/login">Entrar</Link>
        )}
      </div>
    </nav>
  );
}

function App() {
  return (
    <BrowserRouter>
      <div className="d-flex flex-column min-vh-100">
        <header>
          <Navbar />
        </header>

        <main className="container my-4 flex-grow-1">
          <Routes>
            <Route path="/" element={<Catalogo />} />
            <Route path="/login" element={<Login />} />
            <Route path="/produto/novo" element={<NovoProduto />} />
            <Route path="/produto/editar/:id" element={<EditarProduto />} />
          </Routes>
        </main>

        <footer className="bg-dark text-white text-center py-3">
          <p className="mb-0">© 2026</p>
        </footer>
      </div>
    </BrowserRouter>
  );
}

export default App;