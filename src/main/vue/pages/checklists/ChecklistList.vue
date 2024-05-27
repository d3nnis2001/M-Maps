<script setup>
import {useChecklistTemplateStore} from "@/main/vue/stores/checklistTemplateStore";
import {onMounted} from "vue";
import {RouterLink} from 'vue-router';
import {storeToRefs} from "pinia";

const checklistTemplateStore = useChecklistTemplateStore()

const { templateNames } = storeToRefs(checklistTemplateStore)

onMounted(async () => {
    await checklistTemplateStore.getAllChecklistTemplateNames()
})
</script>

<template>
    <h3 class="items-center text-align">Checklisten</h3>
    <div class="items-center text-align">
        <q-list class="items-center text-align" separator padding>
            <q-item v-for="name in templateNames">
                <RouterLink :to="`checklists/${name}`">
                    <q-btn color="primary" flat :label="name" />
                </RouterLink>
            </q-item>
        </q-list>
        <router-link to="/checklists/create">
            <q-btn label="neue Checkliste erstellen" color="primary"></q-btn>
        </router-link>
    </div>
</template>

<style scoped>
.text-align{
    text-align: center;
}

</style>
