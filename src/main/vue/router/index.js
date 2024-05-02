import { createRouter, createWebHistory } from 'vue-router'

import Start from "@/main/vue/views/Start.vue";
import StartView from '../views/Start.vue'
import MapView from '../views/Map.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
      {path: '/', name: 'start', component: Start},
    {
      path: '/',
      name: 'home',
      component: StartView
    },
      {
          path: '/map',
          name: 'map',
          component: MapView
      }

  ]
})

router.beforeEach((to) => {
  // Something which should be executed before each routing
})

export default router
