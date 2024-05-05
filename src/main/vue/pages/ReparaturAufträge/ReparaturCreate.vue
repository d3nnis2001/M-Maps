<script>

import {ref} from "vue";
import {sendrepair} from "@/main/vue/api/reparatur";
import {useQuasar} from "quasar";


export default {
    setup () {
        const streckenabschnitt = ref('')
        const freigabe = ref('')
        const checkliste = ref('')
        const date = ref('2024/01/01')
        const date2 = ref('2024/01/02')
        const $q = useQuasar()
        const bem = ref('')
        function checkInputs() {
            let errormsg = [];

            if (!streckenabschnitt.value.trim()) {
                errormsg.push("Please fill in the track number");
            }
            if (!date.value.trim()) {
                errormsg.push("Please fill in the starting date");
            }
            if (!date2.value.trim()) {
                errormsg.push("Please fill in the ending date");
            }
            if (!freigabe.value.trim()) {
                errormsg.push("Please choose an authorized person");
            }
            if (!checkliste.value.trim()) {
                errormsg.push("Please select a checklist");
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
        function sendData() {
            const err = checkInputs()
            console.log(err)
            if (err) {
                return sendrepair(streckenabschnitt.value, date.value, date2.value, freigabe.value, checkliste.value, bem.value)
            }
        }

        return {
            streckenabschnitt,
            freigabe,
            checkliste,
            date,
            date2,
            bem,
            sendData
        }
    }
}
</script>

<template>
    <div class="basic-center">
        <div class="rec1">
            <div class="align-basic">
                <h4>Prüfkoordinaten/Streckenabschnitt</h4>
                    <q-input class="extra-mar" outlined v-model="streckenabschnitt" label="Streckenabschnitt" />
            </div>
            <div class="align-basic">
                <h4>Zeitraum</h4>
                <div class="align-mult">
                    <q-input filled v-model="date" mask="date" :rules="['date']">
                        <template v-slot:append>
                            <q-icon name="event" class="cursor-pointer">
                                <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                    <q-date v-model="date">
                                        <div class="row items-center justify-end">
                                            <q-btn v-close-popup label="Close" color="primary" flat />
                                        </div>
                                    </q-date>
                                </q-popup-proxy>
                            </q-icon>
                        </template>
                    </q-input>
                    <h4 class="extra-mar">bis</h4>
                    <q-input filled v-model="date2" mask="date" :rules="['date']">
                        <template v-slot:append>
                            <q-icon name="event" class="cursor-pointer">
                                <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                    <q-date v-model="date2">
                                        <div class="row items-center justify-end">
                                            <q-btn v-close-popup label="Close" color="primary" flat />
                                        </div>
                                    </q-date>
                                </q-popup-proxy>
                            </q-icon>
                        </template>
                    </q-input>
                </div>
            </div>
            <div class="align-basic">
                <div>
                    <h4 class="mar-align">Freigabeberechtigter</h4>
                    <h4>Checkliste</h4>
                </div>
                <div class="align-mult">
                    <q-input class="extra-mar" outlined v-model="freigabe" label="Freigabeberechtigter" />
                    <q-input class="extra-mar" outlined v-model="checkliste" label="Checkliste wählen" />
                </div>
            </div>
            <div class="align-basic">
                <h4>Bemerkungen</h4>
                <q-input class="extra-mar input-bem" outlined v-model="bem" label="Bemerkungen" />
            </div>
            <q-btn label="Reparaturauftrag anlegen" color="grey" @click=sendData class=""></q-btn>
        </div>
    </div>
</template>

<style>
.rec1 {
    display: flex;
    flex-direction: column;
    border-radius: 12px;
    border: 2px solid black;
    width: 90vw;
    height: 70vh;
    align-items: start;
    padding-left: 50px;
    padding-top: 50px;
    margin-bottom: 200px;
}

.input-bem {
    width: 84vw;
}

.basic-center {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    text-align: center;
}

.align-basic {
    display: flex;
    flex-direction: column;
    align-items: start;
    margin-bottom: 30px;
}
.align-mult {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
}
.extra-mar {
    margin-top: 10px;
    margin-right: 20px;
}
.mar-align {
    margin-right: 110px;
}

</style>
