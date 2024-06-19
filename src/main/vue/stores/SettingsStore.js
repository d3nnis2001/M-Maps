import {defineStore} from "pinia";
import api from "../api";
import {ref} from "vue";


export const useSettingsStore = defineStore('settings', () => {
    const impressum = ref("")
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
    return {
        impressum,
        getImpressum
    }
})
