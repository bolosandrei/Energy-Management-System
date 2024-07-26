import './admin-page.css'
import {useNavigate} from "react-router-dom";
import {jwtDecode} from "jwt-decode";

const AdminPage = () => {

    const jwtToken = localStorage.getItem("jwtToken")
    const decodedToken = jwtDecode(jwtToken!);
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    const userName = decodedToken.name;

    const navigate = useNavigate();
    return (
        <div className={"admin-page"}>
            <label>Logged in as:</label>
            <h2>{userName}</h2>
            <button onClick={() => navigate('/chat')}>Chat</button>
            <button onClick={() => navigate('/users')}>Manage Users</button>
            <button onClick={() => navigate(-1)}>Back</button>
        </div>
    );
}
export default AdminPage;