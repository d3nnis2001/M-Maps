import axios from 'axios';

export const repair = async function getRepairs() {
    try {
        const response = await axios.get("/api/repair/getdata")
        return response.data
    } catch (error) {
        console.error("Unseen error while registering:", error);
        return false
    }
}
export const sendRepair = async function sendRepair(track, from, till, authorized, checklist, remarks, long, lat) {
    try {
        const cred = new URLSearchParams()
        cred.append("track", track)
        cred.append("from", from)
        cred.append("till", till)
        cred.append("authorized", authorized)
        cred.append("checklist", checklist)
        cred.append("remarks", remarks)
        cred.append("longitude", long)
        cred.append("latitude", lat)
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

export const updateRepChecklist = async function updateRepChecklist(id, ticked) {
    try {
        console.log("Die angetikten: "+ticked)
        const tickedString = ticked.join(',');
        const cred = new URLSearchParams()
        cred.append("id", id)
        cred.append("ticked", tickedString)
        const response = await axios.post("/api/repair/changeById", cred)
        return response
    } catch (error) {
        console.log("Unseen error when making changes on a repair order")
        return false
    }
}

export const updateStatus = async function updateStatus(name, status) {
    try {
        const cred = new URLSearchParams()
        cred.append("status", status)
        cred.append("name", name)
        const response = await axios.post("/api/repair/changestatus", cred)
        return response
    } catch (error) {
        console.log("Unseen error when changing the status")
    }
}

export const deleteRepairOrder = async function deleteRepairOrder(name) {
    try {
        const cred = new URLSearchParams()
        cred.append("name", name)
        const response = await axios.post("/api/repair/deleterepairorder", cred)
        return response
    } catch (error) {
        console.log("Unseen error when changing the status")
    }
}

export const getTickedItems = async function getTickedItems(id) {
    try {
        const cred = new URLSearchParams()
        cred.append("id", id)
        const response = await axios.post("/api/repair/getticked", cred)
        console.log(response)
        return response
    } catch (error) {
        console.log("Unseen error when getting ticked items")
    }
}

export const setTerminated = async function setTerminated(id, date) {
    try {
        const cred = new URLSearchParams()
        cred.append("id", id)
        cred.append("date", date)
        const response = await axios.post("/api/repair/setterminated", cred)
        return response
    } catch (error) {
        console.log("Unseen error when getting ticked items")
    }
}
