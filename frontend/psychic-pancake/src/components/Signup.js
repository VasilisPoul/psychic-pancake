export default function Signup() {
  return (
    <>
    <div className="col-xs-1" align="center"><h3>Sign Up</h3></div>
    <div className="container d-flex justify-content-center">
  <div class="col-md-8 col-lg-6 col-xl-4 offset-xl-1 d-flex justify-content-center">
    
    <div className="row">
    <form>
    <div className="form-outline mb-4">
        <label className="form-label">Name</label><br/>
        <input required
          type="name"
          name="name"
          placeholder="Enter Name" />
      </div>
      
      <div className="form-outline mb-4">
        <label className="form-label">Email address</label><br/>
        <input required
          type="email"
          name="email"
          placeholder="Enter Email" />
      </div>
      <div className="form-outline mb-4">
        <label>Password</label><br/>
        <input required
          type="password"
          name="password"
          placeholder="Enter your password" />

      </div>
      <button type="submit" className="btn btn-primary btn-block mb-4">Submit</button>
      {/* <p >
              Forgot <a href="#">password?</a>
            </p> */}

    </form>
    </div>
    </div>
    </div>
    </>
  );
}