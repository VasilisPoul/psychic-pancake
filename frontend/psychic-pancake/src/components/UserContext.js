import { createContext, useMemo, useState, useEffect } from "react";
import axios from "../api/axios";
const UserContext = createContext();

function UserContextComponent(props) {

  const [user, setUser] = useState({});

  return (
    <UserContext.Provider value={{ user, setUser }}>
      {props.children}
    </UserContext.Provider>
  );
}


export { UserContext, UserContextComponent }