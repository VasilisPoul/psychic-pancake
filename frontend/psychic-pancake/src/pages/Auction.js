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
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    axios.get(`/api/listings/${auctionId}`)
      .then(
        function (response) {
          setListing(response.data)
          setLoading(false)
        }
      )
      .catch()
  }, [])

  return (
    <>
      {!loading && <div>
        {(isLoggedIn) ? <Navbar /> : <InitialNavbar />}
        <div className="card">
          <img style={{ height: "50vh", objectFit: "cover", objectPosition: "50% 50%" }} className="card-img-top" src={auction} alt="Card image cap" />
          <div className='container mt-2'>
            <div className='row'>
              <div className="col">
                <span>Latest Bid: {listing.currently}$</span>
              </div>
              <div className='col'>
                {user['role'] === 'buyer' && <AddBidModal listing={listing} />}
              </div>
              <div className='col'>
                {user['role'] === 'buyer' && <SendMessageModal to={listing.seller.split('/')[3]} />}
              </div>
            </div>
          </div>
          <div className="card-body container">
            <h5 className="card-title">{listing.name}</h5>
            <p className="card-text">{listing.description}</p>
          </div>
        </div>

      </div>}
    </>
  );
}