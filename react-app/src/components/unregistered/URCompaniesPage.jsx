import { useEffect, useState } from "react";
import { getCompanies } from "../backend-api/CompanyController";


const URCompaniesPage = () => {
    const [stateCompanies, setStateCompanies] = useState([]);

    useEffect(() => {
        populateCompanies();
    }, []);

    const populateCompanies = async () => {
        const companies = await getCompanies();
        setStateCompanies(companies);
    };

    return(
        <>
        <div className="container p-2">
            <div className="row">
                {stateCompanies && stateCompanies.map(iterable => (
                <div className="col col-md-4">
                    <div class="card" >
                        <div class="card-body">
                            <h5 class="card-title">{iterable.name}</h5>
                            <p class="card-text">{iterable.description}</p>
                            <a to={"/"} class="btn btn-primary">Go somewhere</a>
                        </div>
                    </div>
                </div>
                ))}
            </div>
        </div>
        </>
    );
};

export default URCompaniesPage;