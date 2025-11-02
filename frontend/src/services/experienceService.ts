import axios from 'axios'

const experienceApi = axios.create({
  baseURL: 'http://localhost:8081/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

experienceApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export const experienceService = {
  addWorkExperience: async (data: any) => {
    const response = await experienceApi.post('/experience/work', data)
    return response.data
  },

  getUserExperiences: async (userId: number) => {
    const response = await experienceApi.get(`/experience/work/${userId}`)
    return response.data
  },

  addProject: async (data: any) => {
    const response = await experienceApi.post('/experience/projects', data)
    return response.data
  },

  getUserProjects: async (userId: number) => {
    const response = await experienceApi.get(`/experience/projects/${userId}`)
    return response.data
  },
}

