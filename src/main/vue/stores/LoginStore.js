import {defineStore} from 'pinia'
import {emailChecker, checkAccountDetails} from '../api/auth.js';
import api from "../api";

export const useLoginStore = defineStore('users', {
    actions: {
        async checkEmail(email) {
            try {
                return await emailChecker(email);
            } catch (error) {
                console.error("Error checking email: ", error);
                return false;
            }
        },
        async checkPassword(email, password) {
            try {
                const response = await checkAccountDetails(email, password)
                return response;
            } catch (error) {
                console.error("Error when trying to log in: ", error);
                return false;
            }
        }
    }
});
