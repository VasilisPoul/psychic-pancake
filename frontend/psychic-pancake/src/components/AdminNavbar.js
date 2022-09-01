import { Link, Route, Router, Routes, useNavigate } from 'react-router-dom'
import { UserContext } from './UserContext';
import { useContext } from 'react';

export default function AdminNavbar() {
    const { user, setUser } = useContext(UserContext);
    const navigate = useNavigate();
    const HandleLogOut = () => {
        localStorage.removeItem('AuthToken')
        setUser({});
        navigate('/')
    }
    return (
            <nav className="navbar navbar-expand-lg bg-light">
                <div className="container-fluid">
                    <Link className="navbar-brand" to="/users">Psychic Pancake</Link>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                            <li className="nav-link">
                                <Link to={`/users`}>Registered Users</Link>
                            </li>
                            <li className="nav-link">
                                <Link to="/pending">Pending Users</Link>
                            </li>
                        </ul>
                    </div>
                </div>
                <button className="btn btn-light ml-4 col" onClick={HandleLogOut} >Logout</button>
            </nav>
    );
}