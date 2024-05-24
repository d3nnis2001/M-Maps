<script>
import { ref, onMounted } from 'vue'
import {useQuasar} from "quasar";
import {sendInspectionOrder} from "@/main/vue/api/inspection";
import router from "@/main/vue/router";

export default {
    setup () {
        const $q = useQuasar()
        const courseId = ref('')
        const startLocation = ref('')
        const endLocation = ref('')
        const startTime = ref('')
        const endTime = ref('')
        const department = ref('')
        const inspectionData = ref('')
        const remarks = ref('')
        const dense = ref(false)
        const showDialog = ref(false);

        onMounted(async () => {

        })

        function checkInputs() {
            let errormsg = [];

            if (!courseId.value.trim()) {
                errormsg.push("Please fill in the course number");
            }
            if (!startLocation.value.trim()) {
                errormsg.push("Please fill in the starting location");
            }
            if (!endLocation.value.trim()) {
                errormsg.push("Please fill in the ending location");
            }
            if (!startTime.value.trim()) {
                errormsg.push("Please choose choose a starttime");
            }
            if (!endTime.value) {
                errormsg.push("Please choose an endtime");
            }
            if (!department.value) {
                errormsg.push("Please choose a department");
            }
            if (!inspectionData.value) {
                console.log(errormsg);
                errormsg.push("Please select a data type");
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
            const inputs = checkInputs()
            console.log(inputs)
            if (inputs) {
                router.push('/inspectionOrderOverview')
                return sendInspectionOrder(courseId.value, startLocation.value, endLocation.value, startTime.value,
                    endTime.value, department.value, inspectionData.value, remarks.value)

            }
        }

        return {
            courseId,
            startLocation,
            endLocation,
            startTime,
            endTime,
            department,
            inspectionData,
            remarks,
            sendData,
            dense
        }
    }
}
</script>

<template>
    <div class="q-pa-md">
        <div class="q-gutter-y-md column" style="max-width: 300px">

            <q-input outlined v-model="courseId" label="StreckenID" :dense="dense" />

            <q-input outlined v-model="startLocation" label="Startort" :dense="dense" />

            <q-input outlined v-model="endLocation" label="Endort" :dense="dense" />

            <q-input outlined v-model="startTime" label="Von" :dense="dense">
                <template v-slot:prepend>
                    <q-icon name="event" />
                </template>
            </q-input>

            <q-input outlined v-model="endTime" label="Bis" :dense="dense">
                <template v-slot:prepend>
                    <q-icon name="event" />
                </template>
            </q-input>

            <q-input outlined v-model="department" label="Fachabteilung" :dense="dense" />

            <q-input outlined v-model="inspectionData" label="Zu überprüfende Messdaten" :dense="dense" />

            <q-input
                v-model="remarks" label="Bemerkungen" :dense="dense"
                filled
                autogrow
            />

            <q-btn label="Status" @click="showDialog = true" />

            <q-dialog v-model="showDialog">
                <q-card>
                    <q-card-section>
                        //Optionen implementieren für die Status Änderung
                    </q-card-section>
                    <q-card-section>
                        <q-btn flat label="Schließen" color="primary" @click="showDialog = false"></q-btn>
                    </q-card-section>
                </q-card>
            </q-dialog>


            <q-btn label="Speichern" @click="$router.push('/inspectionOrder')" color="primary" class=""></q-btn>
        </div>
    </div>

</template>


<style scoped>

</style>

<!--
<script>

import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { getDetailsByID } from "@/main/vue/api/reparatur";

export default {
    setup() {
        const route = useRoute();
        const repairDetails = ref({});
        const ticked = ref([])
        const options = ref([])

        onMounted(async () => {
            const name = route.params.name;
            repairDetails.value = await getDetailsByID(name);
            for (let i = 0;i<repairDetails.value.checklist.items.length;i++) {
                options.value.push({label: repairDetails.value.checklist.items[i], value: i})
            }
        });

        function doNothing() {
            console.log(ticked.value)
        }
        return {
            repairDetails,
            ticked,
            options,
            doNothing
        };
    }
}

</script>

<template>
    <div class="outline">
        <div class="outer-layer">
            <div>
                <p>ID: {{ repairDetails.id }}</p>
                <p>Zeitraum: {{ repairDetails.from }}</p>
                <p>Freigabeberechtigter: {{ repairDetails.freigabeberechtigter }}</p>
                <p>Status: {{ repairDetails.status }}</p>
            </div>
            <h5>Checkliste</h5>
            <div class="outline-nomar">
                <div class="q-pa-md">
                    <q-option-group
                        :options="options"
                        type="checkbox"
                        v-model="ticked"
                    />
                </div>
            </div>
            <q-btn label="Speichern" color="grey" @click="doNothing" class=""></q-btn>
        </div>
    </div>
</template>

<style scoped>

.outer-layer {
    margin: 20px;
}
.outline {
    border: 1px solid black;
    padding: 10px;
    margin: 20px;
}
.outline-nomar {
    border: 1px solid black;
    padding: 10px;
    margin-bottom: 20px;
}
</style>
-->
