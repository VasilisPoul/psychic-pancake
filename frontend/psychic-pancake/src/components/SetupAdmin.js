import { useContext, useState } from "react";
import { Navigate, useNavigate, Link } from "react-router-dom";
import { UserContext } from "./UserContext";
import '../style.css'
import classNames from "classnames";

export default function Login(props) {
  const frontPage = props.frontPage;
  const { user, setUser } = useContext(UserContext);
  const [email, setEmail] = useState('');
  const navigate = useNavigate();

  const LogInUser = (e) => {
    setUser({ 'username': 'foo', 'email': email, 'user-role': 'buyer' })
    navigate("/")
  }

  const HandleCancel = () => {
    navigate("/")
  }

  return (
    <div className={classNames(frontPage ? '' : "Auth-form-container")}>
      <form className="Auth-form" onSubmit={LogInUser}>
        <div className="Auth-form-content">
          <h3 className="Auth-form-title">Admin Information</h3>
          <div className="form-group mt-3">
            <label>Admin Username</label>
            <input
              type="username"
              className="form-control mt-1"
              placeholder="Enter admin username"
              onChange={(e) => { setEmail(e.target.value) }}
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
            <label>Password</label>
            <input
              type="password"
              className="form-control mt-1"
              placeholder="Enter password"
            />
          </div>
          <div className="d-grid gap-2 mt-3">
            <button type="submit" className="btn btn-primary">
              Submit
            </button>
          </div>
        </div>
      </form>
    </div>
  );
}