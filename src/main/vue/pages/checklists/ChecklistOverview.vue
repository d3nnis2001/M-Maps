<script setup>
import {getChecklists} from "@/main/vue/api/reparatur";
import {onMounted, ref} from "vue";
import router from "@/main/vue/router";

let checklists = ref([])

onMounted(async () => {
    checklists = await getChecklists()
})
</script>

<template>
<li v-for="checklist in checklists">
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
