import {defineStore} from 'pinia'
import {ref} from 'vue'
import emailChecker from "@/main/vue/api/register";
import register from "@/main/vue/api/register";

export const useRegistrationStore = defineStore('users', {
    actions: {
        async registerUser(email, password, firstName, lastName, region, service, roles) {
            try {
                return await register(email, password, firstName, lastName, region, service, roles)
            } catch (error) {
                console.error("Error while trying to register a new User: ", error);
                return false;
            }
        }
    }
});
