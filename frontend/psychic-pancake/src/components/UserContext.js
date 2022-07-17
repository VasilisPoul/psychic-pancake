import { createContext, useMemo, useState, useEffect } from "react";

const UserContext = createContext();

function UserContextComponent(props) {
  let currentUser = null;
  try {
    currentUser = JSON.parse(localStorage.getItem("user"));
  } catch (error) {
    currentUser = JSON.parse('{}')
  }
  const [user, setUser] = useState(currentUser);

  return (
    <UserContext.Provider value={{user, setUser}}>
      {props.children}
    </UserContext.Provider>
  );
}


export { UserContext, UserContextComponent }