import styled from "@emotion/styled";
import { InputAdornment, TextField } from "@mui/material";


export const AuthContainer = styled('div')(() => ({
  height: '100vh',
  width: '100vw',
  display: 'flex',
  justifyContent: 'center',
  backgroundColor: '#42b883'
}))

export const AuthTextField = ({ TextIcon, id, label, type }) => {
  return (
    <TextField fullWidth variant='standard' id={id} label={label} type={type} slotProps={{
      input: {
        startAdornment: (
          <InputAdornment position='start'>
            {TextIcon}
          </InputAdornment>
        )
      }
    }}></TextField>
  )
}

