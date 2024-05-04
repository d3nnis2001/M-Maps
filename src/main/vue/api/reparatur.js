import axios from 'axios';

const repair = async function getRepairs() {
    try {
        const response = await axios.get("/api/repair/getdata")
        console.log(response.data)
        return response.data
    } catch (error) {
        console.error("Unseen error while registering:", error);
        return false
    }
}
const sendrepair = async function sendRepair(track, from, till, authorized, checklist, remarks) {
    try {
        const cred = new URLSearchParams()
        cred.append("track", track)
        cred.append("from", from)
        cred.append("till", till)
        cred.append("authorized", authorized)
        cred.append("checklist", checklist)
        cred.append("remarks", remarks)
        return await axios.post("/api/repair/senddata", cred)
    } catch (error) {
        console.error("Unseen error while registering:", error);
        return false
    }
}

export default {sendrepair, repair};
