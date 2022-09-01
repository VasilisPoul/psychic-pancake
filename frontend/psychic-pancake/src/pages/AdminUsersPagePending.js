import { useState, useEffect } from "react";
import axios from "../api/axios";
import AdminNavbar from "../components/AdminNavbar";

export default function AdminPendingPagePanel() {


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

    // useEffect(() => {
    //     try{
    //         axios.get();
    //     }
    //     catch (error) {

    //     }
    // }, [])

    return (
        <>
            <AdminNavbar />

            <div className="container">
                <table className="table">
                    <thead>
                        <tr>
                            <th scope="col">Id</th>
                            <th scope="col">Username</th>
                            <th scope="col">Email</th>
                            <th scope="col">Response</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map((item) => {
                            return (
                                <tr>
                                    <th scope="row">{item.id}</th>
                                    <td>{item.username}</td>
                                    <td>{item.email}</td>
                                    <td>
                                        <span className="fw-bold" style={{cursor:"pointer"}}>yes</span>&nbsp;<span style={{cursor:"pointer"}} className="fw-bold">no</span>
                                    </td>
                                </tr>
                            );
                        })}
                    </tbody>

                </table>
            </div>
        </>
    );
}