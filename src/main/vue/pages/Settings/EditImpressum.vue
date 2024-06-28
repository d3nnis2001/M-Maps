<script setup>
import {useSettingsStore} from "@/main/vue/stores/SettingsStore";
import {onMounted, ref} from "vue";
import {useQuasar} from "quasar";
import {storeToRefs} from "pinia";

const $q = useQuasar()
const settingsStore = useSettingsStore()
const {impressum, success} = storeToRefs(settingsStore)
const text = ref("")

onMounted(async () => {
    await settingsStore.getImpressum()
    console.log(oldText.value)
})

async function editImpressum() {
    await settingsStore.editImpressum(text.value)
    console.log(success.value)
    if (success.value) {
        $q.notify({
            type: "positive",
            message: "Erfolg",
            caption: "Das Impressum wurde erfolgreich aktualisiert"
        })
    } else {
        $q.notify({
            type: "negative",
            message: "Fehler",
            caption: "Das Impressum konnte nicht aktualisiert werden"
        })
    }
}

</script>

<template>
    <div>
        <h1 class="text-h4">altes Impressum</h1>
        {{impressum}}
    </div>
    <div style="max-width: 480px">
        <h2 class="text-h4">Impressum ändern</h2>
        <q-input type="textarea" v-model="text" filled/>
        <q-btn label="Speichern" color="primary" @click="editImpressum"/>
    </div>
</template>

<style scoped>

</style>
