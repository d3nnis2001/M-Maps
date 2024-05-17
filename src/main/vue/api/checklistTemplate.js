import axios from 'axios';

export default {
    getAllChecklistNames() {
        return axios.get('/api/templates')
    }
}
