import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getCompanyAdminsAPI } from "../../backend-api/UsersController";


const AdminComplaints = () => {
    
    const [stateAdmins, setStateAdmins] = useState([]);

    useEffect(() => {
        populateAdmins();
    }, []);

    const populateAdmins = async () => {
        const admins = await getCompanyAdminsAPI();
        setStateAdmins(admins);
    };

    return(
        <>
             <div className="container p-2">
                <div className="row">
                    {stateAdmins && stateAdmins.map(iterable => (
                    <div className="col col-md-4">
                        <div className="card" >
                            <div className="card-body">
                                <h5 className="card-title">{iterable.name} {iterable.surname}</h5>
                                <p className="card-text">{iterable.description}</p>
                                <Link to={"/registered/complaints/admin/" + iterable.id} className="btn btn-primary">Write complaint</Link>
                            </div>
                        </div>
                    </div>
                    ))}
                </div>
            </div>
        </>
    );
};

export default AdminComplaints;