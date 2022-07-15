import { useContext, useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import { UserContext } from "./UserContext";

export default function Login() {
  const {user, setUser} = useContext(UserContext);
  const [ email, setEmail ] = useState('');
  const navigate = useNavigate();
  const LogInUser = (e) => {
    setUser({'username': 'foo', 'email': email, 'user-role': 'buyer'}) 
    navigate("/")
  }

  return (
    <>
      <div className="col-xs-1" align="center"><h3>Sign In</h3></div>
      <div className="container d-flex justify-content-center" >
        <div class="col-md-8 d-flex justify-content-center" style={{ boxShadow: "0px 14px 80px rgb(34 35 58 / 20%)" }}>

          <div className="row">
            <form onSubmit={LogInUser}>
              <div className="form-outline mb-4">
                <label className="form-label">Email address</label><br />
                <input required
                  type="email"
                  name="email"
                  placeholder="Enter Email"
                  onChange={(e) => {setEmail(e.target.value)}} />
              </div>
              <div className="form-outline mb-4">
                <label>Password</label><br />
                <input required
                  type="password"
                  name="password"
                  placeholder="Enter your password" />

              </div>
              <button type="submit" className="btn btn-primary btn-block mb-4" >Submit</button>
            </form>
          </div>
        </div>
      </div>
    </>
  );
}