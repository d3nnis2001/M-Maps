import axios from 'axios';

export const getGeoData = async function getGeoData() {
    try {
        const response = await axios.get("/api/map/gettracks")
        return response.data;
    } catch (error) {
    }
}
export const getHeatmap = async function getHeatmap() {
    try {
        const response = await axios.post("/api/map/getheatmap")
        return response.data
    } catch (error) {
        console.log("Something went wrong when getting the trackID")
        return false;
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


export default {getGeoData, getHeatmap, getTimeFromHeatmap};
