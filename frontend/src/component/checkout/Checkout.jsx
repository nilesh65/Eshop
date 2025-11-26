import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { useParams, useNavigate } from 'react-router-dom'
import { getUserCart } from '../../store/features/cartSlice'
import { placeOrder, createPaymentIntent } from '../../store/features/orderSlice'
import {useStripe, useElements, CardElement} from '@stripe/react-stripe-js'
import AddressForm from '../common/AddressForm'
import { toast, ToastContainer } from 'react-toastify'
import { cardElementOptions } from '../utils/cardElementOptions'
import { Col, Container, FormGroup, Row , Card, Form} from 'react-bootstrap'
import {ClipLoader} from "react-spinners"
const Checkout = () => {
    const dispatch = useDispatch()
    const {userId} = useParams()
    const cart = useSelector((state)=> state.cart)
    const stripe = useStripe()
    const elements = useElements()
    const [cardError,setCardError] = useState("")
    const [loading, setLoading] = useState(false)
    const navigate = useNavigate()
    const [userInfo, setUserInfo] = useState({
        firstName:"",
        lastName:"",
        email: ""
        });

    const [billingAddress, setBillingAddress] = useState({
        street: "",
        city: "",
        state: "",
        country:""
    })

    const handleInputChange = (event) => {
const { name, value } = event.target;
setUserInfo({ ... userInfo, [name]: value });
};
const handleAddressChange = (event) => {
const { name, value } = event.target;
setBillingAddress({ ... billingAddress, [name]: value });
};

const handlePaymentAndOrder = async (event) => {
        event.preventDefault()
        setLoading(true)
        // 1. check stripe presence
        if(!stripe || !elements){
            toast.error("Loading... please try again")
            return;
        }
            const cardElement = elements.getElement(CardElement)
            try {
    // 2. Create the paymentIntent through the backend;
    const { clientSecret } = await dispatch(
    createPaymentIntent({
    amount: cart.totalAmount, // Convert to cents
    currency: "usd", // Set your desired currency
    })
    ).unwrap();

    //3. confirm the payment intent with card details
    const { error, paymentIntent } = await stripe.confirmCardPayment(
    clientSecret,
    {
    payment_method: {
    card: cardElement,
    billing_details: {
    name: `${userInfo.firstName} ${userInfo.lastName}`,
    email: userInfo.email,
    address: {
    line1: billingAddress.street,
    city: billingAddress.city,
    state: billingAddress.state,
    country: billingAddress.country,
    postal_code: billingAddress.postalCode,
    },
    },
    },
    }
    );
    // 4 place order after succesfull payment
    if(error){
        toast.error(error.message)
        return;
    }
    if(paymentIntent.status === "succeeded"){
        await dispatch(placeOrder({userId})).unwrap()
        toast.success("Payment successfull! Your order has been placed")
        setTimeout(()=> {
            window.location.href=`/user-profile/${userId}/profile`
        },5000)
    }
    } catch (error) {
        toast.error("Error Processing payment: ",error.message)
    }
    finally{
        setLoading(false)
    }
    }

    useEffect(() => {
        dispatch(getUserCart(userId))
    },[dispatch,userId])

    

  return (
    <Container className='mt-5 mb-5'>
        <div className='d-flex justify-content-center'>
      <Row>
        <Col>
        <Form className="p-4 border rounded shadow-sm">
            <Row>
                <Col md={6}>
                    <FormGroup>
                    <label htmlFor='firstName' className='form-label'>
                    Firstname
                    </label>
                    <input
                    type='text'
                    className='form-control mb-2'
                    id='name'
                    name='firstName'
                    value={userInfo.firstName}
                    onChange={handleInputChange}
                    />
                    </FormGroup>
                </Col>
                <Col md={6}>
                    <FormGroup>
                    <label htmlFor='lastName' className='form-label'>
                    Lastname
                    </label>
                    <input
                    type='text'
                    className='form-control mb-2'
                    id='name'
                    name='lastName'
                    value={userInfo.lastName}
                    onChange={handleInputChange}
                    />
                    </FormGroup>
                </Col>
            </Row>
                <FormGroup>
                    <label htmlFor='email' className='form-label'>
                    Email
                    </label>
                    <input
                    type='email'
                    className='form-control mb-2'
                    id='email'
                    name='email'
                    value={userInfo.email}
                    onChange={handleInputChange}
                    />
                </FormGroup>
                <div>
                    <h6>Enter Billing Address</h6>
                    <AddressForm 
                        onChange={handleAddressChange}
                        address={billingAddress}
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="card-element" className="form-label">
                        <h6>Credit or Debt Card</h6>
                    </label>
                    <div className="form-control" id="card-element">
                        <CardElement options={cardElementOptions}
                        onChange={(event) => {
                            setCardError(event.error? event.error.message : "")
                        }}
                        />
                        {cardError && <div className='text-danger'>{cardError}</div>}
                        
                    </div>
                </div>
        </Form>
        </Col>
        <Col md={4}>
        <h6 className='mt-4 text-center cart-title' >Your Order Summary</h6>
        <hr />
        <Card style={{ backgroundColor: "whiteSmoke" }}>
        <Card.Body>
        <Card.Title className='mb-2 text-muted text-success'>
            Total Amount : ${cart. totalAmount .toFixed(2)}
        </Card.Title>
        </Card.Body>
        <button
            type='submit'
            className='btn btn-warning mt-3'
            disabled={!stripe}
            onClick={(e) => handlePaymentAndOrder(e)}> 
            {
                loading ? <ClipLoader size={20} color={"#123abc"}/> : "Pay Now"
            }
            </button>
        </Card>
        </Col>
        </Row>
        </div>
    </Container>
  )
}

export default Checkout
