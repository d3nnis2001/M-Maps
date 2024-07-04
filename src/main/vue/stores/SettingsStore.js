import {defineStore} from "pinia";
import api from "../api";
import {ref} from "vue";
import {colors} from 'quasar';


export const useSettingsStore = defineStore('settings', () => {
    const impressum = ref("")
    const success = ref(true)
    function getImpressum() {
        return new Promise((resolve, reject) => {
            api.settings.getImpressum()
                .then(res => {
                    impressum.value = res.data
                    resolve()
                })
                .catch(() => {
                    impressum.value = ""
                    reject()
                })
        })
    }
    function editImpressum(text) {
        return new Promise((resolve, reject) => {
            const content = {
                content: text
            }
            api.settings.editImpressum(content)
                .then(res => {
                    impressum.value = res.data
                    success.value = true
                    resolve()
                })
                .catch(() => {
                    impressum.value = ""
                    success.value = false
                    reject()
                })
        })
    }
    function changeColors(newColors) {
        document.body.style.setProperty("--q-primary", newColors.primary)
        document.body.style.setProperty("--q-secondary", newColors.secondary)
    }
    function editColors(newColors) {
        return new Promise((resolve, reject) => {
            api.settings.editColors(newColors)
                .then(res => {
                    changeColors(res.data)
                    success.value = true
                    resolve()
                })
                .catch(() => {
                    success.value = false
                    reject()
                })
        })
    }
    return {
        impressum,
        success,
        getImpressum,
        editImpressum,
        editColors
    }
})
