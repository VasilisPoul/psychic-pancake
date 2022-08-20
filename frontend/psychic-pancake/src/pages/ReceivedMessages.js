import { useContext, useEffect, useState } from 'react';
import axios from '../api/axios';
import Navbar from '../components/Navbar';
import { UserContext } from '../components/UserContext';


const MessageInstance = (props) => {
  const messageUrl = props.messageUrl;
  const [message, setMessage] = useState({});

  useEffect(() => {
    axios.get(messageUrl, {
      headers: {
        'authorization': localStorage.getItem('AuthToken')
      }
    })
      .then(
        function (response) {
          response.data.to = response.data.to.split('/')[3]
          setMessage(response.data)
        }
      )
  }, [])
  console.log(message.to)
  return (
    <>
      <div className='row border-bottom' style={{ cursor: 'pointer' }} data-bs-toggle="modal" data-bs-target="#msgModal">
        <div className='col-sm-2 text-truncate'>
          <small>To:{' '}</small>
          {message.to}
        </div>
        <div className='col-sm-4 text-truncate'>
          {message.subject}
        </div>
        <div className='col-sm-4 text-truncate'>
          {message.body}
        </div>
        <div className='col-sm-2 text-truncate'>
          {message.timestamp}
        </div>
      </div>

      <div className="modal fade" id="msgModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div className="Auth-form-container">
          <div className="modal-dialog" role="document">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title" id="exampleModalLabel">Message</h5>
                <button type="button" className="close" data-bs-dismiss="modal" aria-label="Close">
                  <span aria-bs-hidden="true">&times;</span>
                </button>
              </div>
              <div className="modal-body">
                <form className="Auth-form">
                  <div className="Auth-form-content">
                    <div className="form-group mt-3">
                    </div>
                    <div className="form-group mt-3 border-bottom">
                      <small>To</small>
                      <span>{' ' + message.to}</span>
                    </div>

                    <div className="form-group mt-3 border-bottom">
                      <small>Subject</small>
                      <span>{' ' + message.subject}</span>
                    </div>

                    <div className="form-group mt-3 border-bottom">
                      <small>Message</small>
                      <span>{' ' + message.body}</span>
                    </div>
                    <div className="d-grid gap-2 mt-3">
                      
                      <button type="submit" className="btn btn-light" data-bs-dismiss="modal" >
                        Done
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

export default function ReceivedMessages() {
  const [inbox, setInbox] = useState([])
  useEffect(() => {
    axios.get('/api/messages/inbox', {
      headers: {
        'authorization': localStorage.getItem('AuthToken')
      }
    })
      .then(
        function (response) {
          console.log(response.data)
          setInbox(response.data)
        }
      )
      .catch()
  }, [])

  const messages_array = inbox
  console.log(`wiodewi  ${messages_array.length === 0}`)
  return (
    <>
      <Navbar />
      <div className='container mt-3'>
        <div className='mb-2'>
          {messages_array.length === 0 && <div className='text-center'><span>No Received messages</span></div>}
          {messages_array.map((messageUrl) => {
            return (
              <MessageInstance messageUrl={messageUrl} key={messageUrl} />
            );
          })}
        </div>
      </div>
    </>
  );
}
