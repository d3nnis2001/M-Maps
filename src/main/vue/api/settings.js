import axios from 'axios';

export default {
    getImpressum() {
        return axios.get('/api/settings/impressum')
    }
}
