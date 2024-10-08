import axios from 'axios';

export const getTrackLayoutData = async function getData(trackId) {
    try {
        const response = await axios.get("/api/dataviewer/getTrackData2", {params: {trackId}})
        console.log(response.data)
        return response.data
    } catch (error) {
        console.error("Something went wrong when getting the data:", error)
        return false
    }
}

export const getPointData = async function getPointData(pointId) {
    try {
        const response = await axios.get("/api/dataviewer/getPointData", {params: {pointId}})
        console.log(response.data)
        return response.data
    } catch (error) {
        console.error("Something went wrong when getting the data:", error)
        return false
    }
}

export const getPointInfo = async function getPointInfo(pointId) {
    try {
        const response = await axios.get("/api/dataviewer/getPointInfo", {params: {pointId}})
        console.log(response.data)
        return response.data
    } catch (error) {
        console.error("Something went wrong when getting the data:", error)
        return false
    }
}

export const getPointRep = async function getPointRep(lat, lon) {
    try {
        const response = await axios.get("/api/dataviewer/getPointRep", {params: {lat, lon}})
        console.log(response.data)
        return response.data
    } catch (error) {
        console.error("Something went wrong when getting the data:", error)
        return false
    }
}

export const getPointPic = async function getPointPic(repId) {
    try {
        const response = await axios.get("/api/dataviewer/getPointPic", {params: {repId}})
        console.log(response.data)
        return response.data
    } catch (error) {
        console.error("Something went wrong when getting the data:", error)
        return false
    }
}

export default {getTrackLayoutData}
