<script setup>
import {useSettingsStore} from "@/main/vue/stores/SettingsStore";
import {onMounted, ref} from "vue";
import {useQuasar} from "quasar";
import {storeToRefs} from "pinia";

const $q = useQuasar()
const settingsStore = useSettingsStore()
const {success} = storeToRefs(settingsStore)
const text = ref("")

onMounted(async () => {
    text.value = await settingsStore.getImpressum()
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
    <div style="max-width: 300px">
        <q-input type="textarea" v-model="text" filled/>
        <q-btn label="Impressum ändern" color="primary" @click="editImpressum"/>
    </div>
</template>

<style scoped>

</style>
