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
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  MenuItem,
} from '@mui/material'
import { useNavigate } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { RootState } from '../store/store'
import { jobService } from '../services/jobService'
import { applicationService } from '../services/applicationService'
import { matchingService } from '../services/matchingService'
import { profileService } from '../services/profileService'
import WorkIcon from '@mui/icons-material/Work'
import VisibilityIcon from '@mui/icons-material/Visibility'
import AutoAwesomeIcon from '@mui/icons-material/AutoAwesome'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import { toast } from 'react-toastify'

const RecruiterApplicationsPage: React.FC = () => {
  const navigate = useNavigate()
  const { user } = useSelector((state: RootState) => state.auth)
  const [jobs, setJobs] = useState<any[]>([])
  const [selectedJob, setSelectedJob] = useState<any>(null)
  const [applications, setApplications] = useState<any[]>([])
  const [rankedApplications, setRankedApplications] = useState<any[]>([])
  const [loading, setLoading] = useState(true)
  const [ranking, setRanking] = useState(false)
  const [dialogOpen, setDialogOpen] = useState(false)
  const [selectedApp, setSelectedApp] = useState<any>(null)
  const [newStatus, setNewStatus] = useState('')
  const [notes, setNotes] = useState('')
  const [authenticityData, setAuthenticityData] = useState<Map<number, any>>(new Map())

  useEffect(() => {
    if (user?.role === 'RECRUITER') {
      fetchRecruiterJobs()
    }
  }, [])

  const fetchRecruiterJobs = async () => {
    if (!user?.id) return

    try {
      setLoading(true)
      const response = await jobService.getAllJobs({ page: 0, size: 100 })
      if (response.success) {
        // Filter jobs posted by this recruiter
        const recruiterJobs = (response.data?.content || []).filter(
          (job: any) => job.postedBy === user.id
        )
        setJobs(recruiterJobs)
      }
    } catch (error) {
      console.error('Error fetching jobs:', error)
      toast.error('Failed to load your jobs')
    } finally {
      setLoading(false)
    }
  }

  const fetchApplicationsForJob = async (jobId: number) => {
    try {
      setLoading(true)
      const response = await applicationService.getApplicationsByJob(jobId, 0, 100)
      const job = jobs.find(j => j.id === jobId)
      
      if (response.success) {
        const apps = response.data?.content || []
        setApplications(apps)
        setSelectedJob(job)
        
        // Auto-rank candidates with AI
        if (apps.length > 0 && job) {
          rankCandidatesWithAI(job, apps)
        }
      }
    } catch (error) {
      console.error('Error fetching applications:', error)
      toast.error('Failed to load applications')
    } finally {
      setLoading(false)
    }
  }

  const rankCandidatesWithAI = async (job: any, apps: any[]) => {
    try {
      setRanking(true)
      toast.info('🤖 AI is analyzing candidates...')
      
      // Fetch candidate profiles for all applicants
      const candidatesWithProfiles = await Promise.all(
        apps.map(async (app) => {
          try {
            const profileResp = await profileService.getCandidateProfile(app.candidateId)
            return {
              candidateId: app.candidateId,
              candidateName: `Candidate #${app.candidateId}`,
              skills: profileResp.data?.skills || [],
              yearsOfExperience: profileResp.data?.yearsOfExperience || 0,
              currentLocation: profileResp.data?.currentLocation,
              expectedSalary: profileResp.data?.expectedSalary,
              education: profileResp.data?.education,
              applicationId: app.id,
              appliedAt: app.appliedAt,
              status: app.status,
              coverLetter: app.coverLetter,
            }
          } catch (error) {
            console.error('Error fetching profile for candidate:', app.candidateId)
            return {
              candidateId: app.candidateId,
              candidateName: `Candidate #${app.candidateId}`,
              skills: [],
              yearsOfExperience: 0,
              applicationId: app.id,
              appliedAt: app.appliedAt,
              status: app.status,
            }
          }
        })
      )

      console.log('Candidates with profiles:', candidatesWithProfiles)

      // Use simple auto-scoring endpoint
      const scoreResponse = await matchingService.autoScoreApplications(
        {
          requiredSkills: job.requiredSkills || [],
          minExperience: job.minExperience,
          maxExperience: job.maxExperience,
          maxSalary: job.maxSalary,
          location: job.location,
          remoteAllowed: job.remoteAllowed,
        },
        candidatesWithProfiles
      )

      console.log('AI Scoring response:', scoreResponse)

      // Check authenticity for each candidate
      for (const candidate of candidatesWithProfiles) {
        try {
          const authResp = await matchingService.verifyAuthenticity({
            skills: candidate.skills,
            yearsOfExperience: candidate.yearsOfExperience,
            education: candidate.education,
            githubUrl: candidate.githubUrl || '',
            linkedinUrl: candidate.linkedinUrl || '',
            portfolioUrl: candidate.portfolioUrl || '',
          })
          if (authResp.success) {
            setAuthenticityData(prev => new Map(prev).set(candidate.candidateId, authResp.data))
          }
        } catch (error) {
          console.error('Error checking authenticity for candidate:', candidate.candidateId)
        }
      }

      if (scoreResponse.success && scoreResponse.data) {
        // Merge AI scores with application data
        const scored = scoreResponse.data.map((scoredApp: any) => {
          const original = apps.find(a => a.candidateId === scoredApp.candidateId)
          
          // Determine fit level based on score
          let fitLevel = 'WEAK'
          const score = scoredApp.overallScore || 0
          if (score >= 0.8) fitLevel = 'EXCELLENT'
          else if (score >= 0.65) fitLevel = 'GOOD'
          else if (score >= 0.45) fitLevel = 'MODERATE'
          
          // Generate recommendation
          let recommendation = '❌ NOT RECOMMENDED'
          if (score >= 0.85) recommendation = '🌟 HIGHLY RECOMMENDED'
          else if (score >= 0.7) recommendation = '✅ RECOMMENDED'
          else if (score >= 0.55) recommendation = '⚠️ CONSIDER'
          
          // Find matching skills
          const candidateSkills = scoredApp.skills || []
          const requiredSkills = job.requiredSkills || []
          const matchingSkills = candidateSkills.filter((cs: string) =>
            requiredSkills.some((rs: string) => rs.toLowerCase() === cs.toLowerCase())
          )
          const missingSkills = requiredSkills.filter((rs: string) =>
            !candidateSkills.some((cs: string) => cs.toLowerCase() === rs.toLowerCase())
          )

          return {
            ...original,
            ...scoredApp,
            fitLevel,
            recommendation,
            matchingSkills,
            missingSkills,
          }
        })
        
        setRankedApplications(scored)
        toast.success(`✨ ${scored.length} candidates ranked by AI! Top candidate: ${scored[0]?.candidateName || 'N/A'}`)
      } else {
        throw new Error('Scoring failed')
      }
    } catch (error) {
      console.error('Error ranking candidates:', error)
      toast.error('AI ranking unavailable - showing all applicants')
      setRankedApplications(apps)
    } finally {
      setRanking(false)
    }
  }

  const handleUpdateStatus = async () => {
    if (!selectedApp || !newStatus) return

    try {
      const response = await applicationService.updateApplicationStatus(
        selectedApp.id,
        newStatus,
        notes
      )
      if (response.success) {
        toast.success('Application status updated!')
        setDialogOpen(false)
        setNotes('')
        if (selectedJob) {
          fetchApplicationsForJob(selectedJob.id)
        }
      }
    } catch (error: any) {
      toast.error('Failed to update status')
    }
  }

  const openStatusDialog = (app: any) => {
    setSelectedApp(app)
    setNewStatus(app.status)
    setNotes(app.recruiterNotes || '')
    setDialogOpen(true)
  }

  const getStatusColor = (status: string): any => {
    switch (status) {
      case 'SUBMITTED': return 'default'
      case 'REVIEWED': return 'info'
      case 'SHORTLISTED': return 'primary'
      case 'INTERVIEW_SCHEDULED': return 'warning'
      case 'ACCEPTED': return 'success'
      case 'REJECTED':
      case 'WITHDRAWN': return 'error'
      default: return 'default'
    }
  }

  return (
    <Box>
      <Typography variant="h4" sx={{ fontWeight: 700, mb: 3 }}>
        Manage Applications
      </Typography>

      {loading && <LinearProgress sx={{ mb: 3 }} />}

      <Grid container spacing={3}>
        {/* Jobs List */}
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                Your Posted Jobs
              </Typography>
              {jobs.length === 0 && !loading ? (
                <Box sx={{ textAlign: 'center', py: 4 }}>
                  <WorkIcon sx={{ fontSize: 48, color: 'text.secondary', mb: 1 }} />
                  <Typography variant="body2" color="text.secondary">
                    No jobs posted yet
                  </Typography>
                  <Button
                    variant="contained"
                    onClick={() => navigate('/post-job')}
                    sx={{ mt: 2 }}
                  >
                    Post Your First Job
                  </Button>
                </Box>
              ) : (
                jobs.map((job) => (
                  <Card
                    key={job.id}
                    sx={{
                      mb: 2,
                      cursor: 'pointer',
                      border: selectedJob?.id === job.id ? 2 : 0,
                      borderColor: 'primary.main',
                      '&:hover': { boxShadow: 3 },
                    }}
                    onClick={() => fetchApplicationsForJob(job.id)}
                  >
                    <CardContent>
                      <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>
                        {job.title}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        {job.applicationCount} applications
                      </Typography>
                    </CardContent>
                  </Card>
                ))
              )}
            </CardContent>
          </Card>
        </Grid>

        {/* Applications List */}
        <Grid item xs={12} md={8}>
          {selectedJob ? (
            <Card>
              <CardContent>
                <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                  Applications for: {selectedJob.title}
                </Typography>

                {ranking && (
                  <Box sx={{ mb: 2 }}>
                    <LinearProgress />
                    <Typography variant="body2" color="primary" sx={{ mt: 1, display: 'flex', alignItems: 'center' }}>
                      <AutoAwesomeIcon sx={{ mr: 1, fontSize: 18 }} />
                      AI is analyzing and ranking candidates...
                    </Typography>
                  </Box>
                )}

                {applications.length === 0 ? (
                  <Box sx={{ textAlign: 'center', py: 6 }}>
                    <Typography variant="body1" color="text.secondary">
                      No applications yet for this job
                    </Typography>
                  </Box>
                ) : (
                  <TableContainer>
                    <Table>
                      <TableHead>
                        <TableRow>
                          <TableCell>Rank</TableCell>
                          <TableCell>Candidate</TableCell>
                          <TableCell>AI Match Score</TableCell>
                          <TableCell>Skills Match</TableCell>
                          <TableCell>Fit Level</TableCell>
                          <TableCell>🛡️ Authenticity</TableCell>
                          <TableCell>Actions</TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {(rankedApplications.length > 0 ? rankedApplications : applications).map((app, index) => {
                          const getFitColor = (fit: string) => {
                            switch (fit) {
                              case 'EXCELLENT': return 'success'
                              case 'GOOD': return 'primary'
                              case 'MODERATE': return 'warning'
                              default: return 'error'
                            }
                          }

                          return (
                            <TableRow 
                              key={app.applicationId || app.id}
                              sx={{
                                bgcolor: app.rank === 1 ? 'success.50' : 
                                        app.rank === 2 ? 'info.50' :
                                        app.rank === 3 ? 'warning.50' : 'inherit'
                              }}
                            >
                              <TableCell>
                                {app.rank ? (
                                  <Chip
                                    label={`#${app.rank}`}
                                    color={app.rank <= 3 ? 'primary' : 'default'}
                                    size="small"
                                    sx={{ fontWeight: 700 }}
                                  />
                                ) : (
                                  '-'
                                )}
                              </TableCell>
                              <TableCell>
                                <Typography variant="body2" sx={{ fontWeight: 600 }}>
                                  {app.candidateName || `Candidate #${app.candidateId}`}
                                </Typography>
                                <Typography variant="caption" color="text.secondary">
                                  Applied: {new Date(app.appliedAt).toLocaleDateString()}
                                </Typography>
                              </TableCell>
                              <TableCell>
                                {app.overallScore ? (
                                  <Box>
                                    <Typography 
                                      variant="h6" 
                                      sx={{ 
                                        fontWeight: 700,
                                        color: app.overallScore >= 0.7 ? 'success.main' :
                                               app.overallScore >= 0.5 ? 'warning.main' : 'error.main'
                                      }}
                                    >
                                      {Math.round(app.overallScore * 100)}%
                                    </Typography>
                                    <Typography variant="caption" color="text.secondary">
                                      {app.recommendation && app.recommendation.substring(0, 30)}...
                                    </Typography>
                                  </Box>
                                ) : (
                                  'Calculating...'
                                )}
                              </TableCell>
                              <TableCell>
                                {app.matchingSkills && app.matchingSkills.length > 0 ? (
                                  <Box>
                                    <Typography variant="body2" color="success.main">
                                      ✓ {app.matchingSkills.length} skills match
                                    </Typography>
                                    {app.missingSkills && app.missingSkills.length > 0 && (
                                      <Typography variant="caption" color="error.main">
                                        ✗ {app.missingSkills.length} skills missing
                                      </Typography>
                                    )}
                                  </Box>
                                ) : (
                                  '-'
                                )}
                              </TableCell>
                              <TableCell>
                                {app.fitLevel && (
                                  <Chip
                                    label={app.fitLevel}
                                    color={getFitColor(app.fitLevel)}
                                    size="small"
                                    sx={{ fontWeight: 600 }}
                                  />
                                )}
                              </TableCell>
                              <TableCell>
                                {authenticityData.get(app.candidateId) ? (
                                  <Box>
                                    <Typography 
                                      variant="body2" 
                                      sx={{ 
                                        fontWeight: 600,
                                        color: authenticityData.get(app.candidateId).trustLevel === 'HIGH' ? 'success.main' :
                                               authenticityData.get(app.candidateId).trustLevel === 'MEDIUM' ? 'warning.main' : 'error.main'
                                      }}
                                    >
                                      🛡️ {Math.round(authenticityData.get(app.candidateId).authenticityScore)}%
                                    </Typography>
                                    <Typography variant="caption" color="text.secondary">
                                      {authenticityData.get(app.candidateId).trustLevel} TRUST
                                    </Typography>
                                  </Box>
                                ) : (
                                  <Chip
                                    label={app.status?.replace('_', ' ') || 'SUBMITTED'}
                                    color={getStatusColor(app.status || 'SUBMITTED')}
                                    size="small"
                                  />
                                )}
                              </TableCell>
                              <TableCell>
                                <Button
                                  size="small"
                                  variant="contained"
                                  startIcon={<VisibilityIcon />}
                                  onClick={() => openStatusDialog(app)}
                                >
                                  Review
                                </Button>
                              </TableCell>
                            </TableRow>
                          )
                        })}
                      </TableBody>
                    </Table>
                  </TableContainer>
                )}
              </CardContent>
            </Card>
          ) : (
            <Card>
              <CardContent sx={{ textAlign: 'center', py: 8 }}>
                <WorkIcon sx={{ fontSize: 64, color: 'text.secondary', mb: 2 }} />
                <Typography variant="h6" color="text.secondary">
                  Select a job to view applications
                </Typography>
              </CardContent>
            </Card>
          )}
        </Grid>
      </Grid>

      {/* Update Status Dialog */}
      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Update Application Status</DialogTitle>
        <DialogContent>
          {selectedApp && (
                  <Box sx={{ pt: 2 }}>
              <Typography variant="body2" sx={{ mb: 2 }}>
                <strong>Candidate ID:</strong> {selectedApp.candidateId}
              </Typography>
              <Typography variant="body2" sx={{ mb: 2 }}>
                <strong>Applied:</strong> {new Date(selectedApp.appliedAt).toLocaleDateString()}
              </Typography>

              {/* Authenticity Score */}
              {authenticityData.get(selectedApp.candidateId) && (
                <Box sx={{ mb: 2, p: 2, bgcolor: 'grey.50', borderRadius: 1 }}>
                  <Typography variant="subtitle2" sx={{ fontWeight: 600, mb: 1 }}>
                    🛡️ Authenticity Analysis
                  </Typography>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 1 }}>
                    <Typography variant="h6" sx={{ 
                      color: authenticityData.get(selectedApp.candidateId).trustLevel === 'HIGH' ? 'success.main' : 
                             authenticityData.get(selectedApp.candidateId).trustLevel === 'MEDIUM' ? 'warning.main' : 'error.main'
                    }}>
                      {Math.round(authenticityData.get(selectedApp.candidateId).authenticityScore)}%
                    </Typography>
                    <Chip 
                      label={`${authenticityData.get(selectedApp.candidateId).trustLevel} TRUST`}
                      color={authenticityData.get(selectedApp.candidateId).trustLevel === 'HIGH' ? 'success' : 
                             authenticityData.get(selectedApp.candidateId).trustLevel === 'MEDIUM' ? 'warning' : 'error'}
                      size="small"
                    />
                  </Box>
                  
                  {authenticityData.get(selectedApp.candidateId).greenFlags?.length > 0 && (
                    <Box sx={{ mb: 1 }}>
                      <Typography variant="caption" sx={{ fontWeight: 600, color: 'success.main' }}>
                        ✅ Green Flags:
                      </Typography>
                      {authenticityData.get(selectedApp.candidateId).greenFlags.slice(0, 3).map((flag: string, idx: number) => (
                        <Typography key={idx} variant="caption" color="text.secondary" sx={{ display: 'block', ml: 2 }}>
                          • {flag}
                        </Typography>
                      ))}
                    </Box>
                  )}

                  {authenticityData.get(selectedApp.candidateId).redFlags?.length > 0 && (
                    <Box>
                      <Typography variant="caption" sx={{ fontWeight: 600, color: 'error.main' }}>
                        🚩 Red Flags:
                      </Typography>
                      {authenticityData.get(selectedApp.candidateId).redFlags.map((flag: string, idx: number) => (
                        <Typography key={idx} variant="caption" color="error" sx={{ display: 'block', ml: 2 }}>
                          • {flag}
                        </Typography>
                      ))}
                    </Box>
                  )}

                  <Typography variant="caption" color="text.secondary" sx={{ display: 'block', mt: 1, fontStyle: 'italic' }}>
                    {authenticityData.get(selectedApp.candidateId).recommendation}
                  </Typography>
                </Box>
              )}
              
              {selectedApp.coverLetter && (
                <Box sx={{ mb: 3, p: 2, bgcolor: 'grey.50', borderRadius: 1 }}>
                  <Typography variant="caption" sx={{ fontWeight: 600 }}>
                    Cover Letter:
                  </Typography>
                  <Typography variant="body2" sx={{ mt: 1 }}>
                    {selectedApp.coverLetter}
                  </Typography>
                </Box>
              )}

              <TextField
                select
                fullWidth
                label="Status"
                value={newStatus}
                onChange={(e) => setNewStatus(e.target.value)}
                sx={{ mb: 2 }}
              >
                <MenuItem value="SUBMITTED">Submitted</MenuItem>
                <MenuItem value="REVIEWED">Reviewed</MenuItem>
                <MenuItem value="SHORTLISTED">Shortlisted</MenuItem>
                <MenuItem value="INTERVIEW_SCHEDULED">Interview Scheduled</MenuItem>
                <MenuItem value="ACCEPTED">Accepted</MenuItem>
                <MenuItem value="REJECTED">Rejected</MenuItem>
              </TextField>

              <TextField
                fullWidth
                multiline
                rows={3}
                label="Notes (Optional)"
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
                placeholder="Add notes about this candidate..."
              />
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleUpdateStatus}>
            Update Status
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  )
}

export default RecruiterApplicationsPage

