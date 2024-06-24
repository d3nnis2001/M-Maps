import { createRouter, createWebHistory } from 'vue-router'

import StartView from '../pages/Start.vue'
import DataImport from "../pages/DataImport/DataImport.vue";
import Login from '../pages/Login/Login.vue'
import Registration from "../pages/Login/Registration.vue";
import Password from "../pages/Login/Password.vue";
import ForgotPassword from "../pages/Login/ForgotPassword.vue";
import ResetPassword from "../pages/Login/ResetPassword.vue";
import Start from "../pages/Start.vue";
import InspectionOrderOverview from "@/main/vue/pages/Pruefauftrag/InspectionOrderOverview.vue";
import CreateInspectionOrder from "../pages/Pruefauftrag/CreateInspectionOrder.vue";
import EditInspectionOrder from "../pages/Pruefauftrag/EditInspectionOrder.vue";
import ReparaturOverview from "@/main/vue/pages/ReparaturAufträge/ReparaturOverview.vue";
import ReparaturCreate from "@/main/vue/pages/ReparaturAufträge/ReparaturCreate.vue";
import ReparaturEdit from "@/main/vue/pages/ReparaturAufträge/ReparaturEdit.vue";
import AdminMain from "@/main/vue/pages/Nutzerverwaltung/AdminMain.vue";
import MapView from "../pages/Map/Map.vue"
import Archiv from "@/main/vue/pages/archiv/Archiv.vue";

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
          path: '/archiv',
          name: 'archiv',
          component: Archiv
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
        },
        {
            path: "/repair",
            name: "Repair",
            component: ReparaturOverview
        },
        {
            path: "/repair/create",
            name: "RepairCreate",
            component: ReparaturCreate
        },
        {
            path: "/repair/:name/edit",
            name: "RepairEdit",
            component: ReparaturEdit
        },
        {
            path: "/admin",
            name: "adminmain",
            component: AdminMain
        },
      {
          path: "/inspectionOrder",
          name: "inspectionOrderOverview",
          component: InspectionOrderOverview
      },
      {
          path: "/inspectionOrder/create",
          name: "createInspectionOrder",
          component: CreateInspectionOrder
      },
      {
          path: "/inspectionOrder/:inspectionOrderId/edit",
          name: "editInspectionOrder",
          component: EditInspectionOrder
      },
      {
          path: "/impressum",
          name: "impressum",
      }
  ]
})

router.beforeEach((to) => {
    document.title = to.name;
  // Something which should be executed before each routing
})

export default router
