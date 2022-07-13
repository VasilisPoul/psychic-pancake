import { Link, Route, Router, Routes } from 'react-router-dom'

export default function AdminNavbar() {
    return (
        <Router>
            <nav className="navbar navbar-expand-lg bg-light">
                <div className="container-fluid">
                    <a className="navbar-brand" href="#">Psychic Pancake</a>
                    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul className="navbar-nav me-auto mb-2 mb-lg-0">

                            <Routes>


                                <li className="nav-item">
                                    <Link to="/users">Users</Link>
                                </li>
                                <li className="nav-item">
                                    <Link to="/pending">Pending</Link>
                                </li>
                            </Routes>

                        </ul>
                    </div>
                </div>
            </nav>
            <Routes>
                <Route path={`/users`}>
                    <AdminNavbar />
                </Route>
                <Route path={"/pending"}>
                   <> </>
                </Route>
            </Routes>
        </Router>
    );
}