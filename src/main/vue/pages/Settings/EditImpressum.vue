<script setup>
import {useSettingsStore} from "@/main/vue/stores/SettingsStore";
import {storeToRefs} from "pinia";
import {onMounted, ref} from "vue";

const settingsStore = useSettingsStore()
const {impressum} = storeToRefs(settingsStore)
const text = ref("")

onMounted(async () => {
    await settingsStore.getImpressum()
})
</script>

<template>
    <div style="max-width: 300px">
        <q-input type="textarea" v-model="text" filled/>
        <q-btn label="Impressum ändern" color="primary"/>
    </div>

</template>

<style scoped>

</style>
