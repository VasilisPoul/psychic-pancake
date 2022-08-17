import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useContext } from 'react';
import { UserContext } from './UserContext';

export default function Navbar() {
    const { user, setUser } = useContext(UserContext);
    const navigate = useNavigate();
    const HandleLogOut = () => {
        localStorage.setItem("AuthToken", JSON.stringify(''));
        setUser({});    
        navigate('/')
    }

    return (
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container-fluid">
                <Link class="navbar-brand" to="/">Psychic Pancake</Link>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <Link class="nav-link" aria-current="page" to="/">Home</Link>
                        </li>
                        <li class="nav-item">
                            <Link class="nav-link" to="/my-auctions">My Auctions</Link>
                        </li>
                    </ul>
                    <form class="d-flex">
                        <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" />
                            <button class="btn btn-outline-success" type="submit">Search</button>
                    </form>
                    <button className="btn btn-light ml-4" onClick={HandleLogOut} >Logout</button>
                </div>
            </div>
        </nav>
    );
}