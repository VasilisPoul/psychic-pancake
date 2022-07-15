import { Link, Navigate, useNavigate } from 'react-router-dom'

const GeneralInfo = () => {
  return (
    <>
      <div className="form-outline mb-4">
        <label className="form-label">Email address</label><br />
        <input required
          type="email"
          name="email"
          placeholder="Enter Email" />
      </div>
      <div className="form-outline mb-4">
        <label>Password</label><br />
        <input required
          type="password"
          name="password"
          placeholder="Enter your password" />

      </div>
      <div className="form-outline mb-4">
        <label> Re-enter Password</label><br />
        <input required
          type="password"
          name="password"
          placeholder="Re-enter your password" />

      </div>
    </>
  );
}

const AddressInfo = () => {
  return (
    <>
      <div className="form-outline mb-4">
        <label className="form-label">Street name</label><br />
        <input required
          type="text"
          name="street"
          placeholder="Enter Street" />
      </div>
      <div className="form-outline mb-4">
        <label>Street number</label><br />
        <input required
          type="text"
          name="number"
          placeholder="Enter your street number" />

      </div>
      <div className="form-outline mb-4">
        <label> Post code</label><br />
        <input required
          type="text"
          name="post-code"
          placeholder="Re-enter your password" />

      </div>
    </>
  );
}

const PersonalInfo = () => {
  return (
    <>
      <div className="form-outline mb-4">
        <label className="form-label">Name</label><br />
        <input required
          type="text"
          name="name"
          placeholder="Enter Name" />
      </div>
      <div className="form-outline mb-4">
        <label>Surname</label><br />
        <input required
          type="text"
          name="surname"
          placeholder="Enter surname" />

      </div>
      <div className="form-outline mb-4">
        <label>Phone Number</label><br />
        <input required
          type="text"
          name="phone"
          placeholder="Enter phone number" />

      </div>
      <div className="form-outline mb-4">
        <label>VAT Number</label><br />
        <input required
          type="text"
          name="vat"
          placeholder="Enter VAT number" />

      </div>
    </>
  );
}

export default function Signup() {

  const navigate = useNavigate();
  
  const HandleSignUp = () => {
    navigate("/request-sent")
  }
  return (
    <>
      <div className="col-xs-1" align="center"><h3>Sign Up</h3></div>
      <div className="container d-flex justify-content-center">
        <div className="col-md-8 col-lg-6 col-xl-4 offset-xl-1 d-flex justify-content-center" style={{ boxShadow: "0px 14px 80px rgb(34 35 58 / 20%)" }}>

          <div className="row">
            <form onSubmit={HandleSignUp}>
              <nav>
                <div className="nav nav-tabs" id="nav-tab" role="tablist">
                  <button className="nav-link active" id="nav-general-tab" data-bs-toggle="tab" data-bs-target="#nav-general" type="button" role="tab" aria-controls="nav-general" aria-selected="true">General</button>
                  <button className="nav-link" id="nav-personal-tab" data-bs-toggle="tab" data-bs-target="#nav-personal" type="button" role="tab" aria-controls="nav-personal" aria-selected="false">Personal</button>
                  <button className="nav-link" id="nav-address-tab" data-bs-toggle="tab" data-bs-target="#nav-address" type="button" role="tab" aria-controls="nav-address" aria-selected="false">Address</button>
                </div>
              </nav>
              <div className="tab-content" id="nav-tabContent">
                <div className="tab-pane fade show active" id="nav-general" role="tabpanel" aria-labelledby="nav-general-tab">
                  <GeneralInfo />
                </div>
                <div className="tab-pane fade" id="nav-personal" role="tabpanel" aria-labelledby="nav-personal-tab">
                  <PersonalInfo />
                </div>
                <div className="tab-pane fade" id="nav-address" role="tabpanel" aria-labelledby="nav-address-tab">
                  <AddressInfo />
                </div>
              </div>
              <button type="submit" className="btn btn-light btn-block mb-4">
                  Submit
              </button>
            </form>
          </div>
        </div>
      </div>
    </>
  );
}