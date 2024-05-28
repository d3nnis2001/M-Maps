<script>

import {onMounted, ref} from "vue";
import {getChecklists, sendRepair} from "@/main/vue/api/reparatur";
import {useQuasar} from "quasar";
import { useRouter } from "vue-router";
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";

export default {
    components: {StandardInput},
    setup () {
        const streckenabschnitt = ref('')
        const freigabe = ref('')
        const checkliste = ref('')
        const checkvals = ref([])
        const date = ref('2024/01/01')
        const date2 = ref('2024/01/02')
        const $q = useQuasar()
        const bem = ref('')
        const router = useRouter()

        onMounted(async () => {
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
                const response = sendRepair(streckenabschnitt.value, date.value, date2.value, freigabe.value, checkliste.value["label"], bem.value)
                router.push({ path: "/repair" });
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
            <StandardInput class="extra-mar" v-model="streckenabschnitt" label="Streckenabschnitt"></StandardInput>
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
        <div class="align-basic">
            <p class="mar-align">Freigabeberechtigter</p>
            <StandardInput class="extra-mar" v-model="freigabe" label="Freigabeberechtigter" ></StandardInput>
            <p>Checkliste</p>
            <q-select class="checkListInput" outlined v-model="checkliste" :options="checkvals" label="Checkliste" />
            <p>Bemerkungen</p>
            <q-input class="extra-mar input-bem" outlined v-model="bem" label="Bemerkungen" />
        </div>
        <q-btn label="Reparaturauftrag anlegen" color="primary" @click=sendData class="button-set"></q-btn>
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
}
.align-mult {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    justify-content: flex-start;
}
.button-set {
    margin-top: 20px;
}

.input-bem {
    width: 100%;
}
.checkListInput {
    width: 150px;
}

</style>
