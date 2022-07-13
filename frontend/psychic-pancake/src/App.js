import './App.css';
import AdminUsersPage from './pages/AdminUsersPage'
import AdminPendingPagePanel from './pages/AdminUsersPagePending'
import {
  BrowserRouter,
  Routes,
  Route,
  Navigate
} from "react-router-dom";

function App() {
  console.log(process.env.REACT_APP_URL)
  const isAdmin = false;
  return (
    <BrowserRouter>
    <Routes>
      <Route path="/" element={isAdmin ? <Navigate to="users" /> : <span>HI</span>}/>
      <Route path="users" element={ isAdmin ? <AdminUsersPage /> : <span>Not Authorized</span>} />
      <Route path="pending" element={isAdmin? <AdminPendingPagePanel /> : <span>Not Authorized</span>} />
    </Routes>
  </BrowserRouter>
  );
}

export default App;
