import axios from '../api/axios';
import Navbar from '../components/Navbar';
import { useNavigate, useLocation, createSearchParams, useSearchParams } from 'react-router-dom';
import { useEffect, useState } from 'react'
import InitialNavbar from '../components/InitialNavbar';
import countries_csv from '../assets/countries.csv';
import Papa from 'papaparse';

function CardListing(props) {
  const listing_url = props.url;
  const item = props.item;

  const [itemData, setItemData] = useState({});
  const [loading, setLoading] = useState(true);

  const [img, setImg] = useState('')
  useEffect(() => {
    axios.get(item).then(
      function (response) {
        setItemData(response.data);
        setLoading(false);
        axios.get(response.data.images[0]).then(
          function (response) {
            setImg(response.data.image)
          }
        )
      }
    ).catch()
  }, [])
  const item_id = item.split('/')[3]
  const linkTo = '/auction/' + item_id
  const navigate = useNavigate()
  const HandleClick = (e) => {
    navigate(linkTo);
  }
  return (
    <>
      {!loading && <div className='card mb-3' style={{ "height": "10px;", cursor: "pointer" }}>
        <div className="g-0 row" onClick={HandleClick}>
          <div className='col-md-4'>
            <img width={"173px"} height={"173px"} src={img} className="card-img-top embed-responsive-item" style={{ width: "100%;", cursor: "pointer", height: "15vw;", "object-fit": "cover" }} alt="..." />
          </div>
          <div className='col-md-8'>
            <div className="card-body">
              <h5 className="card-title">{itemData.name}</h5>
              <p className="card-text text-truncate">{itemData.description}</p>
              <div><span><small>Highest bid:</small>{itemData.currently}$</span></div>
              <div className='text-truncate'><small>Ends: </small> {itemData.ends}</div>
            </div>
          </div>
        </div>
      </div>}
    </>
  )
}

export default function HomePage(props) {
  const [listings_array, setListings] = useState([])
  const [countriesRecords, setCountiresRecords] = useState([]);
  const [last, setLast] = useState('');
  const currentPath = useLocation().pathname;
  const navigate = useNavigate();
  const useQuery = () => new URLSearchParams(useLocation().after);

  const [isActive, setIsActive] = useState(true);
  const [country, setCountry] = useState('');
  const [name, setName] = useState('');
  const [minPrice, setMinPrice] = useState(null);
  const [maxPrice, setMaxPrice] = useState(null);
  const [categories, setCategories] = useState('');
  const [after, setAfter] = useState('');
  // let query = useQuery();
  const [query] = useSearchParams();
  const dateAfter_q = query.get('after');
  const isActive_q = query.get('only_active');
  const country_q = query.get('country');
  const name_q = query.get('name');
  const minPrice_q = query.get('min_price'); //parseInt
  const maxPrice_q = query.get('max_price'); //parseInt
  const categories_q = query.get('categories');

  useEffect(() => {
    Papa.parse(countries_csv, {
      download: true,
      complete: function (input) {
        const records = input.data;
        records.forEach(element => {
          setCountiresRecords(countriesRecords => [...countriesRecords, element[3]])
        });

      }
    });
    let url = `/api/listings`
    url += isActive_q ? `?only_active=${isActive_q}` : ''
    url += name_q ? `&name=${name_q}` : ''
    url += country_q ? `&country=${country_q}` : ''
    url += minPrice_q ? `&price_min=${minPrice_q}` : ''
    url += maxPrice_q ? `&price_max=${maxPrice_q}` : ''
    url += dateAfter_q ? `&after=${dateAfter_q}` : ''
    url += categories_q ? `&after=${categories_q}` : ''

    axios.get(url).then(
      function (response) {
        setLast(response.data.last)
        setListings(response.data.listings)
      }
    ).catch((error) => {console.log(error)})
  }, [navigate, query])
  
  const HandleSubmit = (e) => {

    const urlParams = new Object()
    if (isActive) urlParams.only_active = isActive;
    if (name) urlParams.name = name;
    if (country) urlParams.country = country;
    if (isActive) urlParams.only_active = isActive;
    if (minPrice) urlParams.price_max = minPrice;
    if (maxPrice) urlParams.price_max = maxPrice;
    if (categories) urlParams.categories = categories;
    if (after) urlParams.after = after;

    navigate({
      pathname: "/auctions",
      search: `?${createSearchParams(urlParams)}`
    });
  }

  const HandleLoadMore = (e) => {
    const urlParams = new Object()
    if (isActive) urlParams.only_active = isActive;
    if (name) urlParams.name = name;
    if (country) urlParams.country = country;
    if (isActive) urlParams.only_active = isActive;
    if (minPrice) urlParams.price_max = minPrice;
    if (maxPrice) urlParams.price_max = maxPrice;
    if (categories) urlParams.categories = categories;
    if (after) urlParams.after = after;
    navigate({
      pathname: "/auctions",
      search: `?${createSearchParams(urlParams)}`
    });
  }
  return (
    <div key={query}>
      {localStorage.getItem('AuthToken') ? <Navbar /> : <InitialNavbar />}
      <div className='container mt-3'>
        <form className="row mr-2" onSubmit={HandleSubmit}>
          <div className="dropdown col" style={{ paddingLeft: "0px" }}>
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
            type="number"
            placeholder="Min Price"
            aria-label="min-price"
            step="0.01"
            onChange={(e) => { setMinPrice(e.target.value) }}
          />
          <input
            className="form-control col "
            type="number"
            placeholder="Max Price"
            aria-label="max-price"
            step="0.01"
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
                <CardListing item={item} key={item} />
              </>
            );
          })}
           {listings_array.length===10 && <div className='btn' onClick={HandleLoadMore}>Load More</div>}
        </div>
      </div>
    </div>
  );
}