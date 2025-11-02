import axios from 'axios'

// Profile service connects to User Service
const profileApi = axios.create({
  baseURL: 'http://localhost:8081/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

profileApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export interface CandidateProfileData {
  headline?: string
  summary?: string
  skills?: string[]
  yearsOfExperience?: number
  currentLocation?: string
  preferredLocations?: string[]
  currentJobTitle?: string
  education?: string
  university?: string
  resumeUrl?: string
  portfolioUrl?: string
  githubUrl?: string
  linkedinUrl?: string
  languages?: string[]
  expectedSalary?: number
  noticePeriod?: string
  jobPreference?: string
  openToRemote?: boolean
  openToRelocation?: boolean
}

export const profileService = {
  getCandidateProfile: async (userId: number) => {
    const response = await profileApi.get(`/profiles/candidate/${userId}`)
    return response.data
  },

  updateCandidateProfile: async (userId: number, data: CandidateProfileData) => {
    const response = await profileApi.put(`/profiles/candidate/${userId}`, data)
    return response.data
  },

  getRecruiterProfile: async (userId: number) => {
    const response = await profileApi.get(`/profiles/recruiter/${userId}`)
    return response.data
  },

  updateRecruiterProfile: async (userId: number, data: any) => {
    const response = await profileApi.put(`/profiles/recruiter/${userId}`, data)
    return response.data
  },
}

