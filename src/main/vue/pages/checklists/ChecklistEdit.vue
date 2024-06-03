<script setup>

import CheckPointList from "@/main/vue/pages/checklists/CheckPointEdit.vue";
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";
import {ref} from "vue";
import {useChecklistTemplateStore} from "@/main/vue/stores/checklistTemplateStore";
import {storeToRefs} from "pinia";
import {useQuasar} from "quasar";
import {useRouter} from "vue-router";
import {onMounted} from "vue";


const checklistTemplateStore = useChecklistTemplateStore()
const $q = useQuasar()
const router = useRouter()

const {template, templateEdit, invalidInput, listsEmpty, templateAdded} = storeToRefs(checklistTemplateStore)

const name = ref('')
const temporary = template.tasks
const taskList = ref([])
const materialList = ref([])

onMounted(()=> {
    for (let i = 0; i < temporary.length; i++) {
        taskList.value.push({
            id: i,
            text: temporary[i]
        });
    }
    materialList.value = template.material.map((item, index) => ({
        id: index,
        text: item
    }));
})

async function editChecklist() {
    templateEdit.name = name.value
    templateEdit.tasks = taskList.value.map(entry => entry.text);
    templateEdit.material = materialList.value.map(entry => entry.text)
    await checklistTemplateStore.editChecklist()
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
    <h1 class="text-align items-center text-h4">{{template.name}} bearbeiten</h1>
    <div class="text-align items-center">
        <StandardInput v-model="name" label="Name ändern" class="padding-md"/>
        <CheckPointList :list="taskList" label="Aufgaben">Aufgabenliste</CheckPointList>
        {{taskList}}
        <CheckPointList :list="materialList" label="Material">Materialliste</CheckPointList>
        <span>
            <router-link to="/checklists">
                <q-btn label="Abbrechen" flat color="primary"/>
            </router-link>
            <q-btn label="Änderungen speichern" @click="editChecklist" color="primary"/>
        </span>
    </div>
</template>

<style scoped>

</style>
