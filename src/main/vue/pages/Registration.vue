<script setup>
import {ref} from 'vue'
import {useUserStore} from "../stores/RegistrationStore"
import {useQuasar} from 'quasar'
import {useRouter} from 'vue-router'

const $q = useQuasar()
const router = useRouter()
const userStore = useUserStore()

const firstName = ref('')
const name = ref('')
const email = ref('')
const pass = ref('')
const passAgain = ref('')
const model1 = ref(null)
const model2 = ref(null)
const model3 = ref(null)
const roles = ref([
    { label: "Prüfer", value: "1" },
    { label: "Bearbeiter", value: "2" },
    { label: "Datenverwalter", value: "3" },
    { label: "Administrator", value: "4" }
]);

const region = ref([
    { label: "BY", value: "1" },
    { label: "BW", value: "2" },
    { label: "MI", value: "3" },
    { label: "NW", value: "4" },
    { label: "SO", value: "5" },
    { label: "N", value: "6" },
    { label: "NO", value: "7" }
]);

const service = ref([
    { label: "DB Systemtechnik", value: "1" },
    { label: "DB Kommunikationstechnik", value: "2" }
]);

function register() {
    if (1 === 1) {
        router.push('/')
    } else {
        $q.notify({
            type: 'negative',
            message: 'Login Fehlgeschlagen',
            caption: 'Diese Email-Adresse ist noch nicht registriert'
        })
    }
}
// https://www.figma.com/file/KE0rSr4lUzKzWQp7nY2jDZ/M-Maps-Prototyp?type=design&node-id=2-8&mode=design&t=atOavQXYG9PWr9Zr-4
</script>

<template>
    <div class="full-height-center">
        <div class="content-container">
            <div class="items-center">
                <q-card class="q-pa-md">
                    <q-card-section class="inner-card">
                        <div class="row-auto text-align extra-padding">
                            <img src="../../resources/db-logo.png" alt="Nicht verfügbar">
                        </div>
                        <div class="row-auto extra-padding">
                            <div class="text-h4 text-align ">Registrieren</div>
                        </div>
                        <div class="row-auto extra-padding">
                            <div class="rectangle"></div>
                        </div>
                        <div class="row-auto extra-padding">
                            <div class="text-align">
                                Bitte füllen Sie folgende Informationen aus
                            </div>
                        </div>
                        <div class="row-auto text-align extra-padding">
                            <q-input class="input-field extra-padding" v-model="firstName" label="Vorname"/>
                            <q-input class="input-field extra-padding" v-model="name" label="Nachname"/>
                            <q-input class="input-field extra-padding" v-model="email" label="E-Mail"/>
                            <q-input type="password" class="input-field extra-padding" v-model="pass" label="Passwort"/>
                            <q-input type="password" class="input-field extra-padding" v-model="passAgain" label="Passwort erneut angeben"/>
                            <q-select class="input-field extra-padding" multiple use-chips standout v-model="model1" :options="roles" label="Rollen" />
                            <q-select class="input-field extra-padding" standout v-model="model2" :options="region" label="Region" />
                            <q-select class="input-field extra-padding" standout v-model="model3" :options="service" label="Fachdienst" />
                            <q-btn label="Registrieren" color="primary" class=""></q-btn>
                        </div>
                        <div class="bg-grey-4 impressum-padding">
                            <div class="text-black text-align justify-center">Impressum</div>
                        </div>
                    </q-card-section>
                </q-card>
            </div>
        </div>
    </div>
</template>

<style scoped>

.full-height-center {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    width: 100%;
}

.impressum-padding {
    padding-bottom: 200px;
}

.extra-padding {
    padding-bottom: 20px;
}

.content-container {
    width: calc(100% - 40px);
    max-width: 500px;
    padding: 20px;
    box-shadow: 0 0 10px rgba(0,0,0,0.6);
    background: white;
}

.input-field {
    width: 100%;
    max-width: 300px;
    margin: 0 auto;
}

.text-align {
    text-align: center;
}

.rectangle {
    width: 72px;
    height: 7px;
    background-color: rgba(236, 0, 22, 1);
    border-radius: 20px;
    margin: auto;
}
</style>
