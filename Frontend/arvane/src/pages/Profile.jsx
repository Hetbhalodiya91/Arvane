import { useState } from 'react';
import { User, LogOut, Mail, MapPin, Phone } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

const Profile = () => {
  const { currentUser, logout } = useAuth();
  const [activeTab, setActiveTab] = useState('profile');

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-4xl mx-auto px-4">
        <div className="bg-white rounded-lg shadow-md">
          {/* Profile Header */}
          <div className="bg-gradient-to-r from-indigo-600 to-purple-600 rounded-t-lg p-6 text-white">
            <div className="flex items-center">
              <User size={48} className="mr-4" />
              <div>
                <h1 className="text-2xl font-bold">Account Settings</h1>
                <p className="opacity-90">Manage your profile and preferences</p>
              </div>
            </div>
          </div>

          {/* Tabs */}
          <div className="flex border-b border-gray-200">
            <button
              onClick={() => setActiveTab('profile')}
              className={`px-6 py-3 font-medium ${
                activeTab === 'profile'
                  ? 'border-b-2 border-indigo-600 text-indigo-600'
                  : 'text-gray-500 hover:text-gray-700'
              }`}
            >
              Profile
            </button>
            <button
              onClick={() => setActiveTab('orders')}
              className={`px-6 py-3 font-medium ${
                activeTab === 'orders'
                  ? 'border-b-2 border-indigo-600 text-indigo-600'
                  : 'text-gray-500 hover:text-gray-700'
              }`}
            >
              My Orders
            </button>
            <button
              onClick={() => setActiveTab('notifications')}
              className={`px-6 py-3 font-medium ${
                activeTab === 'notifications'
                  ? 'border-b-2 border-indigo-600 text-indigo-600'
                  : 'text-gray-500 hover:text-gray-700'
              }`}
            >
              Notifications
            </button>
          </div>

          {/* Content */}
          <div className="p-6">
            {activeTab === 'profile' && (
              <div className="space-y-6">
                <div>
                  <h2 className="text-xl font-bold text-gray-900 mb-4">Personal Information</h2>
                  <div className="space-y-4">
                    <div className="flex items-center">
                      <Mail className="h-5 w-5 text-gray-400 mr-3" />
                      <span className="text-gray-700">{currentUser?.email}</span>
                    </div>
                    <div className="flex items-center">
                      <User className="h-5 w-5 text-gray-400 mr-3" />
                      <span className="text-gray-700">{currentUser?.name || 'User Name'}</span>
                    </div>
                  </div>
                </div>

                <button
                  onClick={logout}
                  className="flex items-center gap-2 bg-red-50 text-red-600 px-4 py-2 rounded-lg hover:bg-red-100"
                >
                  <LogOut size={20} />
                  Sign Out
                </button>
              </div>
            )}

            {activeTab === 'orders' && (
              <div>
                <h2 className="text-xl font-bold text-gray-900 mb-4">My Orders</h2>
                <p className="text-gray-500">No orders yet. Start shopping to see your orders here.</p>
              </div>
            )}

            {activeTab === 'notifications' && (
              <div>
                <h2 className="text-xl font-bold text-gray-900 mb-4">Notifications</h2>
                <p className="text-gray-500">You will receive notifications about your order status, promotions, and more.</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;