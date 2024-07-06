<script>
import {onMounted, ref} from "vue";
import {getChecklists, sendRepair} from "@/main/vue/api/reparatur";
import {useQuasar} from "quasar";
import {useRoute, useRouter} from "vue-router";
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";

export default {
    components: {StandardInput},
    setup () {
        let streckenabschnitt = ref('')
        const freigabe = ref('')
        const checkliste = ref('')
        const checkvals = ref([])
        const date = ref('2024/01/01')
        const date2 = ref('2024/01/02')
        const $q = useQuasar()
        const bem = ref('')
        const router = useRouter()
        let vorLatitude = ref('')
        let vorLongitude = ref('')
        const freigabeValues = ref([])

        const route = useRoute();

        onMounted(async () => {
            const response = await getChecklists()
            console.log(response.length)
            for (let i = 0; i<response.length; i++) {
                checkvals.value.push({label: response[i], value: i})
            }
            console.log(checkvals.value[0].label)
            console.log('Query parameters:', route.query);
            console.log('streckenID:', route.query.streckenID);
            if (route.query.streckenID !== undefined) {
                streckenabschnitt.value = route.query.streckenID
                vorLatitude.value = route.query.latitude
                vorLongitude.value = route.query.longitude
            }
            freigabeValues.value.push("Regina S.");
            freigabeValues.value.push("Manfred D.");
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
                const response = sendRepair(streckenabschnitt.value, date.value, date2.value,
                    freigabe.value, checkliste.value["label"], bem.value, vorLongitude.value, vorLatitude.value)
                router.push({ path: "/repair" });
            }

        }

        return {
            streckenabschnitt,
            freigabe,
            freigabeValues,
            checkliste,
            date,
            date2,
            bem,
            sendData,
            checkvals,
            vorLatitude,
            vorLongitude
        }
    }
}
</script>

<template>
    <div class="outline">
        <div class="outer-container">
            <div class="text-with-input">
                <p style="font-weight: bold;">Prüfkoordinaten/Streckenabschnitt</p>
                <StandardInput class="extra-mar" v-model="streckenabschnitt" label="Streckenabschnitt"></StandardInput>
                <div class="row">
                    <div class="text-with-input mar-right">
                        <p style="font-weight: bold;">Latitude</p>
                        <StandardInput class="extra-mar" v-model="vorLatitude" label="Latitude" ></StandardInput>
                    </div>
                    <div class="text-with-input">
                        <p style="font-weight: bold;">Longitude</p>
                        <StandardInput class="extra-mar" v-model="vorLongitude" label="Longitude" ></StandardInput>
                    </div>
                </div>
                <div>
            </div>
            <p style="font-weight: bold;">Zeitraum (von - bis)</p>
            <div class="text-with-input row extra-mar">
                <q-input class="input-style mar-right" filled v-model="date" mask="date" :rules="['date']">
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
                <q-input  class="input-style" filled v-model="date2" mask="date" :rules="['date']">
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
            <div class="row extra-mar">
                    <div class="checkListInput">
                        <p style="font-weight: bold;">Freigabeberechtigter</p>
                        <q-select class="" outlined v-model="freigabe" :options="freigabeValues" label="Freigabeberechtigter" />
                    </div>
                    <div class="checkListInput">
                        <p style="font-weight: bold;">Checkliste</p>
                        <q-select class="" outlined v-model="checkliste" :options="checkvals" label="Checkliste" />
                    </div>
            </div>
            <div>
                <p style="font-weight: bold;">Bemerkungen</p>
                <q-input class="input-bem text-with-input" outlined color="primary" rounded v-model="bem" label="Bemerkungen" />
            </div>
            <q-btn class="button-set" size="16px" no-caps rounded label="Reparaturauftrag anlegen" color="primary" @click=sendData></q-btn>
        </div>
    </div>
</template>

<style lang="scss">
.outline {
    border: 2px solid $primary;
    padding: 20px;
    margin: 10px;
    border-radius: 15px;
    background-color: #F7F7F7;
    box-shadow: 0 6px 10px rgba(0, 0, 0, 0.3);
}

p {
    font-size: 16px;
    font-weight: bold;
}

.mar-right {
    margin-right: 20px;
}


.input-style {
    width: 100%;
    max-width: 288px;
}

.outer-container {
    display: flex;
    flex-direction: column;
}

.button-set {
    margin-top: 20px;
    width: 100%;
    max-width: 288px;
}

.input-bem {
    width: 100%;
}

.checkListInput {
    width: 100%;
    max-width: 288px;
    margin-bottom: 20px;
}

.extra-mar {
    margin-bottom: 20px;
}
</style>

