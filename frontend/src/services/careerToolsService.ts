import axios from 'axios'

const careerToolsApi = axios.create({
  baseURL: 'http://localhost:8083/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

careerToolsApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export const careerToolsService = {
  calculateSkillROI: async (data: any) => {
    const response = await careerToolsApi.post('/matching/calculate-skill-roi', data)
    return response.data
  },

  negotiateSalary: async (data: any) => {
    const response = await careerToolsApi.post('/matching/negotiate-salary', data)
    return response.data
  },
}

