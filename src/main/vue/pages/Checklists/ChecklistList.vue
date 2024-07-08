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
    <div class="centered-content">
        <h3 class="items-center text-align"><b>Checklisten</b></h3>
        <div class="outline-nomar">
            <div class="items-center text-align">
                <q-list class="items-center text-align" separator padding>
                    <q-item v-for="name in templateNames">
                        <RouterLink :to="`checklists/${name}`">
                            <q-btn color="primary" flat :label="name" />
                        </RouterLink>
                    </q-item>
                </q-list>
            </div>
        </div>
        <div style="justify-content: flex-start">
            <router-link to="/checklists/create">
                <q-btn label="neue Checkliste erstellen" color="primary"></q-btn>
            </router-link>
        </div>
    </div>
</template>

<style lang="scss">

.outline {
    border: 2px solid $primary;
    padding: 20px;
    border-radius: 15px;
}
.centered-content {
    display: flex;
    flex-direction: column;
    justify-content: center;
    text-align: center;
    margin-right: 5vw;
    margin-left: 5vw;
}

.text-align{
    text-align: center;
}

</style>
