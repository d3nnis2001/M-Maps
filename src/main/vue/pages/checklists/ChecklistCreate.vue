<script setup>
import {useChecklistTemplateStore} from "@/main/vue/stores/checklistTemplateStore";
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";
import {ref} from "vue";

const checklistTemplateStore = useChecklistTemplateStore();

const name = ref('')

async function addChecklist() {
    await checklistTemplateStore.addChecklist(name.value)
}

</script>

<template>
    <StandardInput v-model="name">Name der Checkliste</StandardInput>
    <span>
        <router-link to="/checklists">
            <q-btn label="Abbrechen"></q-btn>
        </router-link>
        <q-btn label="Checkliste erstellen" @click="addChecklist"></q-btn>
    </span>

</template>

<style scoped>

</style>
