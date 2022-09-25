import { useEffect, useState } from "react";
import AdminNavbar from "../components/AdminNavbar";
import axios from "../api/axios";
import { useNavigate } from "react-router-dom";

export default function AdminUsersPagePanel() {
    const navigate = useNavigate();
    const UserView = ({user_link}) => {
        console.log({user_link})
        const [user, setUser] = useState({}); 
        useEffect(() => {
            axios.get(user_link, 
                {
                    headers: {
                        'authorization': localStorage.getItem('AuthToken')
                    }
                }
            ).then(
                (response) => {
                    setUser(response.data)
                }
            ).catch((error) => {console.log(error)})
    }, [])
    console.log(user)
    return (
        <>
            <tr style={{cursor: 'pointer'}} onClick={() => {navigate(`/user/${user.uid}`)}}>
                <th scope="row">{user.uid}</th>
                <td>{user.uid}</td>
                <td>{user.email}</td>

            </tr>
        </>
    );
}

const [usersList, setUsersList] = useState([]);
useEffect(() => {
    try {
        axios.get('/api/admin/users', {
            headers: {
                'authorization': localStorage.getItem('AuthToken')
            }
        }).then((response) => {
            setUsersList(response.data);
        })
    }
    catch (error) {

    }
}, [])
console.log(usersList)
return (
    <>
        <AdminNavbar />

        <div className="container">
            <table className="table">
                <thead>
                    <tr>
                        <th scope="col">Id</th>
                        <th scope="col">Username</th>
                        <th scope="col">Email</th>
                    </tr>
                </thead>
                <tbody>
                    {usersList.map((item) => {
                        return (
                            <>
                            <UserView user_link={item} />
                            </>
                        );
                    })}
                </tbody>

            </table>
        </div>
    </>
);
}