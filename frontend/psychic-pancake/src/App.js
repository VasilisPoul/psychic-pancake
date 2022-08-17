import RoutesComponent from './components/Routes';
import { useMemo, useContext } from 'react';
import { UserContextComponent, UserContext } from './components/UserContext';

function App() {
  // console.log(process.env.REACT_APP_URL)
  

  return (
    <UserContextComponent>
      <RoutesComponent />
    </UserContextComponent>
  );
}

export default App;
