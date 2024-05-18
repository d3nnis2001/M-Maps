import axios from 'axios';

export default {
    getAllChecklistNames() {
        return axios.get('/api/templates/all/names')
    },
    getAllTemplates() {
        return axios.get('/api/templates/all')
    },
    addChecklist(name) {
        return axios.post('/api/templates/create/' + name)
    }
}
