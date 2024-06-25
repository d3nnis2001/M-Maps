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
        const response = await axios.delete("/api/admin/deleteUser", {
            params: {
                userName:username
            }
        });
        return response.data;
    } catch (error) {
        console.log("Unseen error while deleting User");
    }
}

export const getUserByUsername = async function getUserByUsername(username) {
    try {
        const response = await axios.get("/api/admin/getUserByUsername", {
            params: {
                userName: username
            }
        });
        console.log(response)
        console.log(response.data)
        return response.data;
    } catch (error) {
        console.error("Unseen error while try to get User data for editing:", error);
        return false
    }
}

export const updateRoles = async function updateRoles(username, roles){
    try {
        console.log("Die Rollen: " + roles);
        console.log(username)
        const cred = new URLSearchParams();
        const rolesString = roles.join(",");
        cred.append("userName", username);
        cred.append("roles", rolesString);
        const response = await axios.post("/api/admin/updateRoles", cred);
        return response;
    } catch (error) {
        console.error("Error making role changes:", error);
        if (error.response && error.response.status === 404) {
            console.log("Error making role changes: Resource not found");
        } else {
            console.log("Error making role changes:", error.message);
        }
        throw new Error(`Error making role changes: ${error.message}`);
    }
}
