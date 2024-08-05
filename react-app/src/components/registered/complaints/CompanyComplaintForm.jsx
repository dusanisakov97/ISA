import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { checkIsAllowedToWriteComplaintForCompany, saveCompanyComplaintAPI } from "../../backend-api/ReservationController";


const CompanyComplaintForm = () => {
    
    const [isAllowedToWriteComplaint, setIsAllowedToWriteComplaint] = useState([]);
    const [complaint, setComplaint] = useState('');

    const {id} = useParams();

    useEffect(() => {
        performValidation();
    }, []);

    const performValidation = async () => {
        const isAllowed = await checkIsAllowedToWriteComplaintForCompany(id);
        setIsAllowedToWriteComplaint(isAllowed);
    };

    const saveComplaint = async () => {
        const success = await saveCompanyComplaintAPI({companyId: id, content: complaint});
        if (success) {
            alert('Complaint saved')
            window.history.go(-1);
        } else {
            alert('Complaint failed to be saved')
        }
    }

    return(
        <>
             <div className="container p-2">
                <div className="row">
                    
                    {!isAllowedToWriteComplaint && <h1 className="text-danger">You had no interaction with this company.</h1>}

                    {isAllowedToWriteComplaint && 
                    <form>
                        <div class="col-md-6">
                            <label for="inputEmail4" class="form-label">Complaint</label>
                            <input type="text" class="form-control" id="inputEmail4"
                            value={complaint}
                            onChange={(e) => setComplaint(e.target.value)} />
                        </div> 

                        <div>
                            <button type="button" className="btn btn-info mt-2" onClick={() => saveComplaint()}>Save complaint</button>
                        </div>
                        
                    </form>}
                    
                </div>
            </div>
        </>
    );
};

export default CompanyComplaintForm;