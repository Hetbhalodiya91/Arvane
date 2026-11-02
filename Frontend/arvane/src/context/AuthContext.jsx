import { createContext, useContext, useState, useEffect } from 'react';
import { apiClient, getToken, setToken, removeToken } from '../api/apiClient';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Check if user is logged in on mount
  useEffect(() => {
    const checkAuth = () => {
      const token = getToken();
      if (token) {
        // Token exists, try to decode it to get user info
        try {
          const payload = JSON.parse(atob(token.split('.')[1]));
          setCurrentUser({
            email: payload.sub,
            role: payload.role || 'USER',
          });
          setIsAuthenticated(true);
        } catch (e) {
          removeToken();
        }
      }
      setLoading(false);
    };
    checkAuth();
  }, []);

  const login = async (email, password) => {
    try {
      const response = await apiClient.post('/auth/login', { email, password });
      setToken(response.token);

      const payload = JSON.parse(atob(response.token.split('.')[1]));
      setCurrentUser({
        email: payload.sub,
        role: payload.role || 'USER',
      });
      setIsAuthenticated(true);
      return { success: true };
    } catch (error) {
      return { success: false, error: error.message };
    }
  };

  const register = async (userData) => {
    try {
      await apiClient.post('/auth/register', userData);
      return { success: true };
    } catch (error) {
      return { success: false, error: error.message };
    }
  };

  const logout = () => {
    removeToken();
    setCurrentUser(null);
    setIsAuthenticated(false);
  };

  return (
    <AuthContext.Provider value={{
      currentUser,
      loading,
      isAuthenticated,
      login,
      register,
      logout,
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};