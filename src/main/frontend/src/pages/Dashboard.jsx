import { useState, useEffect, useMemo, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { useTheme } from '../context/ThemeContext'
import {
  getStatus, toggleServer, getGadgets,
  generatePayload as apiGenerate, updateConfig, setAuthToken
} from '../api'

export default function Dashboard() {
  const navigate = useNavigate()
  const { theme, toggleTheme } = useTheme()
  const [mode, setMode] = useState('jndi')
  const [animKey, setAnimKey] = useState(0)
  const [status, setStatus] = useState({})
  const [loading, setLoading] = useState(false)
  const [msg, setMsg] = useState({ success: '', error: '' })
  const [toggling, setToggling] = useState(null)
  const [gadgets, setGadgets] = useState([])
  const [gadgetSearch, setGadgetSearch] = useState('')
  const [selectedGadget, setSelectedGadget] = useState('')
  const [payloadCmd, setPayloadCmd] = useState('')
  const [payloadResult, setPayloadResult] = useState('')

  const [configForm, setConfigForm] = useState({
    ip: '', ldapPort: 1389, ldapsPort: 1669, httpPort: 3456, rmiPort: 1099,
    AESkey: '123', user: '', PASSWD: '', TLSProxy: false, keyPass: '', certFile: ''
  })

  const filteredGadgets = useMemo(() => {
    if (!gadgetSearch) return gadgets
    return gadgets.filter(g => g.name.toLowerCase().includes(gadgetSearch.toLowerCase()))
  }, [gadgetSearch, gadgets])

  const switchMode = useCallback((next) => {
    if (next === mode) return
    setMode(next)
    setAnimKey(k => k + 1)
    setMsg({ success: '', error: '' })
  }, [mode])

  async function loadStatus() {
    try {
      const res = await getStatus()
      setStatus(res.data)
      setConfigForm(prev => ({
        ...prev,
        ip: res.data.ip || '',
        ldapPort: res.data.ldapPort || 1389,
        ldapsPort: res.data.ldapsPort || 1669,
        httpPort: res.data.httpPort || 3456,
        rmiPort: res.data.rmiPort || 1099,
        AESkey: res.data.AESkey || '123',
        user: res.data.user || '',
        PASSWD: res.data.PASSWD || '',
        TLSProxy: res.data.TLSProxy || false,
        keyPass: res.data.keyPass || '',
        certFile: res.data.certFile || ''
      }))
    } catch (e) {
      if (e.response && e.response.status === 401) {
        setAuthToken(null)
        navigate('/login', { replace: true })
      }
    }
  }

  async function loadGadgets() {
    try {
      const res = await getGadgets()
      setGadgets(res.data)
    } catch (e) { /* ignore */ }
  }

  async function handleToggleServer(server) {
    setToggling(server)
    setMsg({ success: '', error: '' })
    try {
      const res = await toggleServer(server)
      if (res.data.success) {
        setMsg({ success: server.toUpperCase() + ' ' + (res.data.running ? 'started' : 'stopped'), error: '' })
        setStatus(res.data.status)
      }
    } catch (e) {
      setMsg({ success: '', error: 'Failed to toggle ' + server.toUpperCase() })
    } finally { setToggling(null) }
  }

  async function handleSaveConfig() {
    setLoading(true)
    setMsg({ success: '', error: '' })
    try {
      await updateConfig(configForm)
      setMsg({ success: 'Configuration saved', error: '' })
      loadStatus()
    } catch (e) {
      setMsg({ success: '', error: 'Failed to save configuration' })
    } finally { setLoading(false) }
  }

  async function handleGeneratePayload() {
    setLoading(true)
    setMsg({ success: '', error: '' })
    try {
      const res = await apiGenerate({ gadget: selectedGadget, command: payloadCmd })
      if (res.data.success) {
        setPayloadResult(res.data.message || 'Payload generated successfully')
        setMsg({ success: 'Payload generated', error: '' })
      } else {
        setPayloadResult('Error: ' + (res.data.error || 'unknown'))
        setMsg({ success: '', error: res.data.error || 'Generation failed' })
      }
    } catch (e) {
      setMsg({ success: '', error: 'Failed to generate payload' })
    } finally { setLoading(false) }
  }

  function logout() {
    setAuthToken(null)
    navigate('/login', { replace: true })
  }

  useEffect(() => { loadStatus(); loadGadgets() }, [])

  return (
    <div>
      <div className="dashboard-bg" />

      <div className="page-shell">
        <div className="glass-card" style={{ marginBottom: 20 }}>
          <div className="header">
            <h1>
              <span className="logo-dot" />
              JYso
            </h1>
            <div className={'mode-segment-control' + (mode === 'gadget' ? ' gadget-mode' : '')}>
              <button
                className={'mode-segment-btn' + (mode === 'jndi' ? ' active' : '')}
                onClick={() => switchMode('jndi')}
              >
                JNDI EXP
              </button>
              <button
                className={'mode-segment-btn' + (mode === 'gadget' ? ' active' : '')}
                onClick={() => switchMode('gadget')}
              >
                Gadget
              </button>
            </div>
            <div className="header-right">
              <button
                className="theme-toggle"
                onClick={toggleTheme}
                aria-label={theme === 'light' ? 'Switch to dark mode' : 'Switch to light mode'}
                title={theme === 'light' ? 'Dark mode' : 'Light mode'}
              >
                <span className="pill-sun" style={{ position: 'absolute', top: 4, left: 5, opacity: theme === 'dark' ? 0.35 : 1, transition: 'opacity 0.2s' }}>
                  &#x2600;&#xFE0F;
                </span>
                <span className="pill-moon" style={{ position: 'absolute', top: 4, right: 5, opacity: theme === 'dark' ? 1 : 0.35, transition: 'opacity 0.2s' }}>
                  &#x1F319;
                </span>
              </button>
              <button className="logout-btn" onClick={logout}>Logout</button>
            </div>
          </div>
        </div>

        {msg.success && <div className="success-msg section-enter">{msg.success}</div>}
        {msg.error && <div className="error-msg section-enter">{msg.error}</div>}

        <div key={animKey}>
          {mode === 'jndi' && (
            <>
              <div className="glass-card section-enter" style={{ marginBottom: 20 }}>
                <h2>Server Status</h2>
                <div className="status-grid">
                  <div className={'status-item status-clickable' + (toggling === 'ldap' ? ' status-toggling' : '')}
                    onClick={() => handleToggleServer('ldap')}
                    title="Click to toggle LDAP server">
                    <span className="status-label">LDAP ({status.ldapPort})</span>
                    <span className={'status-value ' + (status.ldapRunning ? 'status-online' : 'status-offline')}>
                      {status.ldapRunning ? 'ONLINE' : 'OFFLINE'}
                    </span>
                  </div>
                  <div className={'status-item status-clickable' + (toggling === 'ldaps' ? ' status-toggling' : '')}
                    onClick={() => handleToggleServer('ldaps')}
                    title="Click to toggle LDAPS server">
                    <span className="status-label">LDAPS ({status.ldapsPort})</span>
                    <span className={'status-value ' + (status.ldapsRunning ? 'status-online' : 'status-offline')}>
                      {status.ldapsRunning ? 'ONLINE' : 'OFFLINE'}
                    </span>
                  </div>
                  <div className={'status-item status-clickable' + (toggling === 'http' ? ' status-toggling' : '')}
                    onClick={() => handleToggleServer('http')}
                    title="Click to toggle HTTP server">
                    <span className="status-label">HTTP ({status.httpPort})</span>
                    <span className={'status-value ' + (status.httpRunning ? 'status-online' : 'status-offline')}>
                      {status.httpRunning ? 'ONLINE' : 'OFFLINE'}
                    </span>
                  </div>
                  <div className={'status-item status-clickable' + (toggling === 'rmi' ? ' status-toggling' : '')}
                    onClick={() => handleToggleServer('rmi')}
                    title="Click to toggle RMI server">
                    <span className="status-label">RMI ({status.rmiPort})</span>
                    <span className={'status-value ' + (status.rmiRunning ? 'status-online' : 'status-offline')}>
                      {status.rmiRunning ? 'ONLINE' : 'OFFLINE'}
                    </span>
                  </div>
                  <div className="status-item">
                    <span className="status-label">IP Address</span>
                    <span className="status-value" style={{ color: 'var(--accent)' }}>{status.ip || '0.0.0.0'}</span>
                  </div>
                  <div className="status-item">
                    <span className="status-label">Version</span>
                    <span className="status-value" style={{ color: 'var(--accent)' }}>1.3.8</span>
                  </div>
                </div>
              </div>

              <div className="glass-card section-enter">
                <h2>Config</h2>
                <div className="config-form">
                      <div className="form-group">
                        <label>IP Address</label>
                        <input type="text" value={configForm.ip}
                          onChange={e => setConfigForm({ ...configForm, ip: e.target.value })} />
                      </div>
                      <div className="form-group">
                        <label>LDAP Port</label>
                        <input type="number" value={configForm.ldapPort}
                          onChange={e => setConfigForm({ ...configForm, ldapPort: parseInt(e.target.value) || 1389 })} />
                      </div>
                      <div className="form-group">
                        <label>LDAPS Port</label>
                        <input type="number" value={configForm.ldapsPort}
                          onChange={e => setConfigForm({ ...configForm, ldapsPort: parseInt(e.target.value) || 1669 })} />
                      </div>
                      <div className="form-group">
                        <label>HTTP Port</label>
                        <input type="number" value={configForm.httpPort}
                          onChange={e => setConfigForm({ ...configForm, httpPort: parseInt(e.target.value) || 3456 })} />
                      </div>
                      <div className="form-group">
                        <label>RMI Port</label>
                        <input type="number" value={configForm.rmiPort}
                          onChange={e => setConfigForm({ ...configForm, rmiPort: parseInt(e.target.value) || 1099 })} />
                      </div>
                      <div className="form-group">
                        <label>AES Key</label>
                        <input type="text" value={configForm.AESkey}
                          onChange={e => setConfigForm({ ...configForm, AESkey: e.target.value })} />
                      </div>
                      <div className="form-group">
                        <label>LDAP User</label>
                        <input type="text" value={configForm.user} placeholder="ldap bind account"
                          onChange={e => setConfigForm({ ...configForm, user: e.target.value })} />
                      </div>
                      <div className="form-group">
                        <label>LDAP Password</label>
                        <input type="password" value={configForm.PASSWD} placeholder="ldap bind password"
                          onChange={e => setConfigForm({ ...configForm, PASSWD: e.target.value })} />
                      </div>
                      <div className="form-group">
                        <label>JKS Key Password</label>
                        <input type="password" value={configForm.keyPass} placeholder="JKS key password"
                          onChange={e => setConfigForm({ ...configForm, keyPass: e.target.value })} />
                      </div>
                      <div className="form-group">
                        <label>JKS Cert File</label>
                        <input type="text" value={configForm.certFile} placeholder="/path/to/cert.jks"
                          onChange={e => setConfigForm({ ...configForm, certFile: e.target.value })} />
                      </div>
                      <div className="form-group" style={{ gridColumn: 'span 2' }}>
                        <label className="form-group" style={{ marginBottom: 0 }}>
                          <input type="checkbox" checked={configForm.TLSProxy}
                            onChange={e => setConfigForm({ ...configForm, TLSProxy: e.target.checked })} /> TLS Proxy (LDAPS Port Forwarding)
                        </label>
                      </div>
                      <button className="btn btn-primary" onClick={handleSaveConfig} disabled={loading}>
                        {loading ? 'Saving...' : 'Save Configuration'}
                      </button>
                    </div>
              </div>
            </>
          )}

          {mode === 'gadget' && (
            <div className="glass-card section-enter">
              <h2>Payload Generator</h2>
              <div className="grid-2">
                <div>
                  <h3 style={{ fontSize: 13, marginBottom: 10, color: 'var(--text-secondary)', fontWeight: 600, textTransform: 'uppercase', letterSpacing: 0.5 }}>
                    Select Gadget
                  </h3>
                  <div className="form-group">
                    <input type="text" placeholder="Search..." value={gadgetSearch}
                      onChange={e => setGadgetSearch(e.target.value)} />
                  </div>
                  <div className="gadget-list">
                    {filteredGadgets.map(g => (
                      <div key={g.name}
                        className={'gadget-item' + (selectedGadget === g.name ? ' selected' : '')}
                        onClick={() => setSelectedGadget(g.name)}>
                        {g.name}
                      </div>
                    ))}
                  </div>
                </div>
                <div>
                  <div className="form-group">
                    <label>Command</label>
                    <textarea rows={4} value={payloadCmd} placeholder="e.g. whoami"
                      onChange={e => setPayloadCmd(e.target.value)} />
                  </div>
                  <button className="btn btn-primary" onClick={handleGeneratePayload}
                    disabled={loading || !selectedGadget}>
                    {loading ? 'Generating...' : 'Generate Payload'}
                  </button>
                  {payloadResult && (
                    <div className="payload-output">{payloadResult}</div>
                  )}
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
