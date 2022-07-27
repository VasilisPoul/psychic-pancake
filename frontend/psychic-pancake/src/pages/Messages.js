// If Buyer list bought products
// If seller list auctions, and let him add new auctions (modal?)
import { useContext } from 'react';
import Navbar from '../components/Navbar';
import { UserContext } from '../components/UserContext';

const messages_array = [
  {
    'type': 'received',
    'message': 'blah blah',
    'user': 'foo',
    'date': '02/02/2022'
  },
  {
    'type': 'sent',
    'message': 'blah blah',
    'user': 'bar',
    'date': '01/02/2022'
  },
  {
    'type': 'sent',
    'message': 'blah blah',
    'user': 'foo',
    'date': '29/01/2022'
  },
  {
    'type': 'received',
    'message': 'blah blah',
    'user': 'bar1',
    'date': '12/01/2022'
  }
]

export default function Messages() {

  const { user, setUser } = useContext(UserContext);
  return (
    <>
      <Navbar />
      <div className='container mt-3'>
        <div className='mb-2'>
          {messages_array.map((message) => {
            return (<>
              <div className='row border-bottom' style={{ cursor: 'pointer' }}>
                <div className='col-sm-2'>
                  {message.type === 'received' ? <small>From:{' '}</small> : <small>To:{' '}</small>}
                  {message.user}
                </div>
                <div className='col-sm-8'>
                  {message.message}
                </div>
                <div className='col-sm-2'>
                  {message.date}
                </div>
              </div>
            </>
            );
          })}
        </div>
      </div>
    </>
  );
}
