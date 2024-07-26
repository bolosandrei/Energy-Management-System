import './login.css'
import {useRef} from 'react';
import {useNavigate} from "react-router-dom";
import {IUser} from "../user/user.tsx";
import {jwtDecode} from "jwt-decode";

const URL = "http://localhost:8080/spring-demo/api/auth";

// interface ILoginProps {
//     setUserRole: (role: string) =>void;
//     setUserId: (id: string) =>void;
//     userRole: string;
// }
const Login = () => {
    const navigate = useNavigate();
    const inputName = useRef<any>(null);
    const inputPassword = useRef<any>(null);
    localStorage.setItem("jwtToken", '');

    const handleRegister = () => {
        navigate('/register');
    }
    const handleLogin = () => {
        const newUser: IUser = {
            name: inputName.current.value,
            // role: "client",
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
        fetch(`${URL}/authenticate`, optionsPost)
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

                        // @ts-ignore
                        const userRole = decodedToken.role;

                        console.log("User Role:", userRole);
                        if (userRole === "admin") {
                            navigate("/admin");
                        } else {
                            navigate("/client");
                        }
                    } catch (error) {
                        console.error("Error decoding JWT token:", error);
                    }
                } else {
                    console.error("JWT token not found in localStorage");
                }

            }).catch((error) => {
            console.error('Error:', error.message);
            inputName.current.value = "";
            inputPassword.current.value = "";
            alert("Access denied! Use valid Name and Password");
        });
    }
    return (
        <div className={"login-form"}>
            <label className={"header"}>Login</label>
            <label>Name:</label>
            <input
                type="text"
                ref={inputName}
            /><label>Password:</label>
            <input
                type="text"
                ref={inputPassword}
            />
            <button onClick={handleLogin}>Login</button>
            <button onClick={handleRegister}>Register</button>
        </div>
    );
};

export default Login;
