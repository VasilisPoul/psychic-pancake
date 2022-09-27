import { useEffect, useState } from "react";
import AdminNavbar from "../components/AdminNavbar";
import axios from "../api/axios";
import { useNavigate } from "react-router-dom";
import parse from 'xml-parser';
import stringify from 'xml-stringify';

export default function ExportAuctions() {
    const [xmlListings, setXmlListings] = useState('');
    const [jsonListings, setJsonListings] = useState('');
    const [active,  setActive] = useState('none')
    const HandleXML = (e) => {
        e.preventDefault();
        axios.get('/api/admin/listings', {
            headers: {
                'accept': 'application/xml',
                'authorization': localStorage.getItem('AuthToken')
            }
        })
        .then((response) => 
            setXmlListings(stringify(parse(response.data)),
            setActive('xml')
        ))
        .catch()
    }

    const HandleJSON = (e) => {
        e.preventDefault();
        axios.get('/api/admin/listings', {
            headers: {
                'accept': 'application/json',
                'authorization': localStorage.getItem('AuthToken')
            }
        })
        .then((response) => setJsonListings(JSON.stringify(response.data)),
        setActive('json'))
        .catch()
    }

    return (
        <>
            <AdminNavbar />

            <div className="container">
                <div className="row mt-5">
                    <div className="col">
                        <div className="btn btn-outline-dark w-100" onClick={HandleXML}>Export in XML</div>
                    </div>
                    <div className="col">
                        <div className="btn btn-outline-dark w-100" onClick={HandleJSON}>Export in JSON</div>
                    </div>
                </div>
                <span>
                    {active === 'json'&& jsonListings}
                    {active === 'xml'&& xmlListings}
                </span>
            </div>
        </>
    );
}