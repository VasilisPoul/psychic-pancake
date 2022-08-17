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
      console.log(`GET: ${JSON.parse(response)}`);
      setUser({ 'username': response.data.id.uid, 'user-role': 'seller' })
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