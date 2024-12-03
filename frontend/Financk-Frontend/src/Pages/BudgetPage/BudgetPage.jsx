import { Box, List, ListItem, ListItemButton, Typography } from "@mui/material"
import NavBar from "../../components/NavBar/NavBar"

const BudgetPage = () => {
    let budgetList = []
    return (
        <>
            <NavBar></NavBar>
            <Box sx={{ width: '100%', minHeight: 'calc(100vh - 48px)', margin: 'auto auto', display: 'flex', flexDirection: 'column', alignContent: 'center',backgroundColor:'#33CC66' }}>
                <Typography margin='50px auto' variant="h3">Budgets</Typography>
                {budgetList.length == 0 ? <div>Create new Budget</div>: (
                <List sx={{ width: '100%'}}>
                    {budgetList.map((budget) => (
                        <ListItem key={budget}>
                        <ListItemButton sx={{
                            backgroundColor:'#F5F5F5', borderRadius: '10px', minHeight: '60px', width:'50%',maxWidth:'300px',margin:'auto auto', '&:hover': {
                                backgroundColor:'#FFFFCC',
                                boxShadow: '0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19)'
                            },'&:focus': {
                                backgroundColor:'#FFFFCC',
                                boxShadow: '0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19)'
                            }
                        }}>
                        <Typography textAlign='center' width='100%' variant="button" fontSize='clamp(16px, 2vw, 20px)'>
                        {budget}
                        </Typography>
                        </ListItemButton>
                        </ListItem>
                    ))}
                </List>
                )
                }
            </Box>
        </>
    )
}

export default BudgetPage