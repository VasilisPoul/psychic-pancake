import Login from '../components/Login'
import Footer from '../components/Footer'
import auction from '../resources/auction.webp';
import InitialNavbar from '../components/InitialNavbar';

export default function WelcomePage() {
  return (
    <>
      <InitialNavbar/>
      <br /><br />
      <br /><br />
      <div></div>
      <div className="container mb-4">
        <div className="row">
          <div className="col">
            <Login frontPage={true}/>
          </div>
          <div className="col">
            <img src={auction} className="img-thumbnail" />
          </div>
        </div>

      </div>
      <div className="row">
        <div style={{ backgroundColor: '#B2BEB5' }}>
          <br /><br />
          <br /><br />
          <h1>Find the best product for you</h1>
          <h3>Browse through our list of available auctions and find the best for your needs.</h3>
          <br /><br />
          <br /><br />
        </div>
      </div>
      <Footer />
    </>
  );
}