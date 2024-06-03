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
    <div class="padding-md">
        <div class="text-black text-align items-center padding-sm text-bold text-h6">
            <slot></slot>
        </div>
        <div class="items-center text-align padding-sm">
            <span class="padding-right">
                <StandardInput v-model="newItem" :label="label"/>
            </span>
            <q-btn label="hinzufügen" @click="addItem" color="primary"/>
        </div>
        <div v-for="item in list" :key="item.id" class="items-center text-align width margin padding-xs">
            <span class="margin padding-xs">
                <span class="padding-right">
                    <q-checkbox v-model="checked" :label="item.text" disable/>
                </span>
                <q-btn @click="removeItem(item.id)" label="entfernen" outline color="primary"/>
            </span>
        </div>
    </div>
</template>

<style scoped>
.text-align {
    text-align: center;
}

.padding-xs {
    padding-bottom: 8px;
}

.padding-sm {
    padding-bottom: 16px;
}

.padding-md {
    padding-bottom: 32px;
}

.padding-right {
    padding-right: 16px;
}

.margin {
    margin: 0 auto;
}

.width {
    width: 384px;
}

</style>
