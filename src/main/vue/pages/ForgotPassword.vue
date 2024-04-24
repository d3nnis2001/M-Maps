<script>
import {ref} from 'vue'
import {useQuasar} from 'quasar'
import {useRouter} from 'vue-router'

const $q = useQuasar()
const router = useRouter()

const email = ref('')

function alert() {
    $q.dialog({
        dark: true,
        title: 'Alert',
        message: 'Some message',
        ok: "OK"
    })
}

export default {
    setup () {
        return {
            alert: ref(false),
            confirm: ref(false),
            prompt: ref(false),

            address: ref('')
        }
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
                            <div class="text-h4 text-align ">Passwort Zurücksetzen</div>
                        </div>
                        <div class="row-auto extra-padding">
                            <div class="rectangle"></div>
                        </div>
                        <div class="row-auto extra-padding">
                            <div class="text-align">
                                Bitte geben Sie die E-Mail-Adresse ein, mit der Sie sich registriert haben.
                                Wir senden Ihnen eine E-Mail mit weiteren Schritten zum Zurücksetzen der Login-Daten.
                            </div>
                        </div>
                        <div class="row-auto text-align extra-padding">
                            <q-input class="email-input extra-padding" v-model="email" label="E-Mail Adresse"/>
                            <q-btn label="Passwort Zurücksetzen" @click="alert = true" color="primary" class=""></q-btn>
                            <q-dialog v-model="alert">
                                <q-card>
                                    <q-card-section>
                                        <div class="text-h6">E-Mail verschickt</div>
                                    </q-card-section>

                                    <q-card-section class="q-pt-none">
                                        Bitte überprüfen Sie Ihr E-Mail Postfach, um mit der Zurücksetzung des Passworts
                                        abzuschließen!
                                    </q-card-section>

                                    <q-card-actions align="right">
                                        <q-btn flat label="OK" color="primary" v-close-popup />
                                    </q-card-actions>
                                </q-card>
                            </q-dialog>
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
