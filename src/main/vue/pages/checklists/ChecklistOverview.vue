<script setup>
import {useChecklistTemplateStore} from "@/main/vue/stores/checklistTemplateStore";
import {onMounted} from "vue";
import {RouterLink} from 'vue-router';
import {storeToRefs} from "pinia";

const checklistTemplateStore = useChecklistTemplateStore()

const { checklistNames } = storeToRefs(checklistTemplateStore)

onMounted(async () => {
    await checklistTemplateStore.getAllChecklistTemplateNames()
})
</script>

<template>
    <li v-for="name in checklistNames">
        {{ name }}
    </li>
    <router-link to="/checklists/create">
        <q-btn label="neue Checkliste erstellen" color="primary"></q-btn>
    </router-link>
</template>

<style scoped>

</style>
