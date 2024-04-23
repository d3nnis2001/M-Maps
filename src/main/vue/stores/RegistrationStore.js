import {defineStore} from 'pinia'
import {ref} from 'vue'

export const useUserStore = defineStore('users', () => {
    const authenticated = ref(null)

    function authenticate(username) {

    }

    function checkPasswordRequirements(password) {

    }

    return {authenticated, authenticate}
})
