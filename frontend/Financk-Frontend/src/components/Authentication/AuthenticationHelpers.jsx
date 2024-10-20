import { createTheme } from '@mui/material/styles';

export const theme = createTheme({
  palette: {
    primary: {
      main: '#42b883'
    }
  },
});

export const PaperCustomization = {
  minWidth: '250px',
  boxShadow: '0px 0px 50px rgba(0, 0, 0, 0.5)',
  backgroundColor: '#ffffffdf',
  minHeight: '50vh',
  margin: '20px 20px 20px 20px',
  height:'fit-content',
  borderRadius: '20px',
  display: 'flex',
  alignContent: 'center',
  flexDirection: 'column',
  gap: '10px',
  textAlign: "center"
}

export const validatePassword = (password) => {
  const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).+$/;
  if (password.length < 8 || password.length > 32) return false 
  return regex.test(password);
};
