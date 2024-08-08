import React, { useState } from "react";
import { signInAPI } from "../backend-api/IdentityController";

const URSignInPage = () => {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const signIn = async () => {
        const returnValue = await signInAPI({
            username: email,
            password,
        });

        if (returnValue[0]) {
            localStorage.setItem("EMAIL", email);
            localStorage.setItem("PASSWORD", password);
            localStorage.setItem("ROLE", returnValue[0].role);
            window.location.href = "/";
        } else {
            alert(returnValue[1])
        }
    }

    return (
        <div className="container">
            <div className="row">
            <form>
                <div class="form-group">
                    <label for="exampleInputEmail1">Email address</label>
                    <input type="email" class="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)} />
                    <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
                </div>
                <div class="form-group">
                    <label for="exampleInputPassword1">Password</label>
                    <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password" 
                        value={password}
                        onChange={(e) => setPassword(e.target.value)} />
                </div>
                <button type="button" onClick={() => signIn()} class="btn btn-primary">Submit</button>
                </form>
            </div>
        </div>
    )
}

export default URSignInPage;




