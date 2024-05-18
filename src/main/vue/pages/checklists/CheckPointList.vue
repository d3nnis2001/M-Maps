<script setup>
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";
import {ref} from "vue";
import {useQuasar} from "quasar";

const $q = useQuasar()

const pattern = /^.*\S.*/

const newItem = ref("")
const items = ref([])
const checked = ref(false)

function addItem() {
    if (pattern.test(newItem.value)) {
        items.value.push(newItem.value)
        newItem.value = ""
    } else {
        $q.notify({
            type: 'mistake',
            message: 'falsche Eingabe',
            content: 'Die Eingabe für dieses Textfeld ist ungültig'
        })
    }
}
</script>

<template>
    <span>
        <StandardInput v-model="newItem">
            <slot></slot>
        </StandardInput>
        <q-btn label="hinzufügen" @click="addItem" color="primary"></q-btn>
    </span>
    <div v-for="item in items">
        <q-checkbox  v-model="checked" :label="item" disabled="true"></q-checkbox>
    </div>
</template>

<style scoped>

</style>
