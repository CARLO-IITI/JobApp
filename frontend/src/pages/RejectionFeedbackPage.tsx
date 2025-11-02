import React, { useEffect, useState } from 'react'
import {
  Box,
  Card,
  CardContent,
  Typography,
  Chip,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Alert,
  LinearProgress,
  Divider,
  Button,
} from '@mui/material'
import { useParams, useNavigate } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { RootState } from '../store/store'
import { applicationService } from '../services/applicationService'
import { jobService } from '../services/jobService'
import { profileService } from '../services/profileService'
import { matchingService } from '../services/matchingService'
import CheckCircleIcon from '@mui/icons-material/CheckCircle'
import ErrorIcon from '@mui/icons-material/Error'
import LightbulbIcon from '@mui/icons-material/Lightbulb'
import SchoolIcon from '@mui/icons-material/School'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import FavoriteIcon from '@mui/icons-material/Favorite'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'

const RejectionFeedbackPage: React.FC = () => {
  const { applicationId } = useParams<{ applicationId: string }>()
  const navigate = useNavigate()
  const { user } = useSelector((state: RootState) => state.auth)
  const [analysis, setAnalysis] = useState<any>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadAnalysis()
  }, [])

  const loadAnalysis = async () => {
    try {
      setLoading(true)
      
      // Get application details
      const appResponse = await applicationService.getApplicationById(Number(applicationId))
      const application = appResponse.data

      // Get job details
      const jobResponse = await jobService.getJobById(application.jobId)
      const job = jobResponse.data

      // Get candidate profile
      const profileResponse = await profileService.getCandidateProfile(user!.id)
      const profile = profileResponse.data

      // Get AI analysis
      const analysisResponse = await matchingService.analyzeRejection(
        {
          skills: profile.skills || [],
          yearsOfExperience: profile.yearsOfExperience || 0,
          expectedSalary: profile.expectedSalary,
          currentLocation: profile.currentLocation,
        },
        {
          requiredSkills: job.requiredSkills || [],
          minExperience: job.minExperience,
          maxExperience: job.maxExperience,
          maxSalary: job.maxSalary,
          location: job.location,
        }
      )

      if (analysisResponse.success) {
        setAnalysis({
          ...analysisResponse.data,
          jobTitle: job.title,
          companyName: job.companyName,
        })
      }
    } catch (error) {
      console.error('Error loading analysis:', error)
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return <LinearProgress />
  }

  if (!analysis) {
    return (
      <Box textAlign="center" py={8}>
        <Typography>Unable to load feedback</Typography>
      </Box>
    )
  }

  const getScoreColor = (score: number) => {
    if (score >= 0.8) return 'success.main'
    if (score >= 0.6) return 'warning.main'
    return 'error.main'
  }

  return (
    <Box>
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/applications')}
        sx={{ mb: 3 }}
      >
        Back to Applications
      </Button>

      <Typography variant="h4" sx={{ fontWeight: 700, mb: 1 }}>
        Application Feedback & Analysis
      </Typography>
      <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
        {analysis.jobTitle} at {analysis.companyName}
      </Typography>

      {/* Overall Score */}
      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Box sx={{ textAlign: 'center', py: 2 }}>
            <Typography variant="h6" color="text.secondary" gutterBottom>
              Overall Match Score
            </Typography>
            <Typography 
              variant="h2" 
              sx={{ 
                fontWeight: 700, 
                color: getScoreColor(analysis.overallMatchScore),
                mb: 1 
              }}
            >
              {Math.round(analysis.overallMatchScore * 100)}%
            </Typography>
            <Typography variant="body1" color="text.secondary">
              {analysis.summary}
            </Typography>
          </Box>
        </CardContent>
      </Card>

      {/* Strengths */}
      {analysis.strengths && analysis.strengths.length > 0 && (
        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Typography variant="h6" sx={{ fontWeight: 600, mb: 2, display: 'flex', alignItems: 'center' }}>
              <CheckCircleIcon sx={{ mr: 1, color: 'success.main' }} />
              Your Strengths
            </Typography>
            <List>
              {analysis.strengths.map((strength: string, index: number) => (
                <ListItem key={index}>
                  <ListItemIcon>
                    <CheckCircleIcon color="success" />
                  </ListItemIcon>
                  <ListItemText primary={strength} />
                </ListItem>
              ))}
            </List>
          </CardContent>
        </Card>
      )}

      {/* Missing Skills */}
      {analysis.missingSkills && analysis.missingSkills.length > 0 && (
        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Typography variant="h6" sx={{ fontWeight: 600, mb: 2, display: 'flex', alignItems: 'center' }}>
              <ErrorIcon sx={{ mr: 1, color: 'error.main' }} />
              Skills Gap Analysis
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
              The following skills were required but not found in your profile:
            </Typography>
            <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap', mb: 2 }}>
              {analysis.missingSkills.map((skill: string) => (
                <Chip key={skill} label={skill} color="error" variant="outlined" />
              ))}
            </Box>
          </CardContent>
        </Card>
      )}

      {/* Areas for Improvement */}
      {analysis.areasForImprovement && analysis.areasForImprovement.length > 0 && (
        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Typography variant="h6" sx={{ fontWeight: 600, mb: 2, display: 'flex', alignItems: 'center' }}>
              <TrendingUpIcon sx={{ mr: 1, color: 'warning.main' }} />
              Areas for Improvement
            </Typography>
            <List>
              {analysis.areasForImprovement.map((area: string, index: number) => (
                <ListItem key={index}>
                  <ListItemIcon>
                    <TrendingUpIcon color="warning" />
                  </ListItemIcon>
                  <ListItemText primary={area} />
                </ListItem>
              ))}
            </List>
          </CardContent>
        </Card>
      )}

      {/* Experience Gap */}
      {analysis.experienceGapAnalysis && (
        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
              Experience Analysis
            </Typography>
            <Typography variant="body1">{analysis.experienceGapAnalysis}</Typography>
          </CardContent>
        </Card>
      )}

      {/* Recommendations */}
      {analysis.recommendations && analysis.recommendations.length > 0 && (
        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Typography variant="h6" sx={{ fontWeight: 600, mb: 2, display: 'flex', alignItems: 'center' }}>
              <LightbulbIcon sx={{ mr: 1, color: 'primary.main' }} />
              Personalized Recommendations
            </Typography>
            <List>
              {analysis.recommendations.map((rec: string, index: number) => (
                <ListItem key={index}>
                  <ListItemIcon>
                    <LightbulbIcon color="primary" />
                  </ListItemIcon>
                  <ListItemText primary={rec} />
                </ListItem>
              ))}
            </List>
          </CardContent>
        </Card>
      )}

      {/* Suggested Courses */}
      {analysis.suggestedCourses && analysis.suggestedCourses.length > 0 && (
        <Card sx={{ mb: 3 }}>
          <CardContent>
            <Typography variant="h6" sx={{ fontWeight: 600, mb: 2, display: 'flex', alignItems: 'center' }}>
              <SchoolIcon sx={{ mr: 1, color: 'info.main' }} />
              Recommended Learning Resources
            </Typography>
            <List>
              {analysis.suggestedCourses.map((course: string, index: number) => (
                <ListItem key={index}>
                  <ListItemIcon>
                    <SchoolIcon color="info" />
                  </ListItemIcon>
                  <ListItemText 
                    primary={course.split(':')[0]} 
                    secondary={course.split(':')[1] || course}
                  />
                </ListItem>
              ))}
            </List>
          </CardContent>
        </Card>
      )}

      {/* Encouraging Message */}
      <Card sx={{ background: 'linear-gradient(135deg, #0F2B46 0%, #10B981 100%)', color: 'white' }}>
        <CardContent>
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
            <FavoriteIcon sx={{ mr: 1, fontSize: 32 }} />
            <Typography variant="h6" sx={{ fontWeight: 600 }}>
              Keep Going - You've Got This!
            </Typography>
          </Box>
          <Typography variant="body1" sx={{ mb: 2 }}>
            {analysis.encouragingMessage}
          </Typography>
          <Button
            variant="contained"
            onClick={() => navigate('/jobs')}
            sx={{ bgcolor: 'white', color: 'primary.main', '&:hover': { bgcolor: 'grey.100' } }}
          >
            Find More Opportunities
          </Button>
        </CardContent>
      </Card>
    </Box>
  )
}

export default RejectionFeedbackPage

