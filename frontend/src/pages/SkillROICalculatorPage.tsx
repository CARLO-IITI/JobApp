import React, { useState, useEffect } from 'react'
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Chip,
  LinearProgress,
  Alert,
} from '@mui/material'
import { useSelector } from 'react-redux'
import { RootState } from '../store/store'
import { matchingService } from '../services/matchingService'
import { profileService } from '../services/profileService'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import AttachMoneyIcon from '@mui/icons-material/AttachMoney'
import WorkIcon from '@mui/icons-material/Work'
import SchoolIcon from '@mui/icons-material/School'
import { toast } from 'react-toastify'

const SkillROICalculatorPage: React.FC = () => {
  const { user } = useSelector((state: RootState) => state.auth)
  const [roiData, setRoiData] = useState<any[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    calculateROI()
  }, [])

  const calculateROI = async () => {
    if (!user?.id) return

    try {
      setLoading(true)
      
      // Get user profile
      const profileResp = await profileService.getCandidateProfile(user.id)
      const profile = profileResp.data

      // Skills to analyze
      const potentialSkills = [
        'Kubernetes', 'AWS', 'React', 'Python', 'Machine Learning',
        'TypeScript', 'Docker', 'Go', 'GraphQL', 'Next.js'
      ]

      // Calculate ROI
      const roiResp = await matchingService.calculateSkillROI({
        potentialSkills,
        currentSkills: profile?.skills || [],
        currentMatchingJobs: 50,
        currentAvgSalary: profile?.expectedSalary || 100000,
      })

      if (roiResp.success) {
        setRoiData(roiResp.data)
      }
    } catch (error) {
      console.error('Error calculating ROI:', error)
      toast.error('Failed to calculate skill ROI')
    } finally {
      setLoading(false)
    }
  }

  const getPriorityColor = (priority: string) => {
    switch (priority) {
      case 'HIGHEST': return 'error'
      case 'HIGH': return 'warning'
      case 'MEDIUM': return 'info'
      default: return 'default'
    }
  }

  return (
    <Box>
      <Typography variant="h4" sx={{ fontWeight: 700, mb: 1 }}>
        💡 Skill ROI Calculator
      </Typography>
      <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
        Discover which skills to learn for maximum career impact
      </Typography>

      <Alert severity="info" sx={{ mb: 3 }}>
        <Typography variant="body2">
          AI analyzes market data to show you exact ROI for learning each skill. Higher ROI = Better investment!
        </Typography>
      </Alert>

      {loading && <LinearProgress sx={{ mb: 3 }} />}

      {!loading && roiData.length === 0 && (
        <Card>
          <CardContent sx={{ textAlign: 'center', py: 6 }}>
            <Typography color="text.secondary">
              Complete your profile to see personalized skill recommendations
            </Typography>
          </CardContent>
        </Card>
      )}

      {roiData.length > 0 && (
        <Card>
          <CardContent>
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Priority</TableCell>
                    <TableCell>Skill</TableCell>
                    <TableCell>Additional Jobs</TableCell>
                    <TableCell>Salary Increase</TableCell>
                    <TableCell>Learning Time</TableCell>
                    <TableCell>ROI</TableCell>
                    <TableCell>Trend</TableCell>
                    <TableCell>Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {roiData.map((skill) => (
                    <TableRow key={skill.skillName}>
                      <TableCell>
                        <Chip
                          label={skill.priority}
                          color={getPriorityColor(skill.priority)}
                          size="small"
                          sx={{ fontWeight: 600 }}
                        />
                      </TableCell>
                      <TableCell>
                        <Typography variant="body2" sx={{ fontWeight: 600 }}>
                          {skill.skillName}
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                          <WorkIcon sx={{ fontSize: 16, mr: 0.5, color: 'primary.main' }} />
                          <Typography variant="body2">
                            +{skill.additionalJobs} jobs
                          </Typography>
                        </Box>
                      </TableCell>
                      <TableCell>
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                          <AttachMoneyIcon sx={{ fontSize: 16, mr: 0.5, color: 'success.main' }} />
                          <Typography variant="body2" color="success.main" sx={{ fontWeight: 600 }}>
                            +${skill.salaryIncrease.toLocaleString()}
                          </Typography>
                        </Box>
                      </TableCell>
                      <TableCell>
                        <Typography variant="body2">
                          {skill.learningTimeHours}hrs
                        </Typography>
                        <Typography variant="caption" color="text.secondary">
                          ({skill.learningDifficulty})
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Typography variant="h6" color="primary" sx={{ fontWeight: 700 }}>
                          ${Math.round(skill.roi)}/hr
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Chip
                          label={skill.marketTrend}
                          size="small"
                          color={skill.marketTrend === 'RISING' ? 'success' : 'default'}
                          icon={<TrendingUpIcon />}
                        />
                        <Typography variant="caption" color="text.secondary" sx={{ display: 'block' }}>
                          +{skill.demandGrowth}%
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Button
                          size="small"
                          variant="contained"
                          startIcon={<SchoolIcon />}
                        >
                          Learn
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </CardContent>
        </Card>
      )}
    </Box>
  )
}

export default SkillROICalculatorPage

