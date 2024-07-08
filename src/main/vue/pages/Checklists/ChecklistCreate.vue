<script setup>
import {useChecklistTemplateStore} from "@/main/vue/stores/checklistTemplateStore";
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";
import {ref} from "vue";
import {storeToRefs} from "pinia";
import {useQuasar} from "quasar";
import CheckPointList from "@/main/vue/pages/Checklists/CheckPointEdit.vue";
import {useRouter} from "vue-router";

const checklistTemplateStore = useChecklistTemplateStore()
const $q = useQuasar()
const router = useRouter()

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
        await router.push("/checklists")
    }
}
</script>

<template>
    <h1 class="text-align items-center text-h4"><b>Neue Checkliste erstellen</b></h1>
    <div class="centered-content" style="margin-bottom: 20px">
        <StandardInput style="margin-left: 1vw; margin-bottom: 20px" v-model="name" label="Name der Checkliste" class="padding-md"/>
        <h2 class="text-h6" style="display: flex; flex-direction: row; justify-content: flex-start; margin: 0; margin-left: 1vw"><b>Aufgabenliste</b></h2>
        <CheckPointList :list="taskList" label="neue Aufgabe"></CheckPointList>
        <h2 class="text-h6" style="display: flex; flex-direction: row; justify-content: flex-start; margin: 0; margin-left: 1vw"><b>Materialliste</b></h2>
        <CheckPointList :list="materialList" label="neues Material"></CheckPointList>
    </div>
    <div style="margin-left: 6vw">
        <q-btn label="Checkliste erstellen" @click="addChecklist" color="primary"/>
        <router-link to="/checklists">
            <q-btn style="margin-left: 2vw" label="Abbrechen" color="primary"/>
        </router-link>
    </div>
</template>

<style>

.centered-content {
    display: flex;
    flex-direction: column;
    justify-content: center;
    text-align: center;
    margin-right: 5vw;
    margin-left: 5vw;
}

</style>
