import React, { useState, useEffect } from 'react';
import { Link, Navigate, useNavigate } from 'react-router-dom'
import '../style.css'
import axios from '../api/axios';
import countries_csv from '../assets/countries.csv';
import Papa from 'papaparse';
import { GeoSearchControl, OpenStreetMapProvider } from 'leaflet-geosearch';
import { MapContainer, TileLayer, useMap, Marker, Popup, useMapEvents } from 'react-leaflet'
import '../../node_modules/leaflet-geosearch/dist/geosearch.css';
import '../style.css'
import { map } from 'leaflet';


export default function Signup() {
  const [locationData, setLocationData] = useState([]);
  const handleLocation = (result) => {
   
    setLocationData(result.location)
  }

  const SearchField = (props) => {
    console.log(props)
    const provider = new OpenStreetMapProvider();

    // @ts-ignore


    const map = useMap();
    // const mapEvents = useMapEvents();
    useEffect(() => {

      const searchControl = new GeoSearchControl({
        provider: provider,
        ...props,
      });

      // console.log(map['_lastCenter']);
      map.addControl(searchControl);
      map.on('geosearch/showlocation', handleLocation);
      return () => map.removeControl(searchControl);
    }, []);
    return null;
  };

  const navigate = useNavigate();

  const HandleSignUp = (e) => {
    e.preventDefault();

    try {
      if (password !== reenteredPassword)
        alert('Passwords do not match')
      else {
        axios.post("/api/user", {
          uid,
          password,
          role,
          email,
          country,
          location: { name: locationData.label, latitude: locationData.y, longitude: locationData.x },
          last_name: surname,
          first_name: name,
          VAT: vat,
          phone_num: phoneNumber
        }, {
          headers: {
            'accept': 'application/json',
            'Content-Type': 'application/json'
          }
        })
          .then(
            function (response) {
              navigate("/request-sent")

            }
          )
          .catch(function (error) {
            console.log(`${error}`);
            if (error.response.status === 409) {
              alert(error.response.data.reason)
            }
          });
      }
    }
    catch (error) {
      console.log("error" + error)
    }

  }
  const HandleCancel = () => {
    navigate("/")
  }

  const [uid, setUid] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [reenteredPassword, setReenteredPassword] = useState('');
  const [streetName, setStreetName] = useState('');
  const [streetNumber, setStreetNumber] = useState('');
  const [postcode, setPostCode] = useState('');
  const [name, setName] = useState('');
  const [surname, setSurname] = useState('');
  const [vat, setVat] = useState('');
  const [country, setCountry] = useState('');
  const [role, setRole] = useState('buyer');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [countriesRecords, setCountiresRecords] = useState([]);

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

  console.log(locationData)
  return (
    <>
      <div className="Auth-form-container">
        <form className="Signup-form" onSubmit={HandleSignUp}>
          <div className="Auth-form-content">
            <h3 className="Auth-form-title">Sign Up</h3>

            <div className="row">
              <div className="col">
                <>
                  <div className="form-outline mb-4 form-group mt-3">
                    <label className="form-label">Username</label><br />
                    <input required
                      type="text"
                      name="username"
                      className="form-control mt-1"
                      placeholder="Enter username"
                      onChange={(e) => { setUid(e.target.value) }} />

                  </div>
                  <div className="form-outline mb-4 form-group mt-3">
                    <label className="form-label">Role</label><br />
                    <div className="dropdown">
                      <button className="btn btn-light w-100 dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        {role}
                      </button>
                      <div className="dropdown-menu" aria-labelledby="dropdownMenuButton">
                        <div className="dropdown-item" onClick={(e) => setRole('buyer')}>Buyer</div>
                        <div className="dropdown-item" onClick={(e) => setRole('seller')}>Seller</div>
                      </div>
                    </div>
                  </div>
                  <div className="form-outline mb-4 form-group mt-3">
                    <label className="form-label">Email address</label><br />
                    <input required
                      type="email"
                      name="email"
                      className="form-control mt-1"
                      placeholder="Enter Email"
                      onChange={(e) => { setEmail(e.target.value) }} />
                  </div>
                  <div className="form-outline mb-4">
                    <label>Password</label><br />
                    <input required
                      type="password"
                      name="password"
                      className="form-control mt-1"
                      placeholder="Enter your password"
                      onChange={(e) => { setPassword(e.target.value) }} />

                  </div>
                  <div className="form-outline mb-4">
                    <label> Re-enter Password</label><br />
                    <input required
                      type="password"
                      name="password"
                      className="form-control mt-1"
                      placeholder="Re-enter your password"
                      onChange={(e) => { setReenteredPassword(e.target.value) }}
                    />
                  </div>
                </>
              </div>
              <div className="col">
                <>
                  <div className="form-group mt-3">
                    <label className="form-label">Name</label><br />
                    <input required
                      type="text"
                      name="name"
                      className="form-control mt-1"
                      placeholder="Enter Name"
                      onChange={(e) => { setName(e.target.value) }} />
                  </div>
                  <div className="form-group mt-3">
                    <label>Surname</label><br />
                    <input required
                      type="text"
                      name="surname"
                      className="form-control mt-1"
                      placeholder="Enter surname"
                      onChange={(e) => { setSurname(e.target.value) }} />

                  </div>
                  <div className="form-group mt-3">
                    <label>Phone Number</label><br />
                    <input required
                      type="text"
                      name="phone"
                      className="form-control mt-1"
                      placeholder="Enter phone number"
                      onChange={(e) => { setPhoneNumber(e.target.value) }} />

                  </div>
                  <div className="form-group mt-3">
                    <label>VAT Number</label><br />
                    <input required
                      type="text"
                      name="vat"
                      className="form-control mt-1"
                      placeholder="Enter VAT number"
                      onChange={(e) => { setVat(e.target.value) }} />

                  </div>
                </>
              </div>
              <div className="col">
                <>
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
                 <div className='mt-4'>
                  <label>Please fill your address in the map below:</label>
                 
                  <MapContainer center={[51.505, -0.09]} zoom={13} scrollWheelZoom={false}>
                    <TileLayer
                      attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                      url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    <SearchField
                       keepResult={true}
                       popupFormat={({ query, result }) => result.label} />
                  </MapContainer>
                  </div>
                </>
                <div id="map">
                </div>
              </div>
            </div>
            <div className="d-grid gap-2 mt-3">
              <button type="submit" className="btn btn-primary">
                Submit
              </button>
              <button type="submit" className="btn btn-grey" onClick={HandleCancel}>
                Cancel
              </button>
            </div>
          </div>

        </form>

      </div>
    </>
  );
}