import axios from 'axios'

// Resume service for CV parsing
const resumeApi = axios.create({
  baseURL: 'http://localhost:8084/api',
})

resumeApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export const resumeService = {
  parseResume: async (file: File) => {
    const formData = new FormData()
    formData.append('file', file)
    
    const response = await resumeApi.post('/resume/parse', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    return response.data
  },
}

