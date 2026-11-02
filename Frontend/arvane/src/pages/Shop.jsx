import { useState, useEffect } from 'react';
import { Search, ShoppingCart } from 'lucide-react';
import { apiClient } from '../api/apiClient';
import { useAuth } from '../context/AuthContext';

const Shop = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [cart, setCart] = useState([]);
  const { isAuthenticated } = useAuth();

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

  const addToCart = (product) => {
    if (!isAuthenticated) {
      return;
    }
    setCart(prevCart => {
      const existing = prevCart.find(item => item.id === product.id);
      if (existing) {
        return prevCart.map(item =>
          item.id === product.id ? { ...item, quantity: item.quantity + 1 } : item
        );
      }
      return [...prevCart, { ...product, quantity: 1 }];
    });
  };

  const filteredProducts = products.filter(product =>
    product.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    product.sku?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) {
    return (
      <div className="min-h-screen flex justify-center items-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 py-8">
        {/* Search */}
        <div className="mb-6">
          <div className="relative max-w-md">
            <Search className="absolute left-3 top-3 h-5 w-5 text-gray-400" />
            <input
              type="text"
              placeholder="Search products..."
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
        </div>

        {/* Products Grid */}
        <h1 className="text-2xl font-bold text-gray-900 mb-6">Shop</h1>
        {filteredProducts.length === 0 ? (
          <p className="text-gray-500">No products found</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6">
            {filteredProducts.map((product) => (
              <div key={product.id} className="bg-white rounded-lg shadow-md overflow-hidden">
                <img
                  src={product.images?.[0] || product.imageUrl || 'https://via.placeholder.com/300x200'}
                  alt={product.name}
                  className="w-full h-48 object-cover"
                />
                <div className="p-4">
                  <h3 className="font-semibold text-gray-900">{product.name}</h3>
                  <p className="text-sm text-gray-500">{product.sku}</p>
                  <p className="text-indigo-600 font-bold">${product.price?.toFixed(2) || '0.00'}</p>
                  {product.stockQuantity > 0 ? (
                    <button
                      onClick={() => addToCart(product)}
                      disabled={!isAuthenticated}
                      className="mt-2 w-full flex items-center justify-center gap-2 py-2 px-4 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                      <ShoppingCart size={20} />
                      Add to Cart
                    </button>
                  ) : (
                    <button
                      disabled
                      className="mt-2 w-full flex items-center justify-center gap-2 py-2 px-4 bg-gray-300 text-gray-500 rounded-lg cursor-not-allowed"
                    >
                      <ShoppingCart size={20} />
                      Out of Stock
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Shop;