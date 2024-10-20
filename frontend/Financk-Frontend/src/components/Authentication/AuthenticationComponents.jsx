import styled from "@emotion/styled";
import { InputAdornment, TextField, IconButton } from "@mui/material";
import { useState } from "react";
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import KeyIcon from '@mui/icons-material/Key';

export const AuthContainer = styled('div')(() => ({
  minHeight: '100vh',
  minWidth: '100vw',
  display: 'flex',
  justifyContent: 'center',
  backgroundColor: '#42b883'
}))

export const AuthTextField = (props) => {
  return (
    <TextField fullWidth {...props} slotProps={{
      input: {
        startAdornment: (
          <InputAdornment position='start'>
            {props.texticon}
          </InputAdornment>
        )
      }
    }}></TextField>
  )
}

export const AuthPassword = (props) => {
  const [showPassword, setShowPassword] = useState(false)
  const handleClickShowPassword = () => setShowPassword((show) => !show);

  return (
      <TextField fullWidth {...props} type={showPassword ? 'text' : 'password'} slotProps={{
        input: {
          startAdornment: (
            <InputAdornment position='start'>
              <KeyIcon />
            </InputAdornment>
          ),
          endAdornment: (
            <InputAdornment position="end">
              <IconButton
                aria-label="toggle password visibility"
                onClick={handleClickShowPassword}
                edge="end"
              >
                {showPassword ? <VisibilityOff /> : <Visibility />}
              </IconButton>
            </InputAdornment>
          )
        }
      }}></TextField>
    )
}
