import {defineStore} from "pinia";
import api from "../api";
import {ref} from "vue";


export const useSettingsStore = defineStore('settings', () => {
    const impressum = ref("")
    const success = ref(true)
    const imageURL = ref("")
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
    function getLogo() {
        return new Promise((resolve, reject) => {
            api.settings.getLogo()
                .then(res => {
                    let byteArray = new Uint8Array(res.data)
                    //imageURL.value = URL.createObjectURL(new Blob(byteArray, {type: 'image/jpeg'}))
                    imageURL.value = res.data
                    resolve(imageURL.value)
                })
                .catch(() => {
                    reject()
                })
        })
    }
    return {
        impressum,
        success,
        imageURL,
        getImpressum,
        editImpressum,
        editColors,
        getColors,
        getLogo
    }
})
