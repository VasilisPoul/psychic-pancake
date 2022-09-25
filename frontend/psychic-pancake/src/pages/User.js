import AdminNavbar from "../components/AdminNavbar";
import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "../api/axios";

export default function User() {
    const userId = useParams().id;
    const [user, setUser] = useState({});
    const [loading, setLoading] = useState(true);
    useEffect(() => {
        axios.get(`/api/user/${userId}`)
            .then((response) => {
                setUser(response.data)
                setLoading(false)
            })
    }, [])
    console.log(user)
    return (
        <>
            <AdminNavbar />
            {loading? 
            <span>Loading</span> : 
            <div className='container'>
                <table className="table">
                    <tbody>
                        <tr>
                            <th>Role</th>
                            <td>{user.role ? user.role : '-'}</td>
                        </tr>
                        <tr>
                            <th>Url</th>
                            <td>{user.self ? user.self : '-'}</td>
                        </tr>
                        <tr>
                            <th>Username</th>
                            <td>{user.uid ? user.uid : '-'}</td>
                        </tr>
                        <tr>
                            <th>Email</th>
                            <td>{user.email ? user.email :'-'}</td>
                        </tr>
                        <tr>
                            <th>First Name</th>
                            <td>{user.first_name ? user.first_name : '-'}</td>
                        </tr>
                        <tr>
                            <th>Last Name</th>
                            <td>{user.last_name ? user.last_name : '-'}</td>
                        </tr>

                    </tbody>
                </table>
            </div>}
        </>
    );
}