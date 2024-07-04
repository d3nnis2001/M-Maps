import {defineStore} from 'pinia'
import {ref} from 'vue'
import axios from "axios";
import api from "../api";

export const useUserStore = defineStore('userStore', () => {
    const authenticated = ref(null)

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
            api.auth.login(credentials.username, credentials.password).then(res => {
                console.log(res.headers.authorization)
                authenticate(res.headers.authorization)
                resolve()
            }).catch(() => {
                authenticate(null)
                reject()
            })
        })
    }
    function decodeToken() {
        const token = localStorage.getItem("token")
        if (token !== null) {
            let jwtData = token.split('.')[1]
            let decodedJwtJsonData = window.atob(jwtData)
            let decodedJwtData = JSON.parse(decodedJwtJsonData)
            console.log(decodedJwtData)
            return decodedJwtData
        }
        return { rol: [], sub: "", dep: ""}
    }


    function getUserRoles() {
        return decodeToken().rol
    }

    function getUsername() {
        return decodeToken().sub
    }

    function isAdmin() {
        return getUserRoles().includes('Administrator')
    }

    function isPruefer() {
        return getUserRoles().includes('Prüfer')
    }

    function isBearbeiter() {
        return getUserRoles().includes('Bearbeiter')
    }

    function isDatenverwalter() {
        return getUserRoles().includes('Datenverwalter')
    }

    function logout() {
        authenticated.value = false;
    }
    return {authenticated,
        authenticate,
        requestToken,
        logout,
        getUserRoles,
        getUsername,
        isAdmin,
        isPruefer,
        isBearbeiter,
        isDatenverwalter,
        decodeToken}
})

axios.defaults.headers['Authorization'] = localStorage.getItem('token')
