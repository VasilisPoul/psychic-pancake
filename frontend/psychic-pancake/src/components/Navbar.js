import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useContext, useState } from 'react';
import { UserContext } from './UserContext';
import axios from '../api/axios';

export default function Navbar() {
    const { user, setUser } = useContext(UserContext);
    const navigate = useNavigate();
    const HandleLogOut = () => {
        localStorage.removeItem('AuthToken')
        setUser({});
        navigate('/')
    }

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <div className="container-fluid">
                <Link className="navbar-brand" to="/">Psychic Pancake</Link>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        <li className="nav-item">
                            <Link className="nav-link" aria-current="page" to="/">Home</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/my-auctions">My Auctions</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/messages">Messages</Link>
                        </li>
                    </ul>
                </div>
            </div>
            <button className="btn btn-light ml-4 col" onClick={HandleLogOut} >Logout</button>
        </nav>
    );
}