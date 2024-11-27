import { Navigate } from 'react-router-dom'

const AuthenticationRoute = ({ children, auth }) => {
    const {isAuthenticated,isLoading} = auth
    return (
        <>
            {!isLoading && (isAuthenticated ? <Navigate to="/" replace /> : children )}
        </>
    )
}

export default AuthenticationRoute