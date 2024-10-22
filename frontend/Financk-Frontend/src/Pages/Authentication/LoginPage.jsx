import { Typography, Paper, Stack, Button, ThemeProvider, Link, Container, Snackbar, Alert } from '@mui/material'
import LockPersonIcon from '@mui/icons-material/LockPerson';
import EmailIcon from '@mui/icons-material/Email';


import { AuthContainer, AuthTextField, AuthPassword } from '../../components/Authentication/AuthenticationComponents';
import { theme, PaperCustomization, validatePassword } from '../../components/Authentication/AuthenticationHelpers';
import { useNavigate } from 'react-router-dom';
import styled from '@emotion/styled';
import { useState } from 'react';

const LoginForm = styled('form')(() => ({
  display: 'flex',
  width: '100%',
  maxWidth: '290px',
  alignContent: 'center',
  flexDirection: 'column',
  gap: '20px',
  textAlign: "center",
  margin: 'auto auto'
}));

const LoginPage = () => {
  const [formValues, setFormValues] = useState({
    email: '',
    password: ''
  })
  const [validPassword, setValidPassword] = useState(true)
  const [validEmail, setValidEmail] = useState(true)
  const [error, setError] = useState(false)
  const [errorMessage, setErrorMessage] = useState(false)
  const navigate = useNavigate()

  const handleSubmit = async (event) => {
    event.preventDefault();
    console.log("Submit");
    try {
      const response = await fetch("http://localhost:8080/auth/login", {
        method: 'POST',
        body: JSON.stringify(formValues),
        headers: {
          'Content-Type': 'application/json',
        },
        credentials:'include'
      })
      if (response.ok) {
        const responseBody = await response.json()
        setFormValues({
          email: '',
          password: ''
        })
      console.log(responseBody);
      navigate('/')
    } else {
      setErrorMessage((await response.json()).message)
      setError(true)
      return
    }} catch (error) {
      console.log(error);
      setError(true)
      setErrorMessage("Check your network connection and try again!")
    }
  }

  const handleInputChange = (event) => {
    event.preventDefault();
    const { name, value } = event.target;
    if (name == 'password') {
      if (!validatePassword(value)) {
        setValidPassword(false)
      }
      else setValidPassword(true)
    }
    if (name == 'email') {
      if (event.target.checkValidity()) {
        setValidEmail(true)
      } else setValidEmail(false)
    }
    setFormValues({
      ...formValues,
      [name]: value
    })
  }

  const handleCreateAccount = (event) => {
    event.preventDefault();
    navigate("/register")
  }

  const handleSnackBarClose = () => {
    setError(false)
    setErrorMessage('')
  }

  return (
    <ThemeProvider theme={theme}>
      <AuthContainer theme={theme}>
        <Stack flex='column' justifyContent='center'>
          <Paper sx={{...PaperCustomization,padding:'10px 30px'}} color='white' elevation={24} square={false}>
            <LockPersonIcon color='success' fontSize='large' sx={{ margin: 'auto auto' }}></LockPersonIcon>
            <Typography variant='h4' component='h1' textAlign='center' color='#4c9173' m={'auto auto'}>Welcome!</Typography>
            <LoginForm onSubmit={handleSubmit}>
              <AuthTextField texticon={<EmailIcon />} variant='standard' id='email-input' type='email' label='Email' name='email' error={!validEmail} value={formValues.email} onChange={handleInputChange} autoFocus></AuthTextField>
              <AuthPassword variant='standard' id='Password-input' label='Password' name='password' error={!validPassword} value={formValues.password} onChange={handleInputChange}></AuthPassword>
              <Button type='submit' sx={{ margin: 'auto auto', color: 'white' }} variant='contained' color='primary'>Login</Button>
            </LoginForm>
            <Container sx={{ display: 'flex', justifyContent: 'space-between', width: '100%' }}>
              <Link href='#' variant='body2' fontWeight='700' padding={'10px'}>Forgot Password?</Link>
              <Link href='/register' variant='body2' fontWeight='700' padding={'10px'} onClick={handleCreateAccount}>Create Account</Link>
            </Container>
            <Snackbar anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }} open={error} key={'bottom' + 'center' + 'error'} onClose={handleSnackBarClose} autoHideDuration={2000} sx={{ width: 'fit-content', margin: 'auto auto' }}>
                <Alert severity="warning" variant="filled" sx={{ width: '100%' }}>
                  {errorMessage}
                </Alert>
              </Snackbar>
          </Paper>
        </Stack>
      </AuthContainer>
    </ThemeProvider>
  )
}

export default LoginPage