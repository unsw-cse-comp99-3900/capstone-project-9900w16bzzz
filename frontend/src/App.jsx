import React, {useEffect} from 'react';
import Navbar from './Components/Navbar';
import Header from './Components/Header';
import Signup from './Components/Signup';
import Login from './Components/Login';
import InvoiceCreation from './Components/InvoiceCreation';
import Myinvoice from './Components/Myinvoice';
import Invoicedetail from './Components/Invoicedetail';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ResetPassword from './Components/ResetPassword';
import { PopupProvider } from './Components/PopupWindow/PopupContext';
import GlobalPopup from './Components/PopupWindow/usePopup';
import ValidationPage from './Components/ValidationPage/ValidationPage';


function App() {
  useEffect(() => {
    const handleBeforeUnload = () => {
      localStorage.removeItem('token');
    };
    window.addEventListener('beforeunload', handleBeforeUnload);
    return () => {
      window.removeEventListener('beforeunload', handleBeforeUnload);
    };
  }, []);
  return (
    <PopupProvider>
      <Router>
        <GlobalPopup />
        <div className="App">
          <Navbar/>
            <Routes>
              <Route path="/" element={<Header />} />
              <Route path="/sign-up" element = {<Signup/>}/>
              <Route path="/log-in" element={<Login />} />
              <Route path="/create-invoice" element={<InvoiceCreation />} />
              <Route path="/reset-password" element={<ResetPassword />} />
              <Route path="/my-invoice" element={<Myinvoice />} />
              <Route path="/invoice/:invoiceId" element={<Invoicedetail />} />
              <Route path="/validation/:invoiceId" element={<ValidationPage />} />
            </Routes>
        </div>
        </Router>
      </PopupProvider>
  );
}

export default App;
