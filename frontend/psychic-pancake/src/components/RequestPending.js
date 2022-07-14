import { Link } from "react-router-dom";

export default function RequestPending() {

  return (
    <>
      <div className="container d-flex justify-content-center mt-4" >
        <div class="" style={{ boxShadow: "0px 14px 80px rgb(34 35 58 / 20%)" }}>
          <div className="row">
            <p>Your request has been received</p>
          </div>
          <div className="row">
            <p>You will be able to fully use Psychic Pancake when you are accepted</p>
          </div>
          <div>
            Go to the <Link to="/">home page</Link>.
          </div>
        </div>
      </div>
    </>
  );
}