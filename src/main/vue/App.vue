<script setup>
import {ref} from 'vue'
import {RouterView} from 'vue-router'

const rightDrawerOpen = ref(false)

function toggleRightDrawer() {
    rightDrawerOpen.value = !rightDrawerOpen.value
}

const sites = ref([{name: 'Home'}, {name: 'Start'}])

</script>
<template v-for="site in sites">
    <q-layout view="hHh lpR fFf">
        <q-header bordered class="q-py-xs">
            <!--
            <q-space></q-space>
            <q-title v-if="$route.name === 'Home'">Home</q-title>
            <q-space></q-space>
            <slot class="text-black" name="title">Default Title</slot>
            <q-space></q-space>-->
            <q-toolbar>
                <q-img :src="'/src/main/resources/DB_Logo.png'" align="left"></q-img>
                <q-toolbar-title v-if="$route.name === 'Home'" align="middle" style="font-size: 35px">
                    Home
                </q-toolbar-title>
                <q-toolbar-title v-if="$route.name === 'Start'" style="color:#1D1D1D" align="middle">
                    Start
                </q-toolbar-title>
                <q-btn dense flat round icon="menu" @click="toggleRightDrawer" align="right" />
            </q-toolbar>
        </q-header>

        <q-drawer show-if-above v-model="rightDrawerOpen" side="right" behavior="mobile" elevated :width="200" :breakpoint="500">
            <q-list>
                <q-item clickable v-ripple>
                    <q-item-section align="middle" class="text-black">
                        <router-link to="/">Home</router-link>
                    </q-item-section>
                </q-item>
                <q-item>
                    <q-item-section align="middle" class="text-black">
                        <router-link to="/Start">Start</router-link>
                    </q-item-section>
                </q-item>
            </q-list>
        </q-drawer>

        <q-page-container>
            <router-view/>
        </q-page-container>

    </q-layout>
</template>

<style  lang="scss" scoped>
.q-header {
    background-color: white;
}

.q-toolbar {
    color: $dark;
}

.q-img {
    max-width: 50px;
}
</style>
