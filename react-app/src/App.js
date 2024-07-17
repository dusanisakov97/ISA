import './App.css';
import { Link, Route, Routes } from 'react-router-dom';
import UrlNotFoundPage from './components/UrlNotFound';
import LandingPage from './components/unregistered/LandingPage';
import URCompaniesPage from './components/unregistered/URCompaniesPage';
import URProductsPage from './components/unregistered/URProductsPage';
import URSignInPage from './components/unregistered/URSignInPage';
import URRegisterPage from './components/unregistered/URSRegisterPage';
import { useEffect, useState } from 'react';

function App() {
  const unregisteredUserUrlPrefix = '/unregistered'

  const [email, setEmail] = useState('')

  useEffect(() => {
    setEmail(localStorage.getItem('EMAIL'));
  }, []);

  return (
    <>
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <div className="container-fluid">
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
            {email && 
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
                <Link className="nav-link" to={"/unregistered/register"}>Register</Link>
              </li>
            </ul>
            }

            {!email && 
            <ul className="navbar-nav">
              <li className="nav-item">
                <Link className="nav-link active" aria-current="page" to={"/"}>Home</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to={"/registered/companies"}>Company</Link>
              </li>
              <li className="nav-item">
                <button className="nav-link">Loogut</button>
              </li>
            </ul>
            }
        </div>
      </div>
    </nav>
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path={unregisteredUserUrlPrefix + '/companies'} element={<URCompaniesPage />} />
      <Route path={unregisteredUserUrlPrefix + '/sign-in'} element={<URSignInPage />} />
      <Route path={unregisteredUserUrlPrefix + '/register'} element={<URRegisterPage />} />
      <Route path={unregisteredUserUrlPrefix + '/products/company/:companyId'} element={<URProductsPage />} />
      <Route path="*" element={<UrlNotFoundPage />} />
    </Routes>
    </>  
);
}

export default App;
