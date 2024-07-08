<script setup>
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";
import {onMounted, ref} from "vue";
import {useQuasar} from "quasar";

const $q = useQuasar()

const pattern = /^.*\S.*/

const newItem = ref("")
const checked = ref(false)

const {list, label} = defineProps(["list", "label"])

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
    <div class="center-it outline">
        <slot></slot>
        <div class="input-container">
            <StandardInput v-model="newItem" :label="label" />
            <q-btn class="mar-left" @click="addItem" icon="add" outline color="primary" />
        </div>
        <div class="list-container">
            <div v-for="item in list" :key="item.id" class="list-item">
                <q-checkbox v-model="checked" :label="item.text" disable />
                <q-btn class="mar-left" @click="removeItem(item.id)" icon="delete" outline color="primary" />
            </div>
        </div>
    </div>
</template>

<style lang="scss">

.center-it {
    display: flex;
    flex-direction: column;
    align-items: start;
}

.input-container {
    display: flex;
    flex-direction: row;
    align-items: center;
    margin-bottom: 20px;
    justify-content: center;
}

.mar-left {
    margin-left: 20px;
}

.list-container {
    display: flex;
    flex-direction: column;
    width: 100%;
}

.list-item {
    display: flex;
    flex-direction: row;
    align-items: center;
    margin-bottom: 10px;
}

.outline {
    border: 1px solid var(--q-primary);
    padding: 20px;
    border-radius: 15px;
}
</style>
