import axios from "axios";


export const sendImage = async function sendImage(id, formData) {
    try {
        formData.append("orderId", id);

        const response = await axios.post("/api/images/upload", formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        return response;

    } catch (error) {
        console.error("Unseen error: ", error);
        return false;
    }
}


