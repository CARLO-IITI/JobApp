import React, { useEffect, useState } from 'react'
import {
  Box,
  Card,
  CardContent,
  Typography,
  TextField,
  Button,
  Grid,
  Chip,
  MenuItem,
  Switch,
  FormControlLabel,
  Divider,
  Alert,
  LinearProgress,
  IconButton,
} from '@mui/material'
import { useNavigate } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { RootState } from '../store/store'
import { profileService } from '../services/profileService'
import { resumeService } from '../services/resumeService'
import { experienceService } from '../services/experienceService'
import AddExperienceDialog from '../components/AddExperienceDialog'
import ExperienceTimeline from '../components/ExperienceTimeline'
import { toast } from 'react-toastify'
import CloudUploadIcon from '@mui/icons-material/CloudUpload'
import AutoAwesomeIcon from '@mui/icons-material/AutoAwesome'
import SaveIcon from '@mui/icons-material/Save'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import WorkIcon from '@mui/icons-material/Work'

const EditProfilePage: React.FC = () => {
  const navigate = useNavigate()
  const { user } = useSelector((state: RootState) => state.auth)
  const [loading, setLoading] = useState(false)
  const [uploading, setUploading] = useState(false)
  const [skillInput, setSkillInput] = useState('')
  const [locationInput, setLocationInput] = useState('')
  const [languageInput, setLanguageInput] = useState('')
  const [experiences, setExperiences] = useState<any[]>([])
  const [expDialogOpen, setExpDialogOpen] = useState(false)
  
  const [profile, setProfile] = useState({
    headline: '',
    summary: '',
    skills: [] as string[],
    yearsOfExperience: 0,
    currentLocation: '',
    preferredLocations: [] as string[],
    currentJobTitle: '',
    education: '',
    university: '',
    portfolioUrl: '',
    githubUrl: '',
    linkedinUrl: '',
    languages: [] as string[],
    expectedSalary: 0,
    noticePeriod: '',
    jobPreference: 'FULL_TIME',
    openToRemote: true,
    openToRelocation: false,
  })

  useEffect(() => {
    if (user?.id) {
      loadProfile()
      loadExperiences()
    }
  }, [user])

  const loadProfile = async () => {
    try {
      setLoading(true)
      const response = await profileService.getCandidateProfile(user!.id)
      if (response.success && response.data) {
        setProfile({
          ...profile,
          ...response.data,
          skills: response.data.skills || [],
          preferredLocations: response.data.preferredLocations || [],
          languages: response.data.languages || [],
        })
      }
    } catch (error) {
      console.error('Error loading profile:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target
    setProfile(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }))
  }

  const handleAddSkill = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && skillInput.trim()) {
      e.preventDefault()
      if (!profile.skills.includes(skillInput.trim())) {
        setProfile(prev => ({
          ...prev,
          skills: [...prev.skills, skillInput.trim()],
        }))
      }
      setSkillInput('')
    }
  }

  const handleRemoveSkill = (skill: string) => {
    setProfile(prev => ({
      ...prev,
      skills: prev.skills.filter(s => s !== skill),
    }))
  }

  const handleAddLocation = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && locationInput.trim()) {
      e.preventDefault()
      if (!profile.preferredLocations.includes(locationInput.trim())) {
        setProfile(prev => ({
          ...prev,
          preferredLocations: [...prev.preferredLocations, locationInput.trim()],
        }))
      }
      setLocationInput('')
    }
  }

  const handleRemoveLocation = (location: string) => {
    setProfile(prev => ({
      ...prev,
      preferredLocations: prev.preferredLocations.filter(l => l !== location),
    }))
  }

  const handleAddLanguage = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && languageInput.trim()) {
      e.preventDefault()
      if (!profile.languages.includes(languageInput.trim())) {
        setProfile(prev => ({
          ...prev,
          languages: [...prev.languages, languageInput.trim()],
        }))
      }
      setLanguageInput('')
    }
  }

  const handleRemoveLanguage = (language: string) => {
    setProfile(prev => ({
      ...prev,
      languages: prev.languages.filter(l => l !== language),
    }))
  }

  const handleCVUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (!file) return

    // Check file type
    const validTypes = ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document']
    if (!validTypes.includes(file.type)) {
      toast.error('Please upload a PDF or Word document')
      return
    }

    // Check file size (max 5MB)
    if (file.size > 5 * 1024 * 1024) {
      toast.error('File size must be less than 5MB')
      return
    }

    try {
      setUploading(true)
      toast.info('Parsing your resume... This may take a moment')
      
      const response = await resumeService.parseResume(file)
      
      if (response.success && response.data) {
        const parsed = response.data
        
        // Auto-fill profile with extracted data
        setProfile(prev => ({
          ...prev,
          skills: parsed.skills || prev.skills,
          yearsOfExperience: parsed.experience || prev.yearsOfExperience,
          education: parsed.education || prev.education,
        }))
        
        toast.success('✨ Resume parsed! Profile auto-filled with extracted data')
        toast.info('Review and save the extracted information')
      }
    } catch (error: any) {
      console.error('Resume parse error:', error)
      toast.error('Failed to parse resume. You can still fill the form manually.')
    } finally {
      setUploading(false)
    }
  }

  const loadExperiences = async () => {
    if (!user?.id) return
    try {
      const response = await experienceService.getUserExperiences(user.id)
      if (response.success) {
        setExperiences(response.data || [])
      }
    } catch (error) {
      console.error('Error loading experiences:', error)
    }
  }

  const handleAddExperience = async (expData: any) => {
    if (!user?.id) return
    
    try {
      const response = await experienceService.addWorkExperience({
        ...expData,
        userId: user.id,
      })
      
      if (response.success) {
        toast.success('Work experience added!')
        loadExperiences()
      }
    } catch (error: any) {
      toast.error('Failed to add experience')
    }
  }

  const handleSave = async () => {
    if (!user?.id) return

    try {
      setLoading(true)
      const response = await profileService.updateCandidateProfile(user.id, profile)
      
      if (response.success) {
        toast.success('Profile saved successfully!')
        navigate('/profile')
      }
    } catch (error: any) {
      toast.error(error.response?.data?.message || 'Failed to save profile')
    } finally {
      setLoading(false)
    }
  }

  return (
    <Box>
      <Box sx={{ mb: 3, display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <Box>
          <Button
            startIcon={<ArrowBackIcon />}
            onClick={() => navigate('/profile')}
            sx={{ mb: 1 }}
          >
            Back to Profile
          </Button>
          <Typography variant="h4" sx={{ fontWeight: 700 }}>
            Edit Profile
          </Typography>
        </Box>
      </Box>

      {loading && <LinearProgress sx={{ mb: 3 }} />}

      {/* CV Upload Section */}
      <Card sx={{ mb: 3, background: 'linear-gradient(135deg, #0F2B46 0%, #10B981 100%)', color: 'white' }}>
        <CardContent>
          <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: 2 }}>
            <Box>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 1, display: 'flex', alignItems: 'center' }}>
                <AutoAwesomeIcon sx={{ mr: 1 }} />
                AI-Powered Resume Parser
              </Typography>
              <Typography variant="body2" sx={{ opacity: 0.9 }}>
                Upload your CV and we'll automatically extract skills, experience, and education!
              </Typography>
              <Typography variant="caption" sx={{ opacity: 0.8, mt: 1, display: 'block' }}>
                Supports: PDF, DOC, DOCX • Max size: 5MB
              </Typography>
            </Box>
            <Button
              variant="contained"
              component="label"
              disabled={uploading}
              startIcon={<CloudUploadIcon />}
              sx={{
                bgcolor: 'white',
                color: 'primary.main',
                '&:hover': { bgcolor: 'grey.100' },
                '&:disabled': { bgcolor: 'grey.300' },
              }}
            >
              {uploading ? 'Parsing...' : 'Upload Resume'}
              <input
                type="file"
                hidden
                accept=".pdf,.doc,.docx"
                onChange={handleCVUpload}
              />
            </Button>
          </Box>
          {uploading && (
            <Box sx={{ mt: 2 }}>
              <LinearProgress sx={{ bgcolor: 'rgba(255,255,255,0.3)', '& .MuiLinearProgress-bar': { bgcolor: 'white' } }} />
              <Typography variant="caption" sx={{ mt: 1, display: 'block' }}>
                Extracting data from your resume using Apache Tika...
              </Typography>
            </Box>
          )}
        </CardContent>
      </Card>

      {/* Profile Form */}
      <Card>
        <CardContent>
          <Grid container spacing={3}>
            {/* Headline */}
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Professional Headline"
                name="headline"
                value={profile.headline}
                onChange={handleChange}
                placeholder="e.g., Full Stack Developer | React & Node.js Expert"
              />
            </Grid>

            {/* Summary */}
            <Grid item xs={12}>
              <TextField
                fullWidth
                multiline
                rows={4}
                label="Professional Summary"
                name="summary"
                value={profile.summary}
                onChange={handleChange}
                placeholder="Tell us about yourself, your experience, and career goals..."
              />
            </Grid>

            <Grid item xs={12}>
              <Divider />
            </Grid>

            {/* Skills */}
            <Grid item xs={12}>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                Skills & Expertise
              </Typography>
              <TextField
                fullWidth
                label="Add Skills (Press Enter to add)"
                value={skillInput}
                onChange={(e) => setSkillInput(e.target.value)}
                onKeyDown={handleAddSkill}
                placeholder="Type a skill and press Enter: Java, Python, React..."
                helperText="AI extracted skills will appear here after CV upload"
              />
              <Box sx={{ mt: 2, display: 'flex', gap: 1, flexWrap: 'wrap' }}>
                {profile.skills.map((skill) => (
                  <Chip
                    key={skill}
                    label={skill}
                    onDelete={() => handleRemoveSkill(skill)}
                    color="primary"
                  />
                ))}
              </Box>
            </Grid>

            {/* Experience */}
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                type="number"
                label="Years of Experience"
                name="yearsOfExperience"
                value={profile.yearsOfExperience}
                onChange={handleChange}
                helperText="Auto-extracted from CV"
              />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Current Job Title"
                name="currentJobTitle"
                value={profile.currentJobTitle}
                onChange={handleChange}
                placeholder="e.g., Senior Software Engineer"
              />
            </Grid>

            <Grid item xs={12}>
              <Divider />
            </Grid>

            {/* Education */}
            <Grid item xs={12}>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                Education
              </Typography>
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Highest Degree"
                name="education"
                value={profile.education}
                onChange={handleChange}
                placeholder="e.g., Bachelor's in Computer Science"
                helperText="Auto-extracted from CV"
              />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="University/College"
                name="university"
                value={profile.university}
                onChange={handleChange}
                placeholder="e.g., MIT"
              />
            </Grid>

            <Grid item xs={12}>
              <Divider />
            </Grid>

            {/* Location */}
            <Grid item xs={12}>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                Location Preferences
              </Typography>
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Current Location"
                name="currentLocation"
                value={profile.currentLocation}
                onChange={handleChange}
                placeholder="e.g., San Francisco, CA"
              />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Preferred Locations (Press Enter to add)"
                value={locationInput}
                onChange={(e) => setLocationInput(e.target.value)}
                onKeyDown={handleAddLocation}
                placeholder="Add preferred work locations"
              />
            </Grid>

            <Grid item xs={12}>
              <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
                {profile.preferredLocations.map((location) => (
                  <Chip
                    key={location}
                    label={location}
                    onDelete={() => handleRemoveLocation(location)}
                    color="secondary"
                  />
                ))}
              </Box>
            </Grid>

            <Grid item xs={12} md={6}>
              <FormControlLabel
                control={
                  <Switch
                    checked={profile.openToRemote}
                    onChange={handleChange}
                    name="openToRemote"
                  />
                }
                label="Open to Remote Work"
              />
            </Grid>

            <Grid item xs={12} md={6}>
              <FormControlLabel
                control={
                  <Switch
                    checked={profile.openToRelocation}
                    onChange={handleChange}
                    name="openToRelocation"
                  />
                }
                label="Open to Relocation"
              />
            </Grid>

            <Grid item xs={12}>
              <Divider />
            </Grid>

            {/* Job Preferences */}
            <Grid item xs={12}>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                Job Preferences
              </Typography>
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                select
                label="Job Type Preference"
                name="jobPreference"
                value={profile.jobPreference}
                onChange={handleChange}
              >
                <MenuItem value="FULL_TIME">Full Time</MenuItem>
                <MenuItem value="PART_TIME">Part Time</MenuItem>
                <MenuItem value="CONTRACT">Contract</MenuItem>
                <MenuItem value="INTERNSHIP">Internship</MenuItem>
                <MenuItem value="ANY">Any</MenuItem>
              </TextField>
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                select
                label="Notice Period"
                name="noticePeriod"
                value={profile.noticePeriod}
                onChange={handleChange}
              >
                <MenuItem value="Immediate">Immediate</MenuItem>
                <MenuItem value="1 week">1 Week</MenuItem>
                <MenuItem value="2 weeks">2 Weeks</MenuItem>
                <MenuItem value="1 month">1 Month</MenuItem>
                <MenuItem value="2 months">2 Months</MenuItem>
                <MenuItem value="3 months">3 Months</MenuItem>
              </TextField>
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                type="number"
                label="Expected Salary (USD per year)"
                name="expectedSalary"
                value={profile.expectedSalary}
                onChange={handleChange}
                placeholder="e.g., 100000"
              />
            </Grid>

            <Grid item xs={12}>
              <Divider />
            </Grid>

            {/* Links */}
            <Grid item xs={12}>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                Professional Links
              </Typography>
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Portfolio URL"
                name="portfolioUrl"
                value={profile.portfolioUrl}
                onChange={handleChange}
                placeholder="https://yourportfolio.com"
              />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="GitHub URL"
                name="githubUrl"
                value={profile.githubUrl}
                onChange={handleChange}
                placeholder="https://github.com/username"
              />
            </Grid>

            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="LinkedIn URL"
                name="linkedinUrl"
                value={profile.linkedinUrl}
                onChange={handleChange}
                placeholder="https://linkedin.com/in/username"
              />
            </Grid>

            <Grid item xs={12}>
              <Divider />
            </Grid>

            {/* Languages */}
            <Grid item xs={12}>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                Languages
              </Typography>
              <TextField
                fullWidth
                label="Add Languages (Press Enter)"
                value={languageInput}
                onChange={(e) => setLanguageInput(e.target.value)}
                onKeyDown={handleAddLanguage}
                placeholder="English, Spanish, French..."
              />
              <Box sx={{ mt: 2, display: 'flex', gap: 1, flexWrap: 'wrap' }}>
                {profile.languages.map((language) => (
                  <Chip
                    key={language}
                    label={language}
                    onDelete={() => handleRemoveLanguage(language)}
                  />
                ))}
              </Box>
            </Grid>

            {/* Save Button */}
            <Grid item xs={12}>
              <Box sx={{ display: 'flex', gap: 2, mt: 2 }}>
                <Button
                  variant="contained"
                  size="large"
                  startIcon={<SaveIcon />}
                  onClick={handleSave}
                  disabled={loading}
                >
                  {loading ? 'Saving...' : 'Save Profile'}
                </Button>
                <Button
                  variant="outlined"
                  size="large"
                  onClick={() => navigate('/profile')}
                >
                  Cancel
                </Button>
              </Box>
            </Grid>
          </Grid>
        </CardContent>
      </Card>

      {/* Work Experience Section */}
      <Card sx={{ mt: 3 }}>
        <CardContent>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
            <Typography variant="h6" sx={{ fontWeight: 600, display: 'flex', alignItems: 'center' }}>
              <WorkIcon sx={{ mr: 1 }} />
              Work Experience
            </Typography>
            <Button
              variant="contained"
              startIcon={<WorkIcon />}
              onClick={() => setExpDialogOpen(true)}
            >
              Add Experience
            </Button>
          </Box>
          <ExperienceTimeline 
            experiences={experiences}
            editable={false}
          />
        </CardContent>
      </Card>

      {/* Add Experience Dialog */}
      <AddExperienceDialog
        open={expDialogOpen}
        onClose={() => setExpDialogOpen(false)}
        onSave={handleAddExperience}
      />

      {/* Info Box */}
      <Alert severity="info" sx={{ mt: 3 }}>
        <Typography variant="body2" sx={{ fontWeight: 600, mb: 1 }}>
          💡 How CV Parsing Works:
        </Typography>
        <Typography variant="body2" component="div">
          <ul style={{ margin: 0, paddingLeft: '20px' }}>
            <li><strong>Apache Tika</strong> extracts text from PDF/Word files</li>
            <li><strong>Regex patterns</strong> identify email, phone, education</li>
            <li><strong>NLP algorithms</strong> extract skills from 50+ tech keywords</li>
            <li><strong>Experience calculation</strong> finds "X years of experience" mentions</li>
            <li><strong>Name extraction</strong> from document header</li>
          </ul>
        </Typography>
        <Typography variant="body2" sx={{ mt: 1 }}>
          All extracted data is editable - review and save!
        </Typography>
      </Alert>
    </Box>
  )
}

export default EditProfilePage

