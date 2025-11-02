import axios from 'axios'

// Job Service runs on different port
const jobApi = axios.create({
  baseURL: 'http://localhost:8082/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

// Add token to requests
jobApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export interface JobFilters {
  keyword?: string
  location?: string
  page?: number
  size?: number
  sortBy?: string
  sortDir?: 'ASC' | 'DESC'
}

export const jobService = {
  getAllJobs: async (filters: JobFilters = {}) => {
    const params = new URLSearchParams()
    if (filters.keyword) params.append('keyword', filters.keyword)
    if (filters.location) params.append('location', filters.location)
    params.append('page', (filters.page || 0).toString())
    params.append('size', (filters.size || 10).toString())
    if (filters.sortBy) params.append('sortBy', filters.sortBy)
    if (filters.sortDir) params.append('sortDir', filters.sortDir)

    const response = await jobApi.get(`/jobs?${params.toString()}`)
    return response.data
  },

  getJobById: async (jobId: number) => {
    const response = await jobApi.get(`/jobs/${jobId}`)
    return response.data
  },

  searchJobs: async (keyword: string, page = 0, size = 10) => {
    const response = await jobApi.get(`/jobs/search?keyword=${keyword}&page=${page}&size=${size}`)
    return response.data
  },

  getLatestJobs: async () => {
    const response = await jobApi.get('/jobs/latest')
    return response.data
  },

  createJob: async (jobData: any) => {
    const response = await jobApi.post('/jobs', jobData)
    return response.data
  },

  updateJob: async (jobId: number, jobData: any) => {
    const response = await jobApi.put(`/jobs/${jobId}`, jobData)
    return response.data
  },

  deleteJob: async (jobId: number) => {
    const response = await jobApi.delete(`/jobs/${jobId}`)
    return response.data
  },
}

