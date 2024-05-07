import axios from 'axios';

export const repair = async function getRepairs() {
    try {
        const response = await axios.get("/api/repair/getdata")
        console.log(response.data)
        return response.data
    } catch (error) {
        console.error("Unseen error while registering:", error);
        return false
    }
}
export const sendRepair = async function sendRepair(track, from, till, authorized, checklist, remarks) {
    try {
        const cred = new URLSearchParams()
        cred.append("track", track)
        cred.append("from", from)
        cred.append("till", till)
        cred.append("authorized", authorized)
        cred.append("checklist", checklist)
        cred.append("remarks", remarks)
        console.log(remarks)
        const response = await axios.post("/api/repair/senddata", cred)
        console.log(response)
        return response
    } catch (error) {
        console.error("Unseen error while registering:", error);
        return false
    }
}
export const getChecklists = async function getChecklists() {
    try {
        const response = await axios.post("/api/repair/allchecklists");
        console.log(response)
        return response.data

    } catch (error) {
        console.error("Unseen error while getting checklists:", error);
        return false
    }
}
