// If Buyer list bought products
// If seller list auctions, and let him add new auctions (modal?)
import { useContext, useEffect, useState } from 'react';
import Navbar from '../components/Navbar';
import { UserContext } from '../components/UserContext';
import auction from '../resources/auction.webp';
import AddAuctionModal from '../components/AddAuctionModal';

const auctionsList = [
  {
    "name": "auction 1",
    "description": "description 1"
  },
  {
    "name": "auction 2",
    "description": "description 2"
  },
  {
    "name": "auction 3",
    "description": "description 3"
  },
  {
    "name": "auction 4",
    "description": "description 4"
  },
  {
    "name": "auction 5",
    "description": "description 5"
  },

];

export default function UserAuctions() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isSeller, setIsSeller] = useState(false);

  const { user, setUser } = useContext(UserContext);

  useEffect(() => {
    setIsLoggedIn(localStorage.getItem('AuthToken')? true : false);
    setIsSeller(user['role'] == 'seller')
  }, [user])
  console.log(user['role'] === 'seller')
  
  return (
    <>
      <Navbar />
      <div className='container mt-3'>
        { user['role'] === 'seller' && <AddAuctionModal />}

        <div className='row mb-2'>
          {auctionsList.map((item, idx) => {
            return (
              <div className="card mb-3" >
                <div className="row g-0">
                  <div className="col-md-4">
                    <img src={auction} className="img-fluid rounded-start" alt="..." />
                  </div>
                  <div className="col-md-8">
                    <div className="card-body">
                      <h5 className="card-title">{item.name}</h5>
                      <p className="card-text">{item.description}</p>
                      <p className="card-text"><small className="text-muted">Last updated 3 mins ago</small></p>
                    </div>
                  </div>
                </div>
              </div>
            )
          })}
        </div>
      </div>
    </>
  );
}
