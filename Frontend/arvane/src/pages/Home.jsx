import { useState, useEffect } from 'react';
import { ShoppingCart, Heart, User, Search } from 'lucide-react';
import { apiClient } from '../api/apiClient';
import { Link } from 'react-router-dom';

const Home = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const data = await apiClient.get('/products');
      setProducts(data.content || data);
    } catch (error) {
      console.error('Error fetching products:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex justify-center items-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero Section */}
      <section className="bg-gradient-to-r from-indigo-600 to-purple-600 text-white py-20">
        <div className="max-w-4xl mx-auto px-4 text-center">
          <h1 className="text-4xl md:text-6xl font-bold mb-6">
            Welcome to ShopEase
          </h1>
          <p className="text-xl md:text-2xl mb-8 opacity-90">
            Your one-stop shop for all your needs
          </p>
          <Link
            to="/shop"
            className="inline-block bg-white text-indigo-600 px-8 py-3 rounded-full font-semibold hover:bg-gray-100 transition"
          >
            Shop Now
          </Link>
        </div>
      </section>

      {/* Featured Products */}
      <section className="py-16">
        <div className="max-w-7xl mx-auto px-4">
          <h2 className="text-3xl font-bold text-gray-900 mb-8">Featured Products</h2>
          {products.length === 0 ? (
            <p className="text-gray-500">No products available</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6">
              {products.slice(0, 8).map((product) => (
                <div key={product.id} className="bg-white rounded-lg shadow-md overflow-hidden">
                  <img
                    src={product.imageUrl || 'https://via.placeholder.com/300x200'}
                    alt={product.name}
                    className="w-full h-48 object-cover"
                  />
                  <div className="p-4">
                    <h3 className="font-semibold text-gray-900">{product.name}</h3>
                    <p className="text-indigo-600 font-bold">${product.price?.toFixed(2) || '0.00'}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </section>

      {/* Features Section */}
      <section className="bg-white py-16">
        <div className="max-w-7xl mx-auto px-4">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="text-center p-6">
              <ShoppingCart className="mx-auto h-12 w-12 text-indigo-600 mb-4" />
              <h3 className="font-bold text-gray-900 mb-2">Free Shipping</h3>
              <p className="text-gray-600">Get free shipping on orders over $50</p>
            </div>
            <div className="text-center p-6">
              <User className="mx-auto h-12 w-12 text-indigo-600 mb-4" />
              <h3 className="font-bold text-gray-900 mb-2">Easy Returns</h3>
              <p className="text-gray-600">30-day money-back guarantee</p>
            </div>
            <div className="text-center p-6">
              <Heart className="mx-auto h-12 w-12 text-indigo-600 mb-4" />
              <h3 className="font-bold text-gray-900 mb-2">Quality Assurance</h3>
              <p className="text-gray-600">Premium quality products</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;