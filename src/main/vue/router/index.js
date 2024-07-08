import { createRouter, createWebHistory } from 'vue-router'
import { useSettingsStore } from "@/main/vue/stores/SettingsStore";

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

import changeSettings from "@/main/vue/pages/Settings/ChangeSettings.vue";
import editImpressum from "@/main/vue/pages/Settings/EditImpressum.vue";
import editLogo from "@/main/vue/pages/Settings/editLogo.vue";
import Impressum from "@/main/vue/pages/Impressum.vue";
import editColors from "@/main/vue/pages/Settings/editColors.vue";

import checklistOverview from "@/main/vue/pages/Checklists/ChecklistList.vue";
import checklistCreate from "@/main/vue/pages/Checklists/ChecklistCreate.vue";
import checklistSingle from "@/main/vue/pages/Checklists/ChecklistSingle.vue";
import checklistEdit from "@/main/vue/pages/Checklists/ChecklistEdit.vue";
import UserProfile from "@/main/vue/pages/Nutzerprofil/UserProfile.vue";
import axios from "axios";
import {useUserStore} from "../stores/UserStore";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'start',
            component: Start,
            meta: {showLogin: false, showHeader: false, authorized: false}
        },
        {
            path: '/map',
            name: 'map',
            component: MapView,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: '/dataimport',
            name: 'dataimport',
            component: DataImport,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: '/archiv',
            name: 'archiv',
            component: Archiv,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/login",
            name: "login",
            component: Login,
            meta: {showLogin: true, showHeader: false, authorized: false}
        },
        {
            path: "/register",
            name: "register",
            component: Registration,
            meta: {showLogin: false, showHeader: false, authorized: false}
        },
        {
            path: "/password",
            name: "password",
            component: Password,
            meta: {showLogin: false, showHeader: false, authorized: false}
        },
        {
            path: "/forgotPassword",
            name: "forgotPassword",
            component: ForgotPassword,
            meta: {showLogin: false, showHeader: false, authorized: false}
        },
        {
            path: "/reset-password",
            name: "setNewPassword",
            component: ResetPassword,
            meta: {showLogin: false, showHeader: false, authorized: false}
        },
        {
            path: "/repair",
            name: "Repair",
            component: ReparaturOverview,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/repair/create",
            name: "RepairCreate",
            component: ReparaturCreate,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/repair/:name/edit",
            name: "RepairEdit",
            component: ReparaturEdit,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/repair-order-trackbuilder",
            name: "TrackBuilder",
            component: TrackBuilderView,
            meta: {showLogin: false, showHeader: false, authorized: false}
        },
        {
            path: "/admin",
            name: "adminmain",
            component: AdminMain,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/admin/:username/editUser",
            name: "editUser",
            component: EditUser,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/inspectionOrder",
            name: "inspectionOrderOverview",
            component: InspectionOrderOverview,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/inspectionOrder/create",
            name: "createInspectionOrder",
            component: CreateInspectionOrder,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/inspectionOrder/:inspectionOrderId/edit",
            name: "editInspectionOrder",
            component: EditInspectionOrder,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/impressum",
            name: "impressum",
            component: Impressum,
            meta: {showLogin: false, showHeader: true, authorized: false}
        },
        {
            path: "/dataviewer",
            name: "dataviewer",
            component: Dataviewer,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/dataviewer/route/:id/from/:fromId/to/:toId",
            name: "dataviewerRoute",
            component: DataviewerRoute,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/dataviewer/route/:id",
            name: "dataviewerRouteOnly",
            component: DataviewerRoute,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/dataviewer/point/:pointId",
            name: "dataviewerPoint",
            component: DataviewerPoint,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/checklists",
            name: "checklistOverview",
            component: checklistOverview,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/checklists/create",
            name: "checklistCreate",
            component: checklistCreate,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/checklists/:name",
            name: "checklistSingle",
            component: checklistSingle,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/checklists/edit/:name",
            name: "checklistEdit",
            component: checklistEdit,
            meta: {showLogin: false, showHeader: true, authorized: true}

        },
        {
            path: "/settings",
            name: "settings",
            component: changeSettings,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/settings/edit/impressum",
            name: "editImpressum",
            component: editImpressum,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/settings/edit/logo",
            name: "editLogo",
            component: editLogo,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/settings/edit/colors",
            name: "editColors",
            component: editColors,
            meta: {showLogin: false, showHeader: true, authorized: true}
        },
        {
            path: "/userprofile/:username",
            name: "userprofile",
            component: UserProfile,
            meta: {showLogin: false, showHeader: true, authorized: true}
        }
    ]
})

router.beforeEach(async(to, from, next) => {
    const authenticated = axios.defaults.headers['Authorization'] !== null;
    document.title = to.name;
    await useSettingsStore().getColors()
    console.log(to.meta.authorized)
    console.log(authenticated)
    const userStore = useUserStore();

    if (to.meta.authorized && !authenticated) {
        console.log("2")
        next({name: 'start'});
    } else if (authenticated && to.name !== 'start') {
        if (!userStore.hasRole('Administrator') && (to.name === 'adminmain' || to.name === 'EditUser' || to.name === 'checklistOverview' || to.name === 'checklistCreate' || to.name === 'checklistEdit' ||
            to.name === 'checklistSingle' || to.name === 'settings' || to.name === 'editImpressum' || to.name === 'editLogo' || to.name === 'editColors')) {
            console.log("TEST1")
            next({name: from.name})
        } else if (!userStore.hasRole('Prüfer') && (to.name === 'Repair' || to.name === 'RepairCreate' || to.name === 'RepairEdit')) {
            console.log("TEST2")
            next({name: from.name})
        } else if ((!userStore.hasRole('Prüfer') && !userStore.hasRole('Bearbeiter')) && (to.name === 'inspectionOrderOverview' || to.name === 'createInspectionOrder' || to.name === 'editInspectionOrder')) {
            console.log("TEST3")
            next({name: from.name})
        } else if ((!userStore.hasRole('Prüfer') && !userStore.hasRole('Datenverwalter')) && to.name === 'dataimport' ) {
            console.log("TEST4")
            next({name: from.name})
        } else if ((!userStore.hasRole('Prüfer') && !userStore.hasRole('Bearbeiter'))  && to.name === 'archiv' ) {
            console.log("TEST5")
            next({name: from.name})
        } else {
            next();
        }
    } else {
        next()
    }
})

export default router
