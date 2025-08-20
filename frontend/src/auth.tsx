import React, { createContext, useContext, useState } from 'react'
type User = { userId: number, role: 'ADMIN' | 'USER', email: string }
type AuthCtx = { user?: User, setUser: (u?:User)=>void }
const Ctx = createContext<AuthCtx>({ setUser: ()=>{} })
export const useAuth = ()=> useContext(Ctx)
export const AuthProvider: React.FC<{children: React.ReactNode}> = ({children}) => {
  const [user, setUser] = useState<User | undefined>(()=>{
    const email = localStorage.getItem('email'); const role = localStorage.getItem('role') as 'ADMIN'|'USER'|null
    const userId = localStorage.getItem('userId'); const token = localStorage.getItem('token')
    return (email && role && userId && token) ? { email, role, userId: Number(userId) } : undefined
  })
  return <Ctx.Provider value={{user, setUser}}>{children}</Ctx.Provider>
}
