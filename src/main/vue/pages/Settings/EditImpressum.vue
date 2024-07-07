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
    <div>
        <div>
            <h1 class="text-h4 align">Aktuelles Impressum</h1>
            <div class="margin">
                <q-card flat bordered>
                    <q-card-section v-html="impressum" />
                </q-card>
            </div>
        </div>
        <div>
            <h2 class="text-h4 align">Impressum ändern</h2>
            <div>
                <q-editor class="margin" color="dark" v-model="text" min-height="5rem" />
                <div class="align">
                    <q-btn label="Speichern" color="primary" @click="editImpressum"/>
                </div>
            </div>
        </div>
    </div>

</template>

<style scoped>
.margin {
    margin: 32px;
}
.align {
    text-align: center
}

</style>
