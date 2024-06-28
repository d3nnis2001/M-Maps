import axios from "axios";

export const sendImage = async function sendImage(id, image) {
    try {
        const derc = new URLSearchParams();
        derc.append("orderId", id);
        console.log(image)
        derc.append("image", image);

        const response = await axios.post("/api/images/upload", derc);
        return response;

    } catch (error) {
        console.error("Unseen error: ", error);
        return false;
    }
}



