import axios from 'axios';

export default {
    getAllChecklistNames() {
        return axios.get('/api/templates/all/names')
    },
    getAllTemplates() {
        return axios.get('/api/templates/all')
    },
    addChecklist(checklist) {
        return axios.post('/api/templates/create', checklist)
    },
    getTemplate(name) {
        return axios.get(`/api/templates/${name}`)
    },
    deleteTemplate(name) {
        return axios.delete(`/api/templates/delete/${name}`)
    },
    duplicateTemplate(template) {
        return axios.post(`/api/templates/duplicate`, template)
    }
}
