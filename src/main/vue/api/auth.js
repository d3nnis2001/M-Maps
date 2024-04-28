import axios from 'axios';

const emailChecker = async function checkEmail(email) {
    try {
        const response = await axios.get("/api/authenticate", {
            params: {
            email:email
        }});
        return response.data;
    } catch (error) {
        console.error("Error checking email existance:", error);
        return false;
    }
}

export default emailChecker;
