<script>
import { ref, onMounted } from 'vue'
import {useQuasar} from "quasar";
import { useRoute } from 'vue-router';
import {getDataById, sendDataById, sendInspectionOrder} from "@/main/vue/api/inspection";
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
        const route = useRoute();
        let currentInspectionOrderId = route.params.inspectionOrderId

        onMounted(async () => {
            const inspectionOrder = await getDataById(currentInspectionOrderId);
            courseId.value = inspectionOrder.courseId;
            startLocation.value = inspectionOrder.startLocation;
            endLocation.value = inspectionOrder.endLocation;
            startTime.value = inspectionOrder.startTime;
            endTime.value = inspectionOrder.endTime;
            department.value = inspectionOrder.department;
            inspectionData.value = inspectionOrder.inspectionData;
            remarks.value = inspectionOrder.remarks;

        });

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
                router.push('/inspectionOrder')
                return sendDataById(currentInspectionOrderId, courseId.value, startLocation.value, endLocation.value, startTime.value,
                    endTime.value, department.value, inspectionData.value, remarks.value)

            }
        }

        return {
            currentInspectionOrderId,
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

            <q-btn label="Speichern" @click="sendData" color="primary" class=""></q-btn>
        </div>
    </div>

</template>


<style scoped>

</style>

