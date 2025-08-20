import React from 'react'
import { Routes, Route, Link, Navigate, useNavigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './auth'
import Tasks from './Tasks'
import Login from './Login'
import Register from './Register'

const Guard: React.FC<{children: React.ReactNode}> = ({children})=>{
  const { user } = useAuth()
  if (!user) return <Navigate to="/login" replace />
  return <>{children}</>
}

const Layout: React.FC<{children: React.ReactNode}> = ({children})=>{
  const nav = useNavigate(); const { user, setUser } = useAuth()
  const logout = ()=>{ localStorage.clear(); setUser(undefined); nav('/login') }
  return (
    <div className="max-w-5xl mx-auto p-6">
      <div className="flex items-center justify-between mb-6">
        <Link to="/" className="text-2xl font-bold">Task Manager</Link>
        <div className="space-x-3">
          {user ? (<>
            <span className="text-sm">Hi, {user.email} ({user.role})</span>
            <button className="px-3 py-1 rounded bg-gray-800 text-white" onClick={logout}>Logout</button>
          </>) : (<>
            <Link to="/login" className="px-3 py-1 rounded bg-gray-800 text-white">Login</Link>
            <Link to="/register" className="px-3 py-1 rounded border">Register</Link>
          </>)}
        </div>
      </div>
      {children}
    </div>
  )
}

const App: React.FC = () => (
  <AuthProvider>
    <Layout>
      <Routes>
        <Route path="/" element={<Guard><Tasks/></Guard>} />
        <Route path="/login" element={<Login/>} />
        <Route path="/register" element={<Register/>} />
      </Routes>
    </Layout>
  </AuthProvider>
)

export default App
