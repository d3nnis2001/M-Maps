import axios from 'axios';

export const getUserData = async function getData() {
    try {
        const response = await axios.get("/api/admin/getUserData")
        return response.data
    } catch (error) {
        console.error("Something went wrong when getting the data:", error)
        return false
    }
}

export const deleteUser = async function deleteUser(username) {
    try {
        const cred = new URLSearchParams()
        cred.append("username", username)
        const response = await axios.delete("api/admin/deleteUser", cred)
        return response
    } catch (error) {
        console.log("Unssen error while deleting User");
    }
}

export const getUserById = async function getUserById(id) {
    try {
        const response = await axios.get("api/admin/getUserById", {
            params: {
                username: id
            }
        });
        return response.data;
    } catch (error) {
        console.error("Unseen error while try to get User data for editing:", error);
        return false
    }
}

export const setRolesById = async function setRolesById(id){
    try {
        const cred = new URLSearchParams()
    } catch (error) {

    }
}
