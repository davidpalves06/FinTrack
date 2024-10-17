import { createTheme } from '@mui/material/styles';

export const theme = createTheme({
    palette: {
      primary: {
        main: '#42b883'
      }
    },
  });
  
  export const PaperCustomization = {
    padding: '10px 50px',
    minWidth: '250px',
    boxShadow: '0px 0px 50px rgba(0, 0, 0, 0.5)',
    backgroundColor: '#ffffffdf',
    minHeight: '50vh',
    borderRadius: '20px',
    display: 'flex',
    alignContent: 'center',
    flexDirection: 'column',
    gap: '10px',
    textAlign: "center"
  }
  