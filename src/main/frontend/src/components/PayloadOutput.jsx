import CopyButton from './CopyButton'
import useTypewriter from '../hooks/useTypewriter'

function TypewriterPayload({ text }) {
  const { displayed } = useTypewriter(text, 15, true)
  return <>{displayed}</>
}

export default function PayloadOutput({ text, style = {} }) {
  if (!text) return null
  return (
    <div className="payload-output" style={{ marginTop: 14, position: 'relative', paddingRight: 42, ...style }}>
      <TypewriterPayload text={text} />
      <CopyButton text={text} />
    </div>
  )
}
