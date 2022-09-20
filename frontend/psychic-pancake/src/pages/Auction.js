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
import '../style.css'
import { MapContainer, TileLayer, useMap, Marker, Popup } from 'react-leaflet'

export default function Auction() {
  const { user, setUser } = useContext(UserContext);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const auctionId = useParams().id
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

  return (
    <>
      {/* <head><link rel="stylesheet" href="https://unpkg.com/leaflet@1.8.0/dist/leaflet.css"
        integrity="sha512-hoalWLoI8r4UszCkZ5kL8vayOGVae1oxXe/2A4AO6J9+580uKHDO3JdHb7NzwwzK5xr/Fs0W40kiNHxM9vyTtQ=="
        crossorigin="" /><link rel="stylesheet" href="https://unpkg.com/leaflet@1.0.1/dist/leaflet.css" />
      </head> */}
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
                <div className='col'>
                {user['role'] === 'buyer' && <AddBidModal listing={listing} />}
              </div>
              <div className='col'>
                {user['role'] === 'buyer' && <SendMessageModal to={listing.seller.split('/')[3]} />}
              </div>
                  <h4>Latest Bid:</h4>
                  <p className='lead flex-shrink-0'> {listing.currently}$</p>
                </div>
                <div>
                  <h4>Description:</h4>
                  <p className='lead flex-shrink-0'> {listing.description}</p>
                </div>
                <div id="map d-flex align-items-center jusify-content-center mt-4" style={{width: "80%", height: "100vh", zIndex: '1'}}>
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