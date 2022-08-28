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
  const [images, setImages] = useState([]);
  let images_array = [];
  useEffect(() => {
    axios.get(`/api/listings/${auctionId}`)
      .then(
        function (response) {
          setListing(response.data);
          for (let i = 0; i < response.data.images.length; i++) {
            axios.get(response.data.images[i]).then(
              function (response) {
                images_array.push(response.data.image)
                setImages(oldArray => [...oldArray, response.data.image])
              }
            )
          }

          setLoading(false);

        }
      )
      .catch()
  }, [])
  !loading && console.log("images", images)
  return (
    <>
      {!loading &&
        <div>
          {(isLoggedIn) ? <Navbar /> : <InitialNavbar />}
          <div className="container mt-3">
            <div className="row">
              <h1>{listing.name}</h1>
              <div className='col'>
                {user['role'] === 'buyer' && <AddBidModal listing={listing} />}
              </div>
              <div className='col'>
                {user['role'] === 'buyer' && <SendMessageModal to={listing.seller.split('/')[3]} />}
              </div>
            </div>

            <div className='row'>
              <div className='col'>

                {
                  images.map((image) => {
                    return (
                      <img src={image} className=" w-100 mb-2" alt="alt" />
                    );
                  })
                }


              </div>
              <div className="col">

                <div className="row">
                  <h4>Latest Bid:</h4>
                  <span> {listing.currently}$</span>
                </div>
                <div>
                  <h4>Description:</h4>
                  <span> {listing.description}</span>
                </div>

              </div>
            </div>

          </div>

        </div>}
    </>
  );
}