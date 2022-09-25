import AdminNavbar from "../components/AdminNavbar";
import { useParams } from "react-router-dom";
import { useEffect, useState, useContext } from "react";
import axios from "../api/axios";
import { UserContext } from "../components/UserContext";
import InitialNavbar from "../components/InitialNavbar";
import Navbar from "../components/Navbar";



export default function User() {
    const userId = useParams().id;
    const { user, setUser } = useContext(UserContext);
    const [selectedUser, setSelectedUser] = useState({});
    const [loading, setLoading] = useState(true);
    const [rating, setRating] = useState(0);
    useEffect(() => {
        axios.get(`/api/user/${userId}`)
            .then((response) => {
                setSelectedUser(response.data)
                setLoading(false)
            })
    }, [])
    console.log({rating})

    const HandleSubmit = (e) => {
        
        e.preventDefault()
        axios.put(`/api/user/${userId}/rating`, {rate:rating}, {
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'authorization': localStorage.getItem('AuthToken')
            }
        })
        .then((response) => alert('Done'))
    }
    console.log({user})

    return (
        <>
           {user.role==='admin' && <AdminNavbar />}
           {(user.role==='buyer' || user.role === 'seller') && <Navbar/>}
           {Object.keys(user).length === 0 && <InitialNavbar />}
            {loading? 
            <span>Loading</span> : 
            <div className='container'>
                <table className="table">
                    <tbody>
                        <tr>
                            <th>Role</th>
                            <td>{selectedUser.role ? selectedUser.role : '-'}</td>
                        </tr>
                        <tr>
                            <th>Username</th>
                            <td>{selectedUser.uid ? selectedUser.uid : '-'}</td>
                        </tr>
                        <tr>
                            <th>Email</th>
                            <td>{selectedUser.email ? selectedUser.email :'-'}</td>
                        </tr>
                        <tr>
                            <th>First Name</th>
                            <td>{selectedUser.first_name ? selectedUser.first_name : '-'}</td>
                        </tr>
                        <tr>
                            <th>Last Name</th>
                            <td>{selectedUser.last_name ? selectedUser.last_name : '-'}</td>
                        </tr>

                    </tbody>
                </table>
            </div>}
            {!loading && selectedUser.role==='seller' && user.username !== userId && user.role !== 'admin' &&
            <div className='container d-flex align-items-center justify-content-center'>
            <small> Rate Seller: </small>
            <form className='container d-flex align-items-center justify-content-center' onSubmit={HandleSubmit}>
            <div className="form-check ms-3">
              <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1" onClick={()=> setRating(1)}/>
                <label className="form-check-label" for="flexRadioDefault1">
                  1
                </label>
            </div>
            <div className="form-check ms-3">
              <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault2" onClick={()=> setRating(2)}/>
                <label className="form-check-label" for="flexRadioDefault2">
                  2
                </label>
            </div>
            <div className="form-check ms-3">
              <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault3" onClick={()=> setRating(3)}/>
                <label className="form-check-label" for="flexRadioDefault3">
                  3
                </label>
            </div>
            <div className="form-check ms-3">
              <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault4" onClick={()=> setRating(4)}/>
                <label className="form-check-label" for="flexRadioDefault4">
                  4
                </label>
            </div>
            <div className="form-check ms-3">
              <input className="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault5"onClick={()=> setRating(5)}/>
                <label className="form-check-label" for="flexRadioDefault5">
                  5
                </label>
            </div>
            <button className='btn' type='submit'>
                Submit
            </button>
            </form>
          </div>}
        </>
    );
}