import { useTheme } from '../context/ThemeContext'
import { useToast } from '../components/Toast'
import useDashboard from '../hooks/useDashboard'
import CopyButton from '../components/CopyButton'

const ExternalLinkIcon = () => (
  <svg width="12" height="12" viewBox="0 0 16 16" fill="none">
    <path d="M6 2H3a1 1 0 00-1 1v9a1 1 0 001 1h9a1 1 0 001-1V9" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M10 2h4v4M14 2L8 8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
  </svg>
)

const SearchIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/>
  </svg>
)

const CommandIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <polyline points="4 17 10 11 4 5"/><line x1="12" y1="19" x2="20" y2="19"/>
  </svg>
)

const FileIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/><polyline points="14 2 14 8 20 8"/>
  </svg>
)

export default function Dashboard() {
  const { theme, toggleTheme } = useTheme()
  const { showToast } = useToast()
  const d = useDashboard()

  async function handleToggleServer(server) {
    await d.handleToggleServer(server)
    const isRunning = d.status[server + 'Running']
    showToast(`${server.toUpperCase()} ${isRunning ? 'stopped' : 'started'}`, 'success')
  }

  async function handleSaveConfig() {
    await d.handleSaveConfig()
    showToast('Configuration saved', 'success')
  }

  async function handleGeneratePayload() {
    await d.handleGeneratePayload()
    if (d.payloadResult && !d.payloadResult.startsWith('Error')) {
      showToast('Payload generated', 'success')
    } else if (d.payloadResult?.startsWith('Error')) {
      showToast(d.payloadResult, 'error')
    }
  }

  function handleGenerateJndiPayload() {
    d.handleGenerateJndiPayload()
    if (d.jndiPayloadResult) {
      showToast('Payload URLs generated', 'success')
    }
  }

  function handleGenerateClassLoader() {
    d.handleGenerateClassLoader()
    if (d.classLoaderResult) {
      showToast('ClassLoader URLs generated', 'success')
    }
  }

  async function doUpload(file) {
    await d.doUpload(file)
    showToast(`Uploaded: ${file.name}`, 'success')
  }

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
            <div className={'mode-segment-control' + (d.mode === 'gadget' ? ' gadget-mode' : '')}>
              <button
                className={'mode-segment-btn' + (d.mode === 'jndi' ? ' active' : '')}
                onClick={() => d.switchMode('jndi')}
              >
                JNDI EXP
              </button>
              <button
                className={'mode-segment-btn' + (d.mode === 'gadget' ? ' active' : '')}
                onClick={() => d.switchMode('gadget')}
              >
                Gadget
              </button>
            </div>
            <div className="header-right">
              <a className="wiki-btn" href="https://github.com/qi4L/JYso/wiki" target="_blank" rel="noopener noreferrer" title="Wiki">
                Wiki <ExternalLinkIcon />
              </a>
              <button
                className="theme-toggle"
                onClick={toggleTheme}
                aria-label={theme === 'light' ? 'Switch to dark mode' : 'Switch to light mode'}
                title={theme === 'light' ? 'Dark mode' : 'Light mode'}
              />
              <button className="logout-btn" onClick={d.logout}>Logout</button>
            </div>
          </div>
        </div>

        <div key={d.animKey}>
          {d.mode === 'jndi' && (
            <>
              <div className="glass-card section-enter" style={{ marginBottom: 20 }}>
                <h2>Server Status</h2>
                <div className="status-grid">
                  <div className={'status-item status-clickable' + (d.toggling === 'ldap' ? ' status-toggling' : '')}
                    onClick={() => handleToggleServer('ldap')}
                    title="Click to toggle LDAP server">
                    <span className="status-label">LDAP ({d.status.ldapPort})</span>
                    <span className={'status-value ' + (d.status.ldapRunning ? 'status-online' : 'status-offline')}>
                      {d.status.ldapRunning ? 'ONLINE' : 'OFFLINE'}
                    </span>
                  </div>
                  <div className={'status-item status-clickable' + (d.toggling === 'ldaps' ? ' status-toggling' : '')}
                    onClick={() => handleToggleServer('ldaps')}
                    title="Click to toggle LDAPS server">
                    <span className="status-label">LDAPS ({d.status.ldapsPort})</span>
                    <span className={'status-value ' + (d.status.ldapsRunning ? 'status-online' : 'status-offline')}>
                      {d.status.ldapsRunning ? 'ONLINE' : 'OFFLINE'}
                    </span>
                  </div>
                  <div className={'status-item status-clickable' + (d.toggling === 'http' ? ' status-toggling' : '')}
                    onClick={() => handleToggleServer('http')}
                    title="Click to toggle HTTP server">
                    <span className="status-label">HTTP ({d.status.httpPort})</span>
                    <span className={'status-value ' + (d.status.httpRunning ? 'status-online' : 'status-offline')}>
                      {d.status.httpRunning ? 'ONLINE' : 'OFFLINE'}
                    </span>
                  </div>
                  <div className={'status-item status-clickable' + (d.toggling === 'rmi' ? ' status-toggling' : '')}
                    onClick={() => handleToggleServer('rmi')}
                    title="Click to toggle RMI server">
                    <span className="status-label">RMI ({d.status.rmiPort})</span>
                    <span className={'status-value ' + (d.status.rmiRunning ? 'status-online' : 'status-offline')}>
                      {d.status.rmiRunning ? 'ONLINE' : 'OFFLINE'}
                    </span>
                  </div>
                  <div className="status-item">
                    <span className="status-label">IP Address</span>
                    <span className="status-value" style={{ color: 'var(--accent)' }}>{d.status.ip || '0.0.0.0'}</span>
                  </div>
                  <div className="status-item">
                    <span className="status-label">Version</span>
                    <span className="status-value" style={{ color: 'var(--accent)' }}>1.3.8</span>
                  </div>
                </div>
              </div>

              <div className="glass-card section-enter">
                <div className="control-bar">
                  <div className={'tab-segment-control tabs-3' + (d.activeJndiTab === 'payload' ? ' config-tab' : '') + (d.activeJndiTab === 'logs' ? ' tab-3' : '')}>
                    <button
                      className={'tab-segment-btn' + (d.activeJndiTab === 'config' ? ' active' : '')}
                      onClick={() => d.setActiveJndiTab('config')}
                    >
                      Config
                    </button>
                    <button
                      className={'tab-segment-btn' + (d.activeJndiTab === 'payload' ? ' active' : '')}
                      onClick={() => d.setActiveJndiTab('payload')}
                    >
                      Payload
                    </button>
                    <button
                      className={'tab-segment-btn' + (d.activeJndiTab === 'logs' ? ' active' : '')}
                      onClick={() => d.setActiveJndiTab('logs')}
                    >
                      Logs
                    </button>
                  </div>
                </div>

                {d.activeJndiTab === 'config' && (
                  <div key="jndi-config" className="tab-content-enter">
                    <div className="config-form">
                      <div className="form-group">
                        <label>IP Address</label>
                        <input type="text" value={d.configForm.ip}
                          onChange={e => d.setConfigForm({ ...d.configForm, ip: e.target.value })} />
                      </div>
                      <div className="form-group">
                        <label>LDAP Port</label>
                        <input type="number" value={d.configForm.ldapPort}
                          onChange={e => d.setConfigForm({ ...d.configForm, ldapPort: parseInt(e.target.value) || 1389 })} />
                      </div>
                      <div className="form-group">
                        <label>LDAPS Port</label>
                        <input type="number" value={d.configForm.ldapsPort}
                          onChange={e => d.setConfigForm({ ...d.configForm, ldapsPort: parseInt(e.target.value) || 1669 })} />
                      </div>
                      <div className="form-group">
                        <label>HTTP Port</label>
                        <input type="number" value={d.configForm.httpPort}
                          onChange={e => d.setConfigForm({ ...d.configForm, httpPort: parseInt(e.target.value) || 3456 })} />
                      </div>
                      <div className="form-group">
                        <label>RMI Port</label>
                        <input type="number" value={d.configForm.rmiPort}
                          onChange={e => d.setConfigForm({ ...d.configForm, rmiPort: parseInt(e.target.value) || 1099 })} />
                      </div>
                      <div className="form-group">
                        <label>AES Key</label>
                        <input type="text" value={d.configForm.AESkey}
                          onChange={e => d.setConfigForm({ ...d.configForm, AESkey: e.target.value })} />
                      </div>
                      <div className="form-group">
                        <label>LDAP User</label>
                        <input type="text" value={d.configForm.user} placeholder="ldap bind account"
                          onChange={e => d.setConfigForm({ ...d.configForm, user: e.target.value })} />
                      </div>
                      <div className="form-group">
                        <label>LDAP Password</label>
                        <input type="password" value={d.configForm.PASSWD} placeholder="ldap bind password"
                          onChange={e => d.setConfigForm({ ...d.configForm, PASSWD: e.target.value })} />
                      </div>
                      <div className="form-group">
                        <label>JKS Key Password</label>
                        <input type="password" value={d.configForm.keyPass} placeholder="JKS key password"
                          onChange={e => d.setConfigForm({ ...d.configForm, keyPass: e.target.value })} />
                      </div>
                      <div className="form-group">
                        <label>JKS Cert File</label>
                        <input type="text" value={d.configForm.certFile} placeholder="/path/to/cert.jks"
                          onChange={e => d.setConfigForm({ ...d.configForm, certFile: e.target.value })} />
                      </div>
                      <div className="form-group" style={{ gridColumn: 'span 2' }}>
                        <label className="form-group" style={{ marginBottom: 0 }}>
                          <input type="checkbox" checked={d.configForm.TLSProxy}
                            onChange={e => d.setConfigForm({ ...d.configForm, TLSProxy: e.target.checked })} /> TLS Proxy (LDAPS Port Forwarding)
                        </label>
                      </div>
                      <button className="btn btn-primary" onClick={handleSaveConfig} disabled={d.loading}>
                        {d.loading ? 'Saving...' : 'Save Configuration'}
                      </button>
                    </div>
                  </div>
                )}

                {d.activeJndiTab === 'payload' && (
                  <div key="jndi-payload" className="tab-content-enter">
                    <div className="control-bar">
                      <div className={'tab-segment-control' + (d.payloadSubTab === 'classloader' ? ' config-tab' : '')}>
                        <button
                          className={'tab-segment-btn' + (d.payloadSubTab === 'gadget' ? ' active' : '')}
                          onClick={() => d.setPayloadSubTab('gadget')}
                        >
                          Gadget
                        </button>
                        <button
                          className={'tab-segment-btn' + (d.payloadSubTab === 'classloader' ? ' active' : '')}
                          onClick={() => d.setPayloadSubTab('classloader')}
                        >
                          ClassLoader
                        </button>
                      </div>
                    </div>

                    {d.payloadSubTab === 'gadget' && (
                      <div key="payload-gadget" className="tab-content-enter">
                        <div style={{ position: 'relative' }}>
                          <div className="form-group input-icon-wrap">
                            <label>Gadget</label>
                            <input type="text"
                              value={d.jndiGadgetInput}
                              placeholder="Type or search..."
                              onFocus={() => d.setGadgetOpen(true)}
                              onBlur={() => setTimeout(() => d.setGadgetOpen(false), 150)}
                              onChange={e => { d.setJndiGadgetInput(e.target.value); d.setGadgetSearch(e.target.value); }}
                              style={{ cursor: 'text' }}
                            />
                            <span className="input-icon"><SearchIcon /></span>
                          </div>
                          {d.gadgetOpen && (
                            <div className="gadget-dropdown" style={{ top: 'calc(100% - 8px)' }}>
                              <div className="gadget-list" style={{ maxHeight: 180, border: 'none', borderRadius: 12 }}>
                                {d.filteredGadgets.map(g => (
                                  <div key={g.name}
                                    className={'gadget-item' + (d.selectedGadget === g.name ? ' selected' : '')}
                                    onMouseDown={e => { e.preventDefault(); d.setSelectedGadget(g.name); d.setJndiGadgetInput(g.name); d.setGadgetSearch(g.name); d.setGadgetOpen(false); }}>
                                    {g.name}
                                  </div>
                                ))}
                              </div>
                            </div>
                          )}
                        </div>
                        <div className="form-group input-icon-wrap">
                          <label>Command</label>
                          <textarea rows={4} value={d.payloadCmd} placeholder="e.g. whoami"
                            onChange={e => d.setPayloadCmd(e.target.value)} />
                          <span className="input-icon" style={{ top: 38 }}><CommandIcon /></span>
                        </div>
                        <button className="btn btn-primary" onClick={handleGenerateJndiPayload}
                          disabled={d.loading || (!(d.jndiGadgetInput || d.selectedGadget).trim()) || !d.payloadCmd}>
                          {d.loading ? 'Generating...' : 'Generate'}
                        </button>
                        {d.jndiPayloadResult && (
                          <div className="payload-output" style={{ marginTop: 14, position: 'relative', paddingRight: 42 }}>
                            {d.jndiPayloadResult}
                            <CopyButton text={d.jndiPayloadResult} />
                          </div>
                        )}
                        {d.rmiPayloadResult && (
                          <div className="payload-output" style={{ marginTop: 8, position: 'relative', paddingRight: 42 }}>
                            {d.rmiPayloadResult}
                            <CopyButton text={d.rmiPayloadResult} />
                          </div>
                        )}
                        {d.ldapsPayloadResult && (
                          <div className="payload-output" style={{ marginTop: 8, position: 'relative', paddingRight: 42 }}>
                            {d.ldapsPayloadResult}
                            <CopyButton text={d.ldapsPayloadResult} />
                          </div>
                        )}
                      </div>
                    )}

                    {d.payloadSubTab === 'classloader' && (
                      <div key="payload-classloader" className="tab-content-enter">
                        <div style={{ position: 'relative' }}>
                          <div className="form-group input-icon-wrap">
                            <label>Routing</label>
                            <input type="text"
                              value={d.routing}
                              placeholder="Select route..."
                              onFocus={() => d.setRoutingOpen(true)}
                              onBlur={() => setTimeout(() => d.setRoutingOpen(false), 150)}
                              onChange={e => d.setRouting(e.target.value)}
                              style={{ cursor: 'text' }}
                            />
                            <span className="input-icon"><SearchIcon /></span>
                          </div>
                          {d.routingOpen && (
                            <div className="gadget-dropdown" style={{ top: 'calc(100% - 8px)' }}>
                              <div className="gadget-list" style={{ maxHeight: 220, border: 'none', borderRadius: 12 }}>
                                {d.ROUTING_OPTIONS.map(opt => (
                                  <div key={opt}
                                    className={'gadget-item' + (d.routing === opt ? ' selected' : '')}
                                    onMouseDown={e => { e.preventDefault(); d.setRouting(opt); d.setRoutingOpen(false); }}>
                                    {opt}
                                  </div>
                                ))}
                              </div>
                            </div>
                          )}
                        </div>
                        <div className="form-group input-icon-wrap">
                          <label>FilePath</label>
                          <input type="text" value={d.filePath}
                            placeholder="e.g. /Evil.class"
                            onChange={e => d.setFilePath(e.target.value)} />
                          <span className="input-icon"><FileIcon /></span>
                        </div>
                        <button className="btn btn-primary" onClick={handleGenerateClassLoader}
                          disabled={d.loading || !d.filePath.trim() || !d.routing.trim()}>
                          {d.loading ? 'Generating...' : 'Generate'}
                        </button>
                        {d.classLoaderResult && (
                          <div className="payload-output" style={{ marginTop: 14, position: 'relative', paddingRight: 42 }}>
                            {d.classLoaderResult}
                            <CopyButton text={d.classLoaderResult} />
                          </div>
                        )}
                        {d.rmiClassLoaderResult && (
                          <div className="payload-output" style={{ marginTop: 8, position: 'relative', paddingRight: 42 }}>
                            {d.rmiClassLoaderResult}
                            <CopyButton text={d.rmiClassLoaderResult} />
                          </div>
                        )}
                        {d.ldapsClassLoaderResult && (
                          <div className="payload-output" style={{ marginTop: 8, position: 'relative', paddingRight: 42 }}>
                            {d.ldapsClassLoaderResult}
                            <CopyButton text={d.ldapsClassLoaderResult} />
                          </div>
                        )}
                      </div>
                    )}
                  </div>
                )}

                {d.activeJndiTab === 'logs' && (
                  <div key="jndi-logs" className="tab-content-enter">
                    <div className="log-header">
                      <span>Server Events</span>
                      <button className="btn btn-secondary" style={{ padding: '4px 12px', fontSize: 11, width: 'auto' }}
                        onClick={d.fetchLogs} disabled={d.logLoading}>
                        {d.logLoading ? 'Loading...' : 'Refresh'}
                      </button>
                    </div>
                    <div className="log-container">
                      {d.logLines.length === 0 ? (
                        <div className="log-empty">No events yet. Start a server or wait for incoming requests.</div>
                      ) : (
                        d.logLines.map((line, i) => (
                          <div key={i} className="log-line">{line}</div>
                        ))
                      )}
                      <div ref={d.logEndRef} />
                    </div>
                  </div>
                )}
              </div>
            </>
          )}

          {d.mode === 'gadget' && (
            <div className="gadget-layout" style={{ display: 'flex', gap: 'var(--card-gap)', alignItems: 'flex-start' }}>
              <div className="glass-card section-enter" style={{ flex: 3, marginBottom: 0 }}>
                <div className="header" style={{ padding: 0, marginBottom: 16 }}>
                  <h2>Payload Generator</h2>
                  <button
                    className={`btn btn-secondary${d.showAdvanced ? ' active-tab' : ''}`}
                    style={{ padding: '6px 14px', fontSize: 12, width: 'auto' }}
                    onClick={() => d.setShowAdvanced(v => !v)}
                  >
                    {d.showAdvanced ? 'Hide Options' : 'Advanced Options'}
                  </button>
                </div>
                <div className="control-bar" style={{ marginBottom: 12 }}>
                  <label className="form-group" style={{ marginBottom: 0, display: 'flex', alignItems: 'center', gap: 8, cursor: 'pointer' }}>
                    <div className={`base64-toggle${d.encodeBase64 ? ' active' : ''}`}
                      onClick={() => d.setEncodeBase64(v => !v)}
                      role="switch"
                      aria-checked={d.encodeBase64}
                      tabIndex={0}
                      onKeyDown={e => { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); d.setEncodeBase64(v => !v); } }}>
                      <div className="base64-toggle-thumb" />
                    </div>
                    <span style={{ fontSize: 13, fontWeight: 500, color: 'var(--text-secondary)' }}>Base64 Encode Output</span>
                  </label>
                </div>

                {d.showAdvanced && (
                  <div className="advanced-section section-enter">
                    <div className="adv-grid">
                      <label className="adv-check">
                        <input type="checkbox" checked={d.inherit} onChange={e => d.setInherit(e.target.checked)} />
                        <span>Inherit AbstractTranslet <em>(-i)</em></span>
                      </label>
                      <label className="adv-check">
                        <input type="checkbox" checked={d.obscure} onChange={e => d.setObscure(e.target.checked)} />
                        <span>Obscure (reflection bypass RASP) <em>(-o)</em></span>
                      </label>
                      <label className="adv-check">
                        <input type="checkbox" checked={d.noComSun} onChange={e => d.setNoComSun(e.target.checked)} />
                        <span>Force org.apache.XXX.TemplatesImpl <em>(-ncs)</em></span>
                      </label>
                      <label className="adv-check">
                        <input type="checkbox" checked={d.mozillaClassLoader} onChange={e => d.setMozillaClassLoader(e.target.checked)} />
                        <span>Mozilla DefiningClassLoader <em>(-mcl)</em></span>
                      </label>
                      <label className="adv-check">
                        <input type="checkbox" checked={d.rhino} onChange={e => d.setRhino(e.target.checked)} />
                        <span>Rhino Engine <em>(-rh)</em></span>
                      </label>
                      <label className="adv-check">
                        <input type="checkbox" checked={d.utf8Overlong} onChange={e => d.setUtf8Overlong(e.target.checked)} />
                        <span>UTF-8 Overlong Encoding <em>(-utf)</em></span>
                      </label>
                    </div>
                    <div className="adv-grid" style={{ gridTemplateColumns: '1fr 1fr', marginTop: 10 }}>
                      <div className="form-group" style={{ marginBottom: 0 }}>
                        <label style={{ fontSize: 12 }}>DefineClassFromParameter <em>(-dcfp)</em></label>
                        <input type="text" value={d.dcfp} placeholder="parameter name"
                          onChange={e => d.setDcfp(e.target.value)} />
                      </div>
                      <div className="form-group" style={{ marginBottom: 0 }}>
                        <label style={{ fontSize: 12 }}>Dirty Type <em>(-dt)</em></label>
                        <select value={d.dirtyType} onChange={e => d.setDirtyType(e.target.value)}
                          style={{ width: '100%', padding: '9px 12px', borderRadius: 'var(--input-radius)', border: '1px solid var(--border-color)', background: 'var(--bg-input)', color: 'var(--text-primary)', fontSize: 13, fontFamily: 'inherit', outline: 'none' }}>
                          <option value="">None</option>
                          <option value="1">1: Random Hashable Collections</option>
                          <option value="2">2: LinkedList Nesting</option>
                          <option value="3">3: TC_RESET in Serialized Data</option>
                        </select>
                      </div>
                      <div className="form-group" style={{ marginBottom: 0 }}>
                        <label style={{ fontSize: 12 }}>Dirty Length <em>(-dl)</em></label>
                        <input type="number" value={d.dirtyLength} placeholder="length/counts"
                          onChange={e => d.setDirtyLength(e.target.value)} />
                      </div>
                      <div></div>
                    </div>
                  </div>
                )}

                <div style={{ position: 'relative', marginTop: d.showAdvanced ? 14 : 0 }}>
                  <div className="form-group input-icon-wrap">
                    <label>Gadget</label>
                    <input type="text"
                      value={d.gadgetModeInput}
                      placeholder="Type or search..."
                      onFocus={() => d.setGadgetOpen(true)}
                      onBlur={() => setTimeout(() => d.setGadgetOpen(false), 150)}
                      onChange={e => { d.setGadgetModeInput(e.target.value); d.setGadgetSearch(e.target.value); }}
                      style={{ cursor: 'text' }}
                    />
                    <span className="input-icon"><SearchIcon /></span>
                  </div>
                  {d.gadgetOpen && (
                    <div className="gadget-dropdown" style={{ top: 'calc(100% - 8px)' }}>
                      <div className="gadget-list" style={{ maxHeight: 180, border: 'none', borderRadius: 12 }}>
                        {d.filteredGadgets.map(g => (
                          <div key={g.name}
                            className={'gadget-item' + (d.selectedGadget === g.name ? ' selected' : '')}
                            onMouseDown={e => { e.preventDefault(); d.setSelectedGadget(g.name); d.setGadgetModeInput(g.name); d.setGadgetSearch(g.name); d.setGadgetOpen(false); }}>
                            {g.name}
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                </div>
                <div className="form-group input-icon-wrap">
                  <label>Command</label>
                  <textarea rows={4} value={d.payloadCmd} placeholder="e.g. whoami"
                    onChange={e => d.setPayloadCmd(e.target.value)} />
                  <span className="input-icon" style={{ top: 38 }}><CommandIcon /></span>
                </div>
                <div className="form-group input-icon-wrap">
                  <label>Save As (leave empty to discard)</label>
                  <input type="text" value={d.saveFilename}
                    placeholder="e.g. payload.ser"
                    onChange={e => d.setSaveFilename(e.target.value)} />
                  <span className="input-icon"><FileIcon /></span>
                </div>
                <button className="btn btn-primary" onClick={handleGeneratePayload}
                  disabled={d.loading || !(d.gadgetModeInput || d.selectedGadget).trim()}>
                  {d.loading ? 'Generating...' : 'Generate'}
                </button>
                {d.payloadResult && (
                  <div className="payload-output" style={{ marginTop: 14, position: 'relative', paddingRight: 42 }}>
                    {d.payloadResult}
                    <CopyButton text={d.payloadResult} />
                  </div>
                )}
              </div>
              <div className="glass-card section-enter" style={{ flex: 2, marginBottom: 0 }}>
                <div className="header" style={{ padding: 0, marginBottom: 16 }}>
                  <h2>File Manager</h2>
                  <button className="btn btn-secondary" style={{ padding: '4px 10px', fontSize: 11, width: 'auto' }}
                    onClick={d.fetchFiles} disabled={d.filesLoading}>
                    {d.filesLoading ? 'Loading...' : 'Refresh'}
                  </button>
                </div>
                <div
                  className={'file-upload-zone' + (d.dragOver ? ' drag-over' : '') + (d.uploading ? ' uploading' : '')}
                  onDragOver={d.handleDragOver}
                  onDragLeave={d.handleDragLeave}
                  onDrop={d.handleDrop}
                  onClick={() => d.fileInputRef.current?.click()}
                >
                  <input
                    ref={d.fileInputRef}
                    type="file"
                    style={{ display: 'none' }}
                    onChange={d.handleFileSelect}
                  />
                  {d.uploading ? (
                    <span>Uploading...</span>
                  ) : (
                    <span>Drop file here or click to upload</span>
                  )}
                </div>
                <div className="file-list">
                  {d.files.length === 0 ? (
                    <div className="file-empty">No saved files</div>
                  ) : (
                    d.files.map(f => (
                      <div key={f.name} className="file-row">
                        <span className="file-name" title={f.name}>{f.name}</span>
                        <span className="file-size">{f.size > 1024 ? (f.size / 1024).toFixed(1) + ' KB' : f.size + ' B'}</span>
                        <button className="file-btn" onClick={() => d.handleDownloadFile(f.name)} title="Download">
                          <svg width="14" height="14" viewBox="0 0 16 16" fill="none"><path d="M8 2v8M4 7l4 4 4-4M2 13h12" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>
                        </button>
                        <button className="file-btn file-btn-del" onClick={() => d.handleDeleteFile(f.name)} title="Delete">
                          <svg width="14" height="14" viewBox="0 0 16 16" fill="none"><path d="M3 5h10M6 5V3h4v2M5 5v8h6V5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/></svg>
                        </button>
                      </div>
                    ))
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
