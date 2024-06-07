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

export const sendInspectionOrder = async function sendInspectionOrder(courseId, startLocation, endLocation, startTime,
                                                                      endTime, department, dataInspec, remarks) {
    try {
        const derc = new URLSearchParams();
        derc.append("courseId", courseId);
        derc.append("startLocation", startLocation);
        derc.append("endLocation", endLocation);
        derc.append("startTime", startTime);
        derc.append("endTime", endTime);
        derc.append("department", department);
        derc.append("inspectionData", dataInspec);
        derc.append("remarks", remarks);
        const response = await axios.post("/api/inspection/senddata", derc)
        return response;

    } catch (error) {
        console.error("Unseen error: ", error);
        return false;
    }
}


export const getDataById = async function getDataById(id) {
    try {
        console.log("TEST-GETDATABYID");
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

export const sendDataById = async function sendDataById(id, courseId, startLocation, endLocation, startTime,
                                                        endTime, department, dataInspec, remarks) {
    try {
        const derc = new URLSearchParams();
        derc.append("inspectionOrderId", id);
        derc.append("courseId", courseId);
        derc.append("startLocation", startLocation);
        derc.append("endLocation", endLocation);
        derc.append("startTime", startTime);
        derc.append("endTime", endTime);
        derc.append("department", department);
        derc.append("inspectionData", dataInspec);
        derc.append("remarks", remarks);
        const response = await axios.post("/api/inspection/sendById", derc)
        return response;

    } catch (error) {
        console.error("Unseen error: ", error);
        return false;
    }
}

export const deleteInspectionOrder = async function deleteInspectionOrder(inspectionOrderId) {
    try {
        const cred = new URLSearchParams()
        cred.append("inspectionOrderId", inspectionOrderId)
        const response = await axios.post("/api/inspection/deleteInspectionOrder", cred)
        return response
    } catch (error) {
        console.log("Unseen error when changing the status")
    }
}


