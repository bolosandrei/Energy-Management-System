import './device.css'

import {useRef, useState} from "react";
import {useNavigate} from "react-router-dom";

export interface IDevice {
    id?: string;
    description: string;
    address: string;
    maximumHourlyEnergyConsumption: number;
    userId: string;
}

interface IDeviceProps {
    device: IDevice;
    functionDelete: (id?: string) => void;
    functionUpdate: (description: string, address: string, maximumHourlyEnergyConsumption: number, userId: string, id?: string) => void;
}

const Device = ({device, functionDelete, functionUpdate}: IDeviceProps) => {
    const [editMode, setEditMode] = useState(false);
    const navigate = useNavigate();
    // const [input, setInput] = useState('');
    const inputDescription = useRef<any>(null);
    const inputAddress = useRef<any>(null);
    const inputMaximumHourlyEnergyConsumption = useRef<any>(null);
    const inputUserId = useRef<any>(null);

    const handleDelete = () => {
        functionDelete(device.id);
    };

    const onSave = () => {
        setEditMode(false);
        functionUpdate(inputDescription.current.value, inputAddress.current.value, inputMaximumHourlyEnergyConsumption.current.value, device.userId, device.id);
    }

    const handleEdit = () => {
        setEditMode((prev) => !prev);
    };
    return (
        <>
            {editMode ? (
                <li className="device">
                    <label>Edit Device:</label>
                    <label>Description:</label>
                    <input type="text"
                           defaultValue={device?.description}
                           ref={inputDescription}
                    />
                    <label>Address:</label>
                    <input type="text"
                           defaultValue={device?.address}
                           ref={inputAddress}
                    />
                    <label>Maximum Hourly Energy Consumption:</label>
                    <input type="number"
                           defaultValue={device?.maximumHourlyEnergyConsumption}
                           ref={inputMaximumHourlyEnergyConsumption}
                    />
                    <label>User ID:</label>
                    <input type="text"
                           defaultValue={device?.userId}
                           ref={inputUserId}
                    />
                    <button onClick={onSave}>Save</button>
                    <button onClick={handleEdit}>Cancel</button>
                </li>
            ) : (
                <li className={"device"}>
                    <button onClick={() => navigate({pathname: `/monitoring/${device.id}`})}>Monitor Device</button>
                    <label className={"info"}>Device ID: {device.id}</label>
                    <label className={"info"}>Description: {device.description}</label>
                    <label className={"info"}>Address: {device.address}</label>
                    <label
                        className={"info"}>MaximumHourlyEnergyConsumption: {device.maximumHourlyEnergyConsumption}</label>
                    <label className={"info"}>UserId: {device.userId}</label>
                    <button onClick={handleEdit}>Edit</button>
                    <button onClick={handleDelete}>Delete</button>
                </li>
            )}
        </>
    )
};


export default Device;