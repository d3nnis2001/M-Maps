import {defineStore} from 'pinia';
import {ref} from 'vue';
import api from '../api';

export const useChecklistTemplateStore = defineStore('checklistTemplates', () => {
    const checklistNames = ref([])
    const templates = ref([])
    const invalidInput = ref(false)
    const templateAdded = ref(true)
    const listsEmpty = ref(false)

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

    function nameNotAllowed(name)  {
        const pattern = /^.*\S.*/;
        if (!pattern.test(name)) {
            invalidInput.value = true
            return true
        }
        invalidInput.value = false
        return false
    }

    function checkListsEmpty(list1, list2) {
        if (list1.length + list2.length === 0) {
            listsEmpty.value = true
            return true
        } else {
            listsEmpty.value = false
            return false
        }
    }

    function addChecklist(name, tasks, material) {
        if (nameNotAllowed(name)) {

        } else if (checkListsEmpty(tasks, material)) {

        } else {
            return new Promise((resolve, reject) => {
                const checklist = {
                    name: name,
                    tasks: tasks,
                    material: material
                }
                api.checklistTemplate.addChecklist(checklist)
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
        invalidInput,
        templateAdded,
        listsEmpty,
        getAllChecklistTemplateNames,
        getAllTemplates,
        addChecklist,
    }

})
