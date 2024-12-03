import { BrowserRouter, Route, Routes } from 'react-router-dom';
import LoginPage from './Pages/Authentication/LoginPage';
import HomePage from './Pages/HomePage/HomePage';
import RegisterPage from './Pages/Authentication/RegisterPage';
import ProtectedRoute from './components/Authentication/ProtectedRoute';
import { useAuthentication } from './components/Authentication/AuthenticationProvider'
import AuthenticationRoute from './components/Authentication/AuthenticationRoute';
import BudgetPage from './Pages/BudgetPage/BudgetPage';

function App() {
  const auth = useAuthentication()
  
  return (
      <BrowserRouter>
        <Routes>
          <Route index element={<ProtectedRoute auth={auth}><HomePage /></ProtectedRoute>}></Route>
          <Route path='/register' element={<AuthenticationRoute auth={auth}><RegisterPage /> </AuthenticationRoute>}></Route>
          <Route path='/login' element={<AuthenticationRoute auth={auth}><LoginPage /></AuthenticationRoute>}></Route>
          <Route path='/budget' element={<ProtectedRoute auth={auth}><BudgetPage /></ProtectedRoute>}></Route>
        </Routes>
      </BrowserRouter>
  )
}

export default App
