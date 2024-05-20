<script setup>
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";
import {ref} from "vue";
import {useQuasar} from "quasar";

const $q = useQuasar()

const pattern = /^.*\S.*/

const newItem = ref("")
const checked = ref(false)

const {list, label} = defineProps(["list","label"])

function addItem() {
    if (pattern.test(newItem.value)) {
        list.push({id: list.length, text: newItem.value})
        newItem.value = ""
    } else {
        $q.notify({
            type: 'negative',
            message: 'falsche Eingabe',
            caption: 'Die Eingabe für dieses Textfeld ist ungültig'
        })
    }
}

function removeItem(id) {
    list.splice(id, 1)
    let newId = 0
    list.map(entry => entry.id = newId++)
}
</script>

<template>
    <span>
        <StandardInput v-model="newItem" :label="label" />
        <q-btn label="hinzufügen" @click="addItem" color="primary"/>
    </span>
    <div v-for="item in list" :key="item.id">
        <span>
            <q-checkbox  v-model="checked" :label="item.text" disable/>
            <q-btn @click="removeItem(item.id)" label="entfernen" outline color="primary"/>
        </span>
    </div>
</template>

<style scoped>

</style>
