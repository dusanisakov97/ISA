import { useEffect, useState } from "react";
import { getProductsByCompany } from "../backend-api/CompanyController";
import { useParams } from "react-router-dom";
import { getTimeSlotsByCompany } from "../backend-api/TimeSlotController";
import { makeOrderAPI } from "../backend-api/ReservationController";


const REGProductsPage = () => {
    const {companyId} = useParams();

    const [stateProducts, setStateProducts] = useState([]);
    const [stateTimeSlots, setStateTimeSlots] = useState([]);
    const [stateSelectedSlot, setSelectedSlot] = useState(null);

    useEffect(() => {
        populateProducts();
    }, []);

    const populateProducts = async () => {
        const companies = await getProductsByCompany(companyId);
        setStateProducts(companies);
    };

    const populateTimeSlots = async () => {
        const timeSlots = await getTimeSlotsByCompany(companyId);
        setStateTimeSlots(timeSlots);
    }

    const add = (productId) => {
        const products = [...stateProducts];
        const product = products.find(x => x.id === productId);
        if (product.selected) {
            product.selected++;
        } else {
            product.selected = 1;
        }

        setStateProducts(products);
    }

    
    const reduce = (productId) => {
        const products = [...stateProducts];
        const product = products.find(x => x.id === productId);
        
        product.selected--;
        

        setStateProducts(products);
    }

    const pickSlot = (slot) => {
        setSelectedSlot(slot);
    }

    const unpickSlot = (slot) => {
        setSelectedSlot(null);
    }

    const makeReservation = async () => {
        const requestBody = {items: [], timeSlot: stateSelectedSlot};
        for (let i = 0; i < stateProducts.length; i++) {
            if (stateProducts[i].selected && stateProducts[i].selected > 0) {
                requestBody.items.push({
                    productId: stateProducts[i].id,
                    selected: stateProducts[i].selected
                })
            }
        }

        const error = await makeOrderAPI(requestBody);
        if (error !== null) {
            if (typeof error === 'string')
                alert(error)
            else
                alert('Reservation failed. Please try again later')
        } else {
            alert('Reservation saved')
            window.location.href = "/";
        }
    }

    return(
        <>
        <div className="container p-2">
            <div className="row">
                {stateProducts && stateProducts.map(iterable => (
                <div className="col col-md-4">
                    <div className="card" >
                        <div className="card-body">
                            <h5 className="card-title">{iterable.name}</h5>
                            <p className="card-text"> Available: {iterable.lager}</p>
                            <p className="card-text"> Price: {iterable.price}</p>
                            <p className="card-text">
                                <button className="btn btn-info"
                                    disabled={!iterable.selected}
                                    onClick={() => reduce(iterable.id)}>-</button>
                                <span className="mx-3">{iterable.selected || 0}</span>
                                <button className="btn btn-info" 
                                    disabled={iterable.lager === 0 || (iterable.selected && iterable.selected === iterable.lager)}
                                    onClick={() => add(iterable.id)}>+</button>
                            </p>
                        </div>
                    </div>
                </div>
                ))}
            </div>
        </div>





        <button type="button" class="floating-button" data-bs-toggle="modal" data-bs-target="#exampleModal"
            hidden={!stateProducts.find(x => x.selected && x.selected > 0)}
            onClick={() => populateTimeSlots()}
        >
            Proceed to reservation
        </button>

        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">Confirm reservation</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <p>Products for reservation</p>
                {stateProducts && stateProducts.map(iterable => (
                    <>
                        {iterable.selected && iterable.selected > 0 && 
                            <p>
                                <span className="mx-1">{iterable.name}</span>
                                <span className="mx-1">{iterable.selected}</span>
                                <span className="mx-1">x</span>
                                <span className="mx-1">{iterable.price} RSD</span>
                            </p>
                        }
                    </>                
                ))}

                <p className="mt-1">Time slots</p>                
                {stateTimeSlots && stateTimeSlots.map(iterable => (
                    <>
                        
                        <p>
                            <span className="mx-1">{iterable.dateTime.replace('T', ' ')}</span>
                            {stateSelectedSlot && stateSelectedSlot.id === iterable.id && 
                                <button className="btn btn-danger" onClick={() => unpickSlot(iterable)}>Unpick</button>
                            }
                            {stateSelectedSlot && stateSelectedSlot.id !== iterable.id && 
                                <button className="btn btn-info" onClick={() => pickSlot(iterable)} disabled>Pick</button>
                            }
                            {!stateSelectedSlot &&
                                <button className="btn btn-info" onClick={() => pickSlot(iterable)}>Pick</button>
                            }
                            
                        </p>
                        
                    </>
                ))}

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary"
                    disabled={!stateSelectedSlot || !stateProducts.find(x => x.selected && x.selected > 0)}
                    onClick={() => makeReservation()}>
                    Make reservation</button>
            </div>
            </div>
        </div>
        </div>
        </>
    );
};

export default REGProductsPage;