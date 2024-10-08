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
                                                                      endTime, department, dataInspec, remarks, priority) {
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
        derc.append("priority", priority);
        const response = await axios.post("/api/inspection/senddata", derc)
        return response;

    } catch (error) {
        console.error("Unseen error: ", error);
        return false;
    }
}


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

export const sendDataById = async function sendDataById(id, courseId, startLocation, endLocation, startTime,
                                                        endTime, department, dataInspec, remarks, priority) {
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
        derc.append("priority", priority);
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

export const sendNewStatus = async function sendNewStatus(inspectionOrderId, status) {
    try {
        const derc = new URLSearchParams();
        derc.append("inspectionOrderId", inspectionOrderId);
        derc.append("status", status);
        const response = await axios.post("/api/inspection/sendNewStatus", derc)
        return response;

    } catch (error) {
        console.error("Unseen error: ", error);
        return false;
    }
}

export const sendReview = async function sendReview(inspectionOrderId, review, date) {
    try {
        const derc = new URLSearchParams();
        derc.append("inspectionOrderId", inspectionOrderId);
        derc.append("review", review);
        derc.append("date", date);
        console.log(review)
        const response = await axios.post("/api/inspection/sendReview", derc)
        return response;

    } catch (error) {
        console.error("Unseen error: ", error);
        return false;
    }
}

export const sendUsername = async function sendUsername(inspectionOrderId, username) {
    try {
        const derc = new URLSearchParams();
        derc.append("inspectionOrderId", inspectionOrderId);
        derc.append("username", username);
        const response = await axios.post("/api/inspection/sendUsername", derc)
        return response;

    } catch (error) {
        console.error("Unseen error: ", error);
        return false;
    }
}



