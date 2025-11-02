import React, { useState } from 'react'
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Grid,
  MenuItem,
  FormControlLabel,
  Checkbox,
  Chip,
  Box,
  Typography,
} from '@mui/material'

interface AddExperienceDialogProps {
  open: boolean
  onClose: () => void
  onSave: (experience: any) => void
}

const AddExperienceDialog: React.FC<AddExperienceDialogProps> = ({ open, onClose, onSave }) => {
  const [formData, setFormData] = useState({
    jobTitle: '',
    companyName: '',
    location: '',
    employmentType: 'FULL_TIME',
    startDate: '',
    endDate: '',
    currentlyWorking: false,
    description: '',
  })
  const [achievementInput, setAchievementInput] = useState('')
  const [achievements, setAchievements] = useState<string[]>([])
  const [techInput, setTechInput] = useState('')
  const [technologies, setTechnologies] = useState<string[]>([])

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }))
  }

  const handleAddAchievement = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && achievementInput.trim()) {
      e.preventDefault()
      setAchievements([...achievements, achievementInput.trim()])
      setAchievementInput('')
    }
  }

  const handleAddTech = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && techInput.trim()) {
      e.preventDefault()
      setTechnologies([...technologies, techInput.trim()])
      setTechInput('')
    }
  }

  const handleSave = () => {
    onSave({
      ...formData,
      achievements,
      technologiesUsed: technologies,
    })
    onClose()
    // Reset form
    setFormData({
      jobTitle: '',
      companyName: '',
      location: '',
      employmentType: 'FULL_TIME',
      startDate: '',
      endDate: '',
      currentlyWorking: false,
      description: '',
    })
    setAchievements([])
    setTechnologies([])
  }

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <DialogTitle>Add Work Experience</DialogTitle>
      <DialogContent>
        <Grid container spacing={2} sx={{ mt: 1 }}>
          <Grid item xs={12}>
            <TextField
              fullWidth
              required
              label="Job Title"
              name="jobTitle"
              value={formData.jobTitle}
              onChange={handleChange}
              placeholder="e.g., Senior Software Engineer"
            />
          </Grid>

          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              required
              label="Company Name"
              name="companyName"
              value={formData.companyName}
              onChange={handleChange}
              placeholder="e.g., TechCorp"
            />
          </Grid>

          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              label="Location"
              name="location"
              value={formData.location}
              onChange={handleChange}
              placeholder="e.g., San Francisco, CA"
            />
          </Grid>

          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              select
              label="Employment Type"
              name="employmentType"
              value={formData.employmentType}
              onChange={handleChange}
            >
              <MenuItem value="FULL_TIME">Full-time</MenuItem>
              <MenuItem value="PART_TIME">Part-time</MenuItem>
              <MenuItem value="CONTRACT">Contract</MenuItem>
              <MenuItem value="INTERNSHIP">Internship</MenuItem>
              <MenuItem value="FREELANCE">Freelance</MenuItem>
            </TextField>
          </Grid>

          <Grid item xs={12} md={6}>
            <FormControlLabel
              control={
                <Checkbox
                  checked={formData.currentlyWorking}
                  onChange={handleChange}
                  name="currentlyWorking"
                />
              }
              label="I currently work here"
            />
          </Grid>

          <Grid item xs={12} md={6}>
            <TextField
              fullWidth
              type="date"
              label="Start Date"
              name="startDate"
              value={formData.startDate}
              onChange={handleChange}
              InputLabelProps={{ shrink: true }}
              required
            />
          </Grid>

          {!formData.currentlyWorking && (
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                type="date"
                label="End Date"
                name="endDate"
                value={formData.endDate}
                onChange={handleChange}
                InputLabelProps={{ shrink: true }}
              />
            </Grid>
          )}

          <Grid item xs={12}>
            <TextField
              fullWidth
              multiline
              rows={3}
              label="Description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              placeholder="Describe your role and responsibilities..."
            />
          </Grid>

          <Grid item xs={12}>
            <TextField
              fullWidth
              label="Key Achievements (Press Enter to add)"
              value={achievementInput}
              onChange={(e) => setAchievementInput(e.target.value)}
              onKeyDown={handleAddAchievement}
              placeholder="e.g., Reduced server costs by 40%"
            />
            <Box sx={{ mt: 1, display: 'flex', gap: 0.5, flexWrap: 'wrap' }}>
              {achievements.map((achievement, idx) => (
                <Chip
                  key={idx}
                  label={achievement}
                  onDelete={() => setAchievements(achievements.filter((_, i) => i !== idx))}
                  color="success"
                  size="small"
                />
              ))}
            </Box>
          </Grid>

          <Grid item xs={12}>
            <TextField
              fullWidth
              label="Technologies Used (Press Enter to add)"
              value={techInput}
              onChange={(e) => setTechInput(e.target.value)}
              onKeyDown={handleAddTech}
              placeholder="e.g., React, Node.js, PostgreSQL"
            />
            <Box sx={{ mt: 1, display: 'flex', gap: 0.5, flexWrap: 'wrap' }}>
              {technologies.map((tech, idx) => (
                <Chip
                  key={idx}
                  label={tech}
                  onDelete={() => setTechnologies(technologies.filter((_, i) => i !== idx))}
                  color="primary"
                  size="small"
                />
              ))}
            </Box>
          </Grid>
        </Grid>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button 
          onClick={handleSave} 
          variant="contained"
          disabled={!formData.jobTitle || !formData.companyName || !formData.startDate}
        >
          Save Experience
        </Button>
      </DialogActions>
    </Dialog>
  )
}

export default AddExperienceDialog

