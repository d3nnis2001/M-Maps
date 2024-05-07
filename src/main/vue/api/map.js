import axios from 'axios';

export const geoData = async function getGeoData() {
    try {
        const response = await axios.get("/api/map/gettracks")
        return response.data;
    } catch (error) {
    }
}
export const getTrack = async function getTrack(trackID) {
    try {
        const cred = new URLSearchParams()
        cred.append("trackid", trackID)
        const response = await axios.post("/api/map/gettrack", cred)
        return response.data
    } catch (error) {
        console.log("Something went wrong when getting the trackID")
        return false;
    }
}

export const getPartOfTrack = async function getPartOfTrack(from, till) {
    try {
        const cred = new URLSearchParams()
        cred.append("from", from)
        cred.append("till", till)
        const response = await axios.post("/api/map/getparttrack", cred)
        return response.data
    } catch (error) {
        console.log("Something went wront when getting a track part!")
    }
}

export default {geoData, getTrack};
