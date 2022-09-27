import { useEffect, useState } from "react";
import axios from "../api/axios";
import default_image from '../assets/default-image.js'

export default function EditAuctionModal({ listing_url, listing }) {
    const [images, setImages] = useState([]);
    useEffect(() => {
        for (let i = 0; i < listing.images.length; i++) {
            axios.get(listing.images[i]).then(
                function (response) {
                    setImages(oldArray => [...oldArray, response.data.image])
                }
            )
        }
    }, [])
    const [name, setName] = useState(listing.name);
    const [description, setDescription] = useState(listing.description);
    const [categories, setCategories] = useState(listing.categories);
    const [first_bid, setFirstBid] = useState(listing.first_bid);
    const [ends, setEnds] = useState(listing.ends);
    const HandleSubmit = (e) => {
        e.preventDefault()

        try {
            axios.put(listing_url, { name, categories, description, first_bid, ends }, {
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
        let finalDateTime = `${months_arr[month - 1]}-${day}-${year.slice(-2)} ${timeString}:00`
        setEnds(finalDateTime)
    }
    const convertBase64 = (file) => {
        return new Promise((resolve, reject) => {
            const fileReader = new FileReader();
            fileReader.readAsDataURL(file)
            fileReader.onload = () => {
                resolve(fileReader.result);
            }
            fileReader.onerror = (error) => {
                reject(error);
            }
        })
    }
    const HandleImages = async (e) => {
        let base64Array = [];


        for (let i = 0; i < e.target.files.length; i++) {
            base64Array.push(await convertBase64(e.target.files[i]));
        }
        setImages(base64Array)
    }

    return (
        <>
            <button type="button" className="btn btn-outline-dark mb-3 w-100" data-bs-toggle="modal" data-bs-target="#exampleModal">Edit Auction</button>

            <div className="modal fade" id="exampleModal" tabIndex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div className="Auth-form-container">
                    <div className="modal-dialog" role="document">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title" id="exampleModalLabel">Edit Auction</h5>
                                <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close" />
                            </div>
                            <div className="modal-body">
                                <form className="Auth-form" onSubmit={HandleSubmit}>
                                    <div className="Auth-form-content">
                                        <h3 className="Auth-form-title">Auction Information</h3>
                                        <div className="form-group mt-3">
                                            <label>Name</label>
                                            <input
                                                defaultValue={listing.name}
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
                                            ></input>
                                        </div>
                                        <div className="form-group mt-3">
                                            <label>Description</label>
                                            <textarea
                                                defaultValue={listing.description}
                                                type="text"
                                                className="form-control mt-1"
                                                placeholder="Enter Auction Description"
                                                onChange={(e) => { setDescription(e.target.value) }}
                                            />
                                        </div>
                                        <div className="form-group mt-3">
                                            <label>FLoor Price</label>
                                            <input
                                                defaultValue={listing.floor_price}
                                                type="number"
                                                className="form-control mt-1"
                                                placeholder="Enter Auction Floor Price"
                                                step="0.01"
                                                onChange={(e) => { setFirstBid(parseFloat(e.target.value)) }}
                                            ></input>
                                        </div>
                                        <div className="form-group mt-3">
                                            <label>Ending Date</label>
                                            <input
                                                defaultValue={listing.ends}
                                                type="datetime-local"
                                                className="form-control mt-1"
                                                placeholder="Enter Auction Ending Date"
                                                onChange={HandleEnds}
                                            ></input>
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