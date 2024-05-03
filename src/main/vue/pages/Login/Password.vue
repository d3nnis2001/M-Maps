<script setup>
import {ref} from 'vue'
import {useLoginStore} from "../../stores/LoginStore"
import {useQuasar} from 'quasar'
import {useRouter} from 'vue-router'
import {useRoute} from 'vue-router';

const $q = useQuasar()
const router = useRouter()
const route = useRoute();
const loginStore = useLoginStore()

const passwordVar = ref('')

async function password() {
    const email = route.query.email;
    const success = await loginStore.checkPassword(email, passwordVar.value)
    if (success)
    {
        await router.push("map")
    } else {
        $q.notify({
            type: 'negative',
            message: 'Wrong password',
            caption: 'Password does not match email'
        });
    }

}
function navigateForgotPassword() {
    const email = route.query.email;
    router.push({path: "/forgotPassword", query: { email: email }})
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
                            <img src="../../../resources/db-logo.png" alt="Nicht verfügbar">
                        </div>
                        <div class="row-auto extra-padding">
                            <div class="text-h4 text-align ">Passwort Eingabe</div>
                        </div>
                        <div class="row-auto extra-padding">
                            <div class="rectangle"></div>
                        </div>
                        <div class="row-auto extra-padding">
                            <div class="text-align">
                                Bitte geben Sie Ihr Passwort ein
                            </div>
                        </div>
                        <div class="row-auto text-align extra-padding">
                            <q-input type="password" class="email-input extra-padding" v-model="passwordVar" label="Passwort"/>
                            <div class="text-align extra-padding" @click=navigateForgotPassword()>
                                Passwort Vergessen
                            </div>
                            <q-btn label="Anmelden" @click="password()" color="primary" class=""></q-btn>
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

.email-input {
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
