import { useEffect, useRef } from 'react'
import { useTheme } from '../context/ThemeContext'

export default function AnimatedBackground() {
  const { theme } = useTheme()
  const canvasRef = useRef(null)
  const mouseRef = useRef({ x: 0.5, y: 0.5 })
  const rafRef = useRef(null)

  useEffect(() => {
    const canvas = canvasRef.current
    if (!canvas) return
    const ctx = canvas.getContext('2d')
    let width, height
    let time = 0

    function resize() {
      const dpr = Math.min(window.devicePixelRatio || 1, 2)
      width = window.innerWidth
      height = window.innerHeight
      canvas.width = width * dpr
      canvas.height = height * dpr
      canvas.style.width = width + 'px'
      canvas.style.height = height + 'px'
      ctx.setTransform(dpr, 0, 0, dpr, 0, 0)
    }
    resize()
    window.addEventListener('resize', resize)

    function onMouseMove(e) {
      mouseRef.current.x = e.clientX / width
      mouseRef.current.y = e.clientY / height
    }
    window.addEventListener('mousemove', onMouseMove)

    const blobs = [
      { x: 0.25, y: 0.30, r: 0.44, sx: 0.12, sy: 0.08, color: theme === 'dark' ? '90, 40, 255' : '50, 80, 255', phase: 0 },
      { x: 0.70, y: 0.25, r: 0.38, sx: 0.10, sy: 0.11, color: theme === 'dark' ? '255, 30, 180' : '255, 60, 180', phase: 1.5 },
      { x: 0.50, y: 0.70, r: 0.46, sx: 0.09, sy: 0.10, color: theme === 'dark' ? '20, 140, 255' : '30, 200, 230', phase: 3 },
      { x: 0.80, y: 0.55, r: 0.34, sx: 0.11, sy: 0.09, color: theme === 'dark' ? '255, 140, 30' : '255, 150, 40', phase: 4.5 },
      { x: 0.20, y: 0.75, r: 0.36, sx: 0.10, sy: 0.12, color: theme === 'dark' ? '30, 220, 160' : '50, 230, 140', phase: 2 },
    ]

    function draw() {
      time += 0.0035
      ctx.clearRect(0, 0, width, height)

      const mx = mouseRef.current.x
      const my = mouseRef.current.y

      blobs.forEach(blob => {
        const bx = width * (blob.x + Math.sin(time + blob.phase) * blob.sx)
        const by = height * (blob.y + Math.cos(time * 0.7 + blob.phase) * blob.sy)
        const br = Math.min(width, height) * blob.r

        const parallaxX = (mx - 0.5) * 30
        const parallaxY = (my - 0.5) * 30

        const gradient = ctx.createRadialGradient(
          bx + parallaxX, by + parallaxY, 0,
          bx + parallaxX, by + parallaxY, br
        )

        const alpha = theme === 'dark' ? 0.22 : 0.40
        gradient.addColorStop(0, `rgba(${blob.color}, ${alpha})`)
        gradient.addColorStop(0.5, `rgba(${blob.color}, ${alpha * 0.35})`)
        gradient.addColorStop(1, `rgba(${blob.color}, 0)`)

        ctx.fillStyle = gradient
        ctx.fillRect(0, 0, width, height)
      })

      rafRef.current = requestAnimationFrame(draw)
    }

    rafRef.current = requestAnimationFrame(draw)

    return () => {
      window.removeEventListener('resize', resize)
      window.removeEventListener('mousemove', onMouseMove)
      cancelAnimationFrame(rafRef.current)
    }
  }, [theme])

  return (
    <canvas
      ref={canvasRef}
      style={{
        position: 'fixed',
        top: 0,
        left: 0,
        zIndex: -1,
        pointerEvents: 'none',
      }}
    />
  )
}
