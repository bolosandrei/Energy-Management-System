import './user-list.css'

import {useEffect, useMemo, useRef, useState} from "react";
import User, {IUser} from "../user/user.tsx";
import {useNavigate} from "react-router-dom";
import {jwtDecode} from "jwt-decode";

const URL = "http://localhost:8080/spring-demo/users";
const baseUrl = "http://localhost:8080/spring-demo/api/auth"
const UserList = () => {
    const [users, setUsers] = useState<IUser[]>([]);
    const inputName = useRef<any>(null);
    const inputPassword = useRef<any>(null);
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();
    const printUsers = useMemo(() => {
        return [...users];

    }, [users]);
    useEffect(() => {
        const jwtToken = localStorage.getItem('jwtToken');
        if (jwtToken) {
            try {
                const decodedToken = jwtDecode(jwtToken);

                // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                // @ts-ignore
                const userRole = decodedToken.role;
                console.log("User Role:", userRole);
            } catch (error) {
                console.error("Error decoding JWT token:", error);
            }
        } else {
            console.error("JWT token not found in localStorage");
        }
        const optionsGet = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwtToken}`,
                'Content-Type': 'application/json',
            },
        };
        fetch(`${URL}`, optionsGet)
            .then((response) => response.json())
            .then((data) => {
                setUsers(data);
                console.log("DATA:", data);
                inputName.current.value = "";
                inputPassword.current.value = "";
            });
    }, [isLoading]);

    const createNewUser = () => {
        if (inputName.current.value === "" && inputPassword.current.value === "") {
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
            fetch(`${baseUrl}/register`, optionsPost)
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
                            console.log("New User Name:", name);
                            setIsLoading(false);
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
    };
    const deleteById = (id?: string) => {
        const jwtToken = localStorage.getItem('jwtToken');
        const optionsDELETE = {
            method: "DELETE",
            headers: {
                Accept: "application/json",
                'Authorization': `Bearer ${jwtToken}`,
                "Content-Type": "application/json",
            },
        };
        setIsLoading(true);
        fetch(`${URL}/${id}`, optionsDELETE)
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                console.log("data:", data);
                console.log("DELETE operation");
                const newUsers: IUser[] = users.filter(
                    (user: IUser) => user.id !== data.id
                );
                setIsLoading(false);
                setUsers(newUsers);
            });
    };

    const functionUpdate = (name: string, role: string, password: string, id?: string) => {
        for (let i = 0; i < users.length; i++) {
            if (users[i].id === id) {
                const reqBody = {
                    id: id,
                    name: name,
                    role: role,
                    password: password,
                };

                setIsLoading(true);
                fetch(`${URL}/${id}`, {
                    method: "PUT",
                    headers: {
                        Accept: "application/json",
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(reqBody),
                })
                    .then((response) => {
                        return response.json();
                    })
                    .then((data) => {
                        console.log("data:", data);
                        const newUsers: IUser[] = users.map((user: IUser) => {
                            if (user.id === data.id) {
                                return {
                                    ...user,
                                    name: data.name,
                                    role: data.role,
                                };
                            }
                            setIsLoading(false);
                            return user;
                        });
                        setUsers(newUsers);
                    });
            }
        }
    };

    const navigateBack = () => {
        navigate(-1)
    }

    return (
        <>
            <div>
                <div className="new-item">
                    <label>New User, Name:</label>
                    <input type="text" defaultValue={""} ref={inputName}/>
                    <label>Password:</label>
                    <input type="text" defaultValue={""} ref={inputPassword}/>
                    <button onClick={createNewUser}>Add User</button>
                </div>
                <ul className="list">
                    {printUsers.map((user: IUser) => (
                        <User
                            user={user}
                            functionDelete={deleteById}
                            functionUpdate={functionUpdate}
                            key={user.id}
                        />
                    ))}
                </ul>
                <button onClick={navigateBack}>Back</button>
            </div>
        </>
    )
}
export default UserList;