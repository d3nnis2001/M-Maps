import {defineStore} from 'pinia'
import {ref} from 'vue'
import axios from "axios";
import api from "../api";

export const useUserStore = defineStore('userStore', () => {
    const authenticated = ref(null)
    const username = ref("")
    const userRoles = ref([])

    function authenticate(token) {
        console.log("TEST: authenticate")
        console.log(token)
        if (token !== null) {
            authenticated.value = true
            localStorage.setItem('token', token);
            axios.defaults.headers['Authorization'] = token
        } else {
            authenticated.value = false
        }
    }


    function requestToken(credentials) {
        console.log("TEST: requestToken")
        return new Promise((resolve, reject) => {
            console.log("Test3")
            username.value = credentials.username
            api.auth.getToken(credentials.username, credentials.password).then(res => {
                console.log(" ",res.data)
                authenticate(res.data)
                resolve()
            }).catch(() => {
                authenticate(null)
                reject()
            })
        })
    }
    function decodeToken() {
        const token = localStorage.getItem("token")
        api.auth.getRoleByToken(token, username.value).then(res => {
            console.log(" ", res.data)
            console.log("Success-UserStore")
            userRoles.value = res.data
            console.log(userRoles.value)
        }).catch(() => {
            console.log("FEHLER")
        })
    }
    function hasRole(role) {
        for (let i = 0; i < userRoles.value.length; i++) {
            if (userRoles.value[i] === role) {
                return true;
            }
        }
        return false;
    }


    function logout() {
        console.log("logout")
        authenticated.value = false;
        username.value = null;
        localStorage.removeItem('token')
    }

    return {authenticated,
        authenticate,
        requestToken,
        logout,
        hasRole,
        decodeToken}
})

axios.defaults.headers['Authorization'] = localStorage.getItem('token')
