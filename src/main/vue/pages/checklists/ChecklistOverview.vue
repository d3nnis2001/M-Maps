<script setup>
import {useChecklistTemplateStore} from "@/main/vue/stores/checklistTemplateStore";
import {onMounted, ref} from "vue";
import {RouterLink} from 'vue-router';

const checklistTemplateStore = useChecklistTemplateStore()

let checklistNames = ref([])

onMounted(async () => {
    checklistNames = await checklistTemplateStore.getAllChecklistTemplateNames()
})
</script>

<template>
<li v-for="checklist in checklistNames">
    <div class="text-black padding">
        {{ checklist }}
    </div>
</li>
    <router-link to="/checklists/create">
        <q-btn label="neue Checkliste erstellen"></q-btn>
    </router-link>
</template>

<style scoped>
.padding {
    padding: 8px;
}
</style>
