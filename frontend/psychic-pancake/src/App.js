import RoutesComponent from './components/Routes';
import { useMemo, useContext } from 'react';
import { UserContextComponent, UserContext } from './components/UserContext';

function App() {

  return (
    <UserContextComponent>
      <RoutesComponent />
    </UserContextComponent>
  );
}

export default App;
