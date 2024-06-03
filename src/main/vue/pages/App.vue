<script setup>
import {ref} from 'vue'
import {RouterView} from 'vue-router'
let boolStart = true;

const rightDrawerOpen = ref(false)

function toggleRightDrawer() {
    rightDrawerOpen.value = !rightDrawerOpen.value
}

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
                <q-toolbar-title v-if="$route.name === 'inspectionOrderOverview'" align="middle">
                    Prüfaufträge
                </q-toolbar-title>
                <q-toolbar-title v-if="$route.name === 'createInspectionOrder'" align="middle">
                    Prüfaufträge erstellen
                </q-toolbar-title>
                <q-toolbar-title v-if="$route.name === 'editInspectionOrder'" align="middle">
                    Prüfaufträge bearbeiten
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

        <q-drawer show-if-above v-model="rightDrawerOpen" side="left" behavior="mobile" elevated :width="200" :breakpoint="500">
            <q-list>
                <q-item clickable v-ripple>
                    <q-item-section align="middle" class="text-black">
                        <router-link to="/">Home</router-link>
                    </q-item-section>
                </q-item>
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
                        <router-link to="/inspectionOrder">Prüfaufträge</router-link>
                    </q-item-section>
                </q-item>
                <q-item clickable v-ripple>
                    <q-item-section align="middle" class="text-black">
                        <router-link to="/admin">Nutzerverwaltung</router-link>
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
    font-family: "Arial Black", system-ui;
}
</style>


