import './App.css';
import { Link, Route, Routes } from 'react-router-dom';
import UrlNotFoundPage from './components/UrlNotFound';
import LandingPage from './components/unregistered/LandingPage';
import URCompaniesPage from './components/unregistered/URCompaniesPage';
import URProductsPage from './components/unregistered/URProductsPage';
import URSignInPage from './components/unregistered/URSignInPage';
import URRegisterPage from './components/unregistered/URSRegisterPage';
import { useEffect, useState } from 'react';
import REGCompaniesPage from './components/registered/REGCompaniesPage';
import REGProductsPage from './components/registered/REGProductsPage';
import REGActiveReservations from './components/registered/REGActiveReservations';
import REGComplaintsPage from './components/registered/REGComplaintsPage';
import CompanyComplaintForm from './components/registered/complaints/CompanyComplaintForm';
import AdminComplaintForm from './components/registered/complaints/AdminComplaintForm';
import ADComplaintsPage from './components/admin/ADComplaintsPage';

function App() {
  const unregisteredUserUrlPrefix = '/unregistered'
  const registeredUserUrlPrefix = '/registered'

  const [role, setRole] = useState('')

  useEffect(() => {
    setRole(localStorage.getItem('ROLE'));
  }, []);

  const logout = () => {
    localStorage.clear();
    window.location.href = '/';
  }

  return (
    <>
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <div className="container-fluid">
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
            {!role && 
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

            {role && role === 'ROLE_USER' &&
            <ul className="navbar-nav">
              <li className="nav-item">
                <Link className="nav-link active" aria-current="page" to={"/"}>Home</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to={"/registered/companies"}>Company</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to={"/registered/active-reservations"}>Reservations</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to={"/registered/complaints"}>Complaints</Link>
              </li>
              <li className="nav-item">
                <button className="nav-link" onClick={() => logout()}>Logout</button>
              </li>
            </ul>
            }
            {role && role === 'ROLE_SYS_ADMIN' &&
            <ul className="navbar-nav">
              <li className="nav-item">
                <Link className="nav-link active" aria-current="page" to={"/"}>Home</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to={"/registered/complaints/admin"}>Complaints</Link>
              </li>
              <li className="nav-item">
                <button className="nav-link" onClick={() => logout()}>Logout</button>
              </li>
            </ul>
            }
        </div>
      </div>
    </nav>
    <Routes>
      <Route path="/" element={<LandingPage />} />
      {!role &&
      <>
        <Route path={unregisteredUserUrlPrefix + '/companies'} element={<URCompaniesPage />} />
        <Route path={unregisteredUserUrlPrefix + '/sign-in'} element={<URSignInPage />} />
        <Route path={unregisteredUserUrlPrefix + '/register'} element={<URRegisterPage />} />
        <Route path={unregisteredUserUrlPrefix + '/products/company/:companyId'} element={<URProductsPage />} />
        <Route path="*" element={<UrlNotFoundPage />} />
      </>
      }

      {role && role === 'ROLE_USER' &&
      <>
        <Route path={registeredUserUrlPrefix + '/companies'} element={<REGCompaniesPage />} />
        <Route path={registeredUserUrlPrefix + '/active-reservations'} element={<REGActiveReservations />} />
        <Route path={registeredUserUrlPrefix + '/complaints'} element={<REGComplaintsPage />} />
        <Route path={registeredUserUrlPrefix + '/complaints/company/:id'} element={<CompanyComplaintForm />} />
        <Route path={registeredUserUrlPrefix + '/complaints/admin/:id'} element={<AdminComplaintForm />} />
        <Route path={registeredUserUrlPrefix + '/products/company/:companyId'} element={<REGProductsPage />} />
        <Route path="*" element={<UrlNotFoundPage />} />
      </>
      }

      {role && role === 'ROLE_SYS_ADMIN' &&
      <>
        <Route path={registeredUserUrlPrefix + '/complaints/admin'} element={<ADComplaintsPage />} />
      </>
      }
    </Routes>
    </>  
);
}

export default App;
