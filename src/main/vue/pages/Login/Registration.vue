<script setup>
import {ref} from 'vue'
import {useRegistrationStore} from "../../stores/RegistrationStore"
import {useQuasar} from 'quasar'
import {useRouter} from 'vue-router'
import Impressum from "@/main/vue/pages/Login/Impressum.vue";
import Heading from "@/main/vue/pages/Login/Heading.vue";
import Description from "@/main/vue/pages/Login/Description.vue";
import StandardCard from "@/main/vue/pages/Login/StandardCard.vue";
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";

const $q = useQuasar()
const router = useRouter()
const regStore = useRegistrationStore()

const firstName = ref('')
const name = ref('')
const email = ref('')
const pass = ref('')
const passAgain = ref('')
const model1 = ref(null)
const model2 = ref(null)
const model3 = ref(null)
const roles = ref([
    {label: "Prüfer", value: "1"},
    {label: "Bearbeiter", value: "2"},
    {label: "Datenverwalter", value: "3"},
    {label: "Administrator", value: "4"}
]);

const region = ref([
    {label: "BY", value: "1"},
    {label: "BW", value: "2"},
    {label: "MI", value: "3"},
    {label: "NW", value: "4"},
    {label: "SO", value: "5"},
    {label: "N", value: "6"},
    {label: "NO", value: "7"}
]);

const service = ref([
    {label: "DB Systemtechnik", value: "1"},
    {label: "DB Kommunikationstechnik", value: "2"}
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
        const region = model2.value ? [{label: model2.value.label}] : [];
        const service = model3.value ? [{label: model3.value.label}] : [];
        console.log(roles)
        if (regStore.registerUser(email.value, pass.value, firstName.value, name.value, region[0].label, service[0].label, roles)) {
            $q.notify({
                type: 'positive',
                message: 'Registration was successful!',
                caption: 'Your account will be activated once an admin unlocks your account'
            })
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
    <StandardCard>
        <Heading>Registrieren</Heading>
        <Description>Bitte füllen Sie folgende Informationen aus.</Description>
        <div class="row-auto text-align padding-md">
            <div class="padding-sm">
                <StandardInput v-model="firstName" label="Vorname"></StandardInput>
                <StandardInput v-model="name" label="Nachname"></StandardInput>
                <StandardInput v-model="email" label="E-Mail"></StandardInput>
            </div>
            <div class="padding-sm">
                <StandardInput type="password" v-model="pass" label="Passwort"></StandardInput>
                <StandardInput type="password" v-model="passAgain" label="Passwort wiederholen"></StandardInput>
            </div>
            <q-select class="input-field padding-sm" multiple use-chips standout v-model="model1"
                      :options="roles" label="Rollen"/>
            <q-select class="input-field padding-sm" standout v-model="model2" :options="region"
                      label="Region"/>
            <q-select class="input-field padding-md" standout v-model="model3" :options="service"
                      label="Fachdienst"/>
            <q-btn label="Registrieren" color="dark" @click=register class=""></q-btn>
        </div>
        <Impressum></Impressum>
    </StandardCard>


</template>

<style scoped>

.padding-md {
    padding-bottom: 32px;
}


.input-field {
    width: 100%;
    max-width: 288px;
    margin: 0 auto;
}

.text-align {
    text-align: center;
}

.padding-sm {
    padding-bottom: 16px;
}

.padding-md {
    padding-bottom: 32px;
}

</style>
