import { useContext, useEffect } from 'react';
import AdminUsersPage from '../pages/AdminUsersPage'
import AdminPendingPagePanel from '../pages/AdminUsersPagePending'
import WelcomePage from '../pages/WelcomePage'
import Login from './Login';
import Signup from './Signup'
import { UserContext } from './UserContext';
import {
  BrowserRouter,
  Routes,
  Route,
  Navigate
} from "react-router-dom";
import RequestPending from './RequestPending';
import HomePage from '../pages/HomePage';
import SetupAdmin from './SetupAdmin';
import UserAuctions from '../pages/UserAuctions';
import Auction from '../pages/Auction';
import Messages from '../pages/Messages';
import axios from '../api/axios';

export default function RoutesComponent() {
  const {user, setUser} = useContext(UserContext);
  let isAdmin = false;
  let isLoggedIn = false;
  
  useEffect(() => {
   
    axios.get("/api/token/check", {
      headers: {
        accept: 'application/json',
        authorization: localStorage.getItem('AuthToken')
      }
    }).then(function (response) {
      // console.log(`GET: ${JSON.parse(response)}`);
      setUser({ 'username': response.data.id.uid, 'user-role': 'seller' })
    }).catch(
      setUser({})
    )
  }, [localStorage.getItem('AuthToken')])
  
  console.log("localStorage: ", localStorage.getItem('AuthToken'))


  if (typeof user !== 'undefined' && user){
    console.log("user: ", user)
    isAdmin = user['user-role'] === 'admin';
    isLoggedIn = user['user-role'] === 'seller' || user['user-role'] === 'buyer';
  }

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={isAdmin ? <Navigate to="users" /> : (isLoggedIn ? <HomePage /> : <WelcomePage />)} />
        <Route path="/signup" element={isAdmin ? <Navigate to="/" /> : (isLoggedIn ? <Navigate to="/" /> : <Signup />)} />
        <Route path="/login" element={isAdmin ? <Navigate to="/" /> : (isLoggedIn ? <Navigate to="/" /> : <Login />)} />
        <Route path="/users" element={isAdmin ? <AdminUsersPage /> : <span>Not Authorized.</span>} />
        <Route path="/pending" element={isAdmin ? <AdminPendingPagePanel /> : <span>Not Authorized.</span>} />
        <Route path="/request-sent" element={(isAdmin || isLoggedIn) ? <Navigate to="/" /> : <RequestPending />} />
        <Route path="/install" element={<SetupAdmin />} />
        <Route path="/my-auctions" element={isLoggedIn ? <UserAuctions/> : <span>Not Authorized.</span>} />
        <Route path="/auction" element={<Auction />} />
        <Route path="/messages" element={<Messages />} />
      </Routes>
    </BrowserRouter>
  );
}