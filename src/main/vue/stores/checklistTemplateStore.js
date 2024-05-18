import {defineStore} from 'pinia';
import {ref} from 'vue';
import api from '../api';

export const useChecklistTemplateStore = defineStore('checklistTemplates', () => {
    const checklistNames = ref([])
    const templates = ref([])
    const error = ref("")
    const validInput = ref(true)
    const templateAdded = ref(true)

    function getAllChecklistTemplateNames() {
        return new Promise((resolve, reject) => {
            api.checklistTemplate.getAllChecklistNames()
                .then(res => {
                    checklistNames.value = res.data
                    resolve()
                })
                .catch(() => {
                    reject()
                })
        })
    }

    function getAllTemplates() {
        return new Promise((resolve, reject) => {
            api.checklistTemplate.getAllTemplates()
                .then(res => {
                    templates.value = res.data
                    resolve()
                })
                .catch(() => {
                    reject()
                })
        })
    }

    function isNameAllowed(name)  {
        const pattern = /^.*\S.*/;
        if (!pattern.test(name)) {
            validInput.value = false
            return false
        }
        validInput.value = true
        return true
    }

    function addChecklist(name) {
        error.value = ""
        if (isNameAllowed(name)) {
            return new Promise((resolve, reject) => {
                api.checklistTemplate.addChecklist(name)
                    .then(res => {
                        templateAdded.value = res.data
                        resolve()
                    })
                    .catch(res => {
                        templateAdded.value = res.data
                        reject()
                    })
            })
        }
    }

    return {
        checklistNames,
        templates,
        validInput,
        templateAdded,
        getAllChecklistTemplateNames,
        getAllTemplates,
        addChecklist,
    }

})
