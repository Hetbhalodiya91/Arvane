import { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ShoppingCart, Trash2 } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

const Cart = () => {
  const { isAuthenticated, currentUser } = useAuth();
  const navigate = useNavigate();
  const [cart, setCart] = useState([]);
  const [loading, setLoading] = useState(true);

  // Initialize from localStorage if exists
  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }

    const storedCart = localStorage.getItem('cart');
    if (storedCart) {
      setCart(JSON.parse(storedCart));
    }
    setLoading(false);
  }, [isAuthenticated, navigate]);

  useEffect(() => {
    if (cart.length > 0) {
      localStorage.setItem('cart', JSON.stringify(cart));
    }
  }, [cart]);

  const removeItem = (productId) => {
    setCart(prevCart => prevCart.filter(item => item.id !== productId));
  };

  const updateQuantity = (productId, quantity) => {
    if (quantity === 0) {
      setCart(prevCart => prevCart.filter(item => item.id !== productId));
      return;
    }
    setCart(prevCart =>
      prevCart.map(item =>
        item.id === productId ? { ...item, quantity } : item
      )
    );
  };

  const subtotal = cart.reduce((sum, item) => sum + (item.price || 0) * item.quantity, 0);
  const tax = subtotal * 0.08;
  const total = subtotal + tax + 10; // Adding $10 shipping

  const checkout = () => {
    navigate('/checkout');
  };

  if (loading) {
    return (
      <div className="min-h-screen flex justify-center items-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  if (cart.length === 0) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <ShoppingCart className="mx-auto h-24 w-24 text-gray-300 mb-4" />
          <h2 className="text-2xl font-bold text-gray-900 mb-2">Your cart is empty</h2>
          <Link
            to="/shop"
            className="inline-block bg-indigo-600 text-white px-6 py-2 rounded-full hover:bg-indigo-700"
          >
            Continue Shopping
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">Shopping Cart</h1>
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Cart Items */}
          <div className="lg:col-span-2">
            {cart.map((item) => (
              <div key={item.id} className="flex items-center bg-white rounded-lg shadow-md p-4 mb-4">
                <img
                  src={item.image || 'https://via.placeholder.com/100x100'}
                  alt={item.name}
                  className="w-20 h-20 object-cover rounded-lg"
                />
                <div className="ml-4 flex-1">
                  <h3 className="font-bold text-gray-900">{item.name}</h3>
                  <p className="text-gray-500">${item.price?.toFixed(2)}</p>
                  <div className="flex items-center mt-2">
                    <button
                      onClick={() => updateQuantity(item.id, item.quantity - 1)}
                      className="px-2 py-1 bg-gray-200 rounded-l hover:bg-gray-300"
                    >
                      -
                    </button>
                    <span className="px-3 py-1">{item.quantity}</span>
                    <button
                      onClick={() => updateQuantity(item.id, item.quantity + 1)}
                      className="px-2 py-1 bg-gray-200 rounded-r hover:bg-gray-300"
                    >
                      +
                    </button>
                  </div>
                </div>
                <button
                  onClick={() => removeItem(item.id)}
                  className="ml-4 text-red-500 hover:text-red-700"
                >
                  <Trash2 size={20} />
                </button>
              </div>
            ))}
          </div>

          {/* Order Summary */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-lg shadow-md p-6">
              <h2 className="text-xl font-bold text-gray-900 mb-4">Order Summary</h2>
              <div className="space-y-4">
                <div className="flex justify-between">
                  <span className="text-gray-600">Subtotal</span>
                  <span className="font-medium">${subtotal.toFixed(2)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Tax</span>
                  <span className="font-medium">${tax.toFixed(2)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Shipping</span>
                  <span className="font-medium">$10.00</span>
                </div>
                <hr />
                <div className="flex justify-between text-lg font-bold">
                  <span>Total</span>
                  <span>${total.toFixed(2)}</span>
                </div>
              </div>
              <button
                onClick={checkout}
                className="mt-6 w-full bg-indigo-600 text-white py-3 rounded-lg font-semibold hover:bg-indigo-700"
              >
                Proceed to Checkout
              </button>
              <Link
                to="/shop"
                className="mt-4 w-full text-center bg-gray-100 text-gray-700 py-3 rounded-lg hover:bg-gray-200 block"
              >
                Continue Shopping
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Cart;