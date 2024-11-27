import { AppBar, Toolbar, IconButton, Typography, Button, Menu, MenuItem, Box } from "@mui/material"
import { useNavigate } from "react-router-dom";
import { useAuthentication } from "../Authentication/AuthenticationProvider";
import AccountCircle from '@mui/icons-material/AccountCircle';
import { useState } from "react";

const pages = ['Budget', 'Contact'];

const NavBar = () => {
  const navigate = useNavigate()
  const handleLoginRedirect = () => {
    navigate('/login')
  }

  const { isAuthenticated, logout } = useAuthentication()
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

  const handleBarNavigation =  (page) => {
    if (page == pages[0]) {
      navigate("/budget")
    }
    if (page == pages[1]) {
      navigate("/contact")
    }
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
        <Box sx={{ display: 'flex', flexGrow: '1', flexDirection: 'row', justifyContent: 'space-between' }}>
          <Typography variant="h6" component="div" width={"fit-content"}>
            FinTrack
          </Typography>
          <Box>
            {pages.map((page) => (
              <Button color="inherit" key={page} onClick={() => handleBarNavigation(page)}>
                {page}
              </Button>
            ))}
          </Box>
        </Box>
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