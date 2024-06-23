import React from 'react';
import Navbar from './Components/Navbar';
import Header from './Components/Header';
import Signup from './Components/Signup';
import Login from './Components/Login';
import InvoiceCreation from './Components/InvoiceCreation';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';


function App() {
  return (
    <Router>
      <div className="App">
        <Navbar/>
          <Routes>
            <Route path="/" element={<Header />} />
            <Route path="/sign-up" element = {<Signup/>}/>
            <Route path="/log-in" element={<Login />} />
          <Route path="/create-invoice" element={<InvoiceCreation />} />
          </Routes>
      </div>
    </Router>
  );
}

export default App;
