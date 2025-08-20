import React, { useEffect, useState } from 'react'
import api from './api'

type Task = {
  id: number
  title: string
  description?: string
  status: 'TODO' | 'IN_PROGRESS' | 'DONE'
  priority: 'LOW' | 'MEDIUM' | 'HIGH'
  dueDate?: string
  attachments: { id:number, filename:string, url:string }[]
}

const Tasks: React.FC = () => {
  const [items, setItems] = useState<Task[]>([])
  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')
  const [priority, setPriority] = useState<'LOW'|'MEDIUM'|'HIGH'>('MEDIUM')
  const [status, setStatus] = useState<'TODO'|'IN_PROGRESS'|'DONE'>('TODO')
  const [dueDate, setDueDate] = useState<string>('')
  const [error, setError] = useState<string|undefined>()

  const load = async ()=>{
    const { data } = await api.get('/api/tasks', { params: { page:0, size:50, sort:'created' } })
    setItems(data.content || data)
  }
  useEffect(()=>{ load() }, [])

  const create = async (e: React.FormEvent)=>{
    e.preventDefault()
    try{
      await api.post('/api/tasks', { title, description, priority, status, dueDate: dueDate || null })
      setTitle(''); setDescription(''); setDueDate(''); setPriority('MEDIUM'); setStatus('TODO')
      await load()
    }catch(e:any){ setError(e?.response?.data?.message || 'Create failed') }
  }

  const del = async (id:number)=>{ await api.delete(`/api/tasks/${id}`); await load() }

  const upload = async (id:number, files: FileList | null)=>{
    if (!files || files.length===0) return
    const form = new FormData()
    Array.from(files).forEach(f=>form.append('files', f))
    await api.post(`/api/tasks/${id}/attachments`, form, { headers: { 'Content-Type': 'multipart/form-data' } })
    await load()
  }

  return (
    <div className="space-y-6">
      <form onSubmit={create} className="bg-white p-4 rounded shadow space-y-3">
        <h2 className="text-lg font-semibold">Create Task</h2>
        {error && <div className="text-sm text-red-600">{error}</div>}
        <input className="w-full border p-2 rounded" placeholder="Title" value={title} onChange={e=>setTitle(e.target.value)} required />
        <textarea className="w-full border p-2 rounded" placeholder="Description" value={description} onChange={e=>setDescription(e.target.value)} />
        <div className="grid grid-cols-3 gap-3">
          <select className="border p-2 rounded" value={priority} onChange={e=>setPriority(e.target.value as any)}>
            <option>LOW</option><option>MEDIUM</option><option>HIGH</option>
          </select>
          <select className="border p-2 rounded" value={status} onChange={e=>setStatus(e.target.value as any)}>
            <option>TODO</option><option>IN_PROGRESS</option><option>DONE</option>
          </select>
          <input type="date" className="border p-2 rounded" value={dueDate} onChange={e=>setDueDate(e.target.value)} />
        </div>
        <button className="px-4 py-2 bg-gray-900 text-white rounded">Add Task</button>
      </form>

      <div className="grid gap-3">
        {items.map(t=>(
          <div key={t.id} className="bg-white p-4 rounded shadow">
            <div className="flex items-center justify-between">
              <div>
                <div className="font-semibold">{t.title}</div>
                <div className="text-sm text-gray-600">{t.description}</div>
                <div className="text-xs mt-1">Status: {t.status} | Priority: {t.priority} {t.dueDate? `| Due: ${t.dueDate}`:''}</div>
              </div>
              <button onClick={()=>del(t.id)} className="px-3 py-1 rounded border">Delete</button>
            </div>
            <div className="mt-3">
              <input type="file" multiple accept="application/pdf" onChange={e=>upload(t.id, e.target.files)} />
              <div className="text-sm mt-2 space-y-1">
                {t.attachments?.map(a=>(<div key={a.id}><a className="underline" href={`http://localhost:8080${a.url}`} target="_blank">{a.filename}</a></div>))}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}

export default Tasks
