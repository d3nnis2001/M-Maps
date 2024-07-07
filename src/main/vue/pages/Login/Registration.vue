<script setup>
import {ref} from 'vue'
import {useQuasar} from 'quasar'
import {useRouter} from 'vue-router'
import registerUs from "@/main/vue/api/register";

const $q = useQuasar()
const router = useRouter()

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
function checkInputs() {
    let errormsg = [];

    if (!firstName.value.trim()) {
        errormsg.push("Please fill the input for your first name");
    }
    if (!name.value.trim()) {
        errormsg.push("Please fill the input for your last name");
    }
    if (!email.value.trim()) {
        errormsg.push("Please fill the input for your email");
    }
    if (!pass.value.trim()) {
        errormsg.push("Please choose a password");
    }
    if (!passAgain.value.trim()) {
        errormsg.push("Please repeat your password");
    }
    if (!model1.value || model1.value.length === 0) {
        errormsg.push("You have to choose atleast one role");
    }
    if (!model2.value) {
        errormsg.push("Choose your region");
    }
    if (!model3.value) {
        errormsg.push("Choose your service for the Deutsche Bahn");
    }
    if (pass.value.trim() && passAgain.value.trim() && pass.value !== passAgain.value) {
        errormsg.push("The actual password doesn't match the repeated one!");
    }

    if (errormsg.length > 0) {
        for (let i = 0; i < errormsg.length; i++) {
            $q.notify({
                type: 'negative',
                message: errormsg[i]
            })
        }
        return false;
    }
    return true;
}
function register() {
    if (checkInputs()) {
        const roles = model1.value.map(role => role.label);
        const region = model2.value ? [{ label: model2.value.label}] : [];
        const service = model3.value ? [{ label: model3.value.label}] : [];
        console.log(roles)
        if (registerUs(email.value, pass.value, firstName.value, name.value,region[0].label, service[0].label, roles)) {
            $q.notify({
                type: 'positive',
                message: 'Registration was successful!',
                caption: 'Your account will be activated once an admin unlocks your account'
            })
            router.push('start')
        } else {
            $q.notify({
                type: 'negative',
                message: 'Login failed',
                caption: 'This email is already registered!'
            })
        }
    }
}
</script>

<template>
    <div class="full-height-center">
        <div class="content-container">
            <div class="items-center">
                <q-card class="q-pa-md">
                    <q-card-section class="inner-card">
                        <div class="row-auto text-align extra-padding">
                            <img src="../../../resources/db-logo.png" alt="Nicht verfügbar">
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
                            <q-btn label="Registrieren" color="primary" @click=register class=""></q-btn>
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
