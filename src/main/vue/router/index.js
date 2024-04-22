import { createRouter, createWebHistory } from 'vue-router'
import StartView from '../views/Start.vue'
import DataImport from '../views/DataImport/DataImport.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: StartView
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
