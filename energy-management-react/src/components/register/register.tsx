import './register.css'

import {useRef} from "react";
import {IUser} from "../user/user.tsx";
import {useNavigate} from "react-router-dom";
import {jwtDecode} from "jwt-decode";

const URL = "http://localhost:8080/spring-demo/api/auth"
const Register = () => {

    const inputName = useRef<any>(null);
    const inputPassword = useRef<any>(null);
    const navigate = useNavigate();

    const handleSubmit = () => {
        if(inputName.current.value === "" && inputPassword.current.value === ""){
            alert("Name and Password are Required!");
        } else {
            const newUser: IUser = {
                name: inputName.current.value,
                role: "client",
                password: inputPassword.current.value
            };
            const optionsPost = {
                method: "POST",
                body: JSON.stringify(newUser),
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
            };
            fetch(`${URL}/register`, optionsPost)
                .then((response) => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    return response.json();
                })
                .then((data) => {
                    console.log("Token:", data.token);
                    inputName.current.value = "";
                    inputPassword.current.value = "";
                    localStorage.setItem("jwtToken", data.token);
                    const jwtToken = localStorage.getItem('jwtToken');
                        if (jwtToken) {
                            try {
                                const decodedToken = jwtDecode(jwtToken);

                                // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                                // @ts-ignore
                                const name = decodedToken.name;
                                // console.log("User Name:", name);
                                alert(`Created User with Name: ${name}`);
                                navigate("/login");
                            } catch (error) {
                                console.error("Error decoding JWT token:", error);
                            }
                        } else {
                            console.error("JWT token not found in localStorage");
                        }
                }).catch((error) => {
                console.error('Error:', error.message);
            });
        }
    }

    const handleLogin = () => {
        navigate("/login");
    }
    return (
        <div className={"register-form"}>
            <label className={"header"}>Register</label>
            <label>Name:</label>
            <input
                type="text"
                ref={inputName}
            /><label>Password:</label>
            <input
                type="text"
                ref={inputPassword}
            />
            <button onClick={handleSubmit}>Submit</button>
            <button onClick={handleLogin}>Login</button>
        </div>
    );
}
export default Register;