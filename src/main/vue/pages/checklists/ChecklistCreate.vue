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

const {invalidInput, templateAdded, listsEmpty} = storeToRefs(checklistTemplateStore)

const pattern = /^.*\S.*/

const newTask = ref("")
const newMaterial = ref("")
const checked = ref(false)

function addTask() {
    if (pattern.test(newTask.value)) {
        tasks.value.push(newTask.value)
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
        material.value.push(newMaterial.value)
        newMaterial.value = ""
    } else {
        $q.notify({
            type: 'warning',
            message: 'falsche Eingabe',
            caption: 'Die Eingabe für dieses Textfeld ist ungültig'
        })
    }
}

async function addChecklist() {
    await checklistTemplateStore.addChecklist(name.value, tasks.value, material.value)
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
    {{invalidInput}}
    {{templateAdded}}
    {{listsEmpty}}
    <StandardInput v-model="name">Name der Checkliste</StandardInput>
    <div>
        <span>
            <StandardInput v-model="newTask" label="neue Aufgabe"> </StandardInput>
            <q-btn label="hinzufügen" @click="addTask" color="primary"></q-btn>
        </span>
        <div v-for="item in tasks">
            <q-checkbox v-model="checked" :label="item" disable></q-checkbox>
        </div>
    </div>
    <div>
        <span>
            <StandardInput v-model="newMaterial" label="neues Material"> </StandardInput>
            <q-btn label="hinzufügen" @click="addMaterial" color="primary"></q-btn>
        </span>
        <div v-for="item in material">
            <q-checkbox v-model="checked" :label="item" disable></q-checkbox>
        </div>
    </div>
    <span>
        <router-link to="/checklists">
            <q-btn label="Abbrechen" flat color="primary"></q-btn>
        </router-link>
        <q-btn label="Checkliste erstellen" @click="addChecklist" color="primary"></q-btn>
    </span>
</template>

<style scoped>

</style>
