import axios from 'axios';
import {useUserStore} from "@/main/vue/stores/UserStore";

export const getUserData = async function getData() {
    try {
        const response = await axios.get("/api/admin/getuserdata")
        //return transformData(response.data)
        return response.data
    } catch (error) {
        console.error("Something went wrong when getting the data:", error)
        return false
    }
}

export async function editUser(selectedUser, router) {
    if (selectedUser[0] === undefined) {
        this.$q.notify({
            message: "Wähle einen Nutzer aus!",
            timeout: 5000,
        });
    } else {
        await router.push("/admin/editUser")
        console.log(selectedUser[0].username)
    }
}
