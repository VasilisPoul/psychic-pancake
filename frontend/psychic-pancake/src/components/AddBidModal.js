import { useState } from "react";
import axios from "../api/axios";

export default function AddBidModal(props) {
  const listing = props.listing;
  const [ amount, setAmount ] = useState('');
  
  const HandleSubmit = (e) => {
    console.log(amount)
    try {
      e.preventDefault();
      axios.post(`/api/listings/${listing.item_id}/bids`, 
        {amount},
        {
          headers: {
            'authorization': localStorage.getItem('AuthToken')
          }
        })
        .then(
          function (response) {
            alert('Successfully placed bid')
            window.location.reload(false)
          }
        )
        .catch(
          function (error) {
            alert('You have to bid higher than the current highest bid.')
            console.log(amount)
            window.location.reload(false)
          }
        )
    }
    catch (error) {
      console.log(error)
    }
  }
  return (
    <>
      <button type="button" className=" btn btn-light mb-3" data-bs-toggle="modal" data-bs-target="#bidModal">New Bid</button>

      <div className="modal fade" id="bidModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div className="Auth-form-container">
          <div className="modal-dialog" role="document">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title" id="exampleModalLabel">New Bid</h5>
                <button type="button" className="close" data-bs-dismiss="modal" aria-label="Close">
                  <span aria-bs-hidden="true">&times;</span>
                </button>
              </div>
              <div className="modal-body">
                <form className="Auth-form" onSubmit={HandleSubmit}>
                  <div className="Auth-form-content">
                    <div className="form-group mt-3">
                    </div>
                    <div className="form-group mt-3">
                      <label>New Bid</label>
                      <input
                        type="text"
                        className="form-control mt-1"
                        placeholder="Enter Bid"
                        onChange={(e) => {setAmount(parseFloat(e.target.value));}}
                      />
                    </div>
                    <div className="d-grid gap-2 mt-3">
                      <button type="submit" className="btn btn-primary">
                        Submit
                      </button>
                      <button type="cancel" className="btn btn-grey" data-bs-dismiss="modal" >
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