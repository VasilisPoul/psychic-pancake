import AdminUserList from "../components/AdminUserList"
import { useState, useEffect } from "react";

export default function AdminPanel() {


    const users = [
        {
            id: 0,
            username: 'foo',
            email: 'foo@test.com'
        },
        {
            id: 1,
            username: 'bar',
            email: 'bar@test.com'
        }
    ];

    const [selectedUser, setSelectedUser] = useState({});

    return (
        <div className="row">
            <div className="col-sm-3">
                <AdminUserList userList={users} setSelectedUser={setSelectedUser} selectedUser={selectedUser} />
            </div>
            <div className="col-sm-9">
                <div className="row">

                    <span className="col-sm-4">{selectedUser.id}</span>
                    <span className="col-sm-4">{selectedUser.username}</span>
                    <span className="col-sm-4">{selectedUser.email}</span>
                    
                </div>
                {/* <div className="row">
                    <span>Photo:</span>
                </div>
                <div className="row">
                    <span>Items :</span>
                </div> */}
            </div>
        </div>
    );
}