import axios from 'axios';

export const getTrackLayoutData = async function getData(trackId) {
    try {
        //const response = await axios.get("/api/dataviewer/getTrackData")
        const response2 = await axios.get("/api/dataviewer/getTrackData2", {params: {trackId}})
        console.log(response2.data)
        //console.log(response.data)
        return response2.data
        //return transformData(response.data)
    } catch (error) {
        console.error("Something went wrong when getting the data:", error)
        return false
    }
}

export const getPointData = async function getPointData(lon, lat) {
    try {
        //const response = await axios.get("/api/dataviewer/getTrackData")
        const response = await axios.get("/api/dataviewer/getPointData", {params: {lat, lon}})
        //console.log(response2.data)
        console.log(response.data)
        return response.data
    } catch (error) {
        console.error("Something went wrong when getting the data:", error)
        return false
    }
    //const response = await axios.get("/api/dataviewer/getPointData", {params: {lon, lat}})
}

export default {getTrackLayoutData}
