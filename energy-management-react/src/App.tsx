import './App.css'
import {BrowserRouter as Router, Navigate, Route, Routes,} from 'react-router-dom';
import Login from "./components/login/login.tsx";
import Register from "./components/register/register.tsx";
import AdminPage from "./components/admin-page/admin-page.tsx";
import ClientPage from "./components/client-page/client-page.tsx";
import DeviceList from "./components/device-list/device-list.tsx";
import UserList from "./components/user-list/user-list.tsx";

// // @ts-ignore
// import {ChatPage} from './components/chat-page';

import ChatPage from "./components/chat-page/chat-page.jsx";
import Monitoring from "./components/monitoring/monitoring.tsx";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate to="/login"/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/register" element={<Register/>}/>
                <Route path="/devices" element={<DeviceList/>}/>
                <Route path="/users" element={<UserList/>}/>
                <Route path="/admin" element={<AdminPage/>}/>
                <Route path="/client" element={<ClientPage/>}/>
                <Route path="/chat" element={<ChatPage/>}/>
                <Route path="/monitoring/:deviceId" element={<Monitoring/>}
                />
            </Routes>
        </Router>
    );
}

export default App
