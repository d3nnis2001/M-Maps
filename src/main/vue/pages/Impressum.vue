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
    <div class="text-align padding">
        {{impressum}}
    </div>
</template>

<style scoped>
.text-align {
    text-align: center;
}
.padding {
    padding: 32px
}
</style>
