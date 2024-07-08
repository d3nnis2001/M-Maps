<script setup>
import Header from "@/main/vue/pages/Header.vue"
import Login from "@/main/vue/pages/Login/Login.vue"
</script>

<template>
    <q-layout view="hHh lpR fFf">
        <Header v-if="$route.meta.showHeader"/>
        <Login v-if="$route.meta.showLogin"/>
        <q-page-container>
            <router-view/>
        </q-page-container>
    </q-layout>
</template>

<style>
</style>
