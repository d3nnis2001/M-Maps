import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { Quasar } from 'quasar'
import quasarUserOptions from './quasar-user-options'
import VueApexCharts from "vue3-apexcharts";

import App from './pages/App.vue'
import router from './router'

// import store from "./stores";

import 'quasar/src/css/index.sass'
import '@quasar/extras/material-icons/material-icons.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Quasar, quasarUserOptions)
app.use(VueApexCharts);
// app.use(store)

app.mount('#app')
