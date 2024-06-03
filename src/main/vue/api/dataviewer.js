import axios from 'axios';

export const getTrackLayoutData = async function getData() {
    try {
        const response = await axios.get("/api/dataviewer/getTrackData")
        console.log(response.data)
        return response.data
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
