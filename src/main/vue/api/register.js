import axios from 'axios';

const register = async function registerUser(email, password, firstName, lastName, region, service, roles) {
    try {
        const cred = new URLSearchParams()
        cred.append("email", email)
        cred.append("password", password)
        cred.append("firstName", firstName)
        cred.append("lastName", lastName)
        cred.append("region", region)
        cred.append("service", service)
        cred.append("roles", roles)
        return axios.post("/api/register", cred)
    } catch (error) {
        console.error("Unseen error while registering:", error);
        return false;
    }
}

export default register;
