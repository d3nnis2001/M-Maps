import axios from 'axios';

export const getGeoData = async function getGeoData() {
    try {
        const response = await axios.get("/api/map/gettracks")
        console.log(response)
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

export const getPartOfGleislage = async function getPartOfGleislage(id) {
    try {
        const cred = new URLSearchParams()
        cred.append("id", id)
        const response = await axios.post("/api/map/fromcolours", cred)
        return response.data
    } catch (error) {
        console.log("Something went wrong when getting the Gleislagedaten!")
    }
}

export const getImagesForTrackId = async(trackId) => {
    try {
        const cred = new URLSearchParams()
        cred.append("trackid", trackId)
        const response = await axios.post("/api/map/getCameraImageforTrack", cred)
        console.log(response.data)
        return response.data
    }catch (error) {
        console.log("Something went wrong, when getting the pictures!")
    }
}

export const getIRImagesForTrackId = async(trackId) => {
    try {
        const cred = new URLSearchParams()
        cred.append("trackid", trackId)
        const response = await axios.post("/api/map/getIRCameraImageforTrack", cred)
        console.log(response.data)
        return response.data
    }catch (error) {
        console.log("Something went wrong, when getting the IR pictures!")
    }
}


export default {getGeoData, getTrack};
