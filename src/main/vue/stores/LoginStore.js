import {defineStore} from 'pinia'
import emailChecker from '../api/auth.js';

export const useLoginStore = defineStore('users', {
    actions: {
        async checkEmail(email) {
            try {
                return await emailChecker(email);
            } catch (error) {
                console.error("Error checking email: ", error);
                return false;
            }
        }
    }
});
