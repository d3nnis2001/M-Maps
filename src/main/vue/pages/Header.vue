<script setup>
import {onMounted, computed, ref} from 'vue'
import {RouterView} from 'vue-router'
import {storeToRefs} from "pinia";
import {useSettingsStore} from "@/main/vue/stores/SettingsStore";
import {useUserStore} from "@/main/vue/stores/UserStore";
import router from "../router";

const rightDrawerOpen = ref(false)
const userStore = useUserStore()
const settingsStore = useSettingsStore()
const {imageEmpty} = storeToRefs(settingsStore)

onMounted(async () => {
    await settingsStore.checkLogo()
})

const links = [
    { name: 'map', label: 'Map', to: '/map', roles: ['Administrator', 'Bearbeiter', 'Prüfer', 'Datenverwalter']},
    { name: 'repair', label: 'Reparaturaufträge', to: '/repair', roles: ['Prüfer', 'Freigabeberechtigter'] },
    { name: 'inspectionOrderOverview', label: 'Prüfaufträge', to: '/inspectionOrder', roles: ['Prüfer', 'Bearbeiter']},
    { name: 'admin', label: 'Nutzerverwaltung', to: '/admin', roles: ['Administrator'] },
    { name: 'dataimport', label: 'Datenverwaltung', to: '/dataimport', roles: ['Datenverwalter', 'Prüfer'] },
    { name: 'settings', label: 'Design anpassen', to: '/settings', roles: ['Administrator']},
    { name: 'templates', label: 'Checklisten', to: '/checklists', roles: ['Administrator', 'Bearbeiter', 'Prüfer', 'Datenverwalter']},
    { name: 'userprofile', label: 'Nutzerprofil', to: '/userprofile/:username', roles: ['Administrator', 'Bearbeiter', 'Prüfer', 'Datenverwalter']},
    { name: 'archiv', label: 'Archiv', to: '/archiv', roles: ['Administrator', 'Bearbeiter', 'Prüfer', 'Datenverwalter']},
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

function handleLinkClick(link) {
    if (link.name === 'home') {
        userStore.logout()
        router.push("/")
    }
}

const filteredLinks = computed(() => {
    return links.filter(link => {
        if (!link.roles) {
            return true
        }
        return link.roles.some(role => userStore.hasRole(role))
    })
})

</script>
<template>
    <q-header bordered class="q-py-xs" elevated>
        <q-toolbar>
            <q-btn dense flat round icon="menu" @click="toggleRightDrawer" align="right" />
            <q-toolbar-title v-if="$route.name === 'map'" align="middle">
                M-MAPS
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'dataimport'" align="middle">
                Datenverwaltung
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'Repair'" align="middle">
                Reparaturaufträge
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'archiv'" align="middle">
                Archiv
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
            <q-toolbar-title v-if="$route.name === 'settings'" align="middle">
                Design anpassen
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'editImpressum'" align="middle">
                Impressum
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'editLogo'" align="middle">
                Logo
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'editColors'" align="middle">
                Farben
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'checklistOverview'" align="middle">
                Checklisten
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'checklistCreate'" align="middle">
                Checkliste erstellen
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'ChecklistEdit'" align="middle">
                Checkliste bearbeiten
            </q-toolbar-title>
            <q-toolbar-title v-if="$route.name === 'userprofile'" align="middle">
                Nutzerprofil
            </q-toolbar-title>
            <q-img v-if="imageEmpty" :src="`/src/main/resources/db-logo.png`" align="left"/>
            <q-img v-else src="/api/settings/logo" align="left"/>
        </q-toolbar>
    </q-header>

    <q-drawer show-if-above v-model="rightDrawerOpen" side="left" overlay behavior="mobile" elevated :width="200" :breakpoint="500">
        <q-list>
            <q-item v-for="link in filteredLinks" :key="link.name" clickable tag="router-link" :to="link.to">
                <q-item-section>
                    <q-item-label class="text-center">{{ link.label }}</q-item-label>
                </q-item-section>
            </q-item>
            <q-separator />
            <q-item v-for="link in links2" @click="handleLinkClick(link)" :key="link.name"  clickable tag="router-link" :to="link.to">
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



