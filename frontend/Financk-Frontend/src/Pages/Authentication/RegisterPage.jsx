import { Typography, Paper, Stack, Button, ThemeProvider, Link, Grid2 } from '@mui/material'
import LockPersonIcon from '@mui/icons-material/LockPerson';
import EmailIcon from '@mui/icons-material/Email';
import KeyIcon from '@mui/icons-material/Key';
import PersonIcon from '@mui/icons-material/Person';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import styled from '@emotion/styled';

import { AuthContainer, AuthTextField } from '../../components/Authentication/AuthenticationComponents';
import { theme, PaperCustomization } from '../../components/Authentication/AuthenticationHelpers';
import { useNavigate } from 'react-router-dom';

const RegisterForm = styled('form')(() => ({
  display: 'flex',
  width: '100%',
  maxWidth:'1000px',
  justifyContent: 'center',
  flexDirection: 'column',
  gap: '20px',
  textAlign: "center"
}));

const PasswordRequirementsList = styled('ul') (() => ({
  listStyle:'disc inside',
  textAlign:'initial',
  margin:'0px 0px 0px 0px',
  width:"fit-content"
}))

const RegisterPage = () => {
  const navigate = useNavigate()
  const handleSubmit = () => {
    console.log("Submit");
  }

  const handleHasAccount = (event) => {
    event.preventDefault();
    navigate("/login")
  }

  return (
    <ThemeProvider theme={theme}>
      <AuthContainer theme={theme}>
        <Stack flex='column' justifyContent='center'>
          <Paper sx={{...PaperCustomization,padding:'10px 20px'}} color='white' elevation={24} square={false} >
            <LockPersonIcon color='success' fontSize='large' sx={{ margin: 'auto auto' }}></LockPersonIcon>
            <Typography variant='h4' component='h1' textAlign='center' color='#4c9173'>Register!</Typography>
            <RegisterForm>
              <Grid2 container spacing={2} maxWidth='100%' m='auto auto' justifyContent={'center'}>
                <Grid2 size={{ xs: 12 ,md: 6 }}>
                  <AuthTextField texticon={<PersonIcon />} id='first-name-input' type='text' label='First Name'></AuthTextField>
                </Grid2>
                <Grid2 size={{ xs: 12 ,md: 6 }}>
                  <AuthTextField texticon={<PersonIcon />} id='last-name-input' type='text' label='Last Name'></AuthTextField>
                </Grid2>
                <Grid2 size={{ xs: 12 ,md: 6 }}>
                  <AuthTextField texticon={<EmailIcon />} id='email-input' type='email' label='Email'></AuthTextField>
                </Grid2>
                <Grid2 size={{ xs: 12,md: 6 }}>
                  <AuthTextField texticon={<AccountCircleIcon />} id='username-input' type='text' label='Username'></AuthTextField>
                </Grid2>
                <Grid2 size={{ xs: 12 ,md: 6 }}>
                  <AuthTextField texticon={<KeyIcon />} id='Password-input' type='password' label='Password'></AuthTextField>
                </Grid2>
                <Grid2 size={{ xs: 12 ,md: 6 }}>
                  <AuthTextField texticon={<KeyIcon />} id='Confirm-Password-input' type='password' label='Confirm Password'></AuthTextField>
                </Grid2>
              </Grid2>
                <PasswordRequirementsList>
              <Typography variant='subtitle2' color='success'>
                  Password must contain:
                  <li>At least 8 characters</li>
                  <li>At least 1 lowercase letter</li>
                  <li>At least 1 uppercase letter</li>
                  <li>At least 1 digit</li>
              </Typography>
                </PasswordRequirementsList>
              <Button type='submit' onSubmit={handleSubmit} sx={{ margin: 'auto auto auto auto', color: 'white' }} variant='contained' color='primary'>Create Account</Button>
            </RegisterForm>
            <Link href='/login' variant='body2' fontWeight='700' m='10px 10px' onClick={handleHasAccount}>Already have an account?</Link>
          </Paper>
        </Stack>
      </AuthContainer>
    </ThemeProvider>
  )
}

export default RegisterPage