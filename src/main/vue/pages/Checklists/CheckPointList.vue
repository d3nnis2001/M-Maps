<script setup>
import {ref} from "vue";

const {list} = defineProps(["list"])
const checked = ref(false)
</script>

<template>
    <div v-for="item in list" :key="item.id" class="centered-content">
        <span class="margin padding-xs">
            <q-checkbox v-model="checked" :label="item" disable/>
        </span>
    </div>
</template>

<style lang="scss">

.centered-content {
    display: flex;
    flex-direction: column;
    justify-content: center;
    text-align: center;
    margin-right: 5vw;
    margin-left: 5vw;
}

</style>
