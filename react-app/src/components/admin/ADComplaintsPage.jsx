import { useEffect, useState } from "react";
import { getAdminComplaintsAPI, saveComplaintResponseAPI } from "../backend-api/ReservationController";


const ADComplaintsPage = () => {
    
    const [stateComplaints, setStateComplaints] = useState([]);
    const [stateSelectedComplaint, setStateSelectedComplaint] = useState(null);
    const [stateResponse, setStateResponse] = useState(null);

    useEffect(() => {
        populateComplaints();
    }, []);

    const populateComplaints = async () => {
        const complaints = await getAdminComplaintsAPI();
        setStateComplaints(complaints);
    };

    const saveResponse = async (id) => {
        const error = await saveComplaintResponseAPI({complaintId: id, response: stateResponse})
        if (!error) {
            alert('Response saved')
            populateComplaints();
        } else {
            alert(error)   
        }
    }

    return(
        <>
             <div className="container p-2">
                <div className="row">
                    {stateComplaints && stateComplaints.map(iterable => (
                     <div className="col col-md-12">
                        <ul class="list-group">
                            <li class="list-group-item">
                                <div>
                                    {iterable.company && <span>Complaint for company {iterable.company}</span>}
                                    {iterable.admin && <span>Complaint for admin {iterable.admin}</span>} 
                                    {<span> - "{iterable.content}"</span>}
                                    {<span>, at {iterable.dateTime.replace('T', ' ')}</span>}
                                    <button className="btn btn-info" onClick={() => {setStateSelectedComplaint(iterable)}}>Answer</button>
                                </div>
                                {stateSelectedComplaint && stateSelectedComplaint.id === iterable.id &&
                                <div>
                                    <input type="text" className="form-control"
                                        value={stateResponse}
                                        onChange={(e) => setStateResponse(e.target.value)} />
                                    <button className="btn btn-info" onClick={() => saveResponse(iterable.id)}>Save response</button>
                                </div>
                                }
                            </li>
                        </ul>
                    </div>
                    ))}
                </div>
            </div>
        </>
    );
};

export default ADComplaintsPage;