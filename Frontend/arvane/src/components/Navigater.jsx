import { Search, Heart, User, ShoppingCart } from "lucide-react";
import { Link, useLocation } from "react-router-dom";

const Navigater = () => {
  const location = useLocation();
  const currentPath = location.pathname;

  const isActive = (path) => currentPath === path;

  return (
    <nav className="w-full border-b border-gray-200 bg-white">
      <div className="max-w-7xl mx-auto flex items-center justify-between px-8 py-4">
        {/* Left Section */}
        <div className="flex items-center gap-12">
          <Link to="/" className="text-2xl font-bold text-gray-900">
            ShopEase
          </Link>

          <ul className="hidden md:flex items-center gap-8 font-medium">
            <li>
              <Link
                to="/shop"
                className={
                  isActive("/shop")
                    ? "text-black"
                    : "text-gray-600 hover:text-black"
                }
              >
                Shop
              </Link>
            </li>
            <li>
              <Link
                to="/men"
                className={
                  isActive("/men")
                    ? "text-black"
                    : "text-gray-600 hover:text-black"
                }
              >
                Men
              </Link>
            </li>
            <li>
              <Link
                to="/women"
                className={
                  isActive("/women")
                    ? "text-black"
                    : "text-gray-600 hover:text-black"
                }
              >
                Women
              </Link>
            </li>
            <li>
              <Link
                to="/kids"
                className={
                  isActive("/kids")
                    ? "text-black"
                    : "text-gray-600 hover:text-black"
                }
              >
                Kids
              </Link>
            </li>
          </ul>
        </div>

        {/* Right Section */}
        <div className="flex items-center gap-6">
          {/* Search */}
          <div className="hidden md:flex items-center bg-gray-100 rounded-md px-3 py-2 w-64">
            <Search size={18} className="text-gray-500" />
            <input
              type="text"
              placeholder="Search"
              className="bg-transparent outline-none ml-2 w-full text-sm"
            />
          </div>

          {/* Wishlist Icon */}
          <Link to="/wishlist">
            <Heart
              size={20}
              className={`
      transition-all duration-200
      ${
        isActive("/wishlist")
          ? "text-black scale-110 stroke-[2.5]"
          : "text-gray-600 hover:text-black hover:scale-105"
      }
    `}
            />
          </Link>

          {/* Profile Icon */}
          <Link to="/profile">
            <User
              size={20}
              className={`
      transition-all duration-200
      ${
        isActive("/profile")
          ? "text-black scale-110 stroke-[2.5]"
          : "text-gray-600 hover:text-black hover:scale-105"
      }
    `}
            />
          </Link>
          <Link to="/cart">
            <ShoppingCart
              size={22}
              className={`
      transition-all duration-200
      ${
        isActive("/cart")
          ? "text-black scale-110 stroke-[2.5]"
          : "text-gray-600 hover:text-black hover:scale-105"
      }
    `}
            />
          </Link>
        </div>
      </div>
    </nav>
  );
};

export default Navigater;
