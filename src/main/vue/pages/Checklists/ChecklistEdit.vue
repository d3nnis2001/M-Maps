    <script setup>

import CheckPointEdit from "@/main/vue/pages/Checklists/CheckPointEdit.vue";
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
    <div class="centered-content">
        <h1 class="text-h4"><b>{{template.name}} bearbeiten</b></h1>
        <div class="outline">
            <h2 class="text-h6" style="display: flex; flex-direction: row; justify-content: flex-start; margin: 0; margin-left: 1vw"><b>Aufgabenliste bearbeiten</b></h2>
            <CheckPointEdit :list="taskList" label="Aufgaben"></CheckPointEdit>
            <h2 class="text-h6" style="display: flex; flex-direction: row; justify-content: flex-start; margin: 0; margin-left: 1vw"><b>Materialliste bearbeiten</b></h2>
            <CheckPointEdit :list="materialList" label="Material"></CheckPointEdit>
        </div>
        <div style="display: flex; flex-direction: row; justify-content: flex-start; margin-left: 1vw; margin-top: 30px">
            <q-btn label="Änderungen speichern" @click="editChecklist" color="primary"/>
            <router-link to="/checklists">
                <q-btn style="margin-left: 1.5vw" label="Abbrechen" color="primary"/>
            </router-link>
        </div>
    </div>
</template>

<style lang="scss">

.centered-content {
    display: flex;
    flex-direction: column;
    justify-content: center;
    text-align: center;
    margin-right: 5vw;
    margin-left: 5vw;
}

.outline2 {
    border: 2px solid var(--q-primary);
    padding: 20px;
    border-radius: 15px;
}

.outline {
    border: 1px solid var(--q-primary);
    padding: 20px;
    border-radius: 15px;
}

</style>
