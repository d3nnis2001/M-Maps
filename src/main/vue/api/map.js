import axios from 'axios';

export const getGeoData = async function getGeoData() {
    try {
        const response = await axios.get("/api/map/gettracks")
        return response.data;
    } catch (error) {
    }
}
export const getTrack = async function getTrack(trackID) {
    try {
        const cred = new URLSearchParams()
        cred.append("id", trackID)
        const response = await axios.post("/api/map/getmapbyid", cred)
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

export const getTimeFromHeatmap = async function getTimeFromHeatmap(strecke, from, till) {
    try {
        const cred = new URLSearchParams()
        cred.append("strecke", strecke)
        cred.append("from", from)
        cred.append("till", till)
        const response = await axios.post("/api/map/getpartheatmap", cred)
        return response.data
    }  catch (error) {
        console.log("Something went wrong when getting the Heatmap data!")
    }
}


export default {getGeoData, getTrack, getTimeFromHeatmap};
