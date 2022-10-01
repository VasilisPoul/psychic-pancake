import RoutesComponent from './components/Routes';
import { UserContextComponent, UserContext } from './components/UserContext';
import { InstallContext, InstallContextComponent } from './components/InstallContext'

function App() {
console.log("Aaaaa")
  return (
    <InstallContextComponent>
      <UserContextComponent>
        <RoutesComponent />
      </UserContextComponent>
    </InstallContextComponent>
  );
}

export default App;
