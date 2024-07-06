<script setup>
import {ref} from "vue";

const {list} = defineProps(["list"])
const checked = ref(false)
</script>

<template>
    <div v-for="item in list" :key="item.id" class="items-center text-align width margin padding-xs">
        <span class="margin padding-xs">
            <q-checkbox v-model="checked" :label="item" disable/>
        </span>
    </div>
</template>

<style scoped>

</style>
