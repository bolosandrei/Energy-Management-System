import './user.css'
import {useRef, useState} from "react";

export interface IUser {
    id?: string;
    name: string;
    role?: string;
    password: string;
}

interface IUserProps {
    user: IUser;
    functionDelete: (id?: string) => void;
    functionUpdate: (name: string, role: string, password: string, id?: string) => void;
}

const User = ({user, functionDelete, functionUpdate}: IUserProps) => {
    const [editMode, setEditMode] = useState(false);

    // const [input, setInput] = useState('');
    const inputName = useRef<any>(null);
    const inputRole = useRef<any>(null);
    const inputPassword = useRef<any>(null);

    const handleDelete = () => {
        functionDelete(user.id);
    };

    const onSave = () => {
        setEditMode(false);
        functionUpdate(inputName.current.value, inputRole.current.value, inputPassword.current.value, user.id);
    }

    const handleEdit = () => {
        setEditMode((prev) => !prev);
    };
    return (
        <>
            {editMode ? (
                <li className="user">
                    <label>Edit User:</label>
                    <input type="text"
                           defaultValue={user?.name}
                           ref={inputName}
                    />
                    <input type="text"
                           defaultValue={user?.password}
                           ref={inputPassword}
                    />
                    <label>Role: "admin"/"client"</label>
                    <input type="text"
                           defaultValue={user?.role}
                           ref={inputRole}
                    />
                    <button onClick={onSave}>Save</button>
                    <button onClick={handleEdit}>Cancel</button>
                </li>
            ) : (
                <li className={"user"}>
                    <label className={"info"}>{user.name}</label>
                    <button onClick={handleEdit}>Edit</button>
                    <button onClick={handleDelete}>Delete</button>
                </li>
            )}
        </>
    )
};
export default User;