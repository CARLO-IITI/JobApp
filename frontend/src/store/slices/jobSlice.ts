import { createSlice, PayloadAction } from '@reduxjs/toolkit'

export interface Job {
  id: number
  title: string
  description: string
  companyName: string
  companyId: number
  location: string
  jobType: string
  experienceLevel: string
  requiredSkills: string[]
  minExperience?: number
  maxExperience?: number
  minSalary?: number
  maxSalary?: number
  currency?: string
  remoteAllowed: boolean
  openings: number
  status: string
  postedBy: number
  viewCount: number
  applicationCount: number
  createdAt: string
}

interface JobState {
  jobs: Job[]
  currentJob: Job | null
  loading: boolean
  totalPages: number
  currentPage: number
}

const initialState: JobState = {
  jobs: [],
  currentJob: null,
  loading: false,
  totalPages: 0,
  currentPage: 0,
}

const jobSlice = createSlice({
  name: 'jobs',
  initialState,
  reducers: {
    setJobs: (state, action: PayloadAction<{ jobs: Job[]; totalPages: number; currentPage: number }>) => {
      state.jobs = action.payload.jobs
      state.totalPages = action.payload.totalPages
      state.currentPage = action.payload.currentPage
    },
    setCurrentJob: (state, action: PayloadAction<Job>) => {
      state.currentJob = action.payload
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload
    },
    clearJobs: (state) => {
      state.jobs = []
      state.currentJob = null
    },
  },
})

export const { setJobs, setCurrentJob, setLoading, clearJobs } = jobSlice.actions
export default jobSlice.reducer

