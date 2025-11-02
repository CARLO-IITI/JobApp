import React, { useEffect, useState } from 'react'
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  TextField,
  Button,
  Chip,
  InputAdornment,
  Pagination,
  LinearProgress,
} from '@mui/material'
import { useNavigate } from 'react-router-dom'
import { jobService } from '../services/jobService'
import SearchIcon from '@mui/icons-material/Search'
import LocationOnIcon from '@mui/icons-material/LocationOn'
import WorkIcon from '@mui/icons-material/Work'
import { toast } from 'react-toastify'

const JobsPage: React.FC = () => {
  const navigate = useNavigate()
  const [jobs, setJobs] = useState<any[]>([])
  const [loading, setLoading] = useState(false)
  const [searchKeyword, setSearchKeyword] = useState('')
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)

  useEffect(() => {
    fetchJobs()
  }, [page])

  const fetchJobs = async (keyword = '') => {
    try {
      setLoading(true)
      const response = keyword
        ? await jobService.searchJobs(keyword, page, 12)
        : await jobService.getAllJobs({ page, size: 12 })

      if (response.success && response.data) {
        setJobs(response.data.content || [])
        setTotalPages(response.data.totalPages || 0)
      }
    } catch (error: any) {
      console.error('Error fetching jobs:', error)
      toast.error('Failed to load jobs')
    } finally {
      setLoading(false)
    }
  }

  const handleSearch = () => {
    setPage(0)
    fetchJobs(searchKeyword)
  }

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter') {
      handleSearch()
    }
  }

  return (
    <Box>
      <Typography variant="h4" sx={{ fontWeight: 700, mb: 3 }}>
        Browse Jobs
      </Typography>

      {/* Search Bar */}
      <Card sx={{ mb: 4 }}>
        <CardContent>
          <Grid container spacing={2}>
            <Grid item xs={12} md={8}>
              <TextField
                fullWidth
                placeholder="Search jobs by title, skills, or company..."
                value={searchKeyword}
                onChange={(e) => setSearchKeyword(e.target.value)}
                onKeyPress={handleKeyPress}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <SearchIcon />
                    </InputAdornment>
                  ),
                }}
              />
            </Grid>
            <Grid item xs={12} md={4}>
              <Button
                fullWidth
                variant="contained"
                size="large"
                onClick={handleSearch}
                sx={{ height: '100%' }}
              >
                Search Jobs
              </Button>
            </Grid>
          </Grid>
        </CardContent>
      </Card>

      {loading && <LinearProgress sx={{ mb: 3 }} />}

      {/* Jobs Grid */}
      <Grid container spacing={3}>
        {jobs.map((job) => (
          <Grid item xs={12} md={6} lg={4} key={job.id}>
            <Card
              sx={{
                height: '100%',
                cursor: 'pointer',
                transition: 'all 0.3s',
                '&:hover': {
                  transform: 'translateY(-4px)',
                  boxShadow: 6,
                },
              }}
              onClick={() => navigate(`/jobs/${job.id}`)}
            >
              <CardContent>
                <Box sx={{ mb: 2 }}>
                  <Typography variant="h6" sx={{ fontWeight: 600, mb: 1 }}>
                    {job.title}
                  </Typography>
                  <Typography variant="subtitle1" color="primary" sx={{ mb: 1 }}>
                    {job.companyName}
                  </Typography>
                </Box>

                <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                  <LocationOnIcon sx={{ fontSize: 18, mr: 0.5, color: 'text.secondary' }} />
                  <Typography variant="body2" color="text.secondary">
                    {job.location}
                  </Typography>
                  {job.remoteAllowed && (
                    <Chip label="Remote" size="small" color="success" sx={{ ml: 1 }} />
                  )}
                </Box>

                <Box sx={{ mb: 2 }}>
                  <Chip
                    label={job.jobType}
                    size="small"
                    sx={{ mr: 0.5, mb: 0.5 }}
                    color="primary"
                  />
                  <Chip
                    label={job.experienceLevel}
                    size="small"
                    sx={{ mr: 0.5, mb: 0.5 }}
                  />
                </Box>

                <Typography
                  variant="body2"
                  color="text.secondary"
                  sx={{
                    mb: 2,
                    display: '-webkit-box',
                    WebkitLineClamp: 2,
                    WebkitBoxOrient: 'vertical',
                    overflow: 'hidden',
                  }}
                >
                  {job.description}
                </Typography>

                <Box sx={{ mb: 2 }}>
                  {job.requiredSkills?.slice(0, 3).map((skill: string, index: number) => (
                    <Chip
                      key={index}
                      label={skill}
                      size="small"
                      variant="outlined"
                      sx={{ mr: 0.5, mb: 0.5 }}
                    />
                  ))}
                </Box>

                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <Typography variant="caption" color="text.secondary">
                    Posted {new Date(job.createdAt).toLocaleDateString()}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    {job.applicationCount} applications
                  </Typography>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {jobs.length === 0 && !loading && (
        <Card sx={{ textAlign: 'center', py: 8 }}>
          <WorkIcon sx={{ fontSize: 80, color: 'text.secondary', mb: 2 }} />
          <Typography variant="h6" color="text.secondary" sx={{ mb: 1 }}>
            No jobs found
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Try adjusting your search criteria
          </Typography>
        </Card>
      )}

      {/* Pagination */}
      {totalPages > 1 && (
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
          <Pagination
            count={totalPages}
            page={page + 1}
            onChange={(_, value) => setPage(value - 1)}
            color="primary"
            size="large"
          />
        </Box>
      )}
    </Box>
  )
}

export default JobsPage

