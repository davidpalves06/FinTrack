import {AppBar,Toolbar,IconButton, Typography, Button, Menu, MenuItem} from "@mui/material"
import MenuIcon from '@mui/icons-material/Menu';
import { useNavigate } from "react-router-dom";
import { useAuthentication } from "../Authentication/AuthenticationProvider";
import AccountCircle from '@mui/icons-material/AccountCircle';
import { useState } from "react";

const NavBar = () => {
    const navigate = useNavigate()
    const handleLoginRedirect = () => {
        navigate('/login')
    }

    const {isAuthenticated, logout } = useAuthentication()
    const [anchorEl, setAnchorEl] = useState(null);

    const handleProfileMenuOpen = (event) => {
        setAnchorEl(event.currentTarget);
      };

    const handleMenuClose = () => {
        setAnchorEl(null);
      };

    const handleLogout = async () => {
        await logout()
        setAnchorEl(false)
    }

      const isMenuOpen = Boolean(anchorEl);
    const menuId = 'primary-search-account-menu';
  const renderMenu = (
    <Menu
      anchorEl={anchorEl}
      anchorOrigin={{
        vertical: 'top',
        horizontal: 'right',
      }}
      id={menuId}
      keepMounted
      transformOrigin={{
        vertical: 'top',
        horizontal: 'right',
      }}
      open={isMenuOpen}
      onClose={handleMenuClose}
    >
      <MenuItem onClick={handleMenuClose}>Profile</MenuItem>
      <MenuItem onClick={handleLogout}>Logout</MenuItem>
    </Menu>
  );
  return (
    <AppBar position="static" color="success">
        <Toolbar>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            FinTrack
          </Typography>
            {isAuthenticated ? 
            (<IconButton size="large" edge="end" aria-label="account of current user" aria-controls={menuId} aria-haspopup="true" onClick={handleProfileMenuOpen} color="inherit">
              <AccountCircle />
            </IconButton>) : 
            <Button color="inherit" onClick={handleLoginRedirect}>Login</Button>}
        </Toolbar>
        {renderMenu}
      </AppBar>
  )
}

export default NavBar