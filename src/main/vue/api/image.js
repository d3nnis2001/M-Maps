import axios from "axios";

export const sendImage = async function sendImage(files) {
    try {
        const id = files.get("orderId");
        console.log(id);
        const derc = new URLSearchParams();
        derc.append("orderId", id);
        console.log(files.get("file"));
        derc.append("file", files.get("file"));

        const response = await axios.post("/api/images/upload", derc)
        return response;

    } catch (error) {
        console.error("Unseen error: ", error);
        return false;
    }
}
