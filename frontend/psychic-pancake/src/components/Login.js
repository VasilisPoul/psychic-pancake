import { useContext, useEffect, useState } from "react";
import { Navigate, useNavigate, Link } from "react-router-dom";
import { UserContext } from "./UserContext";
import '../style.css'
import classNames from "classnames";
import axios from '../api/axios';
// import axios from 'axios'

export default function Login(props) {
  const frontPage = props.frontPage;
  const { user, setUser } = useContext(UserContext);
  const [uid, setUid] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const LogInUser = (e) => {
    e.preventDefault();
    try {
      axios.post("/api/token", { uid, password }, {
        headers: {
          'accept': 'application/json',
          'Content-Type': 'application/json'
        }
      })
        .then(
          function (response) {
            localStorage.setItem("AuthToken", response.data.token)
            axios.get("/api/user/me", {
              headers: {
                accept: 'application/json',
                authorization: localStorage.getItem('AuthToken')
              }
            })
              .then(function (get_response) {
                setUser({ 'username': get_response.data.uid, 'role': get_response.data.role });
                navigate("/")
              })
          }
        ).catch (
          function (error){
            alert('Could not login, check your credentials.')
          }
        )
    }
    catch { }
  }

  const HandleCancel = () => {
    navigate("/")
  }

  return (
    <div className={classNames(frontPage ? '' : "form-container")}>
      <form className="a-form" onSubmit={LogInUser}>
        <div className="a-form-c">
          <h3 className="a-form-t">Sign In</h3>
          <div className="text-center">
            Not registered yet?{" "}
            <Link to="/signup" className="link-primary">
              Sign Up
            </Link>
          </div>
          <div className="form-group mt-3">
            <label>Username</label>
            <input
              type="test"
              className="form-control mt-1"
              placeholder="Enter username"
              onChange={(e) => { setUid(e.target.value) }}
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
            {!frontPage && <button type="cancel" className="btn btn-grey" onClick={HandleCancel}>
              Cancel
            </button>}
          </div>
        </div>
      </form>
    </div>
  );
}