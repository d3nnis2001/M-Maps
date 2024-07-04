import axios from 'axios';

export default {
    editImpressum(content) {
        return axios.post('/api/settings/edit/impressum', content)
    },
    getImpressum() {
        return axios.get('/api/settings/impressum')
    },
    editColors(colors) {
        return axios.post('/api/settings/edit/colors', colors)
    },
    getColors() {
        return axios.get('/api/settings/colors')
    },
    getLogo() {
        return axios.get('/api/settings/logo')
    }
}
