import { useContext, useEffect, useState } from 'react';
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
import SentMessages from '../pages/SentMessages';
import ReceivedMessages from '../pages/ReceivedMessages';
import axios from '../api/axios';

export default function RoutesComponent() {
  const {user, setUser} = useContext(UserContext);
  let isAdmin = false;
  let isLoggedIn = false;
  const [isInstalled, setIsInstalled] = useState(false);

  useEffect(() => {
    try {
      axios.get('/api/install')
      .then((response) => {
        setIsInstalled(response.data.installed)
      })
    } catch (error) {

    }
  }, []);

  isLoggedIn = localStorage.getItem('AuthToken');

  isAdmin = isLoggedIn && (user['role'] === 'admin');
  console.log({isInstalled})
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={!isInstalled ? <Navigate to='install' /> : (isAdmin ? <Navigate to="users" /> : (isLoggedIn ? <Navigate to="/auctions" /> : <WelcomePage />))} />
        <Route path="/auctions" element={!isInstalled ? <Navigate to='install' /> : <HomePage />} />
        <Route path="/signup" element={!isInstalled ? <Navigate to='install' /> : (isAdmin ? <Navigate to="/" /> : (isLoggedIn ? <Navigate to="/" /> : <Signup />))} />
        <Route path="/login" element={!isInstalled ? <Navigate to='install' /> : (isAdmin ? <Navigate to="/" /> : (isLoggedIn ? <Navigate to="/" /> : <Login />))} />
        <Route path="/users" element={!isInstalled ? <Navigate to='install' /> : (isAdmin ? <AdminUsersPage /> : <span>Not Authorized.</span>)} />
        <Route path="/pending" element={!isInstalled ? <Navigate to='install' /> : (isAdmin ? <AdminPendingPagePanel /> : <span>Not Authorized.</span>)} />
        <Route path="/request-sent" element={!isInstalled ? <Navigate to='install' /> : ((isAdmin || isLoggedIn) ? <Navigate to="/" /> : <RequestPending />)} />
        <Route path="/install" element={!isInstalled ? <SetupAdmin /> : <span>Not Authorized.</span>} />
        <Route path="/my-auctions" element={!isInstalled ? <Navigate to='install' /> : (isLoggedIn ? <UserAuctions/> : <span>Not Authorized.</span>)} />
        <Route path="/auction/:id" element={!isInstalled ? <Navigate to='install' /> : <Auction />} />
        <Route path="/sent-messages" element={!isInstalled ? <Navigate to='install' /> : <SentMessages />} />
        <Route path="/received-messages" element={!isInstalled ? <Navigate to='install' /> : <ReceivedMessages />} />
      </Routes>
    </BrowserRouter>
  );
}