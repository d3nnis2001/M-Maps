import { createRouter, createWebHistory } from 'vue-router'

import StartView from '../pages/Start.vue'
import MapView from '../pages/Map.vue'
import DataImport from "@/main/vue/pages/DataImport/DataImport.vue";
import Login from '../pages/Login.vue'
import Registration from "@/main/vue/pages/Registration.vue";
import Password from "@/main/vue/pages/Password.vue";
import ForgotPassword from "@/main/vue/pages/ForgotPassword.vue";
import ResetPassword from "@/main/vue/pages/ResetPassword.vue";
import Start from "@/main/vue/pages/Start.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
        {
            path: '/',
            name: 'start',
            component: Start
        },
        {
            path: '/map',
            name: 'map',
            component: MapView
        },
        {
            path: '/dataimport',
            name: 'dataimport',
            component: DataImport
        },
        {
            path: "/",
            name: "home",
            component: StartView
        },
        {
            path: "/login",
            name: "login",
            component: Login
        },
        {
            path: "/register",
            name: "register",
            component: Registration
        },
        {
            path: "/password",
            name: "password",
            component: Password
        },
        {
            path: "/forgotPassword",
            name: "forgotPassword",
            component: ForgotPassword
        },
        {
            path: "/reset-password",
            name: "setNewPassword",
            component: ResetPassword
        }
  ]
})

router.beforeEach((to) => {
  // Something which should be executed before each routing
})

export default router
