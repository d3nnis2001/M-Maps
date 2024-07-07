import {defineStore} from 'pinia';
import {ref} from 'vue';
import api from '../api';

export const useChecklistTemplateStore = defineStore('checklistTemplates', () => {
    const templateNames = ref([])
    const templates = ref([])
    const template = ref({name: "", tasks: [], material: []})
    const templateEdit = ref({name: "", tasks: [], material: []})
    const templateName = ref("")
    const invalidInput = ref(false)
    const templateAdded = ref(true)
    const templateDeleted = ref(true)
    const listsEmpty = ref(false)

    function getAllChecklistTemplateNames() {
        return new Promise((resolve, reject) => {
            api.checklistTemplate.getAllChecklistNames()
                .then(res => {
                    templateNames.value = res.data
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

    function nameNotAllowed(name) {
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

    function getTemplate(name) {
        return new Promise((resolve, reject) => {
            api.checklistTemplate.getTemplate(name)
                .then(res => {
                    template.value = res.data
                    resolve()
                })
                .catch(() => {
                    reject()
                })
        })
    }

    function deleteTemplate() {
        return new Promise((resolve, reject) => {
            api.checklistTemplate.deleteTemplate(template.value.name)
                .then(res => {
                    templateName.value = res.data
                    const index = templates.value.findIndex(
                        (item) => item.name === templateName.value)
                    if (index !== -1) {
                        templateNames.value.splice(index, 1)
                    }
                    templateDeleted.value = true
                    resolve()
                })
                .catch(() => {
                    templateDeleted.value = false
                    reject()
                })
        })
    }

    function duplicateTemplate() {
        return new Promise((resolve, reject) => {
            api.checklistTemplate.duplicateTemplate(template.value)
                .then(res => {
                    templateAdded.value = true
                    templateName.value = res.data
                    templateNames.value.push(templateName.value)
                    resolve()
                })
                .catch(() => {
                    templateAdded.value = false
                    reject()
                })
        })
    }

    function editChecklist(taskList, materialList) {
        templateEdit.value = template.value
        templateEdit.value.tasks = taskList.map(entry => entry.text);
        templateEdit.value.material = materialList.map(entry => entry.text)
        if (checkListsEmpty(templateEdit.value.tasks, templateEdit.value.material)) {

        } else {
            return new Promise((resolve, reject) => {
                api.checklistTemplate.editTemplate(templateEdit.value)
                    .then(res => {
                        templateAdded.value = res.data
                        template.value = templateEdit.value
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
        templateNames,
        templates,
        template,
        templateEdit,
        templateName,
        invalidInput,
        templateAdded,
        templateDeleted,
        listsEmpty,
        getTemplate,
        getAllChecklistTemplateNames,
        getAllTemplates,
        addChecklist,
        deleteTemplate,
        duplicateTemplate,
        editChecklist
    }

})
