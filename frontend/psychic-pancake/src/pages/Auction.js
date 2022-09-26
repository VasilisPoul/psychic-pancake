import { useContext, useEffect, useState } from "react";
import InitialNavbar from "../components/InitialNavbar";
import Navbar from "../components/Navbar";
import { UserContext } from "../components/UserContext";
import EditAuctionModal from "../components/EditAuctionModal"
import SendMessageModal from "../components/SendMessageModal";
import AddBidModal from "../components/AddBidModal";
import { Link, useParams } from "react-router-dom";
import axios from "../api/axios";
import '../style.css'
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet'
import { useNavigate } from "react-router-dom";

const BidView = ({ bid_url }) => {
  const [bid, setBid] = useState({});
  console.log(bid_url)
  useEffect(() => {
    axios.get(bid_url).then(
      (response) => {
        setBid(response.data);
      }
    )
      .catch((error) => console.log(error))

  }, [])
  console.log({ bid })
  return (
    <>
      <li><span>{bid.amount}</span></li>
    </>
  );
}

export default function Auction() {
  const { user, setUser } = useContext(UserContext);
  const auctionId = useParams().id
  const [listing, setListing] = useState({})
  const [loading, setLoading] = useState(true);
  const [images, setImages] = useState([]);
  const [sellerUid, setSellerUid] = useState('')
  const [seller, setSeller] = useState({});
  const [active, setActive] = useState(false);
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
          setActive(response.data.active);
          console.log(response.data.active)
          setSellerUid(response.data.seller.split('/')[3]);
          for (let i = 0; i < response.data.images.length; i++) {
            axios.get(response.data.images[i]).then(
              function (response) {
                setImages(oldArray => [...oldArray, response.data.image])
              }
            )
          }
          axios.get(response.data.seller)
            .then((response1) => setSeller(response1.data))
          setLoading(false)
        }
      )
      .catch();



  }, [])

  const HandleActivate = (e) => {
    e.preventDefault();
    try {
      axios.put(`/api/listings/${auctionId}`, { activate: true }, {
        headers: {
          'accept': 'application/json',
          'Content-Type': 'application/json',
          'authorization': localStorage.getItem('AuthToken')
        }
      })
        .then(function (response) {
          setActive(true);
          window.location.reload(false);
        })
        .catch(
          function (error) {
          }
        )
    }
    catch (error) {
      alert(error)
    }
  }
  console.log({active})
  return (
    <>
      {!loading &&
        <div>
          {(localStorage.getItem('AuthToken')) ? <Navbar /> : <InitialNavbar />}
          <div className="container mt-3">
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
                  {user['username'] === sellerUid && !active && <div className="btn" onClick={HandleActivate} >Activate</div>}
                  <div className='col-sm-6'>
                    {user['role'] === 'buyer' && listing.active && <AddBidModal listing={listing} />}
                    {user['username'] === sellerUid && <EditAuctionModal listing={listing} listing_url={`/api/listings/${auctionId}`} />}
                  </div>
                  <div className='col-sm-6'>
                    {user['role'] === 'buyer' && <SendMessageModal to={listing.seller.split('/')[3]} />}
                    {user['username'] === sellerUid && <div className='btn btn-danger w-100 mb-3' onClick={HandleDelete}>Delete</div>}
                  </div>
                  <div className="row">

                    <div className="col-sm-6">
                      <div className='ml-5'>
                        <h4>Bids</h4>
                        <div className="dropdown w-100">
                          <button className="btn dropdown-toggle w-100 " type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                            {listing.currently}
                          </button>
                          <ul className="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                            {listing.bids && listing.bids.map((bid_url) => {
                              return (
                                <>
                                  <BidView bid_url={bid_url} />
                                </>
                              );
                            })}
                          </ul>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className="row">
                    <div className='col'>
                      <Link to={`/user/${sellerUid}`} > <h4>Seller</h4></Link>
                    </div>

                  </div>
                  <div className="row">
                    <h4>Seller rating</h4>
                    <span>{seller.rating}</span>
                  </div>
                </div>
                <div>
                  <h4>Description:</h4>
                  <p className='lead flex-shrink-0'> {listing.description}</p>
                </div>
                <div id="map d-flex align-items-center jusify-content-center mt-4" style={{ width: "80%", height: "100vh", zIndex: '1' }}>
                  <MapContainer center={[listing.location.latitude, listing.location.longitude]} zoom={13} scrollWheelZoom={false}>
                    <TileLayer
                      attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                      url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    <Marker position={[listing.location.latitude, listing.location.longitude]}>
                      <Popup>
                        {listing.location.name}
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