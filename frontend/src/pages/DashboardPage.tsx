import React, { useEffect, useState } from 'react'
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  Button,
  Chip,
  IconButton,
  Avatar,
  LinearProgress,
} from '@mui/material'
import { useNavigate } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { RootState } from '../store/store'
import { jobService } from '../services/jobService'
import { applicationService } from '../services/applicationService'
import WorkIcon from '@mui/icons-material/Work'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import CheckCircleIcon from '@mui/icons-material/CheckCircle'
import LocationOnIcon from '@mui/icons-material/LocationOn'
import BusinessCenterIcon from '@mui/icons-material/BusinessCenter'
import { toast } from 'react-toastify'

const DashboardPage: React.FC = () => {
  const navigate = useNavigate()
  const { user } = useSelector((state: RootState) => state.auth)
  const [latestJobs, setLatestJobs] = useState<any[]>([])
  const [stats, setStats] = useState({
    totalApplications: 0,
    activeJobs: 0,
    matchedJobs: 0,
  })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchDashboardData()
  }, [])

  const fetchDashboardData = async () => {
    try {
      setLoading(true)
      
      // Fetch latest jobs
      const jobsResponse = await jobService.getLatestJobs()
      if (jobsResponse.success) {
        setLatestJobs(jobsResponse.data || [])
      }

      // Fetch user statistics
      if (user?.role === 'CANDIDATE' && user.id) {
        const appsResponse = await applicationService.getApplicationsByCandidate(user.id, 0, 1)
        if (appsResponse.success) {
          setStats(prev => ({
            ...prev,
            totalApplications: appsResponse.data?.totalElements || 0,
          }))
        }
      }

      setStats(prev => ({
        ...prev,
        activeJobs: jobsResponse.data?.length || 0,
        matchedJobs: Math.floor((jobsResponse.data?.length || 0) * 0.7), // Mock value
      }))
    } catch (error: any) {
      console.error('Error fetching dashboard data:', error)
      toast.error('Failed to load dashboard data')
    } finally {
      setLoading(false)
    }
  }

  const StatCard = ({ icon, title, value, color }: any) => (
    <Card sx={{ height: '100%' }}>
      <CardContent>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <Avatar sx={{ bgcolor: color, mr: 2 }}>{icon}</Avatar>
          <Typography variant="h6" color="text.secondary">
            {title}
          </Typography>
        </Box>
        <Typography variant="h3" sx={{ fontWeight: 700 }}>
          {value}
        </Typography>
      </CardContent>
    </Card>
  )

  return (
    <Box>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" sx={{ fontWeight: 700, mb: 1 }}>
          Welcome back, {user?.fullName}! 👋
        </Typography>
        <Typography variant="body1" color="text.secondary">
          {user?.role === 'CANDIDATE'
            ? "Here's your job search overview"
            : "Here's your recruitment dashboard"}
        </Typography>
      </Box>

      {loading && <LinearProgress sx={{ mb: 3 }} />}

      {/* Statistics */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        {user?.role === 'CANDIDATE' ? (
          <>
            <Grid item xs={12} md={4}>
              <StatCard
                icon={<BusinessCenterIcon />}
                title="Applications"
                value={stats.totalApplications}
                color="primary.main"
              />
            </Grid>
            <Grid item xs={12} md={4}>
              <StatCard
                icon={<WorkIcon />}
                title="Active Jobs"
                value={stats.activeJobs}
                color="success.main"
              />
            </Grid>
            <Grid item xs={12} md={4}>
              <StatCard
                icon={<TrendingUpIcon />}
                title="Matched Jobs"
                value={stats.matchedJobs}
                color="info.main"
              />
            </Grid>
          </>
        ) : (
          <>
            <Grid item xs={12} md={4}>
              <StatCard
                icon={<WorkIcon />}
                title="Posted Jobs"
                value={stats.activeJobs}
                color="primary.main"
              />
            </Grid>
            <Grid item xs={12} md={4}>
              <StatCard
                icon={<CheckCircleIcon />}
                title="Applications"
                value={stats.totalApplications}
                color="success.main"
              />
            </Grid>
            <Grid item xs={12} md={4}>
              <StatCard
                icon={<TrendingUpIcon />}
                title="Hired"
                value="12"
                color="info.main"
              />
            </Grid>
          </>
        )}
      </Grid>

      {/* Latest Jobs */}
      <Box sx={{ mb: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography variant="h5" sx={{ fontWeight: 600 }}>
          Latest Job Opportunities
        </Typography>
        <Button variant="outlined" onClick={() => navigate('/jobs')}>
          View All Jobs
        </Button>
      </Box>

      <Grid container spacing={3}>
        {latestJobs.slice(0, 6).map((job) => (
          <Grid item xs={12} md={6} key={job.id}>
            <Card
              sx={{
                cursor: 'pointer',
                transition: 'all 0.3s',
                '&:hover': {
                  transform: 'translateY(-4px)',
                  boxShadow: 4,
                },
              }}
              onClick={() => navigate(`/jobs/${job.id}`)}
            >
              <CardContent>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                  <Typography variant="h6" sx={{ fontWeight: 600 }}>
                    {job.title}
                  </Typography>
                  <Chip
                    label={job.jobType}
                    color="primary"
                    size="small"
                    sx={{ fontWeight: 600 }}
                  />
                </Box>

                <Typography variant="subtitle1" color="primary" sx={{ mb: 1 }}>
                  {job.companyName}
                </Typography>

                <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                  <LocationOnIcon sx={{ fontSize: 18, mr: 0.5, color: 'text.secondary' }} />
                  <Typography variant="body2" color="text.secondary">
                    {job.location} {job.remoteAllowed && '• Remote'}
                  </Typography>
                </Box>

                <Box sx={{ mb: 2 }}>
                  {job.requiredSkills?.slice(0, 3).map((skill: string, index: number) => (
                    <Chip
                      key={index}
                      label={skill}
                      size="small"
                      sx={{ mr: 0.5, mb: 0.5 }}
                    />
                  ))}
                  {job.requiredSkills?.length > 3 && (
                    <Chip label={`+${job.requiredSkills.length - 3} more`} size="small" />
                  )}
                </Box>

                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <Typography variant="caption" color="text.secondary">
                    {new Date(job.createdAt).toLocaleDateString()}
                  </Typography>
                  <Button variant="contained" size="small">
                    View Details
                  </Button>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {latestJobs.length === 0 && !loading && (
        <Card sx={{ textAlign: 'center', py: 6 }}>
          <WorkIcon sx={{ fontSize: 64, color: 'text.secondary', mb: 2 }} />
          <Typography variant="h6" color="text.secondary">
            No jobs available yet
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
            Check back later for new opportunities
          </Typography>
        </Card>
      )}
    </Box>
  )
}

export default DashboardPage

