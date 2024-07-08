<script>
import {ref} from 'vue'
import {useQuasar} from 'quasar'
import {resetPassword} from "@/main/vue/api/auth";
import Heading from "@/main/vue/pages/Login/Heading.vue";
import Impressum from "@/main/vue/pages/Login/ImpressumTemplate.vue";
import Description from "@/main/vue/pages/Login/Description.vue";
import StandardCard from "@/main/vue/pages/Login/StandardCard.vue";
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";

export default {
    components: {StandardInput, StandardCard, Description, Impressum, Heading},
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
    <StandardCard>
        <Heading>Passwort zurücksetzen</Heading>
        <Description>
            Bitte geben Sie die E-Mail-Adresse ein, mit der Sie sich registriert haben.
            Wir senden Ihnen eine E-Mail mit weiteren Schritten zum Zurücksetzen der Login-Daten.
        </Description>
        <div class="row-auto text-align padding-xl">
            <StandardInput v-model="email" label="E-Mail Adresse"></StandardInput>
            <q-btn label="Passwort Zurücksetzen" @click="sendPasswordMail" color="primary" class=""></q-btn>
            <q-dialog v-model="alert">
                <q-card>
                    <q-card-section>
                        <div class="text-h6">E-Mail verschickt</div>
                    </q-card-section>

                    <q-card-section class="q-pt-none">
                        Bitte überprüfen Sie Ihr E-Mail Postfach, um die Zurücksetzung des Passworts abzuschließen!
                    </q-card-section>

                    <q-card-actions align="right">
                        <q-btn flat label="OK" color="primary" v-close-popup/>
                    </q-card-actions>
                </q-card>
            </q-dialog>
        </div>
        <Impressum></Impressum>
    </StandardCard>
</template>

<style scoped>

.padding-xl {
    padding-bottom: 64px;
}

.text-align {
    text-align: center;
}
</style>
