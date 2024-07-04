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
})

async function editImpressum() {
    await settingsStore.editImpressum(text.value)
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
    <div class="align">
        <div>
            <h1 class="text-h4">Aktuelles Impressum</h1>
            <div>
                {{impressum}}
            </div>
        </div>
        <div>
            <h2 class="text-h4">Impressum ändern</h2>
            <div>
                <q-input type="textarea" v-model="text" filled class="padding"/>
                <q-btn label="Speichern" color="primary" @click="editImpressum"/>
            </div>
        </div>
    </div>

</template>

<style scoped>
.padding {
    padding-left: 32px;
    padding-right: 32px;
    padding-bottom: 16px
}
.align {
    text-align: center
}

</style>
