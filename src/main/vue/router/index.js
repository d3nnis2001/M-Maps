import { createRouter, createWebHistory } from 'vue-router'
import StartView from '../pages/Start.vue'
import Login from '../pages/Login.vue'
import Registration from "@/main/vue/pages/Registration.vue";
import Password from "@/main/vue/pages/Password.vue";
import ForgotPassword from "@/main/vue/pages/ForgotPassword.vue";


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
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
    }
  ]
})

router.beforeEach((to) => {
  // Something which should be executed before each routing
})

export default router
