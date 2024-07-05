<script setup>
import Header from "@/main/vue/pages/Header.vue";
import Login from "@/main/vue/pages/Login/Login.vue";
</script>

<template>
    <Header v-if="$route.meta.showHeader">
        <router-view/>
    </Header>
    <Login v-else-if="$route.meta.showLogin">
        <router-view/>
    </Login>
    <router-view v-else/>
</template>



