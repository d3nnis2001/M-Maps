<script setup>
import {useSettingsStore} from "@/main/vue/stores/SettingsStore";
import {storeToRefs} from "pinia";
import {onMounted} from "vue";

const settingsStore = useSettingsStore()
const {impressum} = storeToRefs(settingsStore)

onMounted(async () => {
    await settingsStore.getImpressum()
})
</script>

<template>
    <div class="margin">
        <q-card flat bordered>
            <q-card-section v-html="impressum" />
        </q-card>
    </div>
</template>

<style scoped>
.margin {
    margin: 32px;
}
</style>
