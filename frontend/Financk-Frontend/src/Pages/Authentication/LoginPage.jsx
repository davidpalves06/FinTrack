import { Typography, Paper, Stack, Button, ThemeProvider, FormControlLabel, Checkbox, Link, Container } from '@mui/material'
import LockPersonIcon from '@mui/icons-material/LockPerson';
import EmailIcon from '@mui/icons-material/Email';
import KeyIcon from '@mui/icons-material/Key';

import { AuthContainer, AuthTextField } from '../../components/Authentication/AuthenticationComponents';
import { theme,PaperCustomization } from '../../components/Authentication/AuthenticationHelpers';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
  const navigate = useNavigate()

  const handleSubmit = () => {
    console.log("Submit");
  }

  const handleCreateAccount = (event) => {
    event.preventDefault();
    navigate("/register")
  }
  
  return (
    <ThemeProvider theme={theme}>
      <AuthContainer theme={theme}>
        <Stack flex='column' justifyContent='center'>
          <Paper sx={PaperCustomization} color='white' elevation={24} square={false} >
            <LockPersonIcon color='success' fontSize='large' sx={{ margin: 'auto auto' }}></LockPersonIcon>
            <Typography variant='h4' component='h1' textAlign='center' color='#4c9173'>Welcome!</Typography>
            <AuthTextField TextIcon={<EmailIcon />} id='email-input' type='email' label='Email'></AuthTextField>
            <AuthTextField TextIcon={<KeyIcon />} id='Password-input' type='password' label='Password'></AuthTextField>
            <FormControlLabel control={<Checkbox />} label="Remember me" />
            <Button type='submit' onSubmit={handleSubmit} sx={{ margin: 'auto auto', color: 'white' }} variant='contained' color='primary'>Login</Button>
            <Container sx={{ display: 'flex', justifyContent: 'space-between', width: '100%' }}>
              <Link href='#' variant='body2' fontWeight='700' padding={'10px'}>Forgot Password?</Link>
              <Link href='/register' variant='body2' fontWeight='700' padding={'10px'} onClick={handleCreateAccount}>Create Account</Link>
            </Container>
          </Paper>
        </Stack>
      </AuthContainer>
    </ThemeProvider>
  )
}

export default LoginPage