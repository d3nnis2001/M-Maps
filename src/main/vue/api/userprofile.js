import axios from "axios";

export const getUserByUsername = async function getUserByUsername(username) {
    try {
        const response = await axios.get("/api/userprofile/getUserByUsername", {
            params: {
                userName: username
            }
        });
        console.log(username)
        //console.log(response)
        //console.log(response.data)
        return response.data;
    } catch (error) {
        console.error("Unseen error while try to get User data for editing: ", error);
        return false
    }
}
