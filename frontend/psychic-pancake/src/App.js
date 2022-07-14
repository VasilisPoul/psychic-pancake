import './App.css';
import AdminUsersPage from './pages/AdminUsersPage'
import AdminPendingPagePanel from './pages/AdminUsersPagePending'
import WelcomePage from './pages/WelcomePage'
import Login from './components/Login';
import Signup from './components/Signup'
import {
  BrowserRouter,
  Routes,
  Route,
  Navigate
} from "react-router-dom";
import RequestPending from './components/RequestPending';

function App() {
  console.log(process.env.REACT_APP_URL)
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
        <Route path="/request-sent" element={<RequestPending />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
