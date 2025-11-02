import React from 'react'
import { Box, AppBar, Toolbar, Typography, Button, Container, Avatar, Menu, MenuItem, IconButton, Chip, Grid } from '@mui/material'
import { useNavigate } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import { RootState } from '../../store/store'
import { logout } from '../../store/slices/authSlice'
import WorkIcon from '@mui/icons-material/Work'
import DashboardIcon from '@mui/icons-material/Dashboard'
import BusinessCenterIcon from '@mui/icons-material/BusinessCenter'
import PersonIcon from '@mui/icons-material/Person'
import LogoutIcon from '@mui/icons-material/Logout'
import AddCircleIcon from '@mui/icons-material/AddCircle'

interface LayoutProps {
  children: React.ReactNode
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const navigate = useNavigate()
  const dispatch = useDispatch()
  const { user } = useSelector((state: RootState) => state.auth)
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null)

  const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget)
  }

  const handleClose = () => {
    setAnchorEl(null)
  }

  const handleLogout = () => {
    dispatch(logout())
    navigate('/login')
  }

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <AppBar 
        position="static" 
        elevation={0} 
        sx={{ 
          background: '#FFFFFF',
          borderBottom: '1px solid rgba(0,0,0,0.08)',
          color: '#0F2B46'
        }}
      >
        <Toolbar>
          <Box sx={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }} onClick={() => navigate('/dashboard')}>
            <WorkIcon sx={{ mr: 1.5, fontSize: 32, color: 'primary.main' }} />
            <Typography variant="h5" component="div" sx={{ fontWeight: 800, color: 'primary.main' }}>
              JobApp
            </Typography>
            <Chip 
              label="AI-Powered" 
              size="small" 
              sx={{ 
                ml: 1, 
                background: 'linear-gradient(135deg, #10B981 0%, #0F2B46 100%)',
                color: 'white',
                fontWeight: 700,
                fontSize: '0.65rem'
              }} 
            />
          </Box>

          <Box sx={{ flexGrow: 1, ml: 4, display: { xs: 'none', md: 'flex' } }}>
            <Button
              color="inherit"
              startIcon={<DashboardIcon />}
              onClick={() => navigate('/dashboard')}
              sx={{ mx: 1 }}
            >
              Dashboard
            </Button>
            <Button
              color="inherit"
              startIcon={<WorkIcon />}
              onClick={() => navigate('/jobs')}
              sx={{ mx: 1 }}
            >
              Jobs
            </Button>
            <Button
              color="inherit"
              startIcon={<BusinessCenterIcon />}
              onClick={() => navigate(user?.role === 'RECRUITER' ? '/manage-applications' : '/applications')}
              sx={{ mx: 1 }}
            >
              Applications
            </Button>
            {user?.role === 'CANDIDATE' && (
              <Button
                color="inherit"
                onClick={() => navigate('/skill-roi')}
                sx={{ mx: 1 }}
              >
                💡 Skill ROI
              </Button>
            )}
            {user?.role === 'RECRUITER' && (
              <Button
                color="inherit"
                startIcon={<AddCircleIcon />}
                onClick={() => navigate('/post-job')}
                sx={{ mx: 1 }}
              >
                Post Job
              </Button>
            )}
          </Box>

          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <Typography variant="body2" sx={{ mr: 2, display: { xs: 'none', sm: 'block' } }}>
              {user?.fullName}
            </Typography>
            <IconButton
              size="large"
              onClick={handleMenu}
              color="inherit"
            >
              <Avatar sx={{ width: 36, height: 36, bgcolor: 'secondary.main' }}>
                {user?.fullName?.[0] || 'U'}
              </Avatar>
            </IconButton>
            <Menu
              anchorEl={anchorEl}
              open={Boolean(anchorEl)}
              onClose={handleClose}
              transformOrigin={{ horizontal: 'right', vertical: 'top' }}
              anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
            >
              <MenuItem onClick={() => { handleClose(); navigate('/profile'); }}>
                <PersonIcon sx={{ mr: 1 }} /> Profile
              </MenuItem>
              <MenuItem onClick={() => { handleClose(); handleLogout(); }}>
                <LogoutIcon sx={{ mr: 1 }} /> Logout
              </MenuItem>
            </Menu>
          </Box>
        </Toolbar>
      </AppBar>

      <Container maxWidth="xl" sx={{ flexGrow: 1, py: 5, bgcolor: 'background.default', minHeight: 'calc(100vh - 200px)' }}>
        {children}
      </Container>

      <Box component="footer" sx={{ py: 4, px: 2, mt: 'auto', bgcolor: '#0F2B46', color: 'white' }}>
        <Container maxWidth="xl">
          <Grid container spacing={3}>
            <Grid item xs={12} md={4}>
              <Typography variant="h6" sx={{ fontWeight: 700, mb: 1 }}>
                JobApp
              </Typography>
              <Typography variant="body2" sx={{ opacity: 0.8 }}>
                AI-Powered Job Matching Platform with Fraud Detection & Career Tools
              </Typography>
            </Grid>
            <Grid item xs={12} md={4}>
              <Typography variant="subtitle2" sx={{ fontWeight: 700, mb: 1 }}>
                Features
              </Typography>
              <Typography variant="body2" sx={{ opacity: 0.8 }}>
                • AI Match Prediction<br/>
                • Fraud Detection<br/>
                • Skill ROI Calculator<br/>
                • Salary Negotiator
              </Typography>
            </Grid>
            <Grid item xs={12} md={4}>
              <Typography variant="subtitle2" sx={{ fontWeight: 700, mb: 1 }}>
                © {new Date().getFullYear()} JobApp
              </Typography>
              <Typography variant="body2" sx={{ opacity: 0.8 }}>
                All rights reserved.<br/>
                Built with AI & Machine Learning
              </Typography>
            </Grid>
          </Grid>
        </Container>
      </Box>
    </Box>
  )
}

export default Layout

