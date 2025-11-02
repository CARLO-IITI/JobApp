import React, { useEffect, useState } from 'react'
import {
  Box,
  Card,
  CardContent,
  Typography,
  LinearProgress,
  Chip,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Collapse,
  Button,
  Alert,
} from '@mui/material'
import { matchingService } from '../services/matchingService'
import { profileService } from '../services/profileService'
import CheckCircleIcon from '@mui/icons-material/CheckCircle'
import ErrorIcon from '@mui/icons-material/Error'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import PsychologyIcon from '@mui/icons-material/Psychology'
import ExpandMoreIcon from '@mui/icons-material/ExpandMore'
import ExpandLessIcon from '@mui/icons-material/ExpandLess'
import { useSelector } from 'react-redux'
import { RootState } from '../store/store'

interface MatchIndicatorProps {
  job: any
}

const MatchIndicator: React.FC<MatchIndicatorProps> = ({ job }) => {
  const { user } = useSelector((state: RootState) => state.auth)
  const [prediction, setPrediction] = useState<any>(null)
  const [loading, setLoading] = useState(true)
  const [expanded, setExpanded] = useState(false)

  useEffect(() => {
    if (user?.id && user?.role === 'CANDIDATE') {
      calculateMatch()
    } else {
      setLoading(false)
    }
  }, [job, user])

  const calculateMatch = async () => {
    try {
      setLoading(true)
      
      // Get candidate profile
      const profileResp = await profileService.getCandidateProfile(user!.id)
      const profile = profileResp.data

      // Get match prediction
      const predictionResp = await matchingService.predictJobMatch(
        {
          skills: profile?.skills || [],
          yearsOfExperience: profile?.yearsOfExperience || 0,
          education: profile?.education,
          currentLocation: profile?.currentLocation,
          expectedSalary: profile?.expectedSalary,
        },
        job
      )

      if (predictionResp.success) {
        setPrediction(predictionResp.data)
      }
    } catch (error) {
      console.error('Error calculating match:', error)
    } finally {
      setLoading(false)
    }
  }

  if (!user || user.role !== 'CANDIDATE') {
    return null
  }

  if (loading) {
    return (
      <Card sx={{ mb: 3 }}>
        <CardContent>
          <LinearProgress />
          <Typography variant="body2" sx={{ mt: 1, textAlign: 'center' }}>
            AI is analyzing your match...
          </Typography>
        </CardContent>
      </Card>
    )
  }

  if (!prediction) {
    return null
  }

  const getMatchColor = (score: number) => {
    if (score >= 0.85) return 'success.main'
    if (score >= 0.70) return 'primary.main'
    if (score >= 0.50) return 'warning.main'
    return 'error.main'
  }

  const getProbabilityColor = (prob: number) => {
    if (prob >= 0.70) return 'success'
    if (prob >= 0.50) return 'info'
    if (prob >= 0.30) return 'warning'
    return 'error'
  }

  return (
    <Card sx={{ mb: 3, border: 2, borderColor: getMatchColor(prediction.overallMatchScore) }}>
      <CardContent>
        {/* Header */}
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          <PsychologyIcon sx={{ fontSize: 32, color: 'primary.main', mr: 1 }} />
          <Typography variant="h6" sx={{ fontWeight: 600 }}>
            AI Match Analysis
          </Typography>
        </Box>

        {/* Main Score */}
        <Box sx={{ textAlign: 'center', mb: 3, p: 3, bgcolor: 'grey.50', borderRadius: 2 }}>
          <Typography variant="body2" color="text.secondary" gutterBottom>
            Your Hiring Probability
          </Typography>
          <Typography 
            variant="h2" 
            sx={{ fontWeight: 700, color: getMatchColor(prediction.hiringProbability), mb: 1 }}
          >
            {Math.round(prediction.hiringProbability * 100)}%
          </Typography>
          <Chip
            label={prediction.matchLevel}
            color={getProbabilityColor(prediction.hiringProbability)}
            sx={{ fontWeight: 600, mb: 1 }}
          />
          <Typography variant="body2" color="text.secondary">
            {prediction.recommendation}
          </Typography>
        </Box>

        {/* Quick Stats */}
        <Box sx={{ display: 'flex', justifyContent: 'space-around', mb: 2 }}>
          <Box sx={{ textAlign: 'center' }}>
            <Typography variant="h6" color="primary.main">
              {Math.round(prediction.overallMatchScore * 100)}%
            </Typography>
            <Typography variant="caption" color="text.secondary">
              Overall Match
            </Typography>
          </Box>
          <Box sx={{ textAlign: 'center' }}>
            <Typography variant="h6" color="secondary.main">
              #{prediction.yourPredictedRank}
            </Typography>
            <Typography variant="caption" color="text.secondary">
              Predicted Rank
            </Typography>
          </Box>
          <Box sx={{ textAlign: 'center' }}>
            <Chip 
              label={prediction.competitionLevel} 
              size="small"
              color={prediction.competitionLevel === 'LOW' ? 'success' : 
                     prediction.competitionLevel === 'MEDIUM' ? 'warning' : 'error'}
            />
            <Typography variant="caption" color="text.secondary" sx={{ display: 'block' }}>
              Competition
            </Typography>
          </Box>
        </Box>

        {/* Skills Match */}
        <Box sx={{ mb: 2 }}>
          <Typography variant="subtitle2" sx={{ fontWeight: 600, mb: 1 }}>
            Skills Analysis:
          </Typography>
          <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
            {prediction.matchingSkills?.map((skill: string) => (
              <Chip key={skill} label={skill} color="success" size="small" icon={<CheckCircleIcon />} />
            ))}
            {prediction.missingSkills?.slice(0, 3).map((skill: string) => (
              <Chip key={skill} label={skill} color="error" size="small" variant="outlined" icon={<ErrorIcon />} />
            ))}
          </Box>
        </Box>

        {/* Application Advice */}
        <Alert severity={prediction.hiringProbability >= 0.5 ? 'success' : 'warning'} sx={{ mb: 2 }}>
          <Typography variant="body2" sx={{ fontWeight: 600 }}>
            {prediction.applicationAdvice}
          </Typography>
        </Alert>

        {/* Expandable Details */}
        <Button
          fullWidth
          onClick={() => setExpanded(!expanded)}
          endIcon={expanded ? <ExpandLessIcon /> : <ExpandMoreIcon />}
          sx={{ textTransform: 'none' }}
        >
          {expanded ? 'Hide Detailed Analysis' : 'Show Detailed Analysis'}
        </Button>

        <Collapse in={expanded}>
          <Box sx={{ mt: 2 }}>
            {/* Score Breakdown */}
            <Typography variant="subtitle2" sx={{ fontWeight: 600, mb: 1 }}>
              Score Breakdown:
            </Typography>
            {Object.entries(prediction.scoreBreakdown || {}).map(([criterion, score]: [string, any]) => (
              <Box key={criterion} sx={{ mb: 1 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 0.5 }}>
                  <Typography variant="caption">{criterion}</Typography>
                  <Typography variant="caption" sx={{ fontWeight: 600 }}>
                    {Math.round(score * 100)}%
                  </Typography>
                </Box>
                <LinearProgress 
                  variant="determinate" 
                  value={score * 100} 
                  sx={{ 
                    height: 6, 
                    borderRadius: 1,
                    bgcolor: 'grey.200',
                    '& .MuiLinearProgress-bar': {
                      bgcolor: score >= 0.7 ? 'success.main' : score >= 0.5 ? 'warning.main' : 'error.main'
                    }
                  }}
                />
              </Box>
            ))}

            {/* Strength Areas */}
            {prediction.strengthAreas && prediction.strengthAreas.length > 0 && (
              <Box sx={{ mt: 2 }}>
                <Typography variant="subtitle2" sx={{ fontWeight: 600, mb: 1 }}>
                  Your Strengths:
                </Typography>
                <List dense>
                  {prediction.strengthAreas.map((strength: string, idx: number) => (
                    <ListItem key={idx}>
                      <ListItemIcon>
                        <CheckCircleIcon color="success" fontSize="small" />
                      </ListItemIcon>
                      <ListItemText primary={strength} />
                    </ListItem>
                  ))}
                </List>
              </Box>
            )}

            {/* Quick Wins */}
            {prediction.quickWins && prediction.quickWins.length > 0 && (
              <Box sx={{ mt: 2 }}>
                <Typography variant="subtitle2" sx={{ fontWeight: 600, mb: 1 }}>
                  Quick Tips to Improve Your Chances:
                </Typography>
                <List dense>
                  {prediction.quickWins.map((tip: string, idx: number) => (
                    <ListItem key={idx}>
                      <ListItemIcon>
                        <TrendingUpIcon color="primary" fontSize="small" />
                      </ListItemIcon>
                      <ListItemText primary={tip} />
                    </ListItem>
                  ))}
                </List>
              </Box>
            )}

            {/* Related Skills */}
            {prediction.relatedSkills && prediction.relatedSkills.length > 0 && (
              <Box sx={{ mt: 2 }}>
                <Typography variant="subtitle2" sx={{ fontWeight: 600, mb: 1, color: 'success.main' }}>
                  💡 You have related skills that help!
                </Typography>
                <Typography variant="caption" color="text.secondary">
                  {prediction.relatedSkills.join(' • ')}
                </Typography>
              </Box>
            )}
          </Box>
        </Collapse>
      </CardContent>
    </Card>
  )
}

export default MatchIndicator

