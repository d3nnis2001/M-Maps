import { createRouter, createWebHistory } from 'vue-router'
import StartView from '../pages/Start.vue'
import Login from '../pages/Login.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: StartView
    },
    {
      path:'/login',
      name: 'login',
      component: Login
    },
  ]
})

router.beforeEach((to) => {
  // Something which should be executed before each routing
})

export default router
