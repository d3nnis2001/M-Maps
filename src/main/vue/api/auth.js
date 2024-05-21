import axios from 'axios';

export const emailChecker = async function checkEmail(email) {
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

export const checkAccountDetails = async function (email, password){
    try {
        const cred = new URLSearchParams()
        cred.append("email", email)
        cred.append("password", password)
        const response = await axios.post("api/login", cred)
        return response.data;
    } catch (error) {
        console.error("Error while logging in:", error);
        return false;
    }
}
export const resetPassword = async function (email) {
    const cred = new URLSearchParams()
    cred.append("email", email)
    console.log(email)
    const response = await axios.post("/api/user/resetPassword",cred)
    return response
}
export const setPasswordNew = async function (email, password, token){
    const cred = new URLSearchParams()
    cred.append("email", email)
    cred.append("password", password)
    cred.append("token", token)
    const response = await axios.post("/api/user/setPassword", cred)
    return response
}

export default {emailChecker, checkAccountDetails, resetPassword, setPasswordNew};
