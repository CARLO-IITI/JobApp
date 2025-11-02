import React from 'react'
import {
  Card,
  CardContent,
  CardMedia,
  Typography,
  Chip,
  Box,
  Button,
  IconButton,
} from '@mui/material'
import LaunchIcon from '@mui/icons-material/Launch'
import GitHubIcon from '@mui/icons-material/GitHub'
import CodeIcon from '@mui/icons-material/Code'
import EditIcon from '@mui/icons-material/Edit'

interface Project {
  id: number
  title: string
  description: string
  technologies: string[]
  projectUrl?: string
  githubUrl?: string
  imageUrl?: string
  type: string
  role?: string
}

interface ProjectCardProps {
  project: Project
  onEdit?: (project: Project) => void
  editable?: boolean
}

const ProjectCard: React.FC<ProjectCardProps> = ({ project, onEdit, editable = false }) => {
  return (
    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      {project.imageUrl && (
        <CardMedia
          component="img"
          height="200"
          image={project.imageUrl}
          alt={project.title}
          sx={{ objectFit: 'cover' }}
        />
      )}
      
      <CardContent sx={{ flexGrow: 1 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', mb: 1 }}>
          <Typography variant="h6" sx={{ fontWeight: 600 }}>
            {project.title}
          </Typography>
          {editable && onEdit && (
            <IconButton size="small" onClick={() => onEdit(project)}>
              <EditIcon />
            </IconButton>
          )}
        </Box>

        <Box sx={{ mb: 2 }}>
          <Chip label={project.type} size="small" color="primary" />
          {project.role && (
            <Chip label={project.role} size="small" sx={{ ml: 0.5 }} />
          )}
        </Box>

        <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
          {project.description}
        </Typography>

        <Box sx={{ mb: 2 }}>
          <Typography variant="caption" sx={{ fontWeight: 600, color: 'text.secondary', mb: 0.5, display: 'block' }}>
            Technologies:
          </Typography>
          <Box sx={{ display: 'flex', gap: 0.5, flexWrap: 'wrap' }}>
            {project.technologies.map((tech, idx) => (
              <Chip key={idx} label={tech} size="small" variant="outlined" icon={<CodeIcon />} />
            ))}
          </Box>
        </Box>

        <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
          {project.projectUrl && (
            <Button
              size="small"
              variant="outlined"
              startIcon={<LaunchIcon />}
              href={project.projectUrl}
              target="_blank"
            >
              Live Demo
            </Button>
          )}
          {project.githubUrl && (
            <Button
              size="small"
              variant="outlined"
              startIcon={<GitHubIcon />}
              href={project.githubUrl}
              target="_blank"
            >
              Source Code
            </Button>
          )}
        </Box>
      </CardContent>
    </Card>
  )
}

export default ProjectCard

