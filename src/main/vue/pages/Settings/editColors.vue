<script setup>
import {ref} from "vue";
import {useSettingsStore} from "@/main/vue/stores/SettingsStore";

const settingsStore = useSettingsStore()

const primary = ref("")
const accent = ref("")
const positive = ref("")
const negative = ref("")
const warning = ref("")
const dark = ref("")
const info = ref("")

async function editColors() {
    const newColors = {
        primary: primary.value,
        accent: accent.value,
        positive: positive.value,
        negative: negative.value,
        warning: warning.value,
        dark: dark.value,
        info: info.value
    }
    await settingsStore.editColors(newColors)
}
</script>

<template>
    <div class="align margin">
        <div>
            <h1 class="text-h4 text-black">Farben ändern</h1>
            <div>
                <div class="padding-md">
                    <div class="text-h5 padding-sm">Primary</div>
                    <input type="color" v-model="primary" />
                </div>
                <div class="padding-md">
                    <div class="text-h5 padding-sm">Accent</div>
                    <input type="color" v-model="accent" />
                </div>
                <div class="padding-md">
                    <div class="text-h5 padding-sm">Positive</div>
                    <input type="color" v-model="positive" />
                </div>
                <div class="padding-md">
                    <div class="text-h5 padding-sm">Negative</div>
                    <input type="color" v-model="negative" />
                </div>
                <div class="padding-md">
                    <div class="text-h5 padding-sm">Warning</div>
                    <input type="color" v-model="warning" />
                </div>
                <div class="padding-md">
                    <div class="text-h5 padding-sm">Info</div>
                    <input type="color" v-model="info" />
                </div>
                <div class="padding-md">
                    <div class="text-h5 padding-sm">Dark</div>
                    <input type="color" v-model="dark" />
                </div>
                <q-btn label="Speichern" color="primary" @click="editColors"/>
            </div>
        </div>
    </div>
</template>

<style scoped>
.padding-sm {
    padding-bottom: 8px;
}
.padding-md {
    padding-bottom: 16px;
}
.margin {
    margin: 32px;
}

</style>
