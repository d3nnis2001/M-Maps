<script setup>
import {useChecklistTemplateStore} from "@/main/vue/stores/checklistTemplateStore";
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";
import {ref} from "vue";
import {storeToRefs} from "pinia";
import {useQuasar} from "quasar";
import CheckPointList from "@/main/vue/pages/checklists/CheckPointList.vue";

const checklistTemplateStore = useChecklistTemplateStore()
const $q = useQuasar()

const name = ref('')
const {validInput, templateAdded} = storeToRefs(checklistTemplateStore)

async function addChecklist() {
    await checklistTemplateStore.addChecklist(name.value)
    if (!validInput.value) {
        $q.notify({
            type: 'negative',
            message: 'Falsche Eingabe',
            caption: 'Die Eingabe hat kein gültiges Format.'
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
    <CheckPointList>neue Aufgabe</CheckPointList>
    <CheckPointList>neues Material</CheckPointList>
    <span>
        <router-link to="/checklists">
            <q-btn label="Abbrechen" flat color="primary"></q-btn>
        </router-link>
        <q-btn label="Checkliste erstellen" @click="addChecklist" color="primary"></q-btn>
    </span>
</template>

<style scoped>

</style>
