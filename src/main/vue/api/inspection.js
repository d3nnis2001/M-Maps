import axios from 'axios';

export const getInspectionOrder = async function getInspectionOrder() {
    try {
        const response = await axios.get("/api/inspection/getdata")
        console.log(response.data)
        return response.data
    } catch(error) {
        console.error("Unseen error: ", error);
        return false;
    }
}

// -> CreateInspectionOrder.vue
export const sendInspectionOrder = async function sendInspectionOrder(inspectionOrderId, streckenId, userId, start, end, startTime,
                                                                      endTime, fachabteilung, messdaten, status, bemerkungen, archiviert) {
    try {
        const derc = new URLSearchParams();
        derc.append("streckenId", streckenId);
        derc.append("userId", userId);
        derc.append("start", start);
        derc.append("end", end);
        derc.append("startTime", startTime);
        derc.append("endTime", endTime);
        derc.append("fachabteilung", fachabteilung);
        derc.append("messdaten", messdaten);
        derc.append("status", status);
        derc.append("bemerkungen", bemerkungen);
        derc.append("archiviert", archiviert);
        const response = await axios.post("/api/inspection/senddata", derc)
        return response;

    } catch (error) {
        console.error("Unseen error: ", error);
        return false;
    }
}

// -> editInspectionOrder.vue
export const getDataById = async function getDataById(id) {
    try {
        const response = await axios.get("/api/inspection/getById", {
            params: {
                inspectionOrderId: id
            }
        });
        return response.data;
    } catch (error) {
        console.error("Unseen error: ", error);
        return false;
    }
}


