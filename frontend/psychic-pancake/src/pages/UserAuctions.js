// If Buyer list bought products
// If seller list auctions, and let him add new auctions (modal?)
import { useContext, useEffect, useState } from 'react';
import Navbar from '../components/Navbar';
import { UserContext } from '../components/UserContext';
import auction from '../resources/auction.webp';
import AddAuctionModal from '../components/AddAuctionModal';
import axios from '../api/axios';
import { Link, useNavigate } from 'react-router-dom';

const ListingView = (item) => {
  const [listing, setListing] = useState({});
  const [img, setImg] = useState('')

  useEffect(() => {
    try {
      axios.get(item.item).then(
        function (response) {
          setListing(response.data)
          axios.get(response.data.images[0]).then(
            function (response) {
              setImg(response.data.image)
            }
          )
        }
      ).catch()
    }
    catch (error) {

    }

  }, [])
  const item_id = item.item.split('/')[3]
  const linkTo = '/auction/'+item_id
  const navigate = useNavigate()
  const HandleClick = (e) => {
    navigate(linkTo);
  }
  console.log(listing)
  return (
    <div className="card mb-3" onClick={HandleClick} style={{cursor: "pointer"}}>
      <div className="row g-0">
        <div className="col-md-4">
          <img src={img} className="img-fluid rounded-start" alt="..." />
        </div>
        <div className="col-md-8">
          <div className="card-body">
            <h5 className="card-title">{listing.name}</h5>
            <p className="card-text">{listing.description}</p>
            <p className="card-text"><small className="text-muted">Ending date: {listing.ends}</small></p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default function UserAuctions() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isSeller, setIsSeller] = useState(false);
  const [list, setList] = useState([])
  const { user, setUser } = useContext(UserContext);

  useEffect(() => {
    if (user['role'] === 'buyer') {
      try {
        axios.get('/api/user/me/items-bought', {
          headers: {
            accept: 'application/json',
            authorization: localStorage.getItem('AuthToken')
          }
        })
          .then((response) => {
            setList(response.data);
          })
      }
      catch (error) {

      }
    }
    else if (user['role'] === 'seller') {
      axios.get('/api/listings?only_active=false&seller_uid=' + user['username']).then(
        function (response) {
          setList(response.data)
        }
      )
    }
  }, [user])
  console.log(list)

  return (
    <>
      <Navbar />
      <div className='container mt-3'>
        {user['role'] === 'seller' && <AddAuctionModal />}

        <div className='row mb-2'>
          {list.map((item, idx) => {
            return (
              <>
                <ListingView key={item} item={item} />
              </>
            )
          })}
        </div>
      </div>
    </>
  );
}
