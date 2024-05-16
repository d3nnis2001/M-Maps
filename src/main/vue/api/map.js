import axios from 'axios';

export const geoData = async function getGeoData() {
    try {
        const response = await axios.get("/api/map/gettracks")
        return response.data;
    } catch (error) {
    }
}

export default {geoData};
