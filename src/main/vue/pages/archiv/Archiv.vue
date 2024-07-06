<script setup>
import {ref} from "vue";
import ReparaturArchiv from "@/main/vue/pages/archiv/ReparaturArchiv.vue";
import PruefArchiv from "@/main/vue/pages/archiv/PruefArchiv.vue";

const route = ref(0);
const onClickPruefauftraege = () => {
    route.value = 0;
}
const onClickReparaturauftraege = () => {
    route.value = 1;
}
</script>

<template>
    <q-page>
        <PruefArchiv v-if="route === 0" :z-index="1"/>
        <ReparaturArchiv v-if="route === 1" :z-index="1"/>
        <q-page-sticky position="bottom-left" :offset="[10, 10]" :z-index="9999">
            <div class="q-mt-md">
                <q-fab
                    vertical-actions-align="left"
                    color="red"
                    icon="keyboard_arrow_right"
                    direction="right"
                    :z-index="9999"
                >
                    <q-fab-action
                        v-model="route"
                        color="red"
                        @click="onClickPruefauftraege"
                        icon="fact_check"
                    />
                    <q-fab-action
                        color="red"
                        @click="onClickReparaturauftraege"
                        icon="construction"
                    />
                </q-fab>
            </div>
        </q-page-sticky>
    </q-page>
</template>

<style scoped>

</style>
