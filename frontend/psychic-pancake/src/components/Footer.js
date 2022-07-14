import React from "react"
import { Link } from 'react-router-dom'

const Footer = () => <footer className="page-footer font-small blue pt-4" style={{ backgroundColor: '#CFCFCF' }}>
  <div className="navbar-brand fw-bold">

    {/* <AiOutlineArrowUp style={{ "float": "left" }} size="60" /> */}
    <text>Psychic<br />Pancake</text>
  </div>
  <div className="container-fluid text-center text-md-left">
    <div className="row">
      <div className="col-md-4 mt-md-0 mt-3">


      </div>

      <hr className="clearfix w-100 d-md-none pb-0" />

      <div className="col-md-3 mb-md-0 mb-3">
        <h5 className="text-uppercase">General</h5>
        <ul className="list-unstyled">
          <li><Link to="/signup">Sign Up</Link></li>
          <li><Link to="/login">Sign In</Link></li>
        </ul>
      </div>

      <div className="col-md-3 mb-md-0 mb-3">
        <h5 className="text-uppercase">Browse</h5>
        <ul className="list-unstyled">
          <li><Link to="/">Feed</Link></li>
          {/* <li><Link to="/Auctions">Jobs</Link></li> */}
        </ul>
      </div>
    </div>
  </div>

  <div className="footer-copyright text-center py-3">© 2022 Copyright:
    <a href="#"> PsychicPancake.com</a>
  </div>

</footer>

export default Footer
