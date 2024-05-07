<script setup>

</script>

<template>

    <q-btn flat color="white" text-color="black" @click="$router.push('/impressum')"  label="Impressum" />
    <q-btn flat color="white" text-color="black" @click="$router.push('/login')" label="Login"/>


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

<style scoped>

</style>
