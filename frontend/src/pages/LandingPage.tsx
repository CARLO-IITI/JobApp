import React from 'react'
import {
  Box,
  Container,
  Typography,
  Button,
  Grid,
  Card,
  CardContent,
  AppBar,
  Toolbar,
} from '@mui/material'
import { useNavigate } from 'react-router-dom'
import WorkIcon from '@mui/icons-material/Work'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import PsychologyIcon from '@mui/icons-material/Psychology'
import SpeedIcon from '@mui/icons-material/Speed'
import SecurityIcon from '@mui/icons-material/Security'
import GroupsIcon from '@mui/icons-material/Groups'

const LandingPage: React.FC = () => {
  const navigate = useNavigate()

  const features = [
    {
      icon: <PsychologyIcon sx={{ fontSize: 48, color: 'primary.main' }} />,
      title: 'AI-Powered Matching',
      description: 'Our intelligent algorithms match you with the perfect opportunities based on skills, experience, and preferences.',
    },
    {
      icon: <SpeedIcon sx={{ fontSize: 48, color: 'primary.main' }} />,
      title: 'Lightning Fast',
      description: 'Get matched with thousands of jobs in seconds. No more endless scrolling through irrelevant listings.',
    },
    {
      icon: <TrendingUpIcon sx={{ fontSize: 48, color: 'primary.main' }} />,
      title: 'Career Growth',
      description: 'Track your applications, get insights, and grow your career with personalized recommendations.',
    },
    {
      icon: <SecurityIcon sx={{ fontSize: 48, color: 'primary.main' }} />,
      title: 'Secure & Private',
      description: 'Your data is encrypted and secure. We never share your information without permission.',
    },
    {
      icon: <GroupsIcon sx={{ fontSize: 48, color: 'primary.main' }} />,
      title: 'Connect with Top Companies',
      description: 'Access opportunities from leading companies actively looking for talented individuals.',
    },
    {
      icon: <WorkIcon sx={{ fontSize: 48, color: 'primary.main' }} />,
      title: 'Free to Use',
      description: 'No hidden fees, no premium tiers. All features are completely free for job seekers.',
    },
  ]

  return (
    <Box sx={{ minHeight: '100vh', background: 'linear-gradient(135deg, #0F2B46 0%, #10B981 100%)' }}>
      {/* Navigation */}
      <AppBar position="static" elevation={0} sx={{ background: 'transparent' }}>
        <Toolbar>
          <WorkIcon sx={{ mr: 2, fontSize: 32 }} />
          <Typography variant="h6" component="div" sx={{ flexGrow: 1, fontWeight: 700 }}>
            JobApp
          </Typography>
          <Button color="inherit" onClick={() => navigate('/login')} sx={{ mr: 1 }}>
            Login
          </Button>
          <Button
            variant="contained"
            onClick={() => navigate('/register')}
            sx={{
              bgcolor: 'white',
              color: 'primary.main',
              '&:hover': { bgcolor: 'grey.100' },
            }}
          >
            Get Started
          </Button>
        </Toolbar>
      </AppBar>

      {/* Hero Section */}
      <Container maxWidth="lg">
        <Box
          sx={{
            pt: 12,
            pb: 8,
            textAlign: 'center',
            color: 'white',
          }}
        >
          <Typography
            variant="h2"
            component="h1"
            gutterBottom
            sx={{
              fontWeight: 800,
              fontSize: { xs: '2.5rem', md: '3.5rem' },
              mb: 3,
            }}
          >
            Find Your Dream Job with
            <br />
            AI-Powered Precision
          </Typography>
          <Typography
            variant="h5"
            sx={{
              mb: 5,
              opacity: 0.95,
              fontWeight: 400,
              maxWidth: '700px',
              mx: 'auto',
            }}
          >
            Connect with opportunities that perfectly match your skills, experience, and career aspirations.
          </Typography>
          <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center', flexWrap: 'wrap' }}>
            <Button
              variant="contained"
              size="large"
              onClick={() => navigate('/register')}
              sx={{
                bgcolor: 'white',
                color: 'primary.main',
                px: 5,
                py: 2,
                fontSize: '1.1rem',
                fontWeight: 600,
                '&:hover': { bgcolor: 'grey.100', transform: 'scale(1.05)' },
                transition: 'all 0.3s',
              }}
            >
              Get Started Free
            </Button>
            <Button
              variant="outlined"
              size="large"
              sx={{
                borderColor: 'white',
                color: 'white',
                px: 5,
                py: 2,
                fontSize: '1.1rem',
                fontWeight: 600,
                '&:hover': {
                  borderColor: 'white',
                  bgcolor: 'rgba(255,255,255,0.1)',
                },
              }}
            >
              Learn More
            </Button>
          </Box>
        </Box>
      </Container>

      {/* Stats Section */}
      <Container maxWidth="lg">
        <Grid container spacing={4} sx={{ mb: 8 }}>
          {[
            { label: 'Active Jobs', value: '10,000+' },
            { label: 'Companies', value: '500+' },
            { label: 'Successful Matches', value: '50,000+' },
            { label: 'Success Rate', value: '95%' },
          ].map((stat, index) => (
            <Grid item xs={6} md={3} key={index}>
              <Card
                sx={{
                  textAlign: 'center',
                  py: 3,
                  background: 'rgba(255,255,255,0.1)',
                  backdropFilter: 'blur(10px)',
                  color: 'white',
                  border: '1px solid rgba(255,255,255,0.2)',
                }}
              >
                <Typography variant="h3" sx={{ fontWeight: 700, mb: 1 }}>
                  {stat.value}
                </Typography>
                <Typography variant="body1">{stat.label}</Typography>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Container>

      {/* Features Section */}
      <Box sx={{ bgcolor: 'background.default', py: 10 }}>
        <Container maxWidth="lg">
          <Typography
            variant="h3"
            align="center"
            gutterBottom
            sx={{ fontWeight: 700, mb: 2, color: 'text.primary' }}
          >
            Why Choose JobApp?
          </Typography>
          <Typography
            variant="h6"
            align="center"
            sx={{ mb: 6, color: 'text.secondary', maxWidth: '600px', mx: 'auto' }}
          >
            We leverage cutting-edge AI technology to make your job search smarter, faster, and more effective.
          </Typography>

          <Grid container spacing={4}>
            {features.map((feature, index) => (
              <Grid item xs={12} md={4} key={index}>
                <Card
                  sx={{
                    height: '100%',
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    p: 3,
                    textAlign: 'center',
                    transition: 'transform 0.3s, box-shadow 0.3s',
                    '&:hover': {
                      transform: 'translateY(-8px)',
                      boxShadow: '0 12px 24px rgba(0,0,0,0.15)',
                    },
                  }}
                >
                  <Box sx={{ mb: 2 }}>{feature.icon}</Box>
                  <CardContent>
                    <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
                      {feature.title}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      {feature.description}
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Container>
      </Box>

      {/* CTA Section */}
      <Box sx={{ py: 10, textAlign: 'center', color: 'white' }}>
        <Container maxWidth="md">
          <Typography variant="h3" gutterBottom sx={{ fontWeight: 700 }}>
            Ready to Transform Your Career?
          </Typography>
          <Typography variant="h6" sx={{ mb: 4, opacity: 0.9 }}>
            Join thousands of professionals who found their perfect match with JobApp.
          </Typography>
          <Button
            variant="contained"
            size="large"
            onClick={() => navigate('/register')}
            sx={{
              bgcolor: 'white',
              color: 'primary.main',
              px: 6,
              py: 2.5,
              fontSize: '1.2rem',
              fontWeight: 600,
              '&:hover': { bgcolor: 'grey.100' },
            }}
          >
            Start Your Journey Today
          </Button>
        </Container>
      </Box>
    </Box>
  )
}

export default LandingPage

