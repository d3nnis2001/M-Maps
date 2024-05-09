<script>
import {ref} from 'vue'
import {useQuasar} from 'quasar'
import {resetPassword} from "@/main/vue/api/auth";
import Heading from "@/main/vue/pages/Login/Heading.vue";
import Impressum from "@/main/vue/pages/Login/Impressum.vue";

export default {
    components: {Impressum, Heading},
    methods: {resetPassword},
    setup() {
        const $q = useQuasar()

        const email = ref('')

        async function sendPasswordMail() {
            if (email.value.includes("@")) {
                const response = resetPassword(email.value)
                if (response) {
                    $q.notify({
                        type: 'positive',
                        message: 'Check your email inbox',
                        caption: 'An email with the link for the password reset has been sent!'
                    });
                } else {
                    $q.notify({
                        type: 'negative',
                        message: 'Error',
                        caption: 'Invalid email!'
                    });
                }
            } else {
                $q.notify({
                    type: 'negative',
                    message: 'Error',
                    caption: 'Invalid email!'
                });
            }
        }

        return {sendPasswordMail, email}
    }
}

</script>

<template>
    <div class="full-height-center bg-grey-2 padding-xl">
        <q-card class="q-pa-md content-container items-center">
            <q-card-section class="inner-card">
                <Heading>Passwort zurücksetzen</Heading>
                <div class="row-auto padding-md">
                    <div class="text-align text-grey-9">
                        Bitte geben Sie die E-Mail-Adresse ein, mit der Sie sich registriert haben.
                        Wir senden Ihnen eine E-Mail mit weiteren Schritten zum Zurücksetzen der Login-Daten.
                    </div>
                </div>
                <div class="row-auto text-align padding-xl">
                    <q-input class="email-input padding-sm bg-grey-1" filled color="dark" v-model="email" label="E-Mail Adresse"/>
                    <q-btn label="Passwort Zurücksetzen" @click="sendPasswordMail" color="dark" class=""></q-btn>
                    <q-dialog v-model="alert">
                        <q-card>
                            <q-card-section>
                                <div class="text-h6">E-Mail verschickt</div>
                            </q-card-section>

                            <q-card-section class="q-pt-none">
                                Bitte überprüfen Sie Ihr E-Mail Postfach, um die Zurücksetzung des Passworts abzuschließen!
                            </q-card-section>

                            <q-card-actions align="right">
                                <q-btn flat label="OK" color="dark" v-close-popup/>
                            </q-card-actions>
                        </q-card>
                    </q-dialog>
                </div>
                <Impressum></Impressum>
            </q-card-section>
        </q-card>
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

.padding-sm {
    padding-bottom: 16px;
}

.padding-md {
    padding-bottom: 32px;
}

.padding-xl {
    padding-bottom: 64px;
}

.content-container {
    width: calc(100% - 48px);
    max-width: 384px;
    padding: 16px;
    height: 100%
}

.email-input {
    width: 100%;
    max-width: 288px;
    margin: 0 auto;
}

.text-align {
    text-align: center;
}
</style>
