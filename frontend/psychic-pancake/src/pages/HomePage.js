import axios from '../api/axios';
import Navbar from '../components/Navbar';
import auction from '../resources/auction.webp';
import { Link } from 'react-router-dom';
import { useEffect, useState } from 'react'


function CardListing(props) {
  const listing_url = props.url;
  const item = props.item;

  const [itemData, setItemData] = useState({});
  const [ loading, setLoading ] = useState(true);
  const [img, setImg] = useState('')
  useEffect(() => {
    axios.get(item).then(
      function (response) {
        setItemData(response.data);
        setLoading(false);
        axios.get(response.data.images[0]).then(
          function(response) {
            console.log(response.data)
            setImg(response.data.image)
          }
        )
      }
    ).catch()
  }, [])
  const item_id = item.split('/')[3]
  const linkTo = '/auction/'+item_id
  return (
    <>
    {!loading && <div className='col-sm-3 mb-3'>
      <div className="card" style={{ "width": "18rem;" }}>
       { itemData.images && <img src={img} className="card-img-top" alt="..." />}
        <div className="card-body">
          <h5 className="card-title">{itemData.name}</h5>
          <p className="card-text">{itemData.description}</p>
          <div><span>{itemData.currently}$</span></div>
          <Link to={linkTo} className="btn btn-light">See more</Link>
        </div>
      </div>
    </div>}
    </>
  )
}

export default function HomePage(props) {
  const [listings_array, setListings] = useState([])
  useEffect(() => {
    axios.get('/api/listings?only_active=true').then(
      function (response) {
        setListings(response.data)
      }
    )
  }, [])

  const HandleSubmit = (e) => {
    try {
      e.preventDefault()
      let url = `/api/listings`
      url += `?only_active=${isActive}`
      url += name ? `&name=${name}` : ''
      url += country ? `&country=${country}` : ''
      url += minPrice ? `&price_min=${minPrice}` : ''
      url += maxPrice ? `&price_max=${maxPrice}` : ''
      axios.get(url).then(
        function (response) {
          setListings(response.data)
        }
      ).catch(
        function (error) {
          alert(error)
        }
      )
    }
    catch (error) {
      alert(error)
    }
  }

  const [isActive, setIsActive] = useState(true);
  const [country, setCountry] = useState('');
  const [name, setName] = useState('');
  const [minPrice, setMinPrice] = useState(null);
  const [maxPrice, setMaxPrice] = useState(null);
  const [categories, setCategories] = useState('');


  return (
    <>
      <Navbar />
      <div className='container mt-3'>
        <form className="row mr-2" onSubmit={HandleSubmit}>
          <div className="dropdown col" style={{paddingLeft: "0px"}}>
            <button className="btn btn-light w-100 dropdown-toggle" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              {isActive ? 'Active Only' : 'All'}
            </button>
            <div className="dropdown-menu" aria-labelledby="dropdownMenuButton">
              <div className="dropdown-item" onClick={(e) => setIsActive(false)}>All</div>
              <div className="dropdown-item" onClick={(e) => setIsActive(true)}>Active Only</div>
            </div>
          </div>
          <input
            className="form-control col"
            type="search"
            placeholder="Country"
            aria-label="Country"
            onChange={(e) => { setCountry(e.target.value) }}
          />
          <input
            className="form-control col "
            type="search"
            placeholder="Name"
            aria-label="Name"
            onChange={(e) => { setName(e.target.value) }}
          />
          <input
            className="form-control col "
            type="search"
            placeholder="Min Price"
            aria-label="min-price"
            onChange={(e) => { setMinPrice(e.target.value) }}
          />
          <input
            className="form-control col "
            type="search"
            placeholder="Max Price"
            aria-label="max-price"
            onChange={(e) => { setMaxPrice(e.target.value) }}
          />
          <input
            className="form-control col "
            type="search"
            placeholder="Categories"
            aria-label="categories"
            onChange={(e) => { setCategories(e.target.value) }}
          />
          <button className="btn btn-outline-success col " type="submit">Search</button>

        </form>
      </div>
      <div className='container mt-3'>
        <div className='row mb-2'>
          {listings_array.map((item, idx) => {
            return (
              <>
                <CardListing item={item} key={item}/>
              </>
            );
          })}
        </div>
      </div>
    </>
  );
}