<script setup>
import {useChecklistTemplateStore} from "@/main/vue/stores/checklistTemplateStore";
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";
import {ref} from "vue";
import {storeToRefs} from "pinia";
import {useQuasar} from "quasar";

const checklistTemplateStore = useChecklistTemplateStore()
const $q = useQuasar()

const name = ref('')
const tasks = ref([])
const material = ref([])
const tasksOnly = ref([])
const materialOnly = ref([])

const {invalidInput, templateAdded, listsEmpty} = storeToRefs(checklistTemplateStore)

const pattern = /^.*\S.*/

const newTask = ref("")
const newMaterial = ref("")
const checked = ref(false)

function addTask() {
    if (pattern.test(newTask.value)) {
        tasks.value.push({id: tasks.value.length, text: newTask.value})
        newTask.value = ""
    } else {
        $q.notify({
            type: 'warning',
            message: 'falsche Eingabe',
            caption: 'Die Eingabe für dieses Textfeld ist ungültig'
        })
    }
}

function addMaterial() {
    if (pattern.test(newMaterial.value)) {
        material.value.push({id: material.value.length, text: newMaterial.value})
        newMaterial.value = ""
    } else {
        $q.notify({
            type: 'warning',
            message: 'falsche Eingabe',
            caption: 'Die Eingabe für dieses Textfeld ist ungültig'
        })
    }
}

function removeTask(id) {
    tasks.value.splice(id, 1)
    let newId = 0
    tasks.value.map(entry => entry.id = newId++)
}

function removeMaterial(id) {
    material.value.splice(id, 1)
    let newId = 0
    material.value.map(entry => entry.id = newId++)
}

async function addChecklist() {
    tasksOnly.value = tasks.value.map(entry => entry.text);
    materialOnly.value = material.value.map(entry => entry.text)
    await checklistTemplateStore.addChecklist(name.value, tasksOnly, materialOnly)
    if (invalidInput.value) {
        $q.notify({
            type: 'negative',
            message: 'Falsche Eingabe',
            caption: 'Die Eingabe hat kein gültiges Format.'
        })
    } else if (listsEmpty.value) {
        $q.notify({
            type: 'negative',
            message: 'Leere Checkliste',
            caption: 'Es dürfen nicht beide Listen leer sein.'
        })
    } else if (!templateAdded.value) {
        $q.notify({
            type: 'negative',
            message: 'Checkliste bereits vorhanden',
            caption: 'Eine Checkliste mit diesem Namen existiert bereits.'
        })
    } else {
        $q.notify({
            type: 'positive',
            message: 'Erfolg',
            caption: 'Die Checkliste wurde hinzugefügt.'
        })
    }
}
</script>

<template>
    <StandardInput v-model="name">Name der Checkliste</StandardInput>
    <div>
        <span>
            <StandardInput v-model="newTask" label="neue Aufgabe"> </StandardInput>
            <q-btn label="hinzufügen" @click="addTask" color="primary"></q-btn>
        </span>
        <div v-for="task in tasks" :key="task.id">
            <span>
                <q-checkbox v-model="checked" :label="task.text" disable></q-checkbox>
                <q-btn @click="removeTask(task.id)" outline color="primary" label="entfernen"></q-btn>
            </span>
        </div>
    </div>
    <div>
        <span>
            <StandardInput v-model="newMaterial" label="neues Material"> </StandardInput>
            <q-btn label="hinzufügen" @click="addMaterial" color="primary"></q-btn>
        </span>
        <div v-for="item in material" :key="item.id">
            <span>
                <q-checkbox v-model="checked" :label="item.text" disable></q-checkbox>
                <q-btn @click="removeMaterial(item.id)" outline color="primary" label="entfernen"></q-btn>
            </span>
        </div>
    </div>
    <span>
        <router-link to="/checklists">
            <q-btn label="Abbrechen" flat color="primary"></q-btn>
        </router-link>
        <q-btn label="Checkliste erstellen" @click="addChecklist" color="primary"></q-btn>
    </span>
    {{tasksOnly}}
    {{materialOnly}}
</template>

<style scoped>

</style>
