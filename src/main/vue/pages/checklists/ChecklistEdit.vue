<script setup>

import CheckPointEdit from "@/main/vue/pages/checklists/CheckPointEdit.vue";
import {ref} from "vue";
import {useChecklistTemplateStore} from "@/main/vue/stores/checklistTemplateStore";
import {storeToRefs} from "pinia";
import {useQuasar} from "quasar";
import {useRouter} from "vue-router";
import {onMounted} from "vue";

const checklistTemplateStore = useChecklistTemplateStore()
const $q = useQuasar()
const router = useRouter()

const {template, templateEdit, listsEmpty, templateAdded} = storeToRefs(checklistTemplateStore)

const name = ref('')
const taskList = ref([])
const materialList = ref([])

onMounted(() => {
    for (let i = 0; i < template.value.tasks.length; i++) {
        taskList.value.push({
            id: i,
            text: template.value.tasks[i]
        });
    }
    for (let i = 0; i < template.value.material.length; i++) {
        materialList.value.push({
            id: i,
            text: template.value.material[i]
        });
    }
})

async function editChecklist() {
    await checklistTemplateStore.editChecklist(taskList.value, materialList.value)
    if (listsEmpty.value) {
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
            caption: 'Die Checkliste wurde erfolgreich aktualisiert.'
        })
        await router.push("/checklists")
    }
}
</script>

<template>
    <h1 class="text-align items-center text-h4">{{template.name}} bearbeiten</h1>
    <div class="text-align items-center">
        <CheckPointEdit :list="taskList" label="Aufgaben">Aufgabenliste bearbeiten</CheckPointEdit>
        {{taskList}}
        <CheckPointEdit :list="materialList" label="Material">Materialliste bearbeiten</CheckPointEdit>
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
