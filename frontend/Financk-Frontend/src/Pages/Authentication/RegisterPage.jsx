import { Typography, Paper, Stack, Button, ThemeProvider, Link, Grid2 } from '@mui/material'
import LockPersonIcon from '@mui/icons-material/LockPerson';
import EmailIcon from '@mui/icons-material/Email';
import KeyIcon from '@mui/icons-material/Key';
import PersonIcon from '@mui/icons-material/Person';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';

import { AuthContainer, AuthTextField } from '../../components/Authentication/AuthenticationComponents';
import { theme,PaperCustomization } from '../../components/Authentication/AuthenticationHelpers';
import { useNavigate } from 'react-router-dom';

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
          <Paper sx={PaperCustomization} color='white' elevation={24} square={false} >
            <LockPersonIcon color='success' fontSize='large' sx={{ margin: 'auto auto' }}></LockPersonIcon>
            <Typography variant='h4' component='h1' textAlign='center' color='#4c9173'>Register!</Typography>
            <Grid2 container spacing={2} maxWidth='450px' m='auto auto '>
              <Grid2 size={{ xs: 6 }}>
                <AuthTextField TextIcon={<PersonIcon />} id='first-name-input' type='text' label='First Name'></AuthTextField>
              </Grid2>
              <Grid2 size={{ xs: 6 }}>
                <AuthTextField TextIcon={<PersonIcon />} id='last-name-input' type='text' label='Last Name'></AuthTextField>
              </Grid2>
              <Grid2 size={{ xs: 6 }}>
                <AuthTextField TextIcon={<EmailIcon />} id='email-input' type='email' label='Email'></AuthTextField>
              </Grid2>
              <Grid2 size={{ xs: 6 }}>
                <AuthTextField TextIcon={<AccountCircleIcon />} id='username-input' type='text' label='Username'></AuthTextField>
              </Grid2>
              <Grid2 size={{ xs: 6 }}>
                <AuthTextField TextIcon={<KeyIcon />} id='Password-input' type='password' label='Password'></AuthTextField>
              </Grid2>
              <Grid2 size={{ xs: 6 }}>
                <AuthTextField TextIcon={<KeyIcon />} id='Confirm-Password-input' type='password' label='Confirm Password'></AuthTextField>
              </Grid2>
            </Grid2>
            <Button type='submit' onSubmit={handleSubmit} sx={{ margin: '20px auto auto auto', color: 'white' }} variant='contained' color='primary'>Create Account</Button>
            <Link href='/login' variant='body2' fontWeight='700' mt='20px' onClick={handleHasAccount}>Already have an account?</Link>
          </Paper>
        </Stack>
      </AuthContainer>
    </ThemeProvider>
  )
}

export default RegisterPage