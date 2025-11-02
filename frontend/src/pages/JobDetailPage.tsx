import React, { useEffect, useState } from 'react'
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  Chip,
  Grid,
  Divider,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  LinearProgress,
} from '@mui/material'
import { useParams, useNavigate } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { RootState } from '../store/store'
import { jobService } from '../services/jobService'
import { applicationService } from '../services/applicationService'
import LocationOnIcon from '@mui/icons-material/LocationOn'
import BusinessIcon from '@mui/icons-material/Business'
import WorkIcon from '@mui/icons-material/Work'
import AttachMoneyIcon from '@mui/icons-material/AttachMoney'
import CalendarTodayIcon from '@mui/icons-material/CalendarToday'
import PeopleIcon from '@mui/icons-material/People'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { toast } from 'react-toastify'
import MatchIndicator from '../components/MatchIndicator'

const JobDetailPage: React.FC = () => {
  const { jobId } = useParams<{ jobId: string }>()
  const navigate = useNavigate()
  const { user } = useSelector((state: RootState) => state.auth)
  const [job, setJob] = useState<any>(null)
  const [loading, setLoading] = useState(true)
  const [applyDialogOpen, setApplyDialogOpen] = useState(false)
  const [coverLetter, setCoverLetter] = useState('')
  const [applying, setApplying] = useState(false)

  useEffect(() => {
    fetchJobDetails()
  }, [jobId])

  const fetchJobDetails = async () => {
    try {
      setLoading(true)
      const response = await jobService.getJobById(Number(jobId))
      if (response.success) {
        setJob(response.data)
      }
    } catch (error) {
      console.error('Error fetching job:', error)
      toast.error('Failed to load job details')
      navigate('/jobs')
    } finally {
      setLoading(false)
    }
  }

  const handleApply = async () => {
    if (!user?.id) {
      toast.error('Please login to apply')
      return
    }

    try {
      setApplying(true)
      const response = await applicationService.applyForJob({
        jobId: Number(jobId),
        candidateId: user.id,
        coverLetter,
      })

      if (response.success) {
        toast.success('Application submitted successfully!')
        setApplyDialogOpen(false)
        setCoverLetter('')
      }
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Failed to submit application'
      toast.error(errorMessage)
    } finally {
      setApplying(false)
    }
  }

  if (loading) {
    return <LinearProgress />
  }

  if (!job) {
    return (
      <Box textAlign="center" py={8}>
        <Typography variant="h6">Job not found</Typography>
      </Box>
    )
  }

  return (
    <Box>
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/jobs')}
        sx={{ mb: 3 }}
      >
        Back to Jobs
      </Button>

      {/* AI Match Indicator for Candidates */}
      {user?.role === 'CANDIDATE' && job && (
        <MatchIndicator job={job} />
      )}

      <Grid container spacing={3}>
        {/* Main Content */}
        <Grid item xs={12} md={8}>
          <Card>
            <CardContent>
              <Box sx={{ mb: 3 }}>
                <Typography variant="h4" sx={{ fontWeight: 700, mb: 2 }}>
                  {job.title}
                </Typography>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, flexWrap: 'wrap', mb: 2 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <BusinessIcon sx={{ mr: 1, color: 'primary.main' }} />
                    <Typography variant="h6" color="primary">
                      {job.companyName}
                    </Typography>
                  </Box>
                  <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <LocationOnIcon sx={{ mr: 0.5, color: 'text.secondary' }} />
                    <Typography color="text.secondary">{job.location}</Typography>
                  </Box>
                </Box>
                <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
                  <Chip label={job.jobType} color="primary" />
                  <Chip label={job.experienceLevel} />
                  {job.remoteAllowed && <Chip label="Remote" color="success" />}
                </Box>
              </Box>

              <Divider sx={{ my: 3 }} />

              <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                Job Description
              </Typography>
              <Typography variant="body1" paragraph sx={{ whiteSpace: 'pre-line' }}>
                {job.description}
              </Typography>

              <Divider sx={{ my: 3 }} />

              {job.responsibilities && (
                <>
                  <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                    Responsibilities
                  </Typography>
                  <Typography variant="body1" paragraph>
                    {job.responsibilities}
                  </Typography>
                  <Divider sx={{ my: 3 }} />
                </>
              )}

              <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                Required Skills
              </Typography>
              <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap', mb: 3 }}>
                {job.requiredSkills?.map((skill: string, index: number) => (
                  <Chip key={index} label={skill} variant="outlined" />
                ))}
              </Box>

              {job.qualifications && (
                <>
                  <Divider sx={{ my: 3 }} />
                  <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                    Qualifications
                  </Typography>
                  <Typography variant="body1" paragraph>
                    {job.qualifications}
                  </Typography>
                </>
              )}

              {job.benefits && (
                <>
                  <Divider sx={{ my: 3 }} />
                  <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                    Benefits
                  </Typography>
                  <Typography variant="body1" paragraph>
                    {job.benefits}
                  </Typography>
                </>
              )}
            </CardContent>
          </Card>
        </Grid>

        {/* Sidebar */}
        <Grid item xs={12} md={4}>
          <Card sx={{ position: 'sticky', top: 20 }}>
            <CardContent>
              {user?.role === 'CANDIDATE' && (
                <Button
                  fullWidth
                  variant="contained"
                  size="large"
                  onClick={() => setApplyDialogOpen(true)}
                  sx={{ mb: 3 }}
                >
                  Apply Now
                </Button>
              )}

              <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                Job Information
              </Typography>

              <Box sx={{ mb: 2 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                  <AttachMoneyIcon sx={{ mr: 1, color: 'text.secondary' }} />
                  <Typography variant="body2" sx={{ fontWeight: 600 }}>
                    Salary Range
                  </Typography>
                </Box>
                <Typography variant="body2" color="text.secondary">
                  {job.minSalary && job.maxSalary
                    ? `${job.currency} ${job.minSalary.toLocaleString()} - ${job.maxSalary.toLocaleString()}`
                    : 'Not specified'}
                </Typography>
              </Box>

              <Divider sx={{ my: 2 }} />

              <Box sx={{ mb: 2 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                  <WorkIcon sx={{ mr: 1, color: 'text.secondary' }} />
                  <Typography variant="body2" sx={{ fontWeight: 600 }}>
                    Experience
                  </Typography>
                </Box>
                <Typography variant="body2" color="text.secondary">
                  {job.minExperience || 0} - {job.maxExperience || '∞'} years
                </Typography>
              </Box>

              <Divider sx={{ my: 2 }} />

              <Box sx={{ mb: 2 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                  <PeopleIcon sx={{ mr: 1, color: 'text.secondary' }} />
                  <Typography variant="body2" sx={{ fontWeight: 600 }}>
                    Openings
                  </Typography>
                </Box>
                <Typography variant="body2" color="text.secondary">
                  {job.openings} position{job.openings > 1 ? 's' : ''}
                </Typography>
              </Box>

              <Divider sx={{ my: 2 }} />

              <Box>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                  <CalendarTodayIcon sx={{ mr: 1, color: 'text.secondary' }} />
                  <Typography variant="body2" sx={{ fontWeight: 600 }}>
                    Posted Date
                  </Typography>
                </Box>
                <Typography variant="body2" color="text.secondary">
                  {new Date(job.createdAt).toLocaleDateString()}
                </Typography>
              </Box>

              <Divider sx={{ my: 2 }} />

              <Typography variant="caption" color="text.secondary">
                {job.viewCount} views • {job.applicationCount} applications
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Apply Dialog */}
      <Dialog
        open={applyDialogOpen}
        onClose={() => setApplyDialogOpen(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>Apply for {job.title}</DialogTitle>
        <DialogContent>
          <TextField
            fullWidth
            multiline
            rows={6}
            label="Cover Letter (Optional)"
            value={coverLetter}
            onChange={(e) => setCoverLetter(e.target.value)}
            placeholder="Tell us why you're a great fit for this role..."
            sx={{ mt: 2 }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setApplyDialogOpen(false)}>Cancel</Button>
          <Button
            variant="contained"
            onClick={handleApply}
            disabled={applying}
          >
            {applying ? 'Submitting...' : 'Submit Application'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  )
}

export default JobDetailPage

