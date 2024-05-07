import { createRouter, createWebHistory } from 'vue-router'

import StartView from '../pages/Start.vue'
import MapView from '../pages/Map/Map.vue'
import DataImport from "../pages/DataImport/DataImport.vue";
import Login from '../pages/Login/Login.vue'
import Registration from "../pages/Login/Registration.vue";
import Password from "../pages/Login/Password.vue";
import ForgotPassword from "../pages/Login/ForgotPassword.vue";
import ResetPassword from "../pages/Login/ResetPassword.vue";
import Start from "../pages/Start.vue";

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
