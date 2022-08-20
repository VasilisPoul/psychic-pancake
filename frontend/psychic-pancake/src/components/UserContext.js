import { createContext, useMemo, useState, useEffect } from "react";

const UserContext = createContext();

function UserContextComponent(props) {
  let currentUser = null;
  try {
    axios.get("/api/token/check", {
      headers: {
        accept: 'application/json',
        authorization: localStorage.getItem('AuthToken')
      }
    }).then(function (response) {
      setUser({ 'username': response.data.uid, 'role': response.data.role })
    })
    currentUser = JSON.parse(localStorage.getItem("user"));
  } catch (error) {
    currentUser = JSON.parse('{}')
  }
  const [user, setUser] = useState(currentUser);

  return (
    <UserContext.Provider value={{ user, setUser }}>
      {props.children}
    </UserContext.Provider>
  );
}


export { UserContext, UserContextComponent }