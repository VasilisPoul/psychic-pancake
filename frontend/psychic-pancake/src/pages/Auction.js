import { useContext, useEffect, useState } from "react";
import InitialNavbar from "../components/InitialNavbar";
import Navbar from "../components/Navbar";
import { UserContext } from "../components/UserContext";
import EditAuctionModal from "../components/EditAuctionModal"
import SendMessageModal from "../components/SendMessageModal";
import AddBidModal from "../components/AddBidModal";
import { useParams } from "react-router-dom";
import axios from "../api/axios";
import '../style.css'
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet'
import { useNavigate } from "react-router-dom";

export default function Auction() {
  const { user, setUser } = useContext(UserContext);
  const auctionId = useParams().id
  const [listing, setListing] = useState({})
  const [loading, setLoading] = useState(true);
  const [images, setImages] = useState([]);
  const [bidsArray, setBidsArray] = useState([]);
  let images_array = [];
  const navigate = useNavigate();
  const HandleDelete = (e) => {
    e.preventDefault();
    try {
      axios.delete(`/api/listings/${auctionId}`, {
        headers: {
          'authorization': localStorage.getItem('AuthToken')
        }
      }).then(
        function (response) {
          navigate('/my-auctions')
        }
      ).catch()
    } catch (error) {

    }
  }

  useEffect(() => {

    axios.get(`/api/listings/${auctionId}`)
      .then(
        function (response) {
          setListing(response.data);
          for (let i = 0; i < response.data.images.length; i++) {
            axios.get(response.data.images[i]).then(
              function (response) {
                setImages(oldArray => [...oldArray, response.data.image])
              }
            )
          }

          setLoading(false);

        }
      )
      .catch();


    axios.get(`/api/listings/${auctionId}/bids`)
      .then(
        function (response) {
          setListing(response.data);
          for (let i = 0; i < response.data.images.length; i++) {
            axios.get(response.data.images[i]).then(
              function (response) {
                setBidsArray(response.data)
              }
            )
          }

          setLoading(false);

        }
      )
      .catch()
  }, [])

  return (
    <>
      {!loading &&
        <div>
          {(localStorage.getItem('AuthToken')) ? <Navbar /> : <InitialNavbar />}
          <div className="container mt-3">
            <div className="row">


            </div>

            <div className='row'>
              <div className='col mb-2'>

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
                  <h1 className='mb-4'>{listing.name}</h1>
                  <div className='col-sm-6'>
                    {user['role'] === 'buyer' && <AddBidModal listing={listing} />}
                    {user['role'] === 'seller' && <EditAuctionModal listing={listing} listing_url={`/api/listings/${auctionId}`} />}
                  </div>
                  <div className='col-sm-6'>
                    {user['role'] === 'buyer' && <SendMessageModal to={listing.seller.split('/')[3]} />}
                    {user['role'] === 'seller' && <div className='btn btn-danger w-100 mb-3' onClick={HandleDelete}>Delete</div>}
                  </div>
                  <div className="row">
                   
                    <div className="col-sm-6">
                      <div className='ml-5'>
                        <h4>Bids</h4>

                        <div class="dropdown w-100">
                          <button class="btn dropdown-toggle w-100 " type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                            {listing.currently}
                          </button>
                          <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                            {bidsArray.map((bid_url) => {
                              return(
                                <>
                                  {/* <BidView bid_url={bid_url} /> */}
                                </>
                              );
                            })}
                          </ul>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div>
                  <h4>Description:</h4>
                  <p className='lead flex-shrink-0'> {listing.description}</p>
                </div>
                <div id="map d-flex align-items-center jusify-content-center mt-4" style={{ width: "80%", height: "100vh", zIndex: '1' }}>
                  <MapContainer center={[51.505, -0.09]} zoom={13} scrollWheelZoom={false}>
                    <TileLayer
                      attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                      url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    <Marker position={[51.505, -0.09]}>
                      <Popup>
                        A pretty CSS3 popup. <br /> Easily customizable.
                      </Popup>
                    </Marker>
                  </MapContainer>
                </div>

              </div>
            </div>

          </div>

        </div>}
    </>
  );
}