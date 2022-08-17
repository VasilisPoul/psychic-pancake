import { useContext } from "react";
import InitialNavbar from "../components/InitialNavbar";
import Navbar from "../components/Navbar";
import { UserContext } from "../components/UserContext";
import auction from '../resources/auction.webp';
import AddAuctionModal from "../components/AddAuctionModal"
import SendMessageModal from "../components/SendMessageModal";

export default function Auction() {
  const { user, setUser } = useContext(UserContext);
  const isLoggedIn = (typeof user['role'] === 'undefined') ? false : true;
  return (
    <>
      {isLoggedIn ? <Navbar /> : <InitialNavbar />}
      <div className="card">
        <img style={{ height: "50vh", objectFit: "cover", objectPosition: "50% 50%" }} className="card-img-top" src={auction} alt="Card image cap" />
        <div className='container mt-2'>
          <div className='row'>
            <div className='col-sm-2'>
              {user['role'] === 'seller' && <AddAuctionModal />}
            </div>
            <div className='col-sm-2'>
              <SendMessageModal/>
            </div>
          </div>
        </div>
        <div className="card-body">
          <h5 className="card-title">Title</h5>
          <p className="card-text">blah</p>
        </div>
      </div>

    </>
  );
}