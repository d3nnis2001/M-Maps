<script setup>
import Header from "@/main/vue/pages/Header.vue";
import Login from "@/main/vue/pages/Login/Login.vue";
import Start from "@/main/vue/pages/Start.vue";
</script>

<template>
    <Login v-if="$route.meta.showLogin">
        <router-view/>
    </Login>
    <Header v-else-if="!$route.meta.authorized">
        <router-view/>
    </Header>
    <router-view v-else/>
</template>



