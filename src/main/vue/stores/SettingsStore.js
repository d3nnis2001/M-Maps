import {defineStore} from "pinia";
import api from "../api";
import {ref} from "vue";


export const useSettingsStore = defineStore('settings', () => {
    const impressum = ref("")
    const success = ref(true)
    const imageEmpty = ref(true)
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
        document.body.style.setProperty("--q-accent", newColors.accent)
        document.body.style.setProperty("--q-positive", newColors.positive)
        document.body.style.setProperty("--q-negative", newColors.negative)
        document.body.style.setProperty("--q-warning", newColors.warning)
        document.body.style.setProperty("--q-info", newColors.info)
        document.body.style.setProperty("--q-dark", newColors.dark)
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
    function getColors() {
        return new Promise((resolve, reject) => {
            api.settings.getColors()
                .then(res => {
                    changeColors(res.data)
                    resolve()
                })
                .catch(() => {
                    reject()
                })
        })
    }
    function checkLogo() {
        return new Promise((resolve, reject) => {
            api.settings.getLogo()
                .then(res => {
                    imageEmpty.value = res.data === "";
                    resolve()
                })
                .catch(() => {
                    reject()
                })
        })
    }
    return {
        impressum,
        success,
        imageEmpty,
        getImpressum,
        editImpressum,
        editColors,
        getColors,
        checkLogo
    }
})
