import axios from 'axios';
import {useUserStore} from "@/main/vue/stores/UserStore";

export const getUserData = async function getData() {
    try {
        const response = await axios.get("/api/admin/getuserdata")
        return transformData(response.data)
    } catch (error) {
        console.error("Something went wrong when getting the data:", error)
        return false
    }
}
function transformData(response) {
    const arr = []
    for (let i = 0;i<response.length;i++)
    {
        const user = response[i]
        const details = [user["username"], user["firstname"], user["lastname"], user["service"]]
        arr.push(details)
    }
    return arr
}

export default {getUserData}
