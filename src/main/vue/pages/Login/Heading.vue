<script setup>
import {useSettingsStore} from "@/main/vue/stores/SettingsStore";
import {storeToRefs} from "pinia";
import {onMounted} from "vue";

const settingsStore = useSettingsStore()
const {imageEmpty} = storeToRefs(settingsStore)

onMounted(async () => {
    await settingsStore.checkLogo()
})
</script>

<template>
    <div class="items-center">
        <div class="row-auto text-align padding-sm">
            <q-img v-if="imageEmpty" :src="`/src/main/resources/db-logo.png`" align="left"/>
            <q-img v-else src="/api/settings/logo" align="left"/>
        </div>
        <h1 class="text-h4 text-align text-bold">
            <slot></slot>
        </h1>
        <div class="row-auto padding-md">
            <div class="rectangle"></div>
        </div>
    </div>
</template>

<style scoped>
.rectangle {
    width: 72px;
    height: 8px;
    background-color: rgba(236, 0, 22, 1);
    border-radius: 16px;
    margin: auto;
}

.text-align {
    text-align: center;
}

.padding-md {
    padding-bottom: 32px;
}

</style>
