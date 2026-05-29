export default function ConfigForm({ config, onChange }) {
  function update(field, value) {
    onChange({ ...config, [field]: value })
  }

  return (
    <div className="config-form">
      <div className="form-group">
        <label>IP Address</label>
        <input type="text" value={config.ip}
          onChange={e => update('ip', e.target.value)} />
      </div>
      <div className="form-group">
        <label>LDAP Port</label>
        <input type="number" value={config.ldapPort}
          onChange={e => update('ldapPort', parseInt(e.target.value) || 1389)} />
      </div>
      <div className="form-group">
        <label>LDAPS Port</label>
        <input type="number" value={config.ldapsPort}
          onChange={e => update('ldapsPort', parseInt(e.target.value) || 1669)} />
      </div>
      <div className="form-group">
        <label>HTTP Port</label>
        <input type="number" value={config.httpPort}
          onChange={e => update('httpPort', parseInt(e.target.value) || 3456)} />
      </div>
      <div className="form-group">
        <label>RMI Port</label>
        <input type="number" value={config.rmiPort}
          onChange={e => update('rmiPort', parseInt(e.target.value) || 1099)} />
      </div>
      <div className="form-group">
        <label>AES Key</label>
        <input type="text" value={config.AESkey}
          onChange={e => update('AESkey', e.target.value)} />
      </div>
      <div className="form-group">
        <label>LDAP User</label>
        <input type="text" value={config.user} placeholder="ldap bind account"
          onChange={e => update('user', e.target.value)} />
      </div>
      <div className="form-group">
        <label>LDAP Password</label>
        <input type="password" value={config.PASSWD} placeholder="ldap bind password"
          onChange={e => update('PASSWD', e.target.value)} />
      </div>
      <div className="form-group">
        <label>JKS Key Password</label>
        <input type="password" value={config.keyPass} placeholder="JKS key password"
          onChange={e => update('keyPass', e.target.value)} />
      </div>
      <div className="form-group">
        <label>JKS Cert File</label>
        <input type="text" value={config.certFile} placeholder="/path/to/cert.jks"
          onChange={e => update('certFile', e.target.value)} />
      </div>
      <div className="form-group" style={{ gridColumn: 'span 3' }}>
        <label className="form-group" style={{ marginBottom: 0 }}>
          <input type="checkbox" checked={config.TLSProxy}
            onChange={e => update('TLSProxy', e.target.checked)} /> TLS Proxy (LDAPS Port Forwarding)
        </label>
      </div>
    </div>
  )
}
