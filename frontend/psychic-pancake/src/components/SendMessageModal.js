import { useState } from "react";
import axios from "../api/axios";

export default function SendMessageModal(props) {
  const to = props.to;

  const HandleSubmit = (e) => {
    try {
      e.preventDefault()
      axios.post('/api/messages',
        {
          to,
          subject,
          body
        },
        {
          headers: {
            'authorization': localStorage.getItem('AuthToken')
          }
        }
      )
      .then(
        function (response){
          window.location.reload(false)
        }
      )
    }
    catch (error) {
      console.log(error)
    }
  }
  const [subject, setSubject] = useState('');
  const [body, setBody] = useState('');

  return (
    <>
      <button type="button" className=" btn btn-light mb-3" data-bs-toggle="modal" data-bs-target="#msgModal">New Message</button>

      <div className="modal fade" id="msgModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div className="Auth-form-container">
          <div className="modal-dialog" role="document">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title" id="exampleModalLabel">New Message</h5>
                <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"/>
              </div>
              <div className="modal-body">
                <form className="Auth-form" onSubmit={HandleSubmit}>
                  <div className="Auth-form-content">
                    <div className="form-group mt-3">
                    </div>
                    <div className="form-group mt-3">
                      <label>Subject</label>
                      <input
                        type="text"
                        className="form-control mt-1"
                        placeholder="Enter Subject"
                        onChange={(e) => { setSubject(e.target.value) }}
                      />
                    </div>
                    <div className="form-group mt-3">
                      <label>Message</label>
                      <textarea
                        type="text"
                        className="form-control mt-1"
                        placeholder="Enter Message"
                        onChange={(e) => { setBody(e.target.value) }}
                      />
                    </div>
                    <div className="d-grid gap-2 mt-3">
                      <button type="submit" className="btn btn-primary" >
                        Submit
                      </button>
                      <button type="" className="btn btn-grey" data-bs-dismiss="modal" onClick={(e) => e.preventDefault()} >
                        Cancel
                      </button>
                    </div>
                  </div>
                </form>
              </div>
              {/* <div className="modal-footer">
              <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
              <button type="button" className="btn btn-primary">Save changes</button>
            </div> */}
            </div>
          </div>
        </div>
      </div>
    </>
  );
}