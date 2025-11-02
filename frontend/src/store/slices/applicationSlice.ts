import { createSlice, PayloadAction } from '@reduxjs/toolkit'

export interface Application {
  id: number
  jobId: number
  candidateId: number
  coverLetter?: string
  resumeUrl?: string
  status: string
  matchScore?: number
  appliedAt: string
}

interface ApplicationState {
  applications: Application[]
  loading: boolean
  totalPages: number
  currentPage: number
}

const initialState: ApplicationState = {
  applications: [],
  loading: false,
  totalPages: 0,
  currentPage: 0,
}

const applicationSlice = createSlice({
  name: 'applications',
  initialState,
  reducers: {
    setApplications: (state, action: PayloadAction<{ applications: Application[]; totalPages: number; currentPage: number }>) => {
      state.applications = action.payload.applications
      state.totalPages = action.payload.totalPages
      state.currentPage = action.payload.currentPage
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload
    },
    addApplication: (state, action: PayloadAction<Application>) => {
      state.applications.unshift(action.payload)
    },
  },
})

export const { setApplications, setLoading, addApplication } = applicationSlice.actions
export default applicationSlice.reducer

