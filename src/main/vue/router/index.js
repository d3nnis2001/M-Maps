import { createRouter, createWebHistory } from 'vue-router'

import DataImport from "../pages/DataImport/DataImport.vue";
import Login from '../pages/Login/Login.vue'
import Registration from "../pages/Login/Registration.vue";
import Password from "../pages/Login/Password.vue";
import ForgotPassword from "../pages/Login/ForgotPassword.vue";
import ResetPassword from "../pages/Login/ResetPassword.vue";
import Start from "../pages/Start.vue";
import Archiv from "@/main/vue/pages/archiv/Archiv.vue";
import InspectionOrderOverview from "@/main/vue/pages/Pruefauftrag/InspectionOrderOverview.vue";
import CreateInspectionOrder from "../pages/Pruefauftrag/CreateInspectionOrder.vue";
import EditInspectionOrder from "../pages/Pruefauftrag/EditInspectionOrder.vue";
import ReparaturOverview from "@/main/vue/pages/ReparaturAufträge/ReparaturOverview.vue";
import ReparaturCreate from "@/main/vue/pages/ReparaturAufträge/ReparaturCreate.vue";
import ReparaturEdit from "@/main/vue/pages/ReparaturAufträge/ReparaturEdit.vue";
import AdminMain from "@/main/vue/pages/Nutzerverwaltung/AdminMain.vue";
import MapView from "../pages/Map/Map.vue"
import TrackBuilderView from "@/main/vue/pages/TrackBuilder/TrackBuilderView.vue"
import EditUser from "@/main/vue/pages/Nutzerverwaltung/EditUser.vue";
import Dataviewer from "@/main/vue/pages/Dataviewer/Dataviewer.vue";
import DataviewerRoute from "@/main/vue/pages/Dataviewer/DataviewerRoute.vue";
import DataviewerPoint from "@/main/vue/pages/Dataviewer/DataviewerPoint.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'start',
            component: Start,
            meta: {showLogin: false, authorized: false}
        },
        {
            path: '/map',
            name: 'map',
            component: MapView,
            meta: {showLogin: false, authorized: true}
        },
        {
            path: '/dataimport',
            name: 'dataimport',
            component: DataImport,
            meta: {showLogin: false, authorized: true}
        },
        {
            path: '/archiv',
            name: 'archiv',
            component: Archiv,
            meta: {showLogin: false, authorized: true}
        },
        {
            path: "/login",
            name: "login",
            component: Login,
            meta: {showLogin: true, authorized: false}
        },
        {
            path: "/register",
            name: "register",
            component: Registration,
            meta: {showLogin: false, authorized: false}
        },
        {
            path: "/password",
            name: "password",
            component: Password,
            meta: {showLogin: false, authorized: false}
        },
        {
            path: "/forgotPassword",
            name: "forgotPassword",
            component: ForgotPassword,
            meta: {showLogin: false, authorized: false}
        },
        {
            path: "/reset-password",
            name: "setNewPassword",
            component: ResetPassword,
            meta: {showLogin: false, authorized: false}
        },
        {
            path: "/repair",
            name: "Repair",
            component: ReparaturOverview,
            meta: {showLogin: false, authorized: true}
        },
        {
            path: "/repair/create",
            name: "RepairCreate",
            component: ReparaturCreate,
            meta: {showLogin: false, authorized: true}
        },
        {
            path: "/repair/:name/edit",
            name: "RepairEdit",
            component: ReparaturEdit,
            meta: {showLogin: false, authorized: true}
        },
        {
            path: "/repair-order-trackbuilder",
            name: "TrackBuilder",
            component: TrackBuilderView,
            meta: {showLogin: false, authorized: true}
        },
        {
            path: "/admin",
            name: "adminmain",
            component: AdminMain,
            meta: {showLogin: false, authorized: true}
        },
        {
            path: "/admin/:username/editUser",
            name: "editUser",
            component: EditUser,
            meta: {showLogin: false, authorized: true}
        },
        {
            path: "/inspectionOrder",
            name: "inspectionOrderOverview",
            component: InspectionOrderOverview,
            meta: {showLogin: false, authorized: true}
        },
        {
            path: "/inspectionOrder/create",
            name: "createInspectionOrder",
            component: CreateInspectionOrder,
            meta: {showLogin: false, authorized: true}
        },
        {
            path: "/inspectionOrder/:inspectionOrderId/edit",
            name: "editInspectionOrder",
            component: EditInspectionOrder,
            meta: {showLogin: false, authorized: true}
        },
        {
            path: "/impressum",
            name: "impressum",
            meta: {showLogin: false, authorized: false}
        },
        {
            path: "/dataviewer",
            name: "dataviewer",
            component: Dataviewer,
            meta: {showLogin: false, authorized: true}
        },
        {
            path: "/dataviewer/route/:id/from/:fromId/to/:toId",
            name: "dataviewerRoute",
            component: DataviewerRoute,
            meta: {showLogin: false, authorized: true}
        },
        {
            path: "/dataviewer/route/:id",
            name: "dataviewerRouteOnly",
            component: DataviewerRoute,
            meta: {showLogin: false, authorized: true}
        },
        {
            path: "/dataviewer/point/:pointId",
            name: "dataviewerPoint",
            component: DataviewerPoint,
            meta: {showLogin: false, authorized: true}
        }
    ]
})

router.beforeEach((to) => {
    document.title = to.name;



    /*
    const authenticated = axios.defaults.headers['Authorization'] != null;

    if (to.name === 'forgotPassword') {
        next()
    }
    if (to.name === 'map' && !authenticated) {
        next({name: 'start'})
    }


    if (to.requiresAuth && !authenticated) {
        next({name: 'start'})
    } else if (to.requiresAuth && authenticated) {
        next({name: 'start'});
    } else if (!to.requiresAuth && to.name !== 'impressum' && authenticated) {
        next({name: ''})
    } else if (!to.requiresAuth && to.name === 'impressum' && authenticated) {
        next()
    } else if (!to.requiresAuth && to.name !== 'impressum' && to.name !== 'link' && authenticated) {
        next({name: 'map'})
    }

     */
})

export default router
