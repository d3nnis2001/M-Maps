import axios from 'axios';

export default {
    getAllChecklistNames() {
        return axios.get('/api/templates/all/names')
    },
    getAllTemplates() {
        return axios.get('/api/templates/all')
    },
    addChecklist(name, tasks, material) {
        const checklist = {
            name: name,
            tasks: tasks,
            material: material
        }
        return axios.post('/api/templates/create', checklist)
    }
}
