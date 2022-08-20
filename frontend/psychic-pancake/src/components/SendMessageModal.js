export default function SendMessageModal() {
  return (
    <>
      <button type="button" className=" btn btn-light mb-3" data-bs-toggle="modal" data-bs-target="#msgModal">New Message</button>

      <div className="modal fade" id="msgModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div className="Auth-form-container">
          <div className="modal-dialog" role="document">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title" id="exampleModalLabel">New Message</h5>
                <button type="button" className="close" data-bs-dismiss="modal" aria-label="Close">
                  <span aria-bs-hidden="true">&times;</span>
                </button>
              </div>
              <div className="modal-body">
                <form className="Auth-form">
                  <div className="Auth-form-content">
                    <div className="form-group mt-3">
                    </div> 
                    <div className="form-group mt-3">
                      <label>Message</label>
                      <textarea
                        type="text"
                        className="form-control mt-1"
                        placeholder="Enter Message"
                      />
                    </div>
                    <div className="d-grid gap-2 mt-3">
                      <button type="submit" className="btn btn-primary">
                        Submit
                      </button>
                      <button type="submit" className="btn btn-grey" data-bs-dismiss="modal" >
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