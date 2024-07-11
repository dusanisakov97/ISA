import './App.css';
import { Link, Route, Routes } from 'react-router-dom';
import UrlNotFoundPage from './components/UrlNotFound';
import LandingPage from './components/unregistered/LandingPage';
import URCompaniesPage from './components/unregistered/URCompaniesPage';
import URProductsPage from './components/unregistered/URProductsPage';
import SignInPage from './components/unregistered/SignInPage';

function App() {
  const unregisteredUserUrlPrefix = '/unregistered'
  return (
    <>
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <div className="container-fluid">
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav">
            <li className="nav-item">
              <Link className="nav-link active" aria-current="page" to={"/"}>Home</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to={"/unregistered/companies"}>Company</Link>
            </li>
            <li className="nav-item">
            <Link className="nav-link" to={"/unregistered/sign-in"}>Sign in</Link>
            </li>
            <li className="nav-item">
              <a className="nav-link disabled" href="#" tabindex="-1" aria-disabled="true">Disabled</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path={unregisteredUserUrlPrefix + '/companies'} element={<URCompaniesPage />} />
      <Route path={unregisteredUserUrlPrefix + '/sign-in'} element={<SignInPage />} />
      <Route path={unregisteredUserUrlPrefix + '/products/company/:companyId'} element={<URProductsPage />} />
      <Route path="*" element={<UrlNotFoundPage />} />
    </Routes>
    </>  
);
}

export default App;
