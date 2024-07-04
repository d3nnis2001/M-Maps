import axios from 'axios';

export const getArchivedRep = async () => {
    try {
        const response = await axios.post("/api/archiv/getAllReparatur")
        console.log(response.data)
        return response.data
    } catch (error)
    {

    }
}

export const undoRep = async (i) => {
    try {
        const cred = new URLSearchParams()
        cred.append("id", i)
        const response = await axios.post("/api/archiv/unarchiveReparatur", cred)
    } catch (error) {
        console.log("error")
    }
}

export const getArchivedPruef = async () => {
    try {
        const response = await axios.post("/api/archiv/getAllInspectionOrders")
        console.log(response.data)
        return response.data
    } catch (error)
    {

    }
}

export const undoPruef = async (i) => {
    try {
        const cred = new URLSearchParams()
        cred.append("id", i)
        const response = await axios.post("/api/archiv/unarchiveInspectionOrder", cred)
    } catch (error) {
        console.log("error")
    }
}

export const deletePruef = async (i) => {
    try {
        const cred = new URLSearchParams()
        cred.append("id", i)
        const response = await axios.post("/api/archiv/deleteArchivedInspectionOrder", cred)
    } catch (error) {
        console.log("error")
    }
}

export const deleteRep = async (i) => {
    try {
        const cred = new URLSearchParams()
        cred.append("id", i)
        const response = await axios.post("/api/archiv/deleteArchivedReparatur", cred)
    } catch (error) {
        console.log("error")
    }
}
