import { Link, Navigate, useNavigate } from 'react-router-dom'
import '../style.css'
const GeneralInfo = () => {
  return (
    <>
      <div className="form-outline mb-4">
        <label className="form-label">Email address</label><br />
        <input required
          type="email"
          name="email"
          className="form-control mt-1"
          placeholder="Enter Email" />
      </div>
      <div className="form-outline mb-4">
        <label>Password</label><br />
        <input required
          type="password"
          name="password"
          className="form-control mt-1"
          placeholder="Enter your password" />

      </div>
      <div className="form-outline mb-4">
        <label> Re-enter Password</label><br />
        <input required
          type="password"
          name="password"
          className="form-control mt-1"
          placeholder="Re-enter your password" />

      </div>
    </>
  );
}

const AddressInfo = () => {
  return (
    <>
      <div className="form-group mt-3">
        <label>Street name</label>
        <input required
          type="text"
          name="street"
          className="form-control mt-1"
          placeholder="Enter Street" />
      </div>
      <div className="form-group mt-3">
        <label>Street number</label><br />
        <input required
          type="text"
          name="number"
          className="form-control mt-1"
          placeholder="Enter your street number" />

      </div>
      <div className="form-group mt-3">
        <label> Post code</label><br />
        <input required
          type="text"
          name="post-code"
          className="form-control mt-1"
          placeholder="Re-enter your password" />

      </div>
    </>
  );
}

const PersonalInfo = () => {
  return (
    <>
      <div className="form-group mt-3">
        <label className="form-label">Name</label><br />
        <input required
          type="text"
          name="name"
          className="form-control mt-1"
          placeholder="Enter Name" />
      </div>
      <div className="form-group mt-3">
        <label>Surname</label><br />
        <input required
          type="text"
          name="surname"
          className="form-control mt-1"
          placeholder="Enter surname" />

      </div>
      <div className="form-group mt-3">
        <label>Phone Number</label><br />
        <input required
          type="text"
          name="phone"
          className="form-control mt-1"
          placeholder="Enter phone number" />

      </div>
      <div className="form-group mt-3">
        <label>VAT Number</label><br />
        <input required
          type="text"
          name="vat"
          className="form-control mt-1"
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
  const HandleCancel = () => {
    navigate("/")
  }

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
                <GeneralInfo />
              </div>
              <div className="tab-pane fade" id="nav-personal" role="tabpanel" aria-labelledby="nav-personal-tab">
                <PersonalInfo />
              </div>
              <div className="tab-pane fade" id="nav-address" role="tabpanel" aria-labelledby="nav-address-tab">
                <AddressInfo />
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