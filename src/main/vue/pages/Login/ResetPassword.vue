<script>
import {ref} from 'vue'
import {useQuasar} from 'quasar'
import {resetPassword, setPasswordNew} from "@/main/vue/api/auth";
import {useRoute, useRouter} from "vue-router";

export default {
    methods: {resetPassword},
    setup() {
        const $q = useQuasar()
        const password = ref('')
        const route = useRoute()
        const passAgain = ref('')
        const router = useRouter()

        async function setNewPassword() {
            if (password !== null && passAgain !== null &&
                password.value !== "" && password.value === passAgain.value) {
                const token = route.query.token;
                const email = route.query.email;
                console.log(password.value, token)
                const response = setPasswordNew(email, password.value, token)
                if (response) {
                    setTimeout(2000);
                    await router.push("login")
                    $q.notify({
                        type: 'positive',
                        message: 'Successfully reseted your password',
                        caption: 'You can now login with your new password'
                    });
                } else {
                    $q.notify({
                        type: 'negative',
                        message: 'Something went wrong when reseting your password',
                        caption: 'We apologize. Contact our support'
                    });
                }
            } else {
                $q.notify({
                    type: 'negative',
                    message: 'Wrong input',
                    caption: 'You have to fill in both password inputs with the same password'
                });
            }
        }
        return {password, passAgain, setNewPassword}
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
                            <div class="text-h4 text-align ">Passwort Neu setzen</div>
                        </div>
                        <div class="row-auto extra-padding">
                            <div class="rectangle"></div>
                        </div>
                        <div class="row-auto extra-padding">
                            <div class="text-align">
                                Bitte geben Sie ihr neues Passwort ein!
                            </div>
                        </div>
                        <div class="row-auto text-align extra-padding">
                            <q-input class="email-input extra-padding" type="password" v-model="password" label="Passwort"/>
                            <q-input class="email-input extra-padding" type="password" v-model="passAgain" label="Passwort erneut eingeben"/>
                            <q-btn label="Passwort neu setzen" @click="setNewPassword" color="primary" class=""></q-btn>
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
