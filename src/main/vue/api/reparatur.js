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
        const response = await axios.post("/api/repair/senddata", cred)
        return response
    } catch (error) {
        console.error("Unseen error while registering:", error);
        return false
    }
}
export const getChecklists = async function getChecklists() {
    try {
        const response = await axios.post("/api/repair/allchecklists");
        return response.data

    } catch (error) {
        console.error("Unseen error while getting checklists:", error);
        return false
    }
}

export const getDetailsByID = async function getDetailsByID(name) {
    try {
        const response = await axios.get("/api/repair/getbyid", {
            params: {
                id: name
            }
        });
        return response.data
    } catch (error) {
        console.error("Unseen error while getting repair order:", error);
        return false
    }
}

export const updateValuesById = async function updateValuesById(values, id) {
    try {
        const cred = new URLSearchParams()
        cred.append("checkVals", values)
        cred.append("id", id)
        const response = await axios.post("/api/repair/changebyid", cred)
        return response.data
    } catch (error) {
        console.error("Unseen error while trying to update Checklist in Backend:", error);
        return false
    }
}
