import React, { useState } from "react";
import { registerAPI } from "../backend-api/IdentityController";
import { useNavigate } from "react-router-dom";

const URRegisterPage = () => {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');
    const [town, setTown] = useState('');
    const [country, setCountry] = useState('');
    const [phone, setPhone] = useState('');
    const [work, setWork] = useState('');

    const navigate = useNavigate();

    const register = async () => {
        
        const returnValue = await registerAPI({
            name,
            surname,
            email,
            password,
            town, 
            country,
            work, 
            phone
        });

        if (returnValue[0]) {
            alert('Successfully registered')

            navigate('/unergistered/login');
        } else {
            alert(returnValue[1])
        }

    }

    return (
        <div className="container">
            <div className="row">
                <form class="row g-3" >
                    <div class="col-md-6">
                        <label for="inputEmail4" class="form-label">Email</label>
                        <input type="email" class="form-control" id="inputEmail4"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)} />
                    </div>
                    <div class="col-md-6">
                        <label for="inputPassword4" class="form-label">Password</label>
                        <input type="password" class="form-control" id="inputPassword4" 
                         value={password}
                         onChange={(e) => setPassword(e.target.value)} />
                    </div>

                    <div class="col-md-6">
                        <label for="inputString1" class="form-label">Town</label>
                        <input type="text" class="form-control" id="inputString1" 
                         value={town}
                         onChange={(e) => setTown(e.target.value)} />
                    </div>
                    <div class="col-md-6">
                        <label for="inputString2" class="form-label">Country</label>
                        <input type="text" class="form-control" id="inputString2" 
                         value={country}
                         onChange={(e) => setCountry(e.target.value)} />
                    </div>

                    <div class="col-md-6">
                        <label for="inputString3" class="form-label">First name</label>
                        <input type="text" class="form-control" id="inputString3" 
                         value={name}
                         onChange={(e) => setName(e.target.value)} />
                    </div>
                    <div class="col-md-6">
                        <label for="inputString4" class="form-label">Surname</label>
                        <input type="text" class="form-control" id="inputString4" 
                         value={surname}
                         onChange={(e) => setSurname(e.target.value)} />
                    </div>

                    <div class="col-md-6">
                        <label for="inputString6" class="form-label">Phone</label>
                        <input type="text" class="form-control" id="inputString6" 
                         value={phone}
                         onChange={(e) => setPhone(e.target.value)} />
                    </div>

                    <div class="col-md-6">
                        <label for="inputString5" class="form-label">Work</label>
                        <input type="text" class="form-control" id="inputString5" 
                         value={work}
                         onChange={(e) => setWork(e.target.value)} />
                    </div>
                    
                    <div class="col-12">
                        <button type="button" onClick={() => register()} class="btn btn-primary"
                            disabled={!(name !== '' && surname !== '' && password !== '' && email !== '' && country !== '' && town !== '')}
                            >Register</button>
                    </div>
                </form>
            </div>
        </div>
    )
}

export default URRegisterPage;




