import axios from 'axios'

let authToken = localStorage.getItem('jyso_token')

export function setAuthToken(token) {
  authToken = token
  if (token) {
    localStorage.setItem('jyso_token', token)
  } else {
    localStorage.removeItem('jyso_token')
  }
}

export function getAuthToken() {
  return authToken || localStorage.getItem('jyso_token')
}

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

api.interceptors.request.use(config => {
  if (authToken) {
    config.headers.Authorization = 'Bearer ' + authToken
  }
  return config
})

api.interceptors.response.use(
  response => response,
  error => {
    if (error.response && error.response.status === 401) {
      authToken = null
      localStorage.removeItem('jyso_token')
    }
    return Promise.reject(error)
  }
)

export function login(username, password) {
  return axios.post('/api/auth/login', { username, password })
}

export function getStatus() {
  return api.get('/status')
}

export function startServers(data) {
  return api.post('/servers/start', data)
}

export function stopServer(server) {
  return api.post('/servers/stop', { server })
}

export function toggleServer(server) {
  return api.post('/servers/toggle', { server })
}

export function getGadgets() {
  return api.get('/gadgets')
}

export function generatePayload(data) {
  return api.post('/payload/generate', data)
}

export function updateConfig(data) {
  return api.post('/config/update', data)
}

export function getConfig() {
  return api.get('/config')
}

export function getLogs(count) {
  return api.get('/logs', { params: { count } })
}

export function getFiles() {
  return api.get('/files')
}

export function downloadFile(name) {
  return api.get('/files/download', { params: { name }, responseType: 'blob' })
}

export function deleteFile(name) {
  return api.post('/files/delete', { name })
}

export function uploadFile(formData) {
  return api.post('/files/upload', formData)
}

export default api
