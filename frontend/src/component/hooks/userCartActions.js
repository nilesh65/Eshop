import { useDispatch, useSelector } from "react-redux";
import { addToCart } from "../../store/features/cartSlice";
import { toast } from "react-toastify";

export const useCartActions = () => {
  const dispatch = useDispatch();
  const isAuthenticated = useSelector(state => state.auth.isAuthenticated);
  const successMessage = useSelector(state => state.cart.successMessage);
  const errorMessage = useSelector(state => state.cart.errorMessage);

  const handleAddToCart = async (productId, quantity) => {
    if (!isAuthenticated) {
      toast.error("You need to be logged in to add items to the cart.");
      return;
    }

    try {
      await dispatch(addToCart({ productId, quantity })).unwrap();
      toast.success(successMessage);
    } catch (error) {
      toast.error(errorMessage);
    }
  };

  return { handleAddToCart };
};
