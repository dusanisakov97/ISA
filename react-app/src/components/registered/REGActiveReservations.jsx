import { useEffect, useState } from "react";
import { deleteReservationAPI, getActiveReservationsAPI } from "../backend-api/ReservationController";


const REGActiveReservations = () => {
    const [stateReservations, setStateReservations] = useState([]);

    useEffect(() => {
        populateReservations();
    }, []);

    const populateReservations = async () => {
        const companies = await getActiveReservationsAPI();
        setStateReservations(companies);
    };

    const deleteReservation = async (reservationId) => {
        const error = await deleteReservationAPI(reservationId);
        if (error) {
            alert(error);
        } else {
            await populateReservations();
        }
    }

    return(
        <>
        <div className="container p-2">
            <div className="row">
                {stateReservations && stateReservations.map(iterable => (
                <div className="col col-md-4">
                    <ul class="list-group">
                        <li class="list-group-item">{iterable.company}, {iterable.where}, @ {iterable.when}
                             <button className="btn btn-info"
                             onClick={() => deleteReservation(iterable.id)}>Delete</button></li>
                    </ul>
                </div>
                ))}
            </div>
        </div>
        </>
    );
};

export default REGActiveReservations;