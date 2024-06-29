import axios from "axios";

export const sendImage = async function sendImage(id, image, name) {
    try {
        const derc = new URLSearchParams();
        derc.append("orderId", id);
        console.log("API-", id);
        if (image !== null) {
            console.log("Success in API");
        }
        derc.append("image", image);
        derc.append("name", name);
        console.log(name)

        const response = await axios.post("/api/images/upload", derc);
        return true;

    } catch (error) {
        console.error("Unseen error: ", error);
        return false;
    }
}



