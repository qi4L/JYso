import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { login, setAuthToken } from '../api'
import ParticleBackground from '../components/ParticleBackground'

const SpinnerIcon = () => (
  <svg className="btn-spinner" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round">
    <path d="M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83M2 12h4M18 12h4M4.93 19.07l2.83-2.83M16.24 7.76l2.83-2.83"/>
  </svg>
)

export default function Login() {
  const navigate = useNavigate()
  const [username, setUsername] = useState('qi')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const res = await login(username, password)
      if (res.data && res.data.token) {
        setAuthToken(res.data.token)
        navigate('/dashboard', { replace: true })
      } else {
        setError('Unexpected response from server')
      }
    } catch (e) {
      if (e.response) {
        if (e.response.status === 401) {
          setError('Invalid username or password.')
        } else {
          setError('Server error: ' + e.response.status)
        }
      } else if (e.request) {
        setError('Cannot connect to server. Is it running?')
      } else {
        setError('Error: ' + e.message)
      }
      setPassword('')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-wrapper">
      <ParticleBackground />
      <div className="login-acrylic-card">
        <div className="login-inner">
          <h1>JYso</h1>
          <p className="subtitle">JNDI Exploitation Toolkit</p>
          <p className="tagline">Sign in to access the dashboard</p>
          {error && <div className="error-msg">{error}</div>}
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Username</label>
              <input
                type="text"
                value={username}
                onChange={e => setUsername(e.target.value)}
                placeholder="Enter username"
              />
            </div>
            <div className="form-group">
              <label>Password</label>
              <input
                type="password"
                value={password}
                onChange={e => setPassword(e.target.value)}
                placeholder="Enter password"
              />
            </div>
            <button className="btn btn-primary" type="submit" disabled={loading}>
              {loading ? (
                <>
                  <SpinnerIcon />
                  Signing in...
                </>
              ) : (
                'Sign In'
              )}
            </button>
          </form>
        </div>
      </div>
    </div>
  )
}
