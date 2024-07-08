import { useEffect, useState } from "react";
import { getProductsByCompany } from "../backend-api/CompanyController";
import { useParams } from "react-router-dom";


const URProductsPage = () => {
    const {companyId} = useParams();

    const [stateProducts, setStateProducts] = useState([]);

    useEffect(() => {
        populateProducts();
    }, []);

    const populateProducts = async () => {
        const companies = await getProductsByCompany(companyId);
        setStateProducts(companies);
    };

    return(
        <>
        <div className="container p-2">
            <div className="row">
                {stateProducts && stateProducts.map(iterable => (
                <div className="col col-md-4">
                    <div class="card" >
                        <div class="card-body">
                            <h5 class="card-title">{iterable.name}</h5>
                            <p class="card-text">{iterable.description}</p>
                        </div>
                    </div>
                </div>
                ))}
            </div>
        </div>
        </>
    );
};

export default URProductsPage;