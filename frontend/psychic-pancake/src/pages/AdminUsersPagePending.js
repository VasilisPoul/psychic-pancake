import { useState, useEffect } from "react";
import axios from "../api/axios";
import AdminNavbar from "../components/AdminNavbar";
import { useNavigate } from "react-router-dom";
const UserInstance = (props) => {
const navigate = useNavigate();
    const HandleYes = (e) => {
        e.preventDefault();
        try {
            axios.post('/api/admin/users/pending', {uid: userData.uid, accept: true}, {
                headers: {
                    'authorization': localStorage.getItem('AuthToken')
                }
            }).then(
                function (response) {
                    window.location.reload(false);
                }
            ).catch()
        }
        catch (error) {

        }
    }

    const HandleNo = (e) => {
        e.preventDefault();
        try {
            axios.post('/api/admin/users/pending', {uid: userData.uid, accept: false}, {
                headers: {
                    'authorization': localStorage.getItem('AuthToken')
                }
            }).then(
                function (response) {
                    window.location.reload(false);
                }
            ).catch()
        }
        catch (error) {

        }
    }

    const [userData, setUserData] = useState({});
    
    useEffect(() => {
        
        axios.get(props.item, {
            headers: {
                'authorization': localStorage.getItem('AuthToken')
            }
        }).then(function (response) {
            setUserData(response.data)
        }).catch();
    }, [])
    return (
        <>
        {userData.uid !== 'admin' && <tr style={{cursor: 'pointer'}} onClick={() => {navigate(`/user/${userData.uid}`)}}>
            {/* <th scope="row">{userData.id}</th> */}
            <td>{userData.uid}</td>
            <td>{userData.first_name}</td>
            <td>{userData.last_name}</td>
            <td>{userData.role}</td>
            <td>
                <span className="fw-bold" style={{ cursor: "pointer" }} onClick={HandleYes}>yes</span>&nbsp;<span style={{ cursor: "pointer" }} className="fw-bold" onClick={HandleNo}>no</span>
            </td>
        </tr>}
        </>
    )
}

export default function AdminPendingPagePanel() {

    const [users, setUsers] = useState([])
    useEffect(() => {
        try {
            
            axios.get('/api/admin/users/pending', {
                headers: {
                    'authorization': localStorage.getItem('AuthToken')
                }
            }).then(function (response) {
                setUsers(response.data);
            }).catch();
        }
        catch (error) {

        }
    }, [])
    return (
        <>
            <AdminNavbar />

            <div className="container">
                <table className="table">
                    <thead>
                        <tr>
                            <th scope="col">Username</th>
                            <th scope="col">First Name</th>
                            <th scope="col">Last Name</th>
                            <th scope="col">Role</th>
                            <th scope="col">Response</th>

                        </tr>
                    </thead>
                    <tbody>
                        {users.map((item) => {
                            return (
                                
                                    <UserInstance item={item} />
                                
                            );
                        })}
                    </tbody>

                </table>
            </div>
        </>
    );
}