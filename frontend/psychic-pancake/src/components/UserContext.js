import { createContext, useMemo, useState, useEffect } from "react";
import axios from "../api/axios";
const UserContext = createContext();

function UserContextComponent(props) {
  let currentUser = null;
  const [user, setUser] = useState({});
  useEffect(()=> {
    try {
      axios.get("/api/user/me", {
        headers: {
          accept: 'application/json',
          authorization: localStorage.getItem('AuthToken')
        }
      }).then(function (response) {
        setUser({ 'username': response.data.uid, 'role': response.data.role })
      })
      // currentUser = JSON.parse(localStorage.getItem("user"));
    } catch (error) {
      setUser(JSON.parse('{}'))
    }
  },[localStorage.getItem('AuthToken')])
  
  

  return (
    <UserContext.Provider value={{ user, setUser }}>
      {props.children}
    </UserContext.Provider>
  );
}


export { UserContext, UserContextComponent }