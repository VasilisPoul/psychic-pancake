import { useContext } from "react";
import InitialNavbar from "../components/InitialNavbar";
import Navbar from "../components/Navbar";
import { UserContext } from "../components/UserContext";
import auction from '../resources/auction.webp';

export default function Auction() {
  const { user, setUser } = useContext(UserContext);
  const isLoggedIn = (typeof user['user-role'] === 'undefined') ? false : true;
  console.log(user)
  console.log(isLoggedIn)
  return (
    <>
      {isLoggedIn ? <Navbar /> : <InitialNavbar />}   
      <div className="card">
        <img style={{height:"50vh", objectFit:"cover", objectPosition:"50% 50%"}} className="card-img-top" src={auction} alt="Card image cap"/>
          <div className="card-body">
            <h5 class="card-title">Title</h5>
            <p className="card-text">blah</p>
          </div>
      </div>

    </>
  );
}