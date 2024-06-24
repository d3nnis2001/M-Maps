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

export const getInformationForGeoPoint = async function getInformationForGeoPoint(id) {
    try {
        const cred = new URLSearchParams()
        cred.append("id", id)
        const response = await axios.post("/api/map/getDataGeoTrack", cred)
        return response.data
    }  catch (error) {
        console.log("Something went wrong when getting the data for a GeoPoint!")
    }
}

export const getReparaturForMap = async function getReparaturForMap() {
    try {

        const response = await axios.post("/api/map/getReparaturForMap")
        console.log(response.data)
        return response.data
    }  catch (error) {
        console.log("Something went wrong when getting the reparatur Aufträge")
    }
}


export default {getGeoData, getHeatmap, getTimeFromHeatmap};
