import { useState } from 'react';
import { Link, Routes, Route, Navigate } from 'react-router-dom';
import { Search, Heart, User, ShoppingCart } from 'lucide-react';
import { useAuth } from './context/AuthContext';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import Shop from './pages/Shop';
import Cart from './pages/Cart';
import Profile from './pages/Profile';

const App = () => {
  const { isAuthenticated } = useAuth();

  return (
    <div className="min-h-screen bg-gray-50">
      <Navigation />

      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={!isAuthenticated ? <Login /> : <Navigate to="/" />} />
          <Route path="/register" element={!isAuthenticated ? <Register /> : <Navigate to="/" />} />
          <Route path="/shop" element={<Shop />} />
          <Route path="/cart" element={isAuthenticated ? <Cart /> : <Navigate to="/login" />} />
          <Route path="/profile" element={isAuthenticated ? <Profile /> : <Navigate to="/login" />} />
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </main>
    </div>
  );
};

const Navigation = () => {
  const { isAuthenticated, logout } = useAuth();
  const location = useLocation();
  const currentPath = location.pathname;

  const isActive = (path) => currentPath === path;
  const isNavActive = (path) => currentPath.startsWith(path);

  return (
    <nav className="w-full border-b border-gray-200 bg-white shadow-sm">
      <div className="max-w-7xl mx-auto flex items-center justify-between px-4 py-3">
        {/* Left Section */}
        <div className="flex items-center gap-8">
          <Link to="/" className="text-2xl font-bold text-indigo-600">
            ShopEase
          </Link>

          <ul className="hidden md:flex items-center gap-6 font-medium">
            <li>
              <Link
                to="/"
                className={isActive('/') ? 'text-indigo-600' : 'text-gray-600 hover:text-indigo-600'}
              >
                Home
              </Link>
            </li>
            <li>
              <Link
                to="/shop"
                className={isActive('/shop') ? 'text-indigo-600' : 'text-gray-600 hover:text-indigo-600'}
              >
                Shop
              </Link>
            </li>
            <li>
              <Link
                to="/cart"
                className="relative text-gray-600 hover:text-indigo-600"
              >
                <ShoppingCart size={20} />
              </Link>
            </li>
          </ul>
        </div>

        {/* Right Section */}
        <div className="flex items-center gap-4">
          {/* Search */}
          <div className="hidden md:flex items-center bg-gray-100 rounded-md px-3 py-1.5 w-64">
            <Search size={16} className="text-gray-500" />
            <input
              type="text"
              placeholder="Search..."
              className="bg-transparent outline-none ml-2 w-full text-sm"
            />
          </div>

          {/* Icons */}
          {isAuthenticated ? (
            <div className="flex items-center gap-4">
              <Link to="/profile" className="text-gray-600 hover:text-indigo-600">
                <User size={20} />
              </Link>
              <button
                onClick={logout}
                className="text-gray-600 hover:text-red-600"
                title="Sign Out"
              >
                <LogOut size={20} />
              </button>
            </div>
          ) : (
            <>
              <Link to="/login" className="text-gray-600 hover:text-indigo-600">
                <User size={20} />
              </Link>
              <Link to="/cart" className="text-gray-600 hover:text-indigo-600">
                <ShoppingCart size={20} />
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

import { LogOut } from 'lucide-react';

export default App;