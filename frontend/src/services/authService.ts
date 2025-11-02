import api from './api'

export interface LoginData {
  email: string
  password: string
}

export interface RegisterData {
  email: string
  password: string
  fullName: string
  phone?: string
  role: 'CANDIDATE' | 'RECRUITER'
}

export const authService = {
  login: async (data: LoginData) => {
    const response = await api.post('/auth/login', data)
    return response.data
  },

  register: async (data: RegisterData) => {
    const response = await api.post('/auth/register', data)
    return response.data
  },

  validateToken: async () => {
    const response = await api.get('/auth/validate')
    return response.data
  },
}

