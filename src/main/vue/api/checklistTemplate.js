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
        const test = axios.get(`/api/templates/${name}`)
        console.log(test)
        return test
    },
    deleteTemplate(name) {
        return axios.delete(`/api/templates/delete/${name}`)
    },
    duplicateTemplate(template) {
        return axios.post(`/api/templates/duplicate`, template)
    },
    editTemplate(template) {
        console.log(template)
        return axios.post(`/api/templates/edit`, template)
    }
}
