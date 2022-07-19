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

export default function RoutesComponent() {
  const {user, setUser} = useContext(UserContext);
  let isAdmin = false;
  let isLoggedIn = false;
  
  // useEffect(() => {
  //   console.log("id: ", user.id)
  //   if (localStorage == null)
  //     localStorage.setItem("user", user.id);

  //   // setUser(user)
  //   console.log("localStorage: ", localStorage.getItem('user'))
  // }, [user])

  if (typeof user !== 'undefined'){
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
      </Routes>
    </BrowserRouter>
  );
}