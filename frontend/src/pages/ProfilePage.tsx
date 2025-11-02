import React, { useEffect, useState } from 'react'
import { Box, Card, CardContent, Typography, Avatar, Button, Chip, Divider, Grid, LinearProgress } from '@mui/material'
import { useSelector } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import { RootState } from '../store/store'
import { profileService } from '../services/profileService'
import { experienceService } from '../services/experienceService'
import ExperienceTimeline from '../components/ExperienceTimeline'
import ProjectCard from '../components/ProjectCard'
import EditIcon from '@mui/icons-material/Edit'
import EmailIcon from '@mui/icons-material/Email'
import PhoneIcon from '@mui/icons-material/Phone'
import WorkIcon from '@mui/icons-material/Work'
import LocationOnIcon from '@mui/icons-material/LocationOn'
import SchoolIcon from '@mui/icons-material/School'
import LanguageIcon from '@mui/icons-material/Language'
import AttachMoneyIcon from '@mui/icons-material/AttachMoney'
import LinkIcon from '@mui/icons-material/Link'
import AddIcon from '@mui/icons-material/Add'

const ProfilePage: React.FC = () => {
  const { user } = useSelector((state: RootState) => state.auth)
  const navigate = useNavigate()
  const [profile, setProfile] = useState<any>(null)
  const [experiences, setExperiences] = useState<any[]>([])
  const [projects, setProjects] = useState<any[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (user?.id && user?.role === 'CANDIDATE') {
      loadProfile()
      loadExperiences()
      loadProjects()
    } else {
      setLoading(false)
    }
  }, [user])

  const loadProfile = async () => {
    try {
      setLoading(true)
      const response = await profileService.getCandidateProfile(user!.id)
      if (response.success) {
        setProfile(response.data)
      }
    } catch (error) {
      console.error('Error loading profile:', error)
    } finally {
      setLoading(false)
    }
  }

  const loadExperiences = async () => {
    try {
      const response = await experienceService.getUserExperiences(user!.id)
      if (response.success) {
        setExperiences(response.data || [])
      }
    } catch (error) {
      console.error('Error loading experiences:', error)
    }
  }

  const loadProjects = async () => {
    try {
      const response = await experienceService.getUserProjects(user!.id)
      if (response.success) {
        setProjects(response.data || [])
      }
    } catch (error) {
      console.error('Error loading projects:', error)
    }
  }

  if (loading) {
    return <LinearProgress />
  }

  return (
    <Box>
      <Typography variant="h4" sx={{ fontWeight: 700, mb: 3 }}>
        My Profile
      </Typography>

      <Grid container spacing={3}>
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent sx={{ textAlign: 'center' }}>
              <Avatar
                sx={{
                  width: 120,
                  height: 120,
                  mx: 'auto',
                  mb: 2,
                  bgcolor: 'primary.main',
                  fontSize: '3rem',
                }}
              >
                {user?.fullName?.[0] || 'U'}
              </Avatar>
              <Typography variant="h5" sx={{ fontWeight: 600, mb: 1 }}>
                {user?.fullName}
              </Typography>
              <Chip
                label={user?.role}
                color="primary"
                sx={{ mb: 2 }}
              />
              {profile?.headline && (
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  {profile.headline}
                </Typography>
              )}
              <Button
                fullWidth
                variant="contained"
                startIcon={<EditIcon />}
                onClick={() => navigate('/edit-profile')}
              >
                Edit Profile
              </Button>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} md={8}>
          <Card>
            <CardContent>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 3 }}>
                Personal Information
              </Typography>

              <Box sx={{ mb: 3 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                  <EmailIcon sx={{ mr: 1, color: 'text.secondary' }} />
                  <Typography variant="body2" sx={{ fontWeight: 600 }}>
                    Email
                  </Typography>
                </Box>
                <Typography variant="body1">{user?.email}</Typography>
              </Box>

              <Divider sx={{ my: 2 }} />

              <Box sx={{ mb: 3 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                  <WorkIcon sx={{ mr: 1, color: 'text.secondary' }} />
                  <Typography variant="body2" sx={{ fontWeight: 600 }}>
                    Account Type
                  </Typography>
                </Box>
                <Typography variant="body1">{user?.role}</Typography>
              </Box>

              {user?.role === 'CANDIDATE' && profile && (
                <>
                  {profile.currentJobTitle && (
                    <>
                      <Divider sx={{ my: 2 }} />
                      <Box sx={{ mb: 3 }}>
                        <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                          <WorkIcon sx={{ mr: 1, color: 'text.secondary' }} />
                          <Typography variant="body2" sx={{ fontWeight: 600 }}>
                            Current Position
                          </Typography>
                        </Box>
                        <Typography variant="body1">{profile.currentJobTitle}</Typography>
                        <Typography variant="body2" color="text.secondary">
                          {profile.yearsOfExperience} years of experience
                        </Typography>
                      </Box>
                    </>
                  )}

                  {profile.currentLocation && (
                    <>
                      <Divider sx={{ my: 2 }} />
                      <Box sx={{ mb: 3 }}>
                        <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                          <LocationOnIcon sx={{ mr: 1, color: 'text.secondary' }} />
                          <Typography variant="body2" sx={{ fontWeight: 600 }}>
                            Location
                          </Typography>
                        </Box>
                        <Typography variant="body1">{profile.currentLocation}</Typography>
                      </Box>
                    </>
                  )}

                  {profile.education && (
                    <>
                      <Divider sx={{ my: 2 }} />
                      <Box sx={{ mb: 3 }}>
                        <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                          <SchoolIcon sx={{ mr: 1, color: 'text.secondary' }} />
                          <Typography variant="body2" sx={{ fontWeight: 600 }}>
                            Education
                          </Typography>
                        </Box>
                        <Typography variant="body1">{profile.education}</Typography>
                        {profile.university && (
                          <Typography variant="body2" color="text.secondary">
                            {profile.university}
                          </Typography>
                        )}
                      </Box>
                    </>
                  )}
                </>
              )}
            </CardContent>
          </Card>

          {user?.role === 'CANDIDATE' && (
            <>
              {/* Skills */}
              {profile?.skills?.length > 0 && (
                <Card sx={{ mt: 3 }}>
                  <CardContent>
                    <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                      Skills & Expertise
                    </Typography>
                    <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
                      {profile.skills.map((skill: string) => (
                        <Chip key={skill} label={skill} color="primary" />
                      ))}
                    </Box>
                  </CardContent>
                </Card>
              )}

              {/* Professional Links */}
              {(profile?.portfolioUrl || profile?.githubUrl || profile?.linkedinUrl) && (
                <Card sx={{ mt: 3 }}>
                  <CardContent>
                    <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                      Professional Links
                    </Typography>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                      {profile.portfolioUrl && (
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                          <LinkIcon sx={{ mr: 1, color: 'text.secondary' }} />
                          <a href={profile.portfolioUrl} target="_blank" rel="noopener noreferrer">
                            Portfolio
                          </a>
                        </Box>
                      )}
                      {profile.githubUrl && (
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                          <LinkIcon sx={{ mr: 1, color: 'text.secondary' }} />
                          <a href={profile.githubUrl} target="_blank" rel="noopener noreferrer">
                            GitHub
                          </a>
                        </Box>
                      )}
                      {profile.linkedinUrl && (
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                          <LinkIcon sx={{ mr: 1, color: 'text.secondary' }} />
                          <a href={profile.linkedinUrl} target="_blank" rel="noopener noreferrer">
                            LinkedIn
                          </a>
                        </Box>
                      )}
                    </Box>
                  </CardContent>
                </Card>
              )}

              {/* Empty State */}
              {(!profile?.skills || profile.skills.length === 0) && (
                <Card sx={{ mt: 3 }}>
                  <CardContent sx={{ textAlign: 'center', py: 4 }}>
                    <Typography variant="h6" sx={{ fontWeight: 600, mb: 1 }}>
                      Complete Your Profile
                    </Typography>
                    <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                      Add skills, experience, and upload your CV to get better job matches
                    </Typography>
                    <Button
                      variant="contained"
                      startIcon={<EditIcon />}
                      onClick={() => navigate('/edit-profile')}
                    >
                      Complete Profile
                    </Button>
                  </CardContent>
                </Card>
              )}

              {/* Work Experience Timeline */}
              {user?.role === 'CANDIDATE' && (
                <Card sx={{ mt: 3 }}>
                  <CardContent>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                      <Typography variant="h6" sx={{ fontWeight: 600 }}>
                        Work Experience
                      </Typography>
                      <Button
                        size="small"
                        startIcon={<AddIcon />}
                        onClick={() => navigate('/edit-profile')}
                      >
                        Add
                      </Button>
                    </Box>
                    <ExperienceTimeline experiences={experiences} editable={false} />
                  </CardContent>
                </Card>
              )}

              {/* Projects Showcase */}
              {user?.role === 'CANDIDATE' && projects.length > 0 && (
                <Box sx={{ mt: 3 }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                    <Typography variant="h6" sx={{ fontWeight: 600 }}>
                      Projects
                    </Typography>
                    <Button
                      size="small"
                      startIcon={<AddIcon />}
                      onClick={() => navigate('/edit-profile')}
                    >
                      Add Project
                    </Button>
                  </Box>
                  <Grid container spacing={2}>
                    {projects.map((project) => (
                      <Grid item xs={12} md={6} key={project.id}>
                        <ProjectCard project={project} editable={false} />
                      </Grid>
                    ))}
                  </Grid>
                </Box>
              )}
            </>
          )}
        </Grid>
      </Grid>
    </Box>
  )
}

export default ProfilePage

