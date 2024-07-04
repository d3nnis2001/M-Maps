<script setup>
import {ref} from "vue";
import {useSettingsStore} from "@/main/vue/stores/SettingsStore";

const settingsStore = useSettingsStore()

const primary = ref("")
const secondary = ref("")

async function editColors() {
    const newColors = {
        primary: primary.value,
        secondary: secondary.value
    }
    console.log(newColors)
    await settingsStore.editColors(newColors)
}
</script>

<template>
    <div class="align">
        <div>
            <h1 class="text-h4 text-black">Impressum ändern</h1>
            <div>
                <input type="color" v-model="primary" />
                <input type="color" v-model="secondary" />
                <q-btn label="Speichern" color="primary" @click="editColors"/>
            </div>
        </div>
    </div>
</template>

<style scoped>

</style>
