import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useUserStore = defineStore('users', () => {
    const authenticated = ref(null)

    function authenticate(username) {
        authenticated.value = (username === 'admin')
    }

    function logout() {
        authenticated.value = false;
    }

    return {authenticated, authenticate, logout}
})
