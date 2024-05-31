<script setup>
import {ref} from 'vue'
import {RouterView} from 'vue-router'
let boolStart = true;

const rightDrawerOpen = ref(false)
const links = [
    { name: 'map', label: 'Map', to: '/map' },
    { name: 'dataimport', label: 'Datenverwaltung', to: '/dataimport' },
    { name: 'repair', label: 'Reparaturaufträge', to: '/repair' },
    { name: 'admin', label: 'Nutzerverwaltung', to: '/admin' },
]
const links2 = [
    { name: 'home', label: 'Abmelden', to: '/' },
]
const links3 = [
    { name: 'impressum', label: 'Impressum', to: '/impressum' },
]

function toggleRightDrawer() {
    rightDrawerOpen.value = !rightDrawerOpen.value
}

function changeBool() {
    boolStart=false;
}

</script>
<template>
    <q-layout v-if="$route.name === 'start' || $route.name === 'login' || $route.name === 'register'
    || $route.name === 'password' || $route.name === 'forgotPassword' || $route.name === 'setNewPassword'">
        <q-page-container>
            <router-view/>
        </q-page-container>
    </q-layout>


    <q-layout view="hHh lpR fFf" v-else>
        <q-header bordered class="q-py-xs" elevated>
            <q-toolbar>
                <q-img :src="'/src/main/resources/db-logo.png'" align="left"></q-img>
                <q-toolbar-title v-if="$route.name === 'map'" align="middle">
                    M-MAPS
                </q-toolbar-title>
                <q-toolbar-title v-if="$route.name === 'dataimport'" align="middle">
                    Datenverwaltung
                </q-toolbar-title>
                <q-toolbar-title v-if="$route.name === 'Repair'" align="middle">
                    Reparaturaufträge
                </q-toolbar-title>
                <q-toolbar-title v-if="$route.name === 'RepairCreate'" align="middle">
                    Reparaturauftrag erstellen
                </q-toolbar-title>
                <q-toolbar-title v-if="$route.name === 'RepairEdit'" align="middle">
                    Reparaturauftrag bearbeiten
                </q-toolbar-title>
                <q-toolbar-title v-if="$route.name === 'adminmain'" align="middle">
                    Nutzerverwaltung
                </q-toolbar-title>
                <q-toolbar-title v-if="$route.name === 'impressum'" align="middle">
                    Impressum
                </q-toolbar-title>
                <q-btn dense flat round icon="menu" @click="toggleRightDrawer" align="right" />
            </q-toolbar>
        </q-header>

        <q-drawer show-if-above v-model="rightDrawerOpen" side="left" overlay behavior="mobile" elevated :width="200" :breakpoint="500">
            <q-list>
                <q-item v-for="link in links" :key="link.name" clickable tag="router-link" :to="link.to">
                    <q-item-section>
                        <q-item-label class="text-center">{{ link.label }}</q-item-label>
                    </q-item-section>
                </q-item>
                <q-separator />
                <q-item v-for="link in links2" :key="link.name" clickable tag="router-link" :to="link.to">
                    <q-item-section>
                        <q-item-label class="text-center">{{ link.label }}</q-item-label>
                    </q-item-section>
                </q-item>
                <q-separator />
                <q-item v-for="link in links3" :key="link.name" clickable tag="router-link" :to="link.to">
                    <q-item-section>
                        <q-item-label class="text-center">{{ link.label }}</q-item-label>
                    </q-item-section>
                </q-item>

                <!--
                <q-item clickable v-ripple>
                    <q-item-section align="middle" class="text-black">
                        <router-link to="/map">Map</router-link>
                    </q-item-section>
                </q-item>
                <q-item clickable v-ripple>
                    <q-item-section align="middle" class="text-black">
                        <router-link to="/dataimport">Datenverwaltung</router-link>
                    </q-item-section>
                </q-item>
                <q-item clickable v-ripple>
                    <q-item-section align="middle" class="text-black">
                        <router-link to="/repair">Reparaturaufträge</router-link>
                    </q-item-section>
                </q-item>
                <q-item clickable v-ripple>
                    <q-item-section align="middle" class="text-black">
                        <router-link to="/admin">Nutzerverwaltung</router-link>
                    </q-item-section>
                </q-item>
                <q-separator />
                <q-item clickable v-ripple>
                    <q-item-section align="middle" class="text-black">
                        <router-link to="/">Abmelden</router-link>
                    </q-item-section>
                </q-item>
                <q-separator />
                <q-item clickable v-ripple>
                    <q-item-section align="middle" class="text-black">
                        <router-link to="/impressum">Impressum</router-link>
                    </q-item-section>
                </q-item>
                -->
            </q-list>
        </q-drawer>

        <q-page-container>
            <router-view/>
        </q-page-container>

    </q-layout>
</template>

<style  lang="scss" scoped>
.q-drawer {
    color: $dark;
}

.q-header {
    background-color: white;
}

.q-toolbar {
    color: $dark;
}

.q-img {
    max-width: 50px;
}

.q-layout {
    font-family: "DB Sans Glyphen", system-ui;
}
</style>


