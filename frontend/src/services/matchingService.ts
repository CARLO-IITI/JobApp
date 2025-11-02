import axios from 'axios'

// Matching service for AI-powered matching
const matchingApi = axios.create({
  baseURL: 'http://localhost:8083/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

matchingApi.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export const matchingService = {
  autoScoreApplications: async (jobRequirements: any, applications: any[]) => {
    const response = await matchingApi.post('/matching/auto-score-applications', {
      jobRequirements,
      applications,
    })
    return response.data
  },

  rankCandidates: async (jobRequirements: any, candidates: any[]) => {
    const response = await matchingApi.post('/matching/rank-candidates', {
      jobRequirements,
      candidates,
    })
    return response.data
  },

  analyzeRejection: async (candidateProfile: any, jobRequirements: any) => {
    const response = await matchingApi.post('/matching/analyze-rejection', {
      candidateProfile,
      jobRequirements,
    })
    return response.data
  },

  findMatchingJobs: async (candidateData: any, jobs: any[]) => {
    const response = await matchingApi.post('/matching/find-jobs', candidateData, {
      params: { jobs },
    })
    return response.data
  },

  predictJobMatch: async (candidateProfile: any, jobDetails: any) => {
    const response = await matchingApi.post('/matching/predict-job-match', {
      candidateProfile,
      jobDetails,
    })
    return response.data
  },

  calculateSkillROI: async (data: any) => {
    const response = await matchingApi.post('/matching/calculate-skill-roi', data)
    return response.data
  },

  negotiateSalary: async (data: any) => {
    const response = await matchingApi.post('/matching/negotiate-salary', data)
    return response.data
  },

  verifyAuthenticity: async (candidateProfile: any) => {
    const response = await matchingApi.post('/matching/verify-authenticity', candidateProfile)
    return response.data
  },
}



