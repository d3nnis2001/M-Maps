<script setup>
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";
import {ref} from "vue";
import {useQuasar} from "quasar";

const $q = useQuasar()

const pattern = /^.*\S.*/

const newItem = ref("")
const items = ref([])
const finalItems = ref([])
const checked = ref(false)

function addItem() {
    if (pattern.test(newItem.value)) {
        items.value.push({id: items.value.length + 1, text: newItem.value})
        finalItems.value.push(newItem.value)
        newItem.value = ""
    } else {
        $q.notify({
            type: 'warning',
            message: 'falsche Eingabe',
            caption: 'Die Eingabe für dieses Textfeld ist ungültig'
        })
    }
}

function removeItem(id) {
    items.value.splice(id-1, 1)
    finalItems.value.splice(id-1, 1)
}
</script>

<template>
    <span>
        <StandardInput v-model="newItem">
            <slot></slot>
        </StandardInput>
        <q-btn label="hinzufügen" @click="addItem" color="primary"></q-btn>
    </span>
    <div v-for="item in items" :key="item.id">
        <span>
            <q-checkbox  v-model="checked" :label="item.text" disable></q-checkbox>
            <q-btn @click="removeItem(item.id)" label="entfernen" outline color="primary"></q-btn>
        </span>
    </div>
    {{finalItems}}
</template>

<style scoped>

</style>
