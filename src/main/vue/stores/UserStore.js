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
            console.log("Success")
            userRoles.value = res.data
            console.log(userRoles.value)
        }).catch(() => {
            console.log("FEHLER")
        })
    }

    function isAdmin() {
        decodeToken();
        return userRoles.includes('Administrator')
    }

    function isPruefer() {
        decodeToken();
        for (let i = 0; i < userRoles.value.length; i++) {
            if (userRoles.value[i] === 'Prüfer') {
                return true;
            }
        }
        return false;
    }

    function isBearbeiter() {
        return decodeToken().includes('Bearbeiter')
    }

    function isDatenverwalter() {
        return decodeToken().includes('Datenverwalter')
    }

    function logout() {
        authenticated.value = false;
        username.value = null;
    }
    return {authenticated,
        authenticate,
        requestToken,
        logout,
        isAdmin,
        isPruefer,
        isBearbeiter,
        isDatenverwalter,
        decodeToken}
})

axios.defaults.headers['Authorization'] = localStorage.getItem('token')
