<script setup>

import { useChecklistTemplateStore } from "@/main/vue/stores/checklistTemplateStore";
import { storeToRefs } from "pinia";
import {onMounted} from "vue";
import {useRoute} from "vue-router";
import CheckPointList from "@/main/vue/pages/checklists/CheckPointList.vue";

const checklistTemplateStore = useChecklistTemplateStore()

const route = useRoute()

const {name} = route.params

const { template } = storeToRefs(checklistTemplateStore)

onMounted(async () => {
    await checklistTemplateStore.getTemplate(name)
})
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
        <q-btn label="Checkliste löschen" outline color="negative"/>
        <q-btn label="Checkliste bearbeiten" color="primary"/>
    </span>
</template>

<style scoped>

</style>
