import axios from 'axios';

//todo: url eintragen 
export const getArchivedRep = async () => {
    try {
        const response = await axios.get("")
        return response.data
    } catch (error)
    {

    }
}

export const undoRep = async (i) => {
    try {
        await axios.post("", {
            id: i
        });
    } catch (error) {

    }
}

export const getArchivedPruef = async () => {
    try {
        const response = await axios.get("")
        return response.data
    } catch (error)
    {

    }
}

export const undoPruef = async (i) => {
    try {
        await axios.post("", {
            id: i
        });
    } catch (error) {

    }
}
