import { Navigate } from 'react-router-dom'


const ProtectedRoute = ({ children, auth }) => {
    const {isAuthenticated,isLoading} = auth
    return (
        <>
            {!isLoading && (isAuthenticated ? children : <Navigate to="/login" replace />)}
        </>
    )
}

export default ProtectedRoute