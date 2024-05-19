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
    }
}
