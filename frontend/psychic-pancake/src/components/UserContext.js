import { createContext, useMemo, useState } from "react";

const UserContext = createContext();

function UserContextComponent(props) {
  
  const [ user, setUser ] = useState({});
  const providerValue = useMemo(() => ({user, setUser}), [user, setUser]);

  return (
    <UserContext.Provider value={providerValue}>
      {props.children}
    </UserContext.Provider>
  );
}


export { UserContext, UserContextComponent }