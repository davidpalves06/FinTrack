import { Container, Typography, Paper, TextField, Stack, Button, InputAdornment, ThemeProvider } from '@mui/material'
import LockPersonIcon from '@mui/icons-material/LockPerson';
import EmailIcon from '@mui/icons-material/Email';
import KeyIcon from '@mui/icons-material/Key';
import { createTheme } from '@mui/material/styles';

const theme = createTheme({
  palette: {
    primary: {
      main: '#42b883'
    }
  },
});

const LoginPage = () => {
  const handleSubmit = () => {
    console.log("Submit");

  }

  return (
    <ThemeProvider theme={theme}>
      <Container sx={{ height: '100vh', width: '100vw', display: 'flex', justifyContent: 'center', backgroundColor: '#42b883' }} maxWidth={false}>
        <Stack flex='column' justifyContent='center'>
          <Paper sx={{ padding:'10px 50px',minWidth: '250px', backgroundColor: 'White', minHeight: '50vh', borderRadius: '20px', display: 'flex', alignItems: 'center', flexDirection: 'column', gap: '20px', textAlign: "center" }} color='white' elevation={24} square={false} >
            <LockPersonIcon color='success' fontSize='large' sx={{ marginTop: '30px' }}></LockPersonIcon>
            <Typography variant='h4' component='h1' textAlign='center' color='#4c9173'>Login</Typography>
            <TextField variant='standard' id='email-input' label='Email' type='email' slotProps={{
              input: {
                startAdornment: (
                  <InputAdornment position='start'>
                    <EmailIcon></EmailIcon>
                  </InputAdornment>
                )
              }
            }}></TextField>
            <TextField variant='standard' id='Password-input' label='Password' type='password' slotProps={{
              input: {
                startAdornment: (
                  <InputAdornment position='start'>
                    <KeyIcon></KeyIcon>
                  </InputAdornment>
                )
              }
            }}></TextField>
            <Button type='submit' onSubmit={handleSubmit} sx={{ margin: 'auto auto', color: 'white' }} variant='contained' color='primary' size='large'>Login</Button>
          </Paper>
        </Stack>
      </Container>
    </ThemeProvider>
  )
}

export default LoginPage