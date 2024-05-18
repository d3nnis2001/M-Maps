import {defineStore} from 'pinia';
import {ref} from 'vue';
import api from '../api';

export const useChecklistTemplateStore = defineStore('checklistTemplates', () => {
    const checklistNames = ref([])
    const templates = ref([])

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

    function addChecklist(name) {
        return new Promise((resolve, reject) => {
            api.checklistTemplate.addChecklist(name)
                .then(() => {
                    resolve()
                })
                .catch(() => {
                    reject()
                })
        })
    }

    return {
        checklistNames,
        templates,
        getAllChecklistTemplateNames,
        getAllTemplates,
        addChecklist
    }

})
