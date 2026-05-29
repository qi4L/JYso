import { useState, useEffect } from 'react'
import { useToast } from '../components/Toast'
import useDashboard from '../hooks/useDashboard'
import ServerStatusCard from '../components/ServerStatusCard'
import GadgetSelector from '../components/GadgetSelector'
import PayloadOutput from '../components/PayloadOutput'
import ConfigForm from '../components/ConfigForm'

const ExternalLinkIcon = () => (
  <svg width="12" height="12" viewBox="0 0 16 16" fill="none">
    <path d="M6 2H3a1 1 0 00-1 1v9a1 1 0 001 1h9a1 1 0 001-1V9" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
    <path d="M10 2h4v4M14 2L8 8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
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

const JndiIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <circle cx="12" cy="12" r="3"/><path d="M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83M2 12h4M18 12h4M4.93 19.07l2.83-2.83M16.24 7.76l2.83-2.83"/>
  </svg>
)

const GadgetIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71"/>
    <path d="M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71"/>
  </svg>
)

const ArrowRightIcon = () => (
  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <polyline points="9 18 15 12 9 6"/>
  </svg>
)

const CloseIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <line x1="18" y1="6" x2="6" y2="18"/>
    <line x1="6" y1="6" x2="18" y2="18"/>
  </svg>
)

const MenuIcon = () => (
  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <line x1="3" y1="6" x2="21" y2="6"/>
    <line x1="3" y1="12" x2="21" y2="12"/>
    <line x1="3" y1="18" x2="21" y2="18"/>
  </svg>
)

const WikiIcon = () => (
  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <circle cx="12" cy="12" r="10"/><path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3"/><line x1="12" y1="17" x2="12.01" y2="17"/>
  </svg>
)

const LogoutIcon = () => (
  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/>
  </svg>
)

export default function Dashboard() {
  const { showToast } = useToast()
  const d = useDashboard()
  const [sidebarHidden, setSidebarHidden] = useState(true)

  useEffect(() => {
    function handleMouseMove(e) {
      const cards = document.querySelectorAll('.glass-card')
      cards.forEach(card => {
        const rect = card.getBoundingClientRect()
        const x = e.clientX - rect.left
        const y = e.clientY - rect.top
        card.style.setProperty('--mouse-x', `${x}px`)
        card.style.setProperty('--mouse-y', `${y}px`)
      })
    }
    window.addEventListener('mousemove', handleMouseMove)
    return () => window.removeEventListener('mousemove', handleMouseMove)
  }, [])

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

  const servers = [
    { key: 'ldap', label: 'LDAP', port: d.status.ldapPort, running: d.status.ldapRunning },
    { key: 'ldaps', label: 'LDAPS', port: d.status.ldapsPort, running: d.status.ldapsRunning },
    { key: 'http', label: 'HTTP', port: d.status.httpPort, running: d.status.httpRunning },
    { key: 'rmi', label: 'RMI', port: d.status.rmiPort, running: d.status.rmiRunning },
  ]

  const ROUTING_ITEMS = d.ROUTING_OPTIONS.map(opt => ({ name: opt }))

  return (
    <div>
      <div className="dashboard-bg" />

      <div className="page-shell dashboard-layout">
        <button
          className="sidebar-trigger-btn"
          onClick={() => setSidebarHidden(v => !v)}
          title={sidebarHidden ? 'Expand sidebar' : 'Collapse sidebar'}
        >
          {sidebarHidden ? <MenuIcon /> : <CloseIcon />}
        </button>

        <aside className={'sidebar sidebar-hidden' + (sidebarHidden ? '' : ' sidebar-visible')}>
          <div className="sidebar-content">
            <button
              className="sidebar-toggle-btn"
              onClick={() => setSidebarHidden(v => !v)}
              title={sidebarHidden ? 'Expand sidebar' : 'Collapse sidebar'}
            >
              <span className="toggle-icon-default">
                {sidebarHidden ? <MenuIcon /> : <CloseIcon />}
              </span>
              <span className="toggle-icon-hover">
                <ArrowRightIcon />
              </span>
            </button>
            <div className="header sidebar-header">
              <h1>JYso <span style={{fontSize: 11, fontWeight: 400, color: 'rgba(255,255,255,0.35)', marginLeft: 4}}>v1.3.8</span></h1>
              <div className="sidebar-nav">
                <button
                  className={'sidebar-nav-item' + (d.mode === 'jndi' ? ' active' : '')}
                  onClick={() => d.switchMode('jndi')}
                  title="JNDI EXP"
                >
                  <JndiIcon />
                  <span>JNDI EXP</span>
                </button>
                <button
                  className={'sidebar-nav-item' + (d.mode === 'gadget' ? ' active' : '')}
                  onClick={() => d.switchMode('gadget')}
                  title="Gadget"
                >
                  <GadgetIcon />
                  <span>Gadget</span>
                </button>
              </div>
              <div className="header-right sidebar-actions">
                <a className="wiki-btn" href="https://github.com/qi4L/JYso/wiki" target="_blank" rel="noopener noreferrer" title="Wiki">
                  <WikiIcon />
                  <span>Wiki <ExternalLinkIcon /></span>
                </a>
                <button className="logout-btn" onClick={d.logout} title="Logout">
                  <LogoutIcon />
                  <span>Logout</span>
                </button>
              </div>
            </div>
          </div>
        </aside>

        <main className={'main-content' + (sidebarHidden ? ' main-content-full' : '')}>
          <div key={d.animKey}>
          {d.mode === 'jndi' && (
            <>
              <div style={{ display: 'flex', gap: 20, marginBottom: 20, alignItems: 'flex-start' }}>
                <div className="glass-card section-enter" style={{ width: 200, flexShrink: 0, marginBottom: 0 }}>
                  <h2>Server Status</h2>
                  <div className="status-grid" style={{ gridTemplateColumns: '1fr' }}>
                    {servers.map(s => (
                      <ServerStatusCard
                        key={s.key}
                        label={s.label}
                        port={s.port}
                        isRunning={s.running}
                        toggling={d.toggling === s.key}
                        onClick={() => handleToggleServer(s.key)}
                      />
                    ))}
                    <div className="status-item">
                      <span className="status-label">IP Address</span>
                      <span className="status-value" style={{ color: 'var(--accent)' }}>{d.status.ip || '0.0.0.0'}</span>
                    </div>
                  </div>
                </div>

                <div className="glass-card section-enter" style={{ flex: 1, marginBottom: 0 }}>
                  <div className="control-bar" style={{ justifyContent: 'space-between', alignItems: 'center' }}>
                    <div className={'tab-segment-control tabs-3' + (d.activeJndiTab === 'payload' ? ' config-tab' : '') + (d.activeJndiTab === 'logs' ? ' tab-3' : '')}>
                      <button className={'tab-segment-btn' + (d.activeJndiTab === 'config' ? ' active' : '')} onClick={() => d.setActiveJndiTab('config')}>Config</button>
                      <button className={'tab-segment-btn' + (d.activeJndiTab === 'payload' ? ' active' : '')} onClick={() => d.setActiveJndiTab('payload')}>Payload</button>
                      <button className={'tab-segment-btn' + (d.activeJndiTab === 'logs' ? ' active' : '')} onClick={() => d.setActiveJndiTab('logs')}>Logs</button>
                    </div>
                    {d.activeJndiTab === 'config' && (
                      <button className="btn btn-primary" style={{ width: 'auto', padding: '8px 18px', fontSize: 14 }} onClick={handleSaveConfig} disabled={d.loading}>
                        {d.loading ? 'Saving...' : 'Save Configuration'}
                      </button>
                    )}
                    {d.activeJndiTab === 'payload' && d.payloadSubTab === 'gadget' && (
                      <button className="btn btn-primary" style={{ width: 'auto', padding: '8px 18px', fontSize: 14 }} onClick={handleGenerateJndiPayload}
                        disabled={d.loading || !(d.jndiGadgetInput || d.selectedGadget).trim() || !d.payloadCmd}>
                        {d.loading ? 'Generating...' : 'Generate'}
                      </button>
                    )}
                    {d.activeJndiTab === 'payload' && d.payloadSubTab === 'classloader' && (
                      <button className="btn btn-primary" style={{ width: 'auto', padding: '8px 18px', fontSize: 14 }} onClick={handleGenerateClassLoader}
                        disabled={d.loading || !d.filePath.trim() || !d.routing.trim()}>
                        {d.loading ? 'Generating...' : 'Generate'}
                      </button>
                    )}
                  </div>

                  {d.activeJndiTab === 'config' && (
                    <div key="jndi-config" className="tab-content-enter">
                      <ConfigForm config={d.configForm} onChange={d.setConfigForm} />
                    </div>
                  )}

                  {d.activeJndiTab === 'payload' && (
                    <div key="jndi-payload" className="tab-content-enter">
                      <div className="control-bar">
                        <div className={'tab-segment-control' + (d.payloadSubTab === 'classloader' ? ' config-tab' : '')}>
                          <button className={'tab-segment-btn' + (d.payloadSubTab === 'gadget' ? ' active' : '')} onClick={() => d.setPayloadSubTab('gadget')}>Gadget</button>
                          <button className={'tab-segment-btn' + (d.payloadSubTab === 'classloader' ? ' active' : '')} onClick={() => d.setPayloadSubTab('classloader')}>ClassLoader</button>
                        </div>
                      </div>

                      {d.payloadSubTab === 'gadget' && (
                        <div key="payload-gadget" className="tab-content-enter">
                          <GadgetSelector
                            value={d.jndiGadgetInput}
                            onChange={d.setJndiGadgetInput}
                            items={d.filteredGadgets}
                            searchValue={d.gadgetSearch}
                            onSearchChange={v => { d.setGadgetSearch(v); d.setSelectedGadget(v); }}
                            open={d.gadgetOpen}
                            onOpenChange={d.setGadgetOpen}
                            selectedGadget={d.selectedGadget}
                          />
                          <div className="form-group input-icon-wrap">
                            <label>Command</label>
                            <textarea rows={4} value={d.payloadCmd} placeholder="e.g. whoami"
                              onChange={e => d.setPayloadCmd(e.target.value)} />
                            <span className="input-icon" style={{ top: 38 }}><CommandIcon /></span>
                          </div>
                          <PayloadOutput text={d.jndiPayloadResult} />
                          <PayloadOutput text={d.rmiPayloadResult} style={{ marginTop: 8 }} />
                          <PayloadOutput text={d.ldapsPayloadResult} style={{ marginTop: 8 }} />
                        </div>
                      )}

                      {d.payloadSubTab === 'classloader' && (
                        <div key="payload-classloader" className="tab-content-enter">
                          <GadgetSelector
                            value={d.routing}
                            onChange={d.setRouting}
                            items={ROUTING_ITEMS}
                            open={d.routingOpen}
                            onOpenChange={d.setRoutingOpen}
                            selectedGadget={d.routing}
                            label="Routing"
                          />
                          <div className="form-group input-icon-wrap">
                            <label>FilePath</label>
                            <input type="text" value={d.filePath}
                              placeholder="e.g. /Evil.class"
                              onChange={e => d.setFilePath(e.target.value)} />
                            <span className="input-icon"><FileIcon /></span>
                          </div>
                          <PayloadOutput text={d.classLoaderResult} />
                          <PayloadOutput text={d.rmiClassLoaderResult} style={{ marginTop: 8 }} />
                          <PayloadOutput text={d.ldapsClassLoaderResult} style={{ marginTop: 8 }} />
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
              </div>
            </>
          )}

          {d.mode === 'gadget' && (
            <div className="gadget-layout" style={{ display: 'flex', gap: 'var(--card-gap)', alignItems: 'flex-start' }}>
              <div className="glass-card section-enter" style={{ flex: 3, marginBottom: 0 }}>
                <div className="header" style={{ padding: 0, marginBottom: 16 }}>
                  <h2>Payload Generator</h2>
                  <div style={{ display: 'flex', gap: 10, alignItems: 'center' }}>
                    <button
                      className={`btn btn-secondary${d.showAdvanced ? ' active-tab' : ''}`}
                      style={{ padding: '6px 14px', fontSize: 12, width: 'auto' }}
                      onClick={() => d.setShowAdvanced(v => !v)}
                    >
                      {d.showAdvanced ? 'Hide Options' : 'Advanced Options'}
                    </button>
                    <button className="btn btn-primary" style={{ width: 'auto', padding: '8px 18px', fontSize: 14 }} onClick={handleGeneratePayload}
                      disabled={d.loading || !(d.gadgetModeInput || d.selectedGadget).trim()}>
                      {d.loading ? 'Generating...' : 'Generate'}
                    </button>
                  </div>
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

                <div style={{ marginTop: d.showAdvanced ? 14 : 0 }}>
                  <GadgetSelector
                    value={d.gadgetModeInput}
                    onChange={d.setGadgetModeInput}
                    items={d.filteredGadgets}
                    searchValue={d.gadgetSearch}
                    onSearchChange={v => { d.setGadgetSearch(v); d.setSelectedGadget(v); }}
                    open={d.gadgetOpen}
                    onOpenChange={d.setGadgetOpen}
                    selectedGadget={d.selectedGadget}
                  />
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
                <PayloadOutput text={d.payloadResult} />
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
                  {d.uploading ? <span>Uploading...</span> : <span>Drop file here or click to upload</span>}
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
        </main>
      </div>
    </div>
  )
}
