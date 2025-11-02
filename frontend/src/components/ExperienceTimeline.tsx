import React from 'react'
import {
  Box,
  Card,
  CardContent,
  Typography,
  Chip,
  IconButton,
  Button,
} from '@mui/material'
import {
  Timeline,
  TimelineItem,
  TimelineSeparator,
  TimelineConnector,
  TimelineContent,
  TimelineDot,
  TimelineOppositeContent,
} from '@mui/lab'
import WorkIcon from '@mui/icons-material/Work'
import SchoolIcon from '@mui/icons-material/School'
import CodeIcon from '@mui/icons-material/Code'
import AddIcon from '@mui/icons-material/Add'
import EditIcon from '@mui/icons-material/Edit'

interface Experience {
  id: number
  jobTitle: string
  companyName: string
  location: string
  startDate: string
  endDate?: string
  currentlyWorking: boolean
  description: string
  achievements?: string[]
  technologiesUsed?: string[]
  durationInMonths: number
}

interface ExperienceTimelineProps {
  experiences: Experience[]
  onAdd?: () => void
  onEdit?: (exp: Experience) => void
  editable?: boolean
}

const ExperienceTimeline: React.FC<ExperienceTimelineProps> = ({ 
  experiences, 
  onAdd, 
  onEdit,
  editable = false 
}) => {
  
  const formatDuration = (months: number) => {
    if (months < 12) return `${months} month${months > 1 ? 's' : ''}`
    const years = Math.floor(months / 12)
    const remainingMonths = months % 12
    if (remainingMonths === 0) return `${years} year${years > 1 ? 's' : ''}`
    return `${years} yr${years > 1 ? 's' : ''} ${remainingMonths} mo`
  }

  const formatDate = (dateString: string) => {
    const date = new Date(dateString)
    return date.toLocaleDateString('en-US', { month: 'short', year: 'numeric' })
  }

  if (!experiences || experiences.length === 0) {
    return (
      <Card>
        <CardContent sx={{ textAlign: 'center', py: 6 }}>
          <WorkIcon sx={{ fontSize: 64, color: 'text.secondary', mb: 2 }} />
          <Typography variant="h6" color="text.secondary" sx={{ mb: 2 }}>
            No work experience added yet
          </Typography>
          {editable && onAdd && (
            <Button
              variant="contained"
              startIcon={<AddIcon />}
              onClick={onAdd}
            >
              Add Experience
            </Button>
          )}
        </CardContent>
      </Card>
    )
  }

  return (
    <Box>
      {editable && onAdd && (
        <Box sx={{ mb: 2, textAlign: 'right' }}>
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={onAdd}
            size="small"
          >
            Add Experience
          </Button>
        </Box>
      )}

      <Timeline position="right">
        {experiences.map((exp, index) => (
          <TimelineItem key={exp.id}>
            <TimelineOppositeContent sx={{ flex: 0.3 }} color="text.secondary">
              <Typography variant="body2" sx={{ fontWeight: 600 }}>
                {formatDate(exp.startDate)} - {exp.currentlyWorking ? 'Present' : formatDate(exp.endDate!)}
              </Typography>
              <Typography variant="caption" color="primary.main">
                {formatDuration(exp.durationInMonths)}
              </Typography>
            </TimelineOppositeContent>

            <TimelineSeparator>
              <TimelineDot color="primary" sx={{ p: 1.5 }}>
                <WorkIcon />
              </TimelineDot>
              {index < experiences.length - 1 && <TimelineConnector sx={{ minHeight: 60 }} />}
            </TimelineSeparator>

            <TimelineContent>
              <Card 
                sx={{ 
                  mb: 2,
                  transition: 'all 0.3s',
                  '&:hover': editable ? { boxShadow: 4, transform: 'translateY(-2px)' } : {}
                }}
              >
                <CardContent>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', mb: 1 }}>
                    <Box sx={{ flex: 1 }}>
                      <Typography variant="h6" sx={{ fontWeight: 600, mb: 0.5 }}>
                        {exp.jobTitle}
                      </Typography>
                      <Typography variant="subtitle1" color="primary" sx={{ mb: 0.5 }}>
                        {exp.companyName}
                      </Typography>
                      <Typography variant="body2" color="text.secondary" sx={{ display: 'flex', alignItems: 'center' }}>
                        📍 {exp.location}
                        {exp.currentlyWorking && (
                          <Chip label="Current" color="success" size="small" sx={{ ml: 1 }} />
                        )}
                      </Typography>
                    </Box>
                    {editable && onEdit && (
                      <IconButton size="small" onClick={() => onEdit(exp)}>
                        <EditIcon />
                      </IconButton>
                    )}
                  </Box>

                  {exp.description && (
                    <Typography variant="body2" sx={{ my: 1.5 }}>
                      {exp.description}
                    </Typography>
                  )}

                  {exp.achievements && exp.achievements.length > 0 && (
                    <Box sx={{ my: 1.5 }}>
                      <Typography variant="caption" sx={{ fontWeight: 600, color: 'text.secondary' }}>
                        Key Achievements:
                      </Typography>
                      <ul style={{ margin: '8px 0', paddingLeft: '20px' }}>
                        {exp.achievements.map((achievement, idx) => (
                          <li key={idx}>
                            <Typography variant="body2">{achievement}</Typography>
                          </li>
                        ))}
                      </ul>
                    </Box>
                  )}

                  {exp.technologiesUsed && exp.technologiesUsed.length > 0 && (
                    <Box sx={{ mt: 2 }}>
                      <Typography variant="caption" sx={{ fontWeight: 600, color: 'text.secondary', mb: 1, display: 'block' }}>
                        Technologies:
                      </Typography>
                      <Box sx={{ display: 'flex', gap: 0.5, flexWrap: 'wrap' }}>
                        {exp.technologiesUsed.map((tech, idx) => (
                          <Chip key={idx} label={tech} size="small" variant="outlined" />
                        ))}
                      </Box>
                    </Box>
                  )}
                </CardContent>
              </Card>
            </TimelineContent>
          </TimelineItem>
        ))}
      </Timeline>
    </Box>
  )
}

export default ExperienceTimeline

