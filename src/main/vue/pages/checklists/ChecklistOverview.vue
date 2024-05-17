<script setup>
import {useChecklistTemplateStore} from "@/main/vue/stores/checklistTemplateStore";
import {onMounted, ref} from "vue";
import router from "@/main/vue/router";

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
    <q-btn label="neue Checkliste erstellen" @click="router.push({name: 'checklistsCreate'})"></q-btn>
</template>

<style scoped>
.padding {
    padding: 8px;
}
</style>
