import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useUserStore = defineStore('users', () => {
    const authenticated = ref(null)

    function authenticate(username, password) {
        authenticated.value = (username === 'admin' && password === 'password')
    }

    function logout() {
        authenticated.value = false;
    }

    return {authenticated, authenticate, logout}
})
