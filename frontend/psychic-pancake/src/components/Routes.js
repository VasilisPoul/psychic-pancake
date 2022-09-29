import { useContext, useEffect, useState } from 'react';
import AdminUsersPage from '../pages/AdminUsersPage'
import AdminPendingPagePanel from '../pages/AdminUsersPagePending'
import WelcomePage from '../pages/WelcomePage'
import Login from './Login';
import Signup from './Signup'
import { UserContext } from './UserContext';
import { InstallContext } from './InstallContext';
import User from '../pages/User';
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
import ExportAuctions from '../pages/ExportAuctions';

export default function RoutesComponent() {
  const {user, setUser} = useContext(UserContext);
  // const {installed, setInstalled} = useContext(InstallContext)
  let isAdmin = false;
  let isLoggedIn = false;
  const installed = localStorage.getItem("InstalledToken");
  isLoggedIn = localStorage.getItem('AuthToken');

  isAdmin = isLoggedIn && (user['role'] === 'admin');
  console.log(installed)
  return (
    <>
      {!installed ? 
    <BrowserRouter>
      <Routes>
        <Route path="/install" element={<SetupAdmin  />} />
        <Route path='*' element={<Navigate to='/install' />} />
      </Routes>
     
    </BrowserRouter>
    :
    <BrowserRouter>
    <Routes>
      <Route path="/" element={ (isAdmin ? <Navigate to="users" /> : (isLoggedIn ? <Navigate to="/auctions" /> : <WelcomePage />))} />
      <Route path="/auctions" element={ <HomePage />} />
      <Route path="/signup" element={ (isAdmin ? <Navigate to="/" /> : (isLoggedIn ? <Navigate to="/" /> : <Signup />))} />
      <Route path="/login" element={ (isAdmin ? <Navigate to="/" /> : (isLoggedIn ? <Navigate to="/" /> : <Login />))} />
      <Route path="/users" element={ (isAdmin ? <AdminUsersPage /> : <span>Not Authorized.</span>)} />
      <Route path="/pending" element={ (isAdmin ? <AdminPendingPagePanel /> : <span>Not Authorized.</span>)} />
      <Route path="/request-sent" element={ ((isAdmin || isLoggedIn) ? <Navigate to="/" /> : <RequestPending />)} />
      <Route path="/install" element={!installed ? <SetupAdmin  /> :  <span>Not Authorized.</span>} />
      <Route path="/my-auctions" element={ (isLoggedIn ? <UserAuctions/> : <span>Not Authorized.</span>)} />
      <Route path="/auction/:id" element={ <Auction />} />
      <Route path="/user/:id" element={<User />} />
      <Route path="/sent-messages" element={ <SentMessages />} />
      <Route path="/received-messages" element={ <ReceivedMessages />} />
      <Route path='/export-auctions' element={(isAdmin ? <ExportAuctions /> : <span>Not Authorized.</span>)} />
    </Routes>
    </BrowserRouter>
  }
  </>
  );
}