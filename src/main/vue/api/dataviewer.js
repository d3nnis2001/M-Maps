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

function transformData(response) {
    const arr = []
    for(let i = 0; i < response.length;i++) {
        const data = response[i]
        const point = {x: data["z_links_railab_3p"], y: data["str_km"]}
        arr.push(point)
        //console.log(data)
    }
    return arr
}

export default {getTrackLayoutData}
