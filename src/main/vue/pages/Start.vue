<script setup>
import axios from "axios";
import {onMounted, onUnmounted, ref} from "vue";

axios.defaults.headers['Authorization'] = null;

const smallScreen = ref(false);
const largeScreen = ref(true);

onMounted(async () => {
    checkScreenSize();
    window.addEventListener('resize', checkScreenSize);

})

onUnmounted(() => {
    window.removeEventListener('resize', checkScreenSize);
});

const checkScreenSize = () => {
    const screenSize = window.innerWidth;
    smallScreen.value = screenSize <= 500;
    largeScreen.value = screenSize > 500;
};
</script>

<template>

    <div class="row extra-mar">
        <q-btn style="margin-left: 20px" flat color="white" text-color="black" @click="$router.push('/impressum')"  label="Impressum" />
        <q-btn style="margin-right: 20px" flat color="white" text-color="black" @click="$router.push('/login')" label="Login"/>

    </div>
    <div style="text-align: center;" v-if="!smallScreen && largeScreen">
        <q-img
            :src="'/src/main/resources/startseiteLogo.png'"
            style="max-width: 300px"
            align="center">
        </q-img>

        <h2 style="color:#EC0016"> Deutsche Bahn</h2>
        <h3 style="color:black"> M-Maps </h3>
        <p style="color:black"> A new way of finding pathways</p>
    </div>
    <div style="text-align: center;" v-if="smallScreen && !largeScreen">
        <q-img
            :src="'/src/main/resources/startseiteLogo.png'"
            style="max-width: 250px"
            align="center">
        </q-img>

        <h4 style="color:#EC0016"> Deutsche Bahn</h4>
        <h5 style="color:black"> M-Maps </h5>
        <p style="color:black"> A new way of finding pathways</p>
    </div>
</template>

<style scoped>
</style>
