import axios from 'axios'

let authToken = null

export function setAuthToken(token) {
  authToken = token
}

export function getAuthToken() {
  return authToken
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

export default api
