export default function Login() {
  return (
    <>
      <div className="col-xs-1" align="center"><h3>Sign In</h3></div>
      <div className="container d-flex justify-content-center" >
        <div class="col-md-8 d-flex justify-content-center" style={{ boxShadow: "0px 14px 80px rgb(34 35 58 / 20%)" }}>

          <div className="row">
            <form>
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