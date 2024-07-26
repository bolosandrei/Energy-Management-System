import React, {useEffect, useState} from 'react'
import {over} from 'stompjs';
import SockJS from 'sockjs-client';
import {useNavigate} from "react-router-dom";
import {jwtDecode} from "jwt-decode";
// import {IUser} from "../user/user.js";
import './chat-page.css'

var stompClient = null;

const URL_server = "http://localhost:8085/spring-demo-chat/ws"
const URL_users = "http://localhost:8080/spring-demo/users"
const ChatPage = () => {
    const navigate = useNavigate();
    const [privateChats, setPrivateChats] = useState(new Map());
    const [publicChats, setPublicChats] = useState([]);
    const [tab, setTab] = useState("CHATROOM");

    const jwtToken = localStorage.getItem("jwtToken")
    const decodedToken = jwtDecode(jwtToken);
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    const userName = decodedToken.name;
    const userId = decodedToken.id;
    const userRole = decodedToken.role;

    const [users, setUsers] = useState([]);

    const optionsGet = {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json',
        },
    };
    const fetchUsers = async () => {
        try {
            const response = await fetch(`${URL_users}`, optionsGet);
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            const data = await response.json();
            data.sort((a, b) => {
                const roleA = a.role;
                const roleB = b.role;
                if (roleA < roleB) {
                    return -1;
                }
                if (roleA > roleB) {
                    return 1;
                }
                return 0;
            });
            setUsers(data);
            // console.log("users fetched:", users);
            return data;
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    }

    // fetchUsers();

    const [userData, setUserData] = useState({
        username: userName,
        userrole: userRole,
        userid: userId,
        receivername: '',
        connected: false,
        message: ''
    });
    useEffect(() => {
        console.log(userData);
    }, [userData]);

    const connect = () => {
        let Sock = new SockJS(`${URL_server}`);
        stompClient = over(Sock);
        stompClient.connect({}, onConnected, onError);
    }

    const onConnected = () => {
        setUserData({...userData, "connected": true});
        stompClient.subscribe('/chatroom/public', onMessageReceived);
        stompClient.subscribe('/user/' + userData.username + '/private', onPrivateMessage);
        userJoin();
    }

    const onMessageReceived = (payload) => {
        var payloadData = JSON.parse(payload.body);
        switch (payloadData.status) {
            case "JOIN":
                if (!privateChats.get(payloadData.senderName)) {
                    privateChats.set(payloadData.senderName, []);
                    setPrivateChats(new Map(privateChats));
                }
                break;
            case "MESSAGE":
                publicChats.push(payloadData);
                setPublicChats([...publicChats]);
                break;
        }
    }

    const userJoin = () => {
        var chatMessage = {
            senderName: userData.username,
            status: "JOIN"
        };
        stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
    }

    const onPrivateMessage = (payload) => {
        // console.log(payload);
        var payloadData = JSON.parse(payload.body);
        console.log("bodyyy:", payload.body)
        if (privateChats.get(payloadData.senderName)) {
            privateChats.get(payloadData.senderName).push(payloadData);
            setPrivateChats(new Map(privateChats));
        } else {
            let list = [];
            list.push(payloadData);
            privateChats.set(payloadData.senderName, list);
            setPrivateChats(new Map(privateChats));
        }
    }

    const onError = (err) => {
        console.log(err);
    }

    const handleMessage = (event) => {
        const {value} = event.target;
        setUserData({...userData, "message": value});
    }
    const sendValue = () => {
        if (stompClient) {
            var chatMessage = {
                senderName: userData.username,
                message: userData.message,
                status: "MESSAGE"
            };
            console.log(chatMessage);
            stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
            setUserData({...userData, "message": ""});
        }
    }

    const sendPrivateValue = () => {
        if (stompClient) {
            var chatMessage = {
                senderName: userData.username,
                receiverName: tab,
                message: userData.message,
                status: "MESSAGE"
            };

            if (userData.username !== tab) {
                privateChats.get(tab).push(chatMessage);
                setPrivateChats(new Map(privateChats));
            }
            stompClient.send("/app/private-message", {}, JSON.stringify(chatMessage));
            setUserData({...userData, "message": ""});
        }
    }

    const handleConnect = async () => {
        connect();
        const fetchedUsers = await fetchUsers();
        setUsers(fetchedUsers);
        console.log("newUsers:", fetchedUsers);
    }
    return (
        <div className="container">

            <label>Confirm joining chat connected as:</label>
            <h2>{userData.username}</h2>
            {userData.userrole === 'admin' ?
                <label>Select between users or make an announcement</label>
                :
                <h3>Report problems to ADMIN</h3>
            }

            {userData.connected ?
                <div className="chat-box">
                    {
                        userData.userrole === 'admin' ?
                            <>
                                <div className="member-list">
                                    <ul>
                                        <li onClick={() => {
                                            setTab("CHATROOM")
                                        }} className={`member ${tab === "CHATROOM" && "active"}`}>Announcements
                                        </li>
                                        {[...privateChats.keys()].map((name, index) => {
                                                if (name !== userName)
                                                    return <li onClick={() => {
                                                        setTab(name)
                                                    }} className={`member ${tab === name && "active"}`}
                                                               key={index}>{name}</li>
                                            }
                                        )}
                                    </ul>
                                </div>
                                {tab === "CHATROOM" &&
                                    <div className="chat-content">
                                        <ul className="chat-messages">
                                            {publicChats.map((chat, index) => (
                                                <li className={`message ${chat.senderName === userData.username && "self"}`}
                                                    key={index}>
                                                    {chat.senderName !== userData.username &&
                                                        <div className="avatar">{chat.senderName}</div>}
                                                    <div className="message-data">{chat.message}</div>
                                                    {chat.senderName === userData.username &&
                                                        <div className="avatar self">{chat.senderName}</div>}
                                                </li>
                                            ))}
                                        </ul>

                                        <div className="send-message">
                                            <input type="text" className="input-message" placeholder="message"
                                                   value={userData.message} onChange={handleMessage}/>
                                            <button type="button" className="send-button" onClick={sendValue}>send
                                            </button>
                                        </div>
                                    </div>
                                }
                                {tab !== "CHATROOM" &&
                                    <div className="chat-content">
                                        <ul className="chat-messages">
                                            {[...privateChats.get(tab)].map((chat, index) => (
                                                <li className={`message ${chat.senderName === userData.username && "self"}`}
                                                    key={index}>
                                                    {chat.senderName !== userData.username &&
                                                        <div className="avatar">{chat.senderName}</div>}
                                                    <div className="message-data">{chat.message}</div>
                                                    {chat.senderName === userData.username &&
                                                        <div className="avatar self">{chat.senderName}</div>}
                                                </li>
                                            ))}
                                        </ul>

                                        <div className="send-message">
                                            <input type="text" className="input-message" placeholder="message"
                                                   value={userData.message} onChange={handleMessage}/>
                                            <button type="button" className="send-button"
                                                    onClick={sendPrivateValue}>send
                                            </button>
                                        </div>
                                    </div>}
                            </>
                            :
                            <>
                                <div className="member-list">
                                    <ul>
                                        <li onClick={() => {
                                            setTab("CHATROOM")
                                        }} className={`member ${tab === "CHATROOM" && "active"}`}>Announcements
                                        </li>
                                        {[...privateChats.keys()].map((name, index) => {
                                                if (name !== userName)
                                                    return <li onClick={() => {
                                                        setTab(name)
                                                    }} className={`member ${tab === name && "active"}`}
                                                               key={index}>{name}</li>
                                            }
                                        )}
                                    </ul>
                                </div>
                                {tab === "CHATROOM" &&
                                    <div className="chat-content">
                                        <ul className="chat-messages">
                                            {publicChats.map((chat, index) => (
                                                <li className={`message ${chat.senderName === userData.username && "self"}`}
                                                    key={index}>
                                                    {chat.senderName !== userData.username &&
                                                        <div className="avatar">{chat.senderName}</div>}
                                                    <div className="message-data">{chat.message}</div>
                                                    {chat.senderName === userData.username &&
                                                        <div className="avatar self">{chat.senderName}</div>}
                                                </li>
                                            ))}
                                        </ul>

                                        {/*<div className="send-message">*/}
                                        {/*    <input type="text" className="input-message" placeholder="enter the message"*/}
                                        {/*           value={userData.message} onChange={handleMessage}/>*/}
                                        {/*    <button type="button" className="send-button" onClick={sendValue}>send*/}
                                        {/*    </button>*/}
                                        {/*</div>*/}
                                    </div>
                                }
                                {tab !== "CHATROOM" &&
                                    <div className="chat-content">
                                        <ul className="chat-messages">
                                            {[...privateChats.get(tab)].map((chat, index) => (
                                                <li className={`message ${chat.senderName === userData.username && "self"}`}
                                                    key={index}>
                                                    {chat.senderName !== userData.username &&
                                                        <div className="avatar">{chat.senderName}</div>}
                                                    <div className="message-data">{chat.message}</div>
                                                    {chat.senderName === userData.username &&
                                                        <div className="avatar self">{chat.senderName}</div>}
                                                </li>
                                            ))}
                                        </ul>

                                        <div className="send-message">
                                            <input type="text" className="input-message" placeholder="message"
                                                   value={userData.message} onChange={handleMessage}/>
                                            <button type="button" className="send-button"
                                                    onClick={sendPrivateValue}>send
                                            </button>
                                        </div>
                                    </div>}
                            </>
                    }

                </div>
                :
                <button type="button" onClick={handleConnect}>
                    Connect
                </button>
            }

            <button onClick={() => navigate(-1)}>Back</button>
        </div>
    )
}

export default ChatPage