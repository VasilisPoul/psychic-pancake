import { useState } from "react";
import { useNavigate } from "react-router-dom";
import '../style.css'
import axios from "../api/axios";

export default function SetupAdmin(props) {
  const [email, setEmail] = useState('');
  const [uid, setUid] = useState('');
  const [password, setPassword] = useState('');
  const [country, setCountry] = useState('')
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false)
  const HandleSubmit = (e) => {
    e.preventDefault();
    try{
      setLoading(true)
      axios.post('/api/install', {uid, email, password, country})
      .then((response) => {
        
        navigate("/")
      }
      )
      .catch()
    } catch (error) {

    }

    
  }



  return (
    <div className="Auth-form-container">
      <form className="Auth-form" onSubmit={HandleSubmit}>
        <div className="Auth-form-content">
          <h3 className="Auth-form-title">Admin Information</h3>
          <div className="form-group mt-3">
            <label>Admin Username</label>
            <input
              type="username"
              className="form-control mt-1"
              placeholder="Enter admin username"
              onChange={(e) => { setUid(e.target.value) }}
            />
          </div>
          <div className="form-group mt-3">
            <label>Email address</label>
            <input
              type="email"
              className="form-control mt-1"
              placeholder="Enter email"
              onChange={(e) => { setEmail(e.target.value) }}
            />
          </div>
          <div className="form-group mt-3">
            <label>Country</label>
            <input
              type="text"
              className="form-control mt-1"
              placeholder="Enter Country"
              onChange={(e) => { setCountry(e.target.value) }}
            />
          </div>
          <div className="form-group mt-3">
            <label>Password</label>
            <input
              type="password"
              className="form-control mt-1"
              placeholder="Enter password"
              onChange={(e) => { setPassword(e.target.value) }}
            />
          </div>
          <div className="d-grid gap-2 mt-3">
            <button type="submit" className="btn btn-primary">
              Submit
            </button>
          </div>
          {loading && <span>Loading...</span>}
        </div>
      </form>
     
    </div>
  );
}