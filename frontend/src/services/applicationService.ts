import axios from 'axios'

// Application Service also on Job Service port
const appApi = axios.create({
  baseURL: 'http://localhost:8082/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

appApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export interface ApplicationData {
  jobId: number
  candidateId: number
  coverLetter?: string
  resumeUrl?: string
}

export const applicationService = {
  applyForJob: async (data: ApplicationData) => {
    const response = await appApi.post('/applications', data)
    return response.data
  },

  getApplicationById: async (applicationId: number) => {
    const response = await appApi.get(`/applications/${applicationId}`)
    return response.data
  },

  getApplicationsByCandidate: async (candidateId: number, page = 0, size = 10) => {
    const response = await appApi.get(`/applications/candidate/${candidateId}?page=${page}&size=${size}`)
    return response.data
  },

  getApplicationsByJob: async (jobId: number, page = 0, size = 10) => {
    const response = await appApi.get(`/applications/job/${jobId}?page=${page}&size=${size}`)
    return response.data
  },

  updateApplicationStatus: async (applicationId: number, status: string, notes?: string) => {
    const params = new URLSearchParams()
    params.append('status', status)
    if (notes) params.append('notes', notes)
    
    const response = await appApi.put(`/applications/${applicationId}/status?${params.toString()}`)
    return response.data
  },

  withdrawApplication: async (applicationId: number, candidateId: number) => {
    const response = await appApi.delete(`/applications/${applicationId}/withdraw?candidateId=${candidateId}`)
    return response.data
  },
}

