<script>

import {onMounted, ref} from "vue";
import {getChecklists, sendRepair} from "@/main/vue/api/reparatur";
import {useQuasar} from "quasar";

export default {
    setup () {
        const streckenabschnitt = ref('')
        const freigabe = ref('')
        const checkliste = ref('')
        const checkvals = ref([])
        const date = ref('2024/01/01')
        const date2 = ref('2024/01/02')
        const $q = useQuasar()
        const bem = ref('')

        onMounted(async () => {
            console.log("hello")
            const response = await getChecklists()
            console.log(response.length)
            for (let i = 0; i<response.length; i++) {
                checkvals.value.push({label: response[i], value: i})
            }
            console.log(checkvals.value[0].label)
        })


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
            if (!checkliste.value) {
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
                return sendRepair(streckenabschnitt.value, date.value, date2.value, freigabe.value, checkliste.value, bem.value)
            }
        }

        return {
            streckenabschnitt,
            freigabe,
            checkliste,
            date,
            date2,
            bem,
            sendData,
            checkvals
        }
    }
}
</script>

<template>
    <div class="outline">
        <div class="align-basic">
            <p>Prüfkoordinaten/Streckenabschnitt</p>
                <q-input class="extra-mar" outlined v-model="streckenabschnitt" label="Streckenabschnitt" />
        </div>
        <div class="align-basic">
            <p>Zeitraum</p>
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
                <p class="extra-mar">bis</p>
            </div>
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
        <div class="align-mult">
            <div class="align-basic">
                <p class="mar-align">Freigabeberechtigter</p>
                <q-input class="extra-mar" outlined v-model="freigabe" label="Freigabeberechtigter" />
            </div>
            <div class="align-basic">
                <p>Checkliste</p>
                <q-select v-model="checkliste" :options="checkvals" label="Checkliste" />
            </div>
        </div>
        <div class="align-basic">
            <p>Bemerkungen</p>
            <q-input class="extra-mar input-bem" outlined v-model="bem" label="Bemerkungen" />
        </div>
        <q-btn label="Reparaturauftrag anlegen" color="grey" @click=sendData class=""></q-btn>
    </div>
</template>

<style>
.outline {
    border: 1px solid black;
    padding: 20px;
    margin: 20px;
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
    align-items: start;
    justify-content: start;
}
.extra-mar {
    margin-top: 10px;
    margin-right: 20px;
}
.mar-align {
    margin-right: 110px;
}

</style>
