import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { api } from "../../component/services/api";

export const placeOrder = createAsyncThunk(
  "order/placeOrder",
  async (payload) => {

    // BUY NOW ORDER
    if (payload.buyNow) {
      const response = await api.post(
        `/orders/user/${payload.userId}/place-order`,
        {
          items: payload.items,     // backend will detect this
          buyNow: true              // extra flag for backend logic
        }
      );
      return response.data;
    }

    // NORMAL CART ORDER
    const response = await api.post(
      `/orders/user/${payload.userId}/place-order`
    );

    return response.data;
  }
);



export const fetchUserOrders = createAsyncThunk(
  "orders/fetchUserOrders",
  async (userId) => {
    const response = await api.get(`/orders/user/${userId}/orders`);
    console.log("User orders:", response.data.data);
    return response.data.data; // array of orders
  }
);

export const createPaymentIntent = createAsyncThunk(
  "payments/createPaymentIntent",
  async ({ amount, currency }) => {
    const response = await api.post("/orders/create-payment-intent", {
      amount,
      currency,
    });
    return response.data;
  }
);

const initialState = {
  orders: [],
  loading: false,
  errorMessage: null,
  successMessage: null,
};

const orderSlice = createSlice({
  name: "order",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(placeOrder.fulfilled, (state, action) => {
        // FIXED: use `data` instead of nonexistent `order`
        state.orders.push(action.payload.data);  
        state.loading = false;
        state.successMessage = action.payload.message;
      })
      .addCase(placeOrder.rejected, (state, action) => {
        state.errorMessage = action.error.message;
        state.loading = false;
      })
      .addCase(fetchUserOrders.fulfilled, (state, action) => {
        // contains clean array
        state.orders = action.payload;
        state.loading = false;
      });
  },
});

export default orderSlice.reducer;
