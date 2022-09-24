import React, { useState } from 'react';
import { Link, Navigate, useNavigate } from 'react-router-dom'
import '../style.css'
import axios from '../api/axios';

export default function Signup() {

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
          //TODO: fix this
          location: {name: 'spiti', latitude: 0, longitude: 0},
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
              console.log(JSON.stringify(response.data));
              navigate("/request-sent")

            }
          )
          .catch(function (error) {
            console.log(`${error}`);
            if (error.response.status === 409){
              alert(error.response.data.reason)
            }
          });
      }
    }
    catch (error) {
      console.log("eroror" + error)
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

  return (
    <>
      <div className="Auth-form-container">
        <form className="Auth-form" onSubmit={HandleSignUp}>
          <div className="Auth-form-content" id="nav-tabContent">
            <h3 className="Auth-form-title">Sign Up</h3>
            <nav>
              <div className="nav nav-tabs" id="nav-tab" role="tablist">
                <button className="nav-link active" id="nav-general-tab" data-bs-toggle="tab" data-bs-target="#nav-general" type="button" role="tab" aria-controls="nav-general" aria-selected="true">General</button>
                <button className="nav-link" id="nav-personal-tab" data-bs-toggle="tab" data-bs-target="#nav-personal" type="button" role="tab" aria-controls="nav-personal" aria-selected="false">Personal</button>
                <button className="nav-link" id="nav-address-tab" data-bs-toggle="tab" data-bs-target="#nav-address" type="button" role="tab" aria-controls="nav-address" aria-selected="false">Address</button>
              </div>
            </nav>
            <div className="tab-content" id="nav-tabContent">
              <div className="tab-pane fade show active" id="nav-general" role="tabpanel" aria-labelledby="nav-general-tab">
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
              <div className="tab-pane fade" id="nav-personal" role="tabpanel" aria-labelledby="nav-personal-tab">
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
              <div className="tab-pane fade" id="nav-address" role="tabpanel" aria-labelledby="nav-address-tab">
                <>
                  <div className="form-group mt-3">
                    <label>Country</label>
                    <input required
                      type="text"
                      name="country"
                      className="form-control mt-1"
                      placeholder="Enter Country"
                      onChange={(e) => { setCountry(e.target.value) }} />
                  </div>
                  <div className="form-group mt-3">
                    <label>Street name</label>
                    <input required
                      type="text"
                      name="street"
                      className="form-control mt-1"
                      placeholder="Enter Street"
                      onChange={(e) => { setStreetName(e.target.value) }} />
                  </div>
                  <div className="form-group mt-3">
                    <label>Street number</label><br />
                    <input required
                      type="text"
                      name="number"
                      className="form-control mt-1"
                      placeholder="Enter your street number"
                      onChange={(e) => { setStreetNumber(e.target.value) }} />

                  </div>
                  <div className="form-group mt-3">
                    <label> Post code</label><br />
                    <input required
                      type="text"
                      name="post-code"
                      className="form-control mt-1"
                      placeholder="Re-enter your password"
                      onChange={(e) => { setPostCode(e.target.value) }} />

                  </div>
                </>
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