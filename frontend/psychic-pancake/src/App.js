import logo from './logo.svg';
import './App.css';
import Navbar from './components/Navbar';
import LandingPage from './pages/Landing';
import AdminPanel from './pages/AdminPage';

function App() {
  return (
    <div>
      <Navbar />
      <AdminPanel />
    </div>
  );
}

export default App;
