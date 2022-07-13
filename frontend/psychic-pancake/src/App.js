import logo from './logo.svg';
import './App.css';
import Navbar from './components/Navbar';
import LandingPage from './pages/Landing';
import AdminPanel from './pages/AdminPage';
import AdminNavbar from './components/AdminNavbar';

function App() {
  console.log(process.env.REACT_APP_URL)
  return (
    <div style={{backgroundColor:"#f0f0e1", width:"100%", height:"100%", minHeight:"100%"}}>
      <AdminNavbar />
      <div>
        <AdminPanel />
      </div>
    </div>
  );
}

export default App;
