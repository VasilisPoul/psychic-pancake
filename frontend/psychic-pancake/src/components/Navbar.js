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
                            <Link className="nav-link" aria-current="page" to="/">Auctions</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/my-auctions">My Auctions</Link>
                        </li>
                        <li className="nav-item">

                            <div className="dropdown">
                                <button className="btn btn-light w-100 dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    Messages
                                </button>
                                <div className="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                    <div className="dropdown-item" >
                                    <Link className="nav-link" to="/sent-messages">Sent</Link>
                                    </div>
                                    <div className="dropdown-item" >
                                    <Link className="nav-link" to="/received-messages">Received</Link>
                                    </div>
                                </div>
                            </div>

                            
                        </li>
                    </ul>
                </div>
            </div>
            <button className="btn btn-light ml-4 col" onClick={HandleLogOut} >Logout</button>
        </nav>
    );
}