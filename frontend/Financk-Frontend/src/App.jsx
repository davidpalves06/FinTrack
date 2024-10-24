import { BrowserRouter, Route, Routes } from 'react-router-dom';
import LoginPage from './Pages/Authentication/LoginPage';
import HomePage from './Pages/HomePage/HomePage';
import RegisterPage from './Pages/Authentication/RegisterPage';
import AuthenticationProvider from './components/Authentication/AuthenticationProvider';

function App() {
  return (
    <AuthenticationProvider>
      <BrowserRouter>
        <Routes>
          <Route index element={<HomePage />}></Route>
          <Route path='/register' element={<RegisterPage />}></Route>
          <Route path='/login' element={<LoginPage />}></Route>
        </Routes>
      </BrowserRouter>
    </AuthenticationProvider>
  )
}

export default App
