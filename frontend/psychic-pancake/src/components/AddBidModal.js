import { useState } from "react";
import axios from "../api/axios";

export default function AddBidModal(props) {
  const listing = props.listing;
  const [amount, setAmount] = useState('');
  const [isSure, setIsSure] = useState(false)
  const [validation, setValidation] = useState(false)

  const HandleValidation = (e) => {
    e.preventDefault();
    setValidation(true);
  }

  const HandleSubmit = (e) => {
    console.log(amount)
    try {
      e.preventDefault();
      axios.post(`/api/listings/${listing.item_id}/bids`,
        { amount },
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
      <button type="button" className=" btn btn-outline-dark mb-3 w-100" data-bs-toggle="modal" data-bs-target="#bidModal">New Bid</button>

      <div className="modal fade" id="bidModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div className="Auth-form-container">
          <div className="modal-dialog" role="document">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title" id="exampleModalLabel">New Bid</h5>
                <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close" />

              </div>
              <div className="modal-body">
                {!validation && <form className="Auth-form" onSubmit={HandleValidation}>
                  <div className="Auth-form-content">
                    <div className="form-group mt-3">
                    </div>
                    <div className="form-group mt-3">
                      <label>New Bid</label>
                      <input
                        type="number"
                        className="form-control mt-1"
                        placeholder="Enter Bid"
                        step="0.01"
                        onChange={(e) => { setAmount(parseFloat(e.target.value)); }}
                      />
                    </div>
                    <div className="d-grid gap-2 mt-3">
                      <button type="submit" className="btn btn-primary">
                        Submit
                      </button>
                      <button type='button' className="btn btn-grey" data-bs-dismiss="modal" aria-label="Close" >
                        Cancel
                      </button>
                    </div>
                  </div>
                </form>}
                {validation && <form onSubmit={HandleSubmit}>
                  <p>Are you sure you want to place a {amount} bid?</p>
                  <div className="d-grid gap-2 mt-3">
                    <button type="submit" className="btn btn-primary">
                      YES
                    </button>
                    <button type='button' className="btn btn-grey" onClick={(e) => { e.preventDefault(); setValidation(false) }}>
                      NO
                    </button>
                  </div>
                </form>}
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}