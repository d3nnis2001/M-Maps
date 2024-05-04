import axios from 'axios';

const repair = async function getRepairs() {
    try {
        const response = await axios.get("/api/repair/getdata")
        console.log(response.data)
        return response.data
    } catch (error) {
        console.error("Unseen error while registering:", error);
        return false;
    }
}

export default repair;
