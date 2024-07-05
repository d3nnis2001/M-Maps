<script setup>
import {ref} from 'vue'
import {RouterView} from 'vue-router'
import {useUserStore} from "@/main/vue/stores/UserStore";

const rightDrawerOpen = ref(false)
const userStore = useUserStore()

const links = [
    { name: 'map', label: 'Map', to: '/map'},
    { name: 'dataimport', label: 'Datenverwaltung', to: '/dataimport', roles: ['Datenverwalter'] },
    { name: 'repair', label: 'Reparaturaufträge', to: '/repair', roles: ['Prüfer', 'Bearbeiter'] },
    { name: 'inspectionOrderOverview', label: 'Prüfaufträge', to: '/inspectionOrder', roles: ['Prüfer', 'Bearbeiter']},
    { name: 'admin', label: 'Nutzerverwaltung', to: '/admin', roles: ['Admin'] },
    { name: 'dataviewer', label: 'Dataviewer', to: '/dataviewer'}
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

</script>
<template>
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
                Prüfauftrag erstellen
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'editInspectionOrder'" align="middle">
                Prüfauftrag bearbeiten
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'adminmain'" align="middle">
                Nutzerverwaltung
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'impressum'" align="middle">
                Impressum
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'dataviewer'" align="middle">
                Dataviewer
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'dataviewerRoute'" align="middle">
                Dataviewer für Route
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'dataviewerPoint'" align="middle">
                Dataviewer für Punkt
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
        </q-list>
    </q-drawer>
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



