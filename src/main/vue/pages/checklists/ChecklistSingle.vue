<script setup>

import { useChecklistTemplateStore } from "@/main/vue/stores/checklistTemplateStore";
import { storeToRefs } from "pinia";
import {onMounted} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useQuasar} from "quasar";
import CheckPointList from "@/main/vue/pages/checklists/CheckPointList.vue";

const checklistTemplateStore = useChecklistTemplateStore()
const $q = useQuasar()
const route = useRoute()
const router = useRouter()

const {name} = route.params

const { template, templateName, templateDeleted} = storeToRefs(checklistTemplateStore)

onMounted(async () => {
    await checklistTemplateStore.getTemplate(name)
})

function deleteTemplate() {
    checklistTemplateStore.deleteTemplate(template.name)
    if (!templateDeleted) {
        $q.notify({
            type: 'negative',
            message: 'Fehler',
            caption: 'Die Checkliste konnte nicht entfernt werden.'
        })
    } else {
        $q.notify({
            type: 'positive',
            message: 'Erfolg',
            caption: 'Die Checkliste wurde erfolgreich entfernt.'
        })
    }
    router.push("/checklists")
}
</script>

<template>
    <h1 class="text-h4"> {{ template.name }} </h1>
    <div>
        <h2 class="text-h6">
            Aufgaben
        </h2>
        <CheckPointList :list="template.tasks" />
    </div>
    <div>
        <h2 class="text-h6">
            Material
        </h2>
        <CheckPointList :list="template.material" />
    </div>
    <span>
        <q-btn label="Checkliste löschen" outline color="negative" @click="deleteTemplate"/>
        <q-btn label="Checkliste bearbeiten" color="primary"/>
    </span>
</template>

<style scoped>

</style>
