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
const taskList = ref([])
const materialList = ref([])

const {invalidInput, templateAdded, listsEmpty} = storeToRefs(checklistTemplateStore)

async function addChecklist() {
    const tasksOnly = taskList.value.map(entry => entry.text);
    const materialOnly = materialList.value.map(entry => entry.text)
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
    <h3 class="text-align items-center">neue Checkliste erstellen</h3>
    <div class="text-align items-center">
        <StandardInput v-model="name" label="Name der Checkliste" class="padding-md"/>
        <CheckPointList :list="taskList" label="neue Aufgabe">Aufgabenliste</CheckPointList>
        <CheckPointList :list="materialList" label="neues Material">Materialliste</CheckPointList>
        <span>
            <router-link to="/checklists">
                <q-btn label="Abbrechen" flat color="primary"></q-btn>
            </router-link>
            <q-btn label="Checkliste erstellen" @click="addChecklist" color="primary"></q-btn>
        </span>
    </div>
</template>

<style scoped>
.text-align {
    text-align: center;
}

.padding-sm {
    padding-bottom: 16px;
}

.padding-md {
    padding-bottom: 32px;
}

</style>
