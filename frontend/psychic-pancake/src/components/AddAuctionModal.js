import { useState } from "react";
import { Navigate } from "react-router-dom";
import axios from "../api/axios";
import { useNavigate } from "react-router-dom";

export default function AddAuctionModal() {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [categories, setCategories] = useState([]);
  const [first_bid, setFirstBid] = useState(null);
  const [ends, setEnds] = useState('');
  const navigate = useNavigate();

  const HandleSubmit = (e) => {
    e.preventDefault()
    try {
      axios.post("/api/listings", { name, categories, description, first_bid, ends }, {
        headers: {
          'accept': 'application/json',
          'Content-Type': 'application/json',
          'authorization': localStorage.getItem('AuthToken')
        }
      })
        .then(function (response) {
          window.location.reload(false);
        })
        .catch(
          function (error) {
          }
        )
    }
    catch (error) {
      alert(error)
    }

  }

  const HandleEnds = (e) => {
    const dateString = e.target.value.split('T')[0]
    const timeString = e.target.value.split('T')[1]
    const year = dateString.split('-')[0]
    const month = dateString.split('-')[1]
    const day = dateString.split('-')[2]
    var months_arr = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
      "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    // console.log(dateString)
    // console.log(year, months_arr[month-1], day)
    let finalDateTime = `${months_arr[month - 1]}-${day}-${year.slice(-2)} ${timeString}:00`
    console.log(finalDateTime)
    setEnds(finalDateTime)
  }

  return (
    <>
      <button type="button" className=" btn btn-light mb-3" data-bs-toggle="modal" data-bs-target="#exampleModal">New Auction</button>

      <div className="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div className="Auth-form-container">
          <div className="modal-dialog" role="document">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title" id="exampleModalLabel">New Auction</h5>
                <button type="button" className="close" data-bs-dismiss="modal" aria-label="Close">
                  <span aria-bs-hidden="true">&times;</span>
                </button>
              </div>
              <div className="modal-body">
                <form className="Auth-form" onSubmit={HandleSubmit}>
                  <div className="Auth-form-content">
                    <h3 className="Auth-form-title">Auction Information</h3>
                    <div className="form-group mt-3">
                      <label>Name</label>
                      <input
                        type="text"
                        className="form-control mt-1"
                        placeholder="Enter Auction Name"
                        onChange={(e) => { setName(e.target.value) }}
                      />
                    </div>
                    <div className="form-group mt-3">
                      <label>Categories</label>
                      <small> (Comma separated)</small>
                      <input
                        type="text"
                        className="form-control mt-1"
                        placeholder="Enter Auction Categories"
                        onChange={(e) => { setCategories(e.target.value.split(',')) }}
                      />
                    </div>
                    <div className="form-group mt-3">
                      <label>Description</label>
                      <textarea
                        type="text"
                        className="form-control mt-1"
                        placeholder="Enter Auction Description"
                        onChange={(e) => { setDescription(e.target.value) }}
                      />
                    </div>
                    <div className="form-group mt-3">
                      <label>FLoor Price</label>
                      <input
                        type="number"
                        className="form-control mt-1"
                        placeholder="Enter Auction Floor Price"
                        step="0.01"
                        onChange={(e) => { setFirstBid(parseFloat(e.target.value)) }}
                      />
                    </div>
                    <div className="form-group mt-3">
                      <label>Ending Date</label>
                      <input
                        type="datetime-local"
                        className="form-control mt-1"
                        placeholder="Enter Auction Ending Date"
                        onChange={HandleEnds}
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
            </div>
          </div>
        </div>
      </div>
    </>
  );
}