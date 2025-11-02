import React, { useEffect, useState } from 'react'
import {
  Box,
  Card,
  CardContent,
  Typography,
  Chip,
  LinearProgress,
  Grid,
  Button,
} from '@mui/material'
import { useNavigate } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { RootState } from '../store/store'
import { applicationService } from '../services/applicationService'
import WorkIcon from '@mui/icons-material/Work'
import CheckCircleIcon from '@mui/icons-material/CheckCircle'
import { toast } from 'react-toastify'

const ApplicationsPage: React.FC = () => {
  const navigate = useNavigate()
  const { user } = useSelector((state: RootState) => state.auth)
  const [applications, setApplications] = useState<any[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchApplications()
  }, [])

  const fetchApplications = async () => {
    if (!user?.id) return

    try {
      setLoading(true)
      const response = await applicationService.getApplicationsByCandidate(user.id, 0, 50)
      if (response.success) {
        setApplications(response.data?.content || [])
      }
    } catch (error) {
      console.error('Error fetching applications:', error)
      toast.error('Failed to load applications')
    } finally {
      setLoading(false)
    }
  }

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'SUBMITTED':
        return 'default'
      case 'REVIEWED':
        return 'info'
      case 'SHORTLISTED':
        return 'primary'
      case 'INTERVIEW_SCHEDULED':
        return 'warning'
      case 'ACCEPTED':
        return 'success'
      case 'REJECTED':
      case 'WITHDRAWN':
        return 'error'
      default:
        return 'default'
    }
  }

  return (
    <Box>
      <Typography variant="h4" sx={{ fontWeight: 700, mb: 3 }}>
        My Applications
      </Typography>

      {loading && <LinearProgress sx={{ mb: 3 }} />}

      {applications.length === 0 && !loading ? (
        <Card sx={{ textAlign: 'center', py: 8 }}>
          <WorkIcon sx={{ fontSize: 80, color: 'text.secondary', mb: 2 }} />
          <Typography variant="h6" color="text.secondary" sx={{ mb: 1 }}>
            No applications yet
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
            Start applying to jobs to see them here
          </Typography>
          <Button variant="contained" onClick={() => navigate('/jobs')}>
            Browse Jobs
          </Button>
        </Card>
      ) : (
        <Grid container spacing={3}>
          {applications.map((application) => (
            <Grid item xs={12} key={application.id}>
              <Card>
                <CardContent>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', mb: 2 }}>
                    <Box sx={{ flex: 1 }}>
                      <Typography variant="h6" sx={{ fontWeight: 600, mb: 1 }}>
                        Job Application #{application.id}
                      </Typography>
                      <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                        Applied on {new Date(application.appliedAt).toLocaleDateString()}
                      </Typography>
                      {application.matchScore && (
                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                          <CheckCircleIcon sx={{ fontSize: 18, color: 'success.main' }} />
                          <Typography variant="body2" color="success.main">
                            Match Score: {Math.round(application.matchScore * 100)}%
                          </Typography>
                        </Box>
                      )}
                    </Box>
                    <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: 1 }}>
                      <Chip
                        label={application.status.replace('_', ' ')}
                        color={getStatusColor(application.status)}
                        sx={{ fontWeight: 600 }}
                      />
                      {application.status === 'REJECTED' && (
                        <Button
                          size="small"
                          variant="outlined"
                          onClick={() => navigate(`/rejection-feedback/${application.id}`)}
                        >
                          View Feedback
                        </Button>
                      )}
                    </Box>
                  </Box>

                  {application.coverLetter && (
                    <Box sx={{ mt: 2, p: 2, bgcolor: 'grey.50', borderRadius: 1 }}>
                      <Typography variant="caption" color="text.secondary" sx={{ fontWeight: 600 }}>
                        Cover Letter:
                      </Typography>
                      <Typography variant="body2" sx={{ mt: 1 }}>
                        {application.coverLetter}
                      </Typography>
                    </Box>
                  )}

                  {application.recruiterNotes && (
                    <Box sx={{ mt: 2, p: 2, bgcolor: 'info.50', borderRadius: 1 }}>
                      <Typography variant="caption" color="info.main" sx={{ fontWeight: 600 }}>
                        Recruiter Notes:
                      </Typography>
                      <Typography variant="body2" sx={{ mt: 1 }}>
                        {application.recruiterNotes}
                      </Typography>
                    </Box>
                  )}
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
    </Box>
  )
}

export default ApplicationsPage

