

export default function AdminUserList({ userList, selectedUser, setSelectedUser }) {

    const HandleUserClick = (e, item) => {
        setSelectedUser(item);
    }
    console.log(selectedUser)
    return (
        <div>
            <span style={{ fontWeight: "bold", fontSize: 24 }}>Users List</span>
            {userList.map((item) => {
                return (
                    <div className="border-bottom" style={{ cursor: "pointer" }} onClick={(event) => setSelectedUser(item)}>
                        <div value={item}><span>Username: {item.username}</span></div>
                        <div value={item}><span>Email: {item.email}</span></div>
                    </div>
                );
            })}
        </div>
    );
}
