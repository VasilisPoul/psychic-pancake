import Login from '../components/Login'
import Footer from '../components/Footer'
import auction from '../resources/auction.webp';
import { Link } from 'react-router-dom'

export default function WelcomePage() {
  return (
    <>
      <nav className="navbar navbar-expand-lg bg-light">
        <div className="container-fluid">
          <Link className="navbar-brand" to="/">Psychic Pancake</Link>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarSupportedContent">
            <ul className="navbar-nav me-auto mb-2 mb-lg-0">
              <li className="nav-link">
                
              </li>
              <li className="nav-link">
                
              </li>
            </ul>
            <Link to={`/login`}>Log In</Link>
            &nbsp;
            <Link to="/signup">Sign Up</Link>
          </div>
        </div>
      </nav>
      <br /><br />
      <br /><br />
      <div></div>
      <div className="container mb-4">
        <div className="row">
          <div className="col">
            <Login />
          </div>
          <div className="col">
            <img src={auction} className="img-thumbnail"/>
          </div>
        </div>

      </div>
      <div className="row">
        <div style={{ backgroundColor: '#B2BEB5' }}>
          <br /><br />
          <br /><br />
          <h1>Find the best product for you</h1>
          <h3>Browse through our list of available auctions and find the best for your needs.</h3>
          <br /><br />
          <br /><br />
        </div>
      </div>
      <Footer />
    </>
  );
}