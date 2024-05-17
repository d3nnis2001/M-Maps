import {defineStore} from 'pinia';
import {ref} from 'vue';
import api from '../api';

export const useChecklistTemplateStore = defineStore('checklistTemplates', () => {
    let checklistNames = ref([])

    function getAllChecklistTemplateNames() {
        return new Promise((resolve, reject) => {
            api.checklistTemplate.getAllChecklistNames()
                .then(res => {
                    checklistNames = res.data
                    resolve()
                })
                .catch(() => {
                    reject()
                })
        })
    }

    return {getAllChecklistTemplateNames}

})
