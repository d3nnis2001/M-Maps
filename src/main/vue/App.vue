<script setup>
import {RouterView} from 'vue-router'
let boolStart = true;

function changeBool() {
    boolStart=false;
}

</script>
<template>
    <template v-if="boolStart">
        <q-btn flat color="white" text-color="black" @click="$router.push('/impressum') && changeBool()"  label="Impressum" />
        <q-btn flat color="white" text-color="black" @click="$router.push('/login') && changeBool()" label="Login"/>


        <center>
            <q-img
                :src="'/src/main/resources/startseiteLogo.png'"
                style="max-width: 300px"
                align="center">
            </q-img>

            <h2 style="color:#EC0016"> Deutsche Bahn</h2>
            <h3 style="color:black"> M-Maps </h3>
            <p style="color:black"> A new way of finding pathways</p>
        </center>
    </template>

    <q-layout view="hHh lpR fFf" v-else="boolStart">

        <q-header elevated class="bg-primary text-white" height-hint="98">
            <q-toolbar>
                <q-toolbar-title>
                    GPSE 2024 Template Project
                </q-toolbar-title>
            </q-toolbar>

            <q-tabs align="left">

                <q-route-tab to="/page1" label="Page One"/>
                <q-route-tab to="/page2" label="Page Two"/>
                <q-route-tab to="/page3" label="Page Three"/>
            </q-tabs>
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



