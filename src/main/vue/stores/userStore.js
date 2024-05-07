import {defineStore} from 'pinia'
import {ref} from 'vue'
import axios from "axios";
import api from "../api";

export const useUserStore = defineStore('users', () => {
    const authenticated = ref(null)

    function authenticate(token) {
        if (token !== null) {
            authenticated.value = true
            localStorage.setItem('token', token);
            axios.defaults.headers['Authorization'] = token
        } else {
            authenticated.value = false
        }
    }

    function requestToken(credentials) {
        return new Promise((resolve, reject) => {
            api.auth.login(credentials.username, credentials.password).then(res => {
                authenticate(res.headers.authorization)
                resolve()
            }).catch(() => {
                authenticate(null)
                reject()
            })
        })
    }

    function logout() {
        authenticated.value = false;
    }

    return {authenticated, authenticate, requestToken, logout}
})

axios.defaults.headers['Authorization'] = localStorage.getItem('token')
