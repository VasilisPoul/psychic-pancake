import Navbar from '../components/Navbar';
import auction from '../resources/auction.webp';
const ItemsList = [
  {
    "title": "Item #1",
    "desc": "blah blah"
  },
  {
    "title": "Item #2",
    "desc": "blah blah"
  },
  {
    "title": "Item #3",
    "desc": "blah blah"
  },
  {
    "title": "Item #4",
    "desc": "blah blah"
  },
  {
    "title": "Item #5",
    "desc": "blah blah"
  },
  {
    "title": "Item #6",
    "desc": "blah blah"
  },
  {
    "title": "Item #7",
    "desc": "blah blah"
  },
  {
    "title": "Item #8",
    "desc": "blah blah"
  },
  {
    "title": "Item #9",
    "desc": "blah blah"
  },
  {
    "title": "Item #10",
    "desc": "blah blah"
  },
  {
    "title": "Item #11",
    "desc": "blah blah"
  },
  {
    "title": "Item #12",
    "desc": "blah blah"
  },
  {
    "title": "Item #13",
    "desc": "blah blah"
  },
  {
    "title": "Item #14",
    "desc": "blah blah"
  },
  {
    "title": "Item #15",
    "desc": "blah blah"
  },
  {
    "title": "Item #16",
    "desc": "blah blah"
  },
  {
    "title": "Item #17",
    "desc": "blah blah"
  },
  {
    "title": "Item #18",
    "desc": "blah blah"
  },
  {
    "title": "Item #19",
    "desc": "blah blah"
  },
  {
    "title": "Item #20",
    "desc": "blah blah"
  },
];

export default function HomePage() {
  console.log("HERE")
  return (
    <>
      <Navbar />
      <div className='container mt-3'>
        <div className='row mb-2'>
          {ItemsList.map((item, idx) => {
            return (
              <div className='col-sm-3 mb-3'>
                <div class="card" style={{ "width": "18rem;" }}>
                  <img src={auction} class="card-img-top" alt="..." />
                  <div class="card-body">
                    <h5 class="card-title">{item.title}</h5>
                    <p class="card-text">{item.desc}</p>
                    {/* <a href="#" class="btn btn-primary">Go somewhere</a> */}
                  </div>
                </div>
              </div>
            )
          })}
        </div>
      </div>
    </>
  );
}