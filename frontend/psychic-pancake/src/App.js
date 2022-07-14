import RoutesComponent from './components/Routes';
import { useState, createContext } from 'react';
import { UserContext } from './components/UserContext';

function App() {
  console.log(process.env.REACT_APP_URL)
  const [ user, setUser ] = useState({'username': 'foo', 'email': 'foo@bar.com', 'user-role': 'buyer'});
  return (
    <UserContext.Provider value={user}>
      <RoutesComponent />
    </UserContext.Provider>
  );
}

export default App;
