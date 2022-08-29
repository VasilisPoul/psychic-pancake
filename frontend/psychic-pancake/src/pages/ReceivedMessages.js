import { useEffect, useState } from 'react';
import axios from '../api/axios';
import Navbar from '../components/Navbar';
import SendMessageModal from '../components/SendMessageModal';


const MessageModal = (props) => {
  const message = props.message;
  const msgModal = props.msgModal;
  const from = props.from;

  return (

    <div className="modal fade" id={msgModal.substring(1)} tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" onClick={(e) => { console.log(message) }}>
      <div className="Auth-form-container">
        <div className="modal-dialog modal-lg" style={{ width: "1250px", height: "250px" }} role="document">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title" id="exampleModalLabel">Message</h5>
              <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"/>
            </div>
            <div className="modal-body">
              <div className="Auth-form-content">
                <div className="form-group mt-3">
                </div>
                <div className="form-group mt-3 border-bottom">
                  <small>From</small>
                  <span>{' ' + from}</span>
                </div>

                <div className="form-group mt-3 border-bottom">
                  <small>Subject</small>
                  <span>{' ' + message.subject}</span>
                </div>

                <div className="form-group mt-3 border-bottom " style={{ whiteSpace: 'pre-wrap', overflowWrap: 'break-word' }}>
                  <small>Message</small>
                  <span>{' ' + message.body}</span>
                </div>
                <div className="d-grid gap-2 mt-3">

                  <button type="submit" className="btn btn-light" data-bs-dismiss="modal" onClick={(e) => e.preventDefault()} >
                    Done
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

const MessageInstance = (props) => {
  const messageUrl = props.messageUrl;
  const idx = props.idx;
  const [message, setMessage] = useState({});
  const [from, setFrom] = useState('')
  console.log(`messageUrl: ${messageUrl}`)
  useEffect(() => {
    axios.get(messageUrl, {
      headers: {
        'authorization': localStorage.getItem('AuthToken')
      }
    })
      .then(
        function (response) {
          setFrom(response.data.from.split('/')[3])
          setMessage(response.data)
        }
      )
  }, [])

  const [currentMessage, setCurrentMessage] = useState('');
  const msgModal = `#msgModal${idx}`
  console.log(messageUrl)
  const HandleDelete = (e) => {
    try {
      e.preventDefault();
      axios.delete(messageUrl, {
        headers: {
          'authorization': localStorage.getItem('AuthToken')
        }
      })
        .then(
          function ( response ) {
            window.location.reload(false)
          }
        )
    }
    catch (error) {

    }
  }

  return (
    <div className='row border-bottom justify-content-center align-items-center'>
      <div className='col-sm-9'>
        <div className='row justify-content-center align-items-center' style={{ cursor: 'pointer' }} data-bs-toggle="modal" data-bs-target={msgModal} onClick={(e) => { setCurrentMessage(message) }}>
          <div className='col-sm-2 text-truncate'>
            <small>From:{' '}</small>
            {from}
          </div>
          <div className='col-sm-4 text-truncate'>
          <small>Subject:{' '}</small>
            {message.subject}
          </div>
          <div className='col-sm-4 text-truncate'>
          <small>Message:{' '}</small>
            {message.body}
          </div>
          <div className='col-sm-2 text-truncate'>
            {message.timestamp}
          </div>
        </div>
        <MessageModal message={currentMessage} msgModal={msgModal} from={from} />
      </div>
      <div className='col-sm-2 mt-3' ><SendMessageModal to={from}/></div>
      <div className='col-sm-1'><button className='btn btn-danger' onClick={HandleDelete} >Delete</button></div>
    </div>
  );
}

export default function ReceivedMessages() {
  const [outbox, setOutbox] = useState([])
  const [loading, setLoading] = useState(true)
  useEffect(() => {
    axios.get('/api/messages/inbox', {
      headers: {
        'authorization': localStorage.getItem('AuthToken')
      }
    })
      .then(
        function (response) {
          console.log(response.data)
          setOutbox(response.data)
          setLoading(false);
        }
      )
      .catch()
  }, [])

  const messages_array = outbox
  return (
    <>
      <Navbar />
      <div className='container mt-3'>
        <div className='mb-2'>
          {messages_array.length === 0 && !loading && <div className='text-center'><span>No Sent messages</span></div>}
          {!loading && messages_array.map((messageUrl, idx) => {
            return (
              <MessageInstance messageUrl={messageUrl} key={messageUrl} idx={idx} />
            );
          })}
        </div>
      </div>
    </>
  );
}
