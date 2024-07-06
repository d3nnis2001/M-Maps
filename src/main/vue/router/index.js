import { createRouter, createWebHistory } from 'vue-router'

import StartView from '../pages/Start.vue'
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
import checklistOverview from "@/main/vue/pages/Checklists/ChecklistList.vue";
import checklistCreate from "@/main/vue/pages/Checklists/ChecklistCreate.vue";
import checklistSingle from "@/main/vue/pages/Checklists/ChecklistSingle.vue";
import checklistEdit from "@/main/vue/pages/Checklists/ChecklistEdit.vue";
import UserProfile from "@/main/vue/pages/Nutzerprofil/UserProfile.vue";

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
            path: "/repair-order-trackbuilder",
            name: "TrackBuilder",
            component: TrackBuilderView
        },
        {
            path: "/admin",
            name: "adminmain",
            component: AdminMain
        },
        {
            path: "/admin/:username/editUser",
            name: "editUser",
            component: EditUser
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
        },
        {
            path: "/dataviewer",
            name: "dataviewer",
            component: Dataviewer
        },
        {
            path: "/dataviewer/route/:id/from/:fromId/to/:toId",
            name: "dataviewerRoute",
            component: DataviewerRoute
        },
        {
            path: "/dataviewer/route/:id",
            name: "dataviewerRouteOnly",
            component: DataviewerRoute
        },
        {
            path: "/dataviewer/point/:pointId",
            name: "dataviewerPoint",
            component: DataviewerPoint
        },
        {
            path: "/checklists",
            name: "checklistOverview",
            component: checklistOverview,
        },
        {
            path: "/checklists/create",
            name: "checklistCreate",
            component: checklistCreate,
        },
        {
            path: "/checklists/:name",
            name: "checklistSingle",
            component: checklistSingle,
        },
        {
            path: "/checklists/edit/:name",
            name: "checklistEdit",
            component: checklistEdit,
        },
        {
            path: "/userprofile/:username",
            name: "userprofile",
            component: UserProfile,
        }
    ]
})

router.beforeEach((to) => {
    document.title = to.name;
    // Something which should be executed before each routing
})

export default router
