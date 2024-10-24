import { useAuthentication } from '../../components/Authentication/AuthenticationProvider'
import NavBar from '../../components/NavBar/NavBar'

const HomePage = () => {
  const { isAuthenticated } = useAuthentication()
  return (
    <>
    <NavBar></NavBar>
    <div>{isAuthenticated ? "Logged in" : "Please login" }</div>
    </>
  )
}

export default HomePage