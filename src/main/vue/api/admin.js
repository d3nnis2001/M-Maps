import axios from 'axios';
import {useUserStore} from "@/main/vue/stores/UserStore";

export const getUserData = async function getData() {
    try {
        const response = await axios.get("/api/admin/getuserdata")
        return response.data
    } catch (error) {
        console.error("Something went wrong when getting the data:", error)
        return false
    }
}

export const deleteUser = async function deleteUser() {
    try {

    } catch (error) {
        console.log("Error while deleting User");
    }
}

