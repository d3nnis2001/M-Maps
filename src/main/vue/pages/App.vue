<script setup>
import {RouterView} from 'vue-router'
let boolStart = true;

function changeBool() {
    boolStart=false;
}

</script>
<template>
    <q-layout v-if="$route.name === 'start'">
        <q-page-container>
            <router-view/>
        </q-page-container>
    </q-layout>

    <q-layout view="hHh lpR fFf" v-else>

        <q-header elevated class="bg-primary text-white" height-hint="98">
            <q-toolbar>
                <q-toolbar-title>
                    GPSE 2024 Template Project
                </q-toolbar-title>
            </q-toolbar>
        </q-header>

        <q-page-container>
            <router-view/>
        </q-page-container>

        <q-footer elevated class="bg-secondary text-white">
            <q-toolbar>
                <q-toolbar-title>
                    <div>This is a footer. Hi!</div>
                </q-toolbar-title>
            </q-toolbar>
        </q-footer>


    </q-layout>
</template>

<style scoped>
</style>



