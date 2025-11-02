import React, { useState } from 'react'
import {
  Box,
  Card,
  CardContent,
  Typography,
  TextField,
  Button,
  Grid,
  MenuItem,
  Chip,
} from '@mui/material'
import { useNavigate } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { RootState } from '../store/store'
import { jobService } from '../services/jobService'
import { toast } from 'react-toastify'

const PostJobPage: React.FC = () => {
  const navigate = useNavigate()
  const { user } = useSelector((state: RootState) => state.auth)
  const [loading, setLoading] = useState(false)
  const [skillInput, setSkillInput] = useState('')
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    companyName: '',
    companyDescription: '',
    location: '',
    jobType: 'FULL_TIME',
    experienceLevel: 'MID',
    minExperience: '',
    maxExperience: '',
    minSalary: '',
    maxSalary: '',
    currency: 'USD',
    remoteAllowed: false,
    openings: '1',
    requiredSkills: [] as string[],
    responsibilities: '',
    qualifications: '',
    benefits: '',
  })

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
  }

  const handleAddSkill = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && skillInput.trim()) {
      e.preventDefault()
      if (!formData.requiredSkills.includes(skillInput.trim())) {
        setFormData(prev => ({
          ...prev,
          requiredSkills: [...prev.requiredSkills, skillInput.trim()],
        }))
      }
      setSkillInput('')
    }
  }

  const handleRemoveSkill = (skill: string) => {
    setFormData(prev => ({
      ...prev,
      requiredSkills: prev.requiredSkills.filter(s => s !== skill),
    }))
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)

    try {
      const jobData = {
        ...formData,
        companyId: user?.id,
        postedBy: user?.id,
        minExperience: formData.minExperience ? parseInt(formData.minExperience) : null,
        maxExperience: formData.maxExperience ? parseInt(formData.maxExperience) : null,
        minSalary: formData.minSalary ? parseFloat(formData.minSalary) : null,
        maxSalary: formData.maxSalary ? parseFloat(formData.maxSalary) : null,
        openings: parseInt(formData.openings),
      }

      const response = await jobService.createJob(jobData)
      if (response.success) {
        toast.success('Job posted successfully!')
        navigate('/dashboard')
      }
    } catch (error: any) {
      toast.error(error.response?.data?.message || 'Failed to post job')
    } finally {
      setLoading(false)
    }
  }

  return (
    <Box>
      <Typography variant="h4" sx={{ fontWeight: 700, mb: 3 }}>
        Post a New Job
      </Typography>

      <Card>
        <CardContent>
          <Box component="form" onSubmit={handleSubmit}>
            <Grid container spacing={3}>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  label="Job Title"
                  name="title"
                  value={formData.title}
                  onChange={handleChange}
                />
              </Grid>

              <Grid item xs={12} md={6}>
                <TextField
                  required
                  fullWidth
                  label="Company Name"
                  name="companyName"
                  value={formData.companyName}
                  onChange={handleChange}
                />
              </Grid>

              <Grid item xs={12} md={6}>
                <TextField
                  required
                  fullWidth
                  label="Location"
                  name="location"
                  value={formData.location}
                  onChange={handleChange}
                />
              </Grid>

              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  multiline
                  rows={4}
                  label="Job Description"
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                />
              </Grid>

              <Grid item xs={12} md={6}>
                <TextField
                  required
                  fullWidth
                  select
                  label="Job Type"
                  name="jobType"
                  value={formData.jobType}
                  onChange={handleChange}
                >
                  <MenuItem value="FULL_TIME">Full Time</MenuItem>
                  <MenuItem value="PART_TIME">Part Time</MenuItem>
                  <MenuItem value="CONTRACT">Contract</MenuItem>
                  <MenuItem value="INTERNSHIP">Internship</MenuItem>
                </TextField>
              </Grid>

              <Grid item xs={12} md={6}>
                <TextField
                  required
                  fullWidth
                  select
                  label="Experience Level"
                  name="experienceLevel"
                  value={formData.experienceLevel}
                  onChange={handleChange}
                >
                  <MenuItem value="ENTRY">Entry Level</MenuItem>
                  <MenuItem value="JUNIOR">Junior</MenuItem>
                  <MenuItem value="MID">Mid Level</MenuItem>
                  <MenuItem value="SENIOR">Senior</MenuItem>
                  <MenuItem value="LEAD">Lead</MenuItem>
                </TextField>
              </Grid>

              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Required Skills (Press Enter to add)"
                  value={skillInput}
                  onChange={(e) => setSkillInput(e.target.value)}
                  onKeyDown={handleAddSkill}
                  helperText="Type a skill and press Enter"
                />
                <Box sx={{ mt: 2, display: 'flex', gap: 1, flexWrap: 'wrap' }}>
                  {formData.requiredSkills.map((skill) => (
                    <Chip
                      key={skill}
                      label={skill}
                      onDelete={() => handleRemoveSkill(skill)}
                      color="primary"
                    />
                  ))}
                </Box>
              </Grid>

              <Grid item xs={12} md={3}>
                <TextField
                  fullWidth
                  type="number"
                  label="Min Experience (years)"
                  name="minExperience"
                  value={formData.minExperience}
                  onChange={handleChange}
                />
              </Grid>

              <Grid item xs={12} md={3}>
                <TextField
                  fullWidth
                  type="number"
                  label="Max Experience (years)"
                  name="maxExperience"
                  value={formData.maxExperience}
                  onChange={handleChange}
                />
              </Grid>

              <Grid item xs={12} md={3}>
                <TextField
                  fullWidth
                  type="number"
                  label="Min Salary"
                  name="minSalary"
                  value={formData.minSalary}
                  onChange={handleChange}
                />
              </Grid>

              <Grid item xs={12} md={3}>
                <TextField
                  fullWidth
                  type="number"
                  label="Max Salary"
                  name="maxSalary"
                  value={formData.maxSalary}
                  onChange={handleChange}
                />
              </Grid>

              <Grid item xs={12} md={6}>
                <TextField
                  required
                  fullWidth
                  type="number"
                  label="Number of Openings"
                  name="openings"
                  value={formData.openings}
                  onChange={handleChange}
                />
              </Grid>

              <Grid item xs={12}>
                <TextField
                  fullWidth
                  multiline
                  rows={3}
                  label="Responsibilities"
                  name="responsibilities"
                  value={formData.responsibilities}
                  onChange={handleChange}
                />
              </Grid>

              <Grid item xs={12}>
                <TextField
                  fullWidth
                  multiline
                  rows={3}
                  label="Qualifications"
                  name="qualifications"
                  value={formData.qualifications}
                  onChange={handleChange}
                />
              </Grid>

              <Grid item xs={12}>
                <TextField
                  fullWidth
                  multiline
                  rows={2}
                  label="Benefits"
                  name="benefits"
                  value={formData.benefits}
                  onChange={handleChange}
                />
              </Grid>

              <Grid item xs={12}>
                <Box sx={{ display: 'flex', gap: 2 }}>
                  <Button
                    type="submit"
                    variant="contained"
                    size="large"
                    disabled={loading}
                  >
                    {loading ? 'Posting...' : 'Post Job'}
                  </Button>
                  <Button
                    variant="outlined"
                    size="large"
                    onClick={() => navigate('/dashboard')}
                  >
                    Cancel
                  </Button>
                </Box>
              </Grid>
            </Grid>
          </Box>
        </CardContent>
      </Card>
    </Box>
  )
}

export default PostJobPage

