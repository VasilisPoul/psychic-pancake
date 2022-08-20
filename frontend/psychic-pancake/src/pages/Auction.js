import { useContext, useEffect, useState } from "react";
import InitialNavbar from "../components/InitialNavbar";
import Navbar from "../components/Navbar";
import { UserContext } from "../components/UserContext";
import auction from '../resources/auction.webp';
import AddAuctionModal from "../components/AddAuctionModal"
import SendMessageModal from "../components/SendMessageModal";
import AddBidModal from "../components/AddBidModal";
import { useParams } from "react-router-dom";
import axios from "../api/axios";

export default function Auction() {
  const { user, setUser } = useContext(UserContext);
  const isLoggedIn = (typeof user['role'] === 'undefined') ? false : true;
  const auctionId = useParams().id
  console.log(auctionId)
  const [listing, setListing] = useState({})
  useEffect(() => {
    axios.get(`/api/listings/${auctionId}`)
      .then(
        function (response) {
          setListing(response.data)
        }
      )
      .catch()
  }, [])

  return (
    <>
      {isLoggedIn ? <Navbar /> : <InitialNavbar />}
      <div className="card">
        <img style={{ height: "50vh", objectFit: "cover", objectPosition: "50% 50%" }} className="card-img-top" src={auction} alt="Card image cap" />
        <div className='container mt-2'>
          <div className='row'>
            <div className='col-sm-2'>
              {user['role'] === 'buyer' && <AddBidModal />}
            </div>
            <div className='col-sm-2'>
              {user['role'] === 'buyer' && <SendMessageModal />}
            </div>
          </div>
        </div>
        <div className="card-body container">
          <h5 className="card-title">{listing.name}</h5>
          <p className="card-text">{listing.description}</p>
        </div>
      </div>

    </>
  );
}