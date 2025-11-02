import React, { useState } from 'react'
import {
  Box,
  Container,
  Paper,
  Typography,
  TextField,
  Button,
  Link,
  InputAdornment,
  IconButton,
  Alert,
  ToggleButtonGroup,
  ToggleButton,
} from '@mui/material'
import { useNavigate } from 'react-router-dom'
import { useDispatch } from 'react-redux'
import { loginSuccess } from '../store/slices/authSlice'
import { authService } from '../services/authService'
import { toast } from 'react-toastify'
import WorkIcon from '@mui/icons-material/Work'
import VisibilityIcon from '@mui/icons-material/Visibility'
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff'
import EmailIcon from '@mui/icons-material/Email'
import LockIcon from '@mui/icons-material/Lock'
import PersonIcon from '@mui/icons-material/Person'
import PhoneIcon from '@mui/icons-material/Phone'
import PersonSearchIcon from '@mui/icons-material/PersonSearch'
import BusinessIcon from '@mui/icons-material/Business'

const RegisterPage: React.FC = () => {
  const navigate = useNavigate()
  const dispatch = useDispatch()
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    fullName: '',
    phone: '',
    role: 'CANDIDATE' as 'CANDIDATE' | 'RECRUITER',
  })
  const [showPassword, setShowPassword] = useState(false)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value })
  }

  const handleRoleChange = (_: React.MouseEvent<HTMLElement>, newRole: 'CANDIDATE' | 'RECRUITER' | null) => {
    if (newRole !== null) {
      setFormData({ ...formData, role: newRole })
    }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      const response = await authService.register(formData)
      
      if (response.success && response.data) {
        const { token, userId, email, fullName, role } = response.data
        dispatch(loginSuccess({
          user: { id: userId, email, fullName, role },
          token,
        }))
        toast.success('Registration successful!')
        navigate('/dashboard')
      } else {
        throw new Error(response.message || 'Registration failed')
      }
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || err.message || 'Registration failed'
      setError(errorMessage)
      toast.error(errorMessage)
    } finally {
      setLoading(false)
    }
  }

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        background: 'linear-gradient(135deg, #0F2B46 0%, #10B981 100%)',
        py: 4,
      }}
    >
      <Container maxWidth="sm">
        <Paper
          elevation={6}
          sx={{
            p: 4,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            borderRadius: 3,
          }}
        >
          <Box
            sx={{
              mb: 3,
              p: 2,
              borderRadius: '50%',
              background: 'linear-gradient(135deg, #0F2B46 0%, #10B981 100%)',
            }}
          >
            <WorkIcon sx={{ fontSize: 40, color: 'white' }} />
          </Box>
          
          <Typography component="h1" variant="h4" sx={{ fontWeight: 700, mb: 1 }}>
            Create Account
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
            Join JobApp and start your journey
          </Typography>

          {error && (
            <Alert severity="error" sx={{ width: '100%', mb: 2 }}>
              {error}
            </Alert>
          )}

          <Box component="form" onSubmit={handleSubmit} sx={{ width: '100%' }}>
            <Typography variant="subtitle2" sx={{ mb: 1, fontWeight: 600 }}>
              I am a:
            </Typography>
            <ToggleButtonGroup
              value={formData.role}
              exclusive
              onChange={handleRoleChange}
              fullWidth
              sx={{ mb: 2 }}
            >
              <ToggleButton value="CANDIDATE" sx={{ py: 1.5 }}>
                <PersonSearchIcon sx={{ mr: 1 }} />
                Job Seeker
              </ToggleButton>
              <ToggleButton value="RECRUITER" sx={{ py: 1.5 }}>
                <BusinessIcon sx={{ mr: 1 }} />
                Recruiter
              </ToggleButton>
            </ToggleButtonGroup>

            <TextField
              margin="normal"
              required
              fullWidth
              id="fullName"
              label="Full Name"
              name="fullName"
              autoComplete="name"
              autoFocus
              value={formData.fullName}
              onChange={handleChange}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <PersonIcon color="action" />
                  </InputAdornment>
                ),
              }}
            />
            
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              value={formData.email}
              onChange={handleChange}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <EmailIcon color="action" />
                  </InputAdornment>
                ),
              }}
            />

            <TextField
              margin="normal"
              fullWidth
              id="phone"
              label="Phone Number"
              name="phone"
              autoComplete="tel"
              value={formData.phone}
              onChange={handleChange}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <PhoneIcon color="action" />
                  </InputAdornment>
                ),
              }}
            />
            
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type={showPassword ? 'text' : 'password'}
              id="password"
              autoComplete="new-password"
              value={formData.password}
              onChange={handleChange}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <LockIcon color="action" />
                  </InputAdornment>
                ),
                endAdornment: (
                  <InputAdornment position="end">
                    <IconButton
                      onClick={() => setShowPassword(!showPassword)}
                      edge="end"
                    >
                      {showPassword ? <VisibilityOffIcon /> : <VisibilityIcon />}
                    </IconButton>
                  </InputAdornment>
                ),
              }}
              helperText="Must be at least 6 characters"
            />

            <Button
              type="submit"
              fullWidth
              variant="contained"
              disabled={loading}
              sx={{
                mt: 3,
                mb: 2,
                py: 1.5,
                fontSize: '1rem',
                fontWeight: 600,
                background: 'linear-gradient(135deg, #0F2B46 0%, #10B981 100%)',
                '&:hover': {
                  background: 'linear-gradient(135deg, #081a2e 0%, #059669 100%)',
                },
              }}
            >
              {loading ? 'Creating Account...' : 'Create Account'}
            </Button>

            <Box sx={{ textAlign: 'center', mt: 2 }}>
              <Typography variant="body2" color="text.secondary">
                Already have an account?{' '}
                <Link
                  component="button"
                  variant="body2"
                  onClick={() => navigate('/login')}
                  sx={{
                    fontWeight: 600,
                    color: 'primary.main',
                    textDecoration: 'none',
                    cursor: 'pointer',
                  }}
                >
                  Sign In
                </Link>
              </Typography>
            </Box>

            <Box sx={{ textAlign: 'center', mt: 2 }}>
              <Link
                component="button"
                variant="body2"
                onClick={() => navigate('/')}
                sx={{ color: 'text.secondary', textDecoration: 'none' }}
              >
                ← Back to Home
              </Link>
            </Box>
          </Box>
        </Paper>
      </Container>
    </Box>
  )
}

export default RegisterPage

