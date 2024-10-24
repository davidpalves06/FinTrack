import { useContext, createContext, useState, useEffect } from "react"

const AuthenticationContext = createContext()

const AuthenticationProvider = ({ children }) => {
    const [user, setUser] = useState(null)
    const [isAuthenticated, setAuthenticated] = useState(false)

    const checkIfUserIsAuthenticated = async () => {
        try {
            const response = await fetch("http://localhost:8080/auth/check", {
                method: 'GET',
                credentials: 'include'
            })
            if (response.ok) {
                const responseBody = await response.json()
                setUser(responseBody.user);
                setAuthenticated(true)
            } else {
                setAuthenticated(false)
                setUser(null)
            }
        }
        catch (error) {
            setAuthenticated(false)
            setUser(null)
        }
    }

    const logout = async () => {
        const response = await fetch("http://localhost:8080/auth/logout", {
            method: 'GET',
            credentials: 'include'
        })
        if (response.ok) {
            setUser(null);
            setAuthenticated(false)
        }
    }

    const login = async (formValues) => {
        try {
            const response = await fetch("http://localhost:8080/auth/login", {
                method: 'POST',
                body: JSON.stringify(formValues),
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            })
            if (response.ok) {
                const responseBody = await response.json()
                setUser(responseBody.user)
                setAuthenticated(true)
                return { success: true }
            } else {
                return { success: false, message: (await response.json()).message }
            }
        } catch (error) {
            return { success: false, message: "Check your network connection and try again!" }
        }
    }

    useEffect(() => {
        checkIfUserIsAuthenticated();
    }, [])

    return (
        <AuthenticationContext.Provider value={{ user, isAuthenticated, logout,login }}>
            {children}
        </AuthenticationContext.Provider>
    )
}

export const useAuthentication = () => {
    return useContext(AuthenticationContext);
}

export default AuthenticationProvider