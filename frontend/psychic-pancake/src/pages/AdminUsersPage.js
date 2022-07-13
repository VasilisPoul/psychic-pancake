import AdminNavbar from "../components/AdminNavbar";

export default function AdminUsersPagePanel() {


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


    return (
        <>
            <AdminNavbar />

            <div className="container">
                <table class="table">
                    <thead>
                        <tr>
                            <th scope="col">Id</th>
                            <th scope="col">Username</th>
                            <th scope="col">Email</th>
                        </tr>
                    </thead>
                        <tbody>
                            {users.map((item) => {
                                return (
                                    <tr>
                                        <th scope="row">{item.id}</th>
                                        <td>{item.username}</td>
                                        <td>{item.email}</td>
                                    
                                    </tr>
                                );
                            })}
                        </tbody>
                    
                </table>
            </div>
        </>
    );
}