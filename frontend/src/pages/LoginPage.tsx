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
} from '@mui/material'
import { useNavigate } from 'react-router-dom'
import { useDispatch } from 'react-redux'
import { loginStart, loginSuccess, loginFailure } from '../store/slices/authSlice'
import { authService } from '../services/authService'
import { toast } from 'react-toastify'
import WorkIcon from '@mui/icons-material/Work'
import VisibilityIcon from '@mui/icons-material/Visibility'
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff'
import EmailIcon from '@mui/icons-material/Email'
import LockIcon from '@mui/icons-material/Lock'

const LoginPage: React.FC = () => {
  const navigate = useNavigate()
  const dispatch = useDispatch()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    dispatch(loginStart())

    try {
      const response = await authService.login({ email, password })
      
      if (response.success && response.data) {
        const { token, userId, email: userEmail, fullName, role } = response.data
        dispatch(loginSuccess({
          user: { id: userId, email: userEmail, fullName, role },
          token,
        }))
        toast.success('Login successful!')
        navigate('/dashboard')
      } else {
        throw new Error(response.message || 'Login failed')
      }
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || err.message || 'Login failed'
      setError(errorMessage)
      dispatch(loginFailure())
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
            Welcome Back
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
            Sign in to continue to JobApp
          </Typography>

          {error && (
            <Alert severity="error" sx={{ width: '100%', mb: 2 }}>
              {error}
            </Alert>
          )}

          <Box component="form" onSubmit={handleSubmit} sx={{ width: '100%' }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              autoFocus
              value={email}
              onChange={(e) => setEmail(e.target.value)}
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
              required
              fullWidth
              name="password"
              label="Password"
              type={showPassword ? 'text' : 'password'}
              id="password"
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
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
              {loading ? 'Signing In...' : 'Sign In'}
            </Button>

            <Box sx={{ textAlign: 'center', mt: 2 }}>
              <Typography variant="body2" color="text.secondary">
                Don't have an account?{' '}
                <Link
                  component="button"
                  variant="body2"
                  onClick={() => navigate('/register')}
                  sx={{
                    fontWeight: 600,
                    color: 'primary.main',
                    textDecoration: 'none',
                    cursor: 'pointer',
                  }}
                >
                  Sign Up
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

export default LoginPage

