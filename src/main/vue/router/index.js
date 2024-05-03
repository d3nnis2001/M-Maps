import { createRouter, createWebHistory } from 'vue-router'

import Start from "@/main/vue/views/Start.vue";
import MapView from '../views/Map.vue'
import DataImport from '../views/DataImport/DataImport.vue'

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
  ]
})

router.beforeEach((to) => {
  // Something which should be executed before each routing
})

export default router
