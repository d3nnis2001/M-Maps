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
import ReparaturOverview from "@/main/vue/pages/ReparaturAufträge/ReparaturOverview.vue";
import ReparaturCreate from "@/main/vue/pages/ReparaturAufträge/ReparaturCreate.vue";
import ReparaturEdit from "@/main/vue/pages/ReparaturAufträge/ReparaturEdit.vue";
import AdminMain from "@/main/vue/pages/Nutzerverwaltung/AdminMain.vue";
import Dataviewer from "@/main/vue/pages/Dataviewer/Dataviewer.vue";
import DataviewerRoute from "@/main/vue/pages/Dataviewer/DataviewerRoute.vue";
import DataviewerPoint from "@/main/vue/pages/Dataviewer/DataviewerPoint.vue";

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
            path: "/dataviewer",
            name: "Dataviewer",
            component: Dataviewer
        },
        {
            path: "/dataviewer/route/:id/from/:fromId/to/:toId",
            name: "DataviewerRoute",
            component: DataviewerRoute
        },
        {
            path: "/dataviewer/point/:lon/:lat",
            name: "DataviewerPoint",
            component: DataviewerPoint
        }
  ]
})

router.beforeEach((to) => {
  // Something which should be executed before each routing
})

export default router
