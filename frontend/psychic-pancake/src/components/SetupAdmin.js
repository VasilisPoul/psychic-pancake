import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import '../style.css'
import axios from "../api/axios";
import countries_csv from '../assets/countries.csv';
import Papa from 'papaparse';

export default function SetupAdmin(props) {
  const [email, setEmail] = useState('');
  const [uid, setUid] = useState('');
  const [password, setPassword] = useState('');
  const [country, setCountry] = useState('')
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false)
  const [countriesRecords, setCountiresRecords] = useState([]);

  const HandleSubmit = (e) => {
    e.preventDefault();
    try {
      setLoading(true)
      axios.post('/api/install', {
        uid,
        email,
        password,
        country,
        phone_num: '0',
        VAT: '0',
        location: {
          name: '',
          latitude: 0,
          longitude: 0
        },
        last_name: 'admin',
        first_name: 'admin',
        role: 'admin'
      })
        .then((response) => {
          navigate("/")
        }
        )
        .catch()
    } catch (error) {

    }
  }

  useEffect(() => {
    Papa.parse(countries_csv, {
      download: true,
      complete: function (input) {
        const records = input.data;
        records.forEach(element => {
          setCountiresRecords(countriesRecords => [...countriesRecords, element[3]])
        });

      }
    });
  }, [])

  const SelectCountry = (e) => {
    setCountry(e.target.innerHTML)
  }

  return (
    <div className="Auth-form-container">
      <form className="Auth-form" onSubmit={HandleSubmit}>
        <div className="Auth-form-content">
          <h3 className="Auth-form-title">Admin Information</h3>
          <div className="form-group mt-3">
            <label>Admin Username</label>
            <input
              type="username"
              className="form-control mt-1"
              placeholder="Enter admin username"
              onChange={(e) => { setUid(e.target.value) }}
            />
          </div>
          <div className="form-group mt-3">
            <label>Email address</label>
            <input
              type="email"
              className="form-control mt-1"
              placeholder="Enter email"
              onChange={(e) => { setEmail(e.target.value) }}
            />
          </div>
          <div className="form-group mt-3">
            <label>Country</label>
            <div class="dropdown">
              <button class="btn btn-light dropdown-toggle w-100" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                {country ? country : 'Country'}
              </button>
              <ul class="dropdown-menu w-100" style={{ overflowY: 'auto', maxHeight: '280px' }} aria-labelledby="dropdownMenuButton1">
                {countriesRecords.slice(1).sort().map(item => {
                  return (<>
                    <li onClick={SelectCountry}><a class="dropdown-item" href="#">{item}</a></li>
                  </>)
                })}
              </ul>
            </div>
          </div>
          <div className="form-group mt-3">
            <label>Password</label>
            <input
              type="password"
              className="form-control mt-1"
              placeholder="Enter password"
              onChange={(e) => { setPassword(e.target.value) }}
            />
          </div>
          <div className="d-grid gap-2 mt-3">
            <button type="submit" className="btn btn-primary">
              Submit
            </button>
          </div>
          {loading && <span>Loading...</span>}
        </div>
      </form>

    </div>
  );
}