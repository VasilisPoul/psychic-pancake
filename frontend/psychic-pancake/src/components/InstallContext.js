import { createContext, useState, useEffect } from "react";
import axios from "../api/axios";
const InstallContext = createContext();

function InstallContextComponent(props) {
  const [installed, setInstalled] = useState('');
  useEffect(() => {
    try {
      axios.get('/api/install')
      .then((response) => {
        if(!response.data.installed){
            localStorage.setItem("InstalledToken", 'false')
          setInstalled(false)
          
        }
        else {
          setInstalled(true);
        }
      })
    } catch (error) {

    }
  }, [localStorage.getItem("InstalledToken")]);
  

  return (
    <InstallContext.Provider value={{ installed, setInstalled }}>
      {props.children}
    </InstallContext.Provider>
  );
}


export { InstallContext, InstallContextComponent }