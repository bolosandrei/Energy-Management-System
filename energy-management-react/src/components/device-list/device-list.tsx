import './device-list.css'

import {useEffect, useMemo, useRef, useState} from "react";
import device from "../device/device.tsx";
import Device, {IDevice} from "../device/device.tsx";
import {useNavigate} from "react-router-dom";
import {jwtDecode} from "jwt-decode";
// import {IUser} from "../user/user.tsx";

const URL = "http://localhost:8081/spring-demo-device/devices";
const baseUrl = "http://localhost:8080/spring-demo/users"

const DeviceList = () => {
    const [devices, setDevices] = useState<IDevice[]>([]);

    const [userId, setUserId] = useState<any>('');
    const inputDescription = useRef<any>(null);
    const inputAddress = useRef<any>(null);
    const inputMaximumHourlyEnergyConsumption = useRef<any>(null);
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);

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
        const options = {
            headers: {
                'Authorization': `Bearer ${jwtToken}`,
                'Content-Type': 'application/json',
            },
        };
        fetch(`${baseUrl}`, options)
            .then((response) => response.json())
            .then((data) => {
                console.log("DATA:", data);
            });
    }, [isLoading]);

    const printDevices = useMemo(() => {
        const jwtToken = localStorage.getItem('jwtToken');
        if (jwtToken) {
            try {
                const decodedToken = jwtDecode(jwtToken);

                // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                // @ts-ignore
                const userId = decodedToken.id;
                setUserId(userId);
                console.log("User Id:", userId);
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
                console.log("device DATA:", data);
            });
        // // return [...devices];
        return devices.filter(
            (device: IDevice) => device.userId == userId
        );
    }, [devices]);

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
                console.log(data);
                setDevices(data);
            });
    }, [isLoading]);

    const createNewDevice = () => {
        if (inputDescription.current.value === "" || inputAddress.current.value === "" || inputMaximumHourlyEnergyConsumption.current.value === "") {
            alert("Blank Input fields aren't allowed!");
        } else {
            const newDevice: IDevice = {
                description: inputDescription.current.value,
                address: inputAddress.current.value,
                maximumHourlyEnergyConsumption: inputMaximumHourlyEnergyConsumption.current.value,
                userId: userId,
            };
            console.log(newDevice.userId)
            const options = {
                method: "POST",
                body: JSON.stringify(newDevice),
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
            };
            setIsLoading(true);
            fetch(`${URL}`, options)
                .then((response) => response.json())
                .then((data) => {
                    console.log("new Device:", data);
                    inputDescription.current.value = "";
                    inputAddress.current.value = "";
                    inputMaximumHourlyEnergyConsumption.current.value = "";
                    setIsLoading(false);
                });
        }
    };
    const deleteById = (id?: string) => {
        const optionsDELETE = {
            method: "DELETE",
            headers: {
                Accept: "application/json",
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
                const newDevices: IDevice[] = devices.filter(
                    (device: IDevice) => device.id !== data.id
                );
                setIsLoading(false);
                setDevices(newDevices);
            });
    };

    const functionUpdate = (description: string, address: string, maximumHourlyEnergyConsumption: number, userId: string, id?: string) => {
        for (let i = 0; i < device.length; i++) {
            if (devices[i].id === id) {
                const reqBody = {
                    id: id,
                    description: description,
                    address: address,
                    maximumHourlyEnergyConsumption: maximumHourlyEnergyConsumption,
                    userId: userId,
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
                        const newDevices: IDevice[] = devices.map((device: IDevice) => {
                            if (device.id === data.id) {
                                return {
                                    ...device,
                                    description: data.description,
                                    address: data.address,
                                    maximumHourlyEnergyConsumption: data.maximumHourlyEnergyConsumption,
                                    userId: data.userId,
                                };
                            }
                            setIsLoading(false);
                            return device;
                        });
                        setDevices(newDevices);
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
                    <label>New Device:</label>
                    <label>Description:</label>
                    <input type="text" defaultValue={""} ref={inputDescription}/>
                    <label>Address:</label>
                    <input type="text" defaultValue={""} ref={inputAddress}/>
                    <label>MaximumHourlyEnergyConsumption:</label>
                    <input type="text" defaultValue={""} ref={inputMaximumHourlyEnergyConsumption}/>
                    <button onClick={createNewDevice}>Add Device</button>
                </div>
                <ul className="list">
                    {printDevices.map((device: IDevice) => (
                        <Device
                            device={device}
                            functionDelete={deleteById}
                            functionUpdate={functionUpdate}
                            key={device.id}
                        />
                    ))}
                </ul>
                <button onClick={navigateBack}>Back</button>
            </div>
        </>
    )
}

export default DeviceList;