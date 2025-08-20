import React, { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import api from './api'
import { useAuth } from './auth'

const Register: React.FC = () => {
  const [email, setEmail] = useState('user@example.com')
  const [password, setPassword] = useState('secret123')
  const [err, setErr] = useState<string|undefined>()
  const nav = useNavigate()
  const { setUser } = useAuth()

  const submit = async (e: React.FormEvent) => {
    e.preventDefault()
    setErr(undefined)
    try {
      const { data } = await api.post('/api/auth/register', { email, password })
      localStorage.setItem('token', data.token)
      localStorage.setItem('email', data.email)
      localStorage.setItem('role', data.role)
      localStorage.setItem('userId', String(data.userId))
      setUser({ email: data.email, role: data.role, userId: data.userId })
      nav('/')
    } catch (e: any) {
      setErr(e?.response?.data?.message || 'Registration failed')
    }
  }

  return (
    <form onSubmit={submit} className="max-w-md mx-auto bg-white p-6 shadow rounded">
      <h1 className="text-xl font-semibold mb-4">Register</h1>
      {err && <div className="mb-3 text-sm text-red-600">{err}</div>}
      <label className="block text-sm">Email</label>
      <input className="w-full border p-2 rounded mb-3" value={email} onChange={e=>setEmail(e.target.value)} />
      <label className="block text-sm">Password</label>
      <input className="w-full border p-2 rounded mb-4" type="password" value={password} onChange={e=>setPassword(e.target.value)} />
      <button className="w-full bg-gray-900 text-white py-2 rounded">Create Account</button>
      <div className="text-sm mt-3">Have an account? <Link to="/login" className="underline">Login</Link></div>
    </form>
  )
}
export default Register
