import AdminUsersPage from '../pages/AdminUsersPage'
import AdminPendingPagePanel from '../pages/AdminUsersPagePending'
import WelcomePage from '../pages/WelcomePage'
import Login from './Login';
import Signup from './Signup'
import {
  BrowserRouter,
  Routes,
  Route,
  Navigate
} from "react-router-dom";
import RequestPending from './/RequestPending';


export default function RoutesComponent() {
  const isAdmin = false;
  const isLoggedIn = false;
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={isAdmin ? <Navigate to="users" /> : (isLoggedIn ? <span>FrontPage</span> : <WelcomePage />)} />
        <Route path="/signup" element={isAdmin ? <Navigate to="/" /> : (isLoggedIn ? <Navigate to="/" /> : <Signup />)} />
        <Route path="/login" element={isAdmin ? <Navigate to="/" /> : (isLoggedIn ? <Navigate to="/" /> : <Login />)} />
        <Route path="/users" element={isAdmin ? <AdminUsersPage /> : <span>Not Authorized</span>} />
        <Route path="/pending" element={isAdmin ? <AdminPendingPagePanel /> : <span>Not Authorized</span>} />
        <Route path="/request-sent" element={(isAdmin || isLoggedIn) ? <Navigate to="/" /> : <RequestPending />} />
      </Routes>
    </BrowserRouter>
  );
}