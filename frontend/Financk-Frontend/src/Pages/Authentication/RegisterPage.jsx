import { Typography, Paper, Stack, Button, ThemeProvider, Link, Grid2, Alert, Snackbar } from '@mui/material'
import LockPersonIcon from '@mui/icons-material/LockPerson';
import EmailIcon from '@mui/icons-material/Email';
import PersonIcon from '@mui/icons-material/Person';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import styled from '@emotion/styled';

import { AuthContainer, AuthPassword, AuthTextField } from '../../components/Authentication/AuthenticationComponents';
import { theme, PaperCustomization, validatePassword } from '../../components/Authentication/AuthenticationHelpers';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';

const RegisterForm = styled('form')(() => ({
  display: 'flex',
  width: '100%',
  maxWidth: '1000px',
  justifyContent: 'center',
  flexDirection: 'column',
  gap: '20px',
  textAlign: "center"
}));

const PasswordRequirementsList = styled('ul')(() => ({
  listStyle: 'disc inside',
  textAlign: 'initial',
  margin: '0px 0px 0px 0px',
  width: "fit-content"
}))

const RegisterPage = () => {
  const [validPassword, setValidPassword] = useState(true)
  const [validEmail, setValidEmail] = useState(true)
  const [firstName, setFirstName] = useState('')
  const [lastName, setLastName] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [error,setError] = useState(false)
  const [errorMessage,setErrorMessage] = useState(false)
  const [formValues, setFormValues] = useState({
    name: '',
    email: '',
    username: '',
    password: ''
  })

  const navigate = useNavigate()
  const handleSubmit = async (event) => {
    event.preventDefault()
    try {

      const response = await fetch("http://localhost:8080/auth/register", {
        method: 'POST',
        body: JSON.stringify(formValues),
        headers: {
          'Content-Type': 'application/json',
        }
      });
      if (response.ok) {
        const validResponse = await response.json()
        setFormValues({
          name: '',
          email: '',
          username: '',
          password: ''
        })
        setFirstName('')
        setLastName('')
        setConfirmPassword('')
        alert(validResponse.message)
        navigate('/login')
      } else {
        setErrorMessage((await response.json()).message)
        setError(true)
        return
      }
    } catch (error) {
      console.log(error);
      setError(true)
    }
  }

  const handleSnackBarClose = () => {
    setError(false)
    setErrorMessage('')
  }

  const handleHasAccount = (event) => {
    event.preventDefault();
    navigate("/login")
  }


  const handleInputChange = (event) => {
    event.preventDefault();
    const { name, value } = event.target;
    if (name == 'confirmPassword') {
      setConfirmPassword(value)
      if (value != formValues.password) {
        setValidPassword(false)
      }
      else setValidPassword(true)
      return
    }
    if (name == 'password') {
      if (!validatePassword(value) || value != confirmPassword) {
        setValidPassword(false)
      }
      else setValidPassword(true)
    }
    if (name == 'email') {
      if (event.target.checkValidity()) {
        setValidEmail(true)
      } else setValidEmail(false)
    }
    if (name == 'firstName') {
      setFirstName(value)
      setFormValues({
        ...formValues,
        name: firstName + ' ' + lastName
      })
      return
    }
    if (name == 'lastName') {
      setLastName(value)
      setFormValues({
        ...formValues,
        name: firstName + ' ' + lastName
      })
      return
    }
    setFormValues({
      ...formValues,
      [name]: value
    })
  }

  return (
    <ThemeProvider theme={theme}>
      <AuthContainer theme={theme}>
        <Stack flex='column' justifyContent='center'>
          <Paper sx={{ ...PaperCustomization, padding: '10px 20px' }} color='white' elevation={24} square={false} >
            <LockPersonIcon color='success' fontSize='large' sx={{ margin: 'auto auto' }}></LockPersonIcon>
            <Typography variant='h4' component='h1' textAlign='center' color='#4c9173'>Register!</Typography>
            <RegisterForm onSubmit={handleSubmit}>
              <Grid2 container spacing={2} maxWidth='100%' m='auto auto' justifyContent={'center'}>
                <Grid2 size={{ xs: 12, md: 6 }}>
                  <AuthTextField texticon={<PersonIcon />} id='first-name-input' type='text' label='First Name' name='firstName' value={firstName} onChange={handleInputChange} required autoFocus></AuthTextField>
                </Grid2>
                <Grid2 size={{ xs: 12, md: 6 }}>
                  <AuthTextField texticon={<PersonIcon />} id='last-name-input' type='text' label='Last Name' name='lastName' value={lastName} onChange={handleInputChange} required></AuthTextField>
                </Grid2>
                <Grid2 size={{ xs: 12, md: 6 }}>
                  <AuthTextField texticon={<EmailIcon />} id='email-input' type='email' label='Email' name='email' error={!validEmail} value={formValues.email} onChange={handleInputChange} required></AuthTextField>
                </Grid2>
                <Grid2 size={{ xs: 12, md: 6 }}>
                  <AuthTextField texticon={<AccountCircleIcon />} id='username-input' type='text' label='Username' name='username' value={formValues.username} onChange={handleInputChange} required></AuthTextField>
                </Grid2>
                <Grid2 size={{ xs: 12, md: 6 }}>
                  <AuthPassword id='Password-input' type='password' label='Password' name='password' error={!validPassword} value={formValues.password} onChange={handleInputChange} required></AuthPassword>
                </Grid2>
                <Grid2 size={{ xs: 12, md: 6 }}>
                  <AuthPassword id='Confirm-Password-input' type='password' label='Confirm Password' name='confirmPassword' error={!validPassword} value={confirmPassword} onChange={handleInputChange} required></AuthPassword>
                </Grid2>
              </Grid2>
              {<Snackbar anchorOrigin={{ vertical:'bottom' ,horizontal:'center' }}
        open={error}
        key={'bottom' + 'center'}
        onClose={handleSnackBarClose}
        autoHideDuration={6000}
        >
          <Alert
    severity="error"
    variant="filled"
    sx={{ width: '100%' }}
  >
    {errorMessage}
  </Alert></Snackbar>}
              <PasswordRequirementsList>
                <Typography variant='subtitle2' color='success'>
                  Password must contain:
                  <li>At least 8 characters, 1 lowercase letter, 1 uppercase letter, 1 digit</li>
                </Typography>
              </PasswordRequirementsList>
              <Button type='submit' sx={{ margin: 'auto auto auto auto', color: 'white' }} variant='contained' color='primary'>Create Account</Button>
            </RegisterForm>
            <Link href='/login' variant='body2' fontWeight='700' m='10px 10px' onClick={handleHasAccount}>Already have an account?</Link>
          </Paper>
        </Stack>
      </AuthContainer>
    </ThemeProvider>
  )
}

export default RegisterPage