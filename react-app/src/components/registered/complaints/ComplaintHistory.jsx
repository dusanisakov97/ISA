import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getComplaintHistoryAPI } from "../../backend-api/ReservationController";


const ComplaintHistory = () => {
    
    const [stateHistory, setStateHistory] = useState([]);

    useEffect(() => {
        populateHistory();
    }, []);

    const populateHistory = async () => {
        const history = await getComplaintHistoryAPI();
        setStateHistory(history);
    };

    return(
        <>
             <div className="container p-2">
                <div className="row">
                    {stateHistory && stateHistory.map(iterable => (
                     <div className="col col-md-12">
                        <ul class="list-group">
                            <li class="list-group-item">
                                {iterable.company && <span>Complaint for company {iterable.company}</span>}
                                {iterable.admin && <span>Complaint for admin {iterable.admin}</span>} 
                                {<span> - "{iterable.content}"</span>}
                                {<span>, at {iterable.dateTime.replace('T', ' ')}</span>}
                                {iterable.response && <span> . Admin responded with "{iterable.response}"</span>}
                                {!iterable.response && <span> . Admin not responded yet.</span>}
                            </li>
                        </ul>
                    </div>
                    ))}
                </div>
            </div>
        </>
    );
};

export default ComplaintHistory;