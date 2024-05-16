<script setup>
import {RouterView} from 'vue-router'

</script>
<template>
    <q-layout v-if="$route.name === 'start' || $route.name === 'login' || $route.name === 'register' || $route.name === 'forgotPassword' || $route.name === 'setNewPassword' || $route.name === 'password'">
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



    </q-layout>
</template>

<style scoped>
</style>



