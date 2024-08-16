import axios from 'axios';

export const getVorlagen = async function getVorlagen(vorlagenId) {
    try {
        console.log("In der API angekommen " + vorlagenId)
        const response = await axios.get(`/api/vorlage/getdata/` + vorlagenId)
        console.log(response.data)
        return response.data
    } catch(error) {
        console.error("Unseen error: ", error);
        return false;
    }
}

export const sendVorlagen = async function sendVorlagen(vorlagenId, stringSubject, stringBody) {
    try {
        const derc = new URLSearchParams();
        derc.append("vorlagenId", vorlagenId);
        derc.append("stringSubject", stringSubject);
        derc.append("stringBody", stringBody);
        const response = await axios.post("/api/vorlage/senddata", derc)
        return response;

    } catch (error) {
        console.error("Unseen error: ", error);
        return false;
    }
}
