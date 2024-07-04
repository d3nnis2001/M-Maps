<script>
import { ref, onMounted } from 'vue'
import {useQuasar} from "quasar";
import { useRoute } from 'vue-router';
import {getDataById, sendDataById, sendInspectionOrder} from "@/main/vue/api/inspection";
import router from "@/main/vue/router";
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";

export default {
    components: {StandardInput},
    setup () {
        const $q = useQuasar()
        const courseId = ref('')
        const startLocation = ref('')
        const endLocation = ref('')
        const startTime = ref('')
        const endTime = ref('')
        const department = ref('')
        const departmentValues = ref([])
        const inspectionData = ref('')
        const inspectionDataValues = ref([])
        const remarks = ref('')
        const priority = ref('')
        const priorityLow = ref(false)
        const priorityMiddle = ref(false)
        const priorityHigh = ref(false)
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
            priority.value = inspectionOrder.priority;

            if (priority.value === 'niedrig') {
                priorityLow.value = true;
            } else if (priority.value === 'mittel') {
                priorityMiddle.value = true;
            } else if (priority.value === 'hoch') {
                priorityHigh.value = true;
            }
            inspectionDataValues.value.push("Gleislagedaten")

            departmentValues.value.push("DB Regio Schiene Nord-Ost (NO)")
            departmentValues.value.push("DB Regio Schiene Süd-West (SW)")

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
            if (endTime.value < startTime.value) {
                errormsg.push("The second date has to be later than the first!")
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
            if (priorityLow.value) {
                priority.value = 'niedrig';
            } else if (priorityMiddle.value) {
                priority.value = 'mittel';
            } else if (priorityHigh.value) {
                priority.value = 'hoch';
            }
            if (inputs) {
                router.push('/inspectionOrder')
                return sendDataById(currentInspectionOrderId, courseId.value, startLocation.value, endLocation.value, startTime.value,
                    endTime.value, department.value, inspectionData.value, remarks.value, priority.value)

            }
        }

        const updateCheckbox = (option) => {
            if (option === 'niedrig') {
                priorityMiddle.value = false;
                priorityHigh.value = false;
            } else if (option === 'mittel') {
                priorityLow.value = false;
                priorityHigh.value = false;
            } else if (option === 'hoch') {
                priorityLow.value = false;
                priorityMiddle.value = false;
            }
        };

        return {
            currentInspectionOrderId,
            courseId,
            startLocation,
            endLocation,
            startTime,
            endTime,
            department,
            departmentValues,
            inspectionData,
            inspectionDataValues,
            remarks,
            sendData,
            updateCheckbox,
            priorityLow,
            priorityMiddle,
            priorityHigh,
            dense
        }
    }
}
</script>

<template>
    <div class="outline">
        <div class="outer-layer">
            <div>
                <div class="row">
                    <p style="margin-right: 5px">PrüfauftragsId: </p>
                    <p style="font-weight: bold">{{ currentInspectionOrderId }}</p>
                </div>
            </div>
            <q-separator size="2px" color="primary" style="margin-top: 20px "></q-separator>

            <p style="font-weight: bold; margin-top: 20px">StreckenId</p>
            <StandardInput class="extra-mar" v-model="courseId" label="StreckenId" ></StandardInput>
            <div class="row">
                <div class="text-with-input mar-right">
                    <p style="font-weight: bold;">Startort</p>
                    <StandardInput class="extra-mar" v-model="startLocation" label="Startort" ></StandardInput>
                </div>
                <div class="text-with-input">
                    <p style="font-weight: bold;">Zielort</p>
                    <StandardInput class="extra-mar" v-model="endLocation" label="Zielort" ></StandardInput>
                </div>
            </div>
            <p style="font-weight: bold;">Zeitraum (von - bis)</p>
            <div class="text-with-input row extra-mar">
                <q-input class="input-style mar-right" filled v-model="startTime" mask="date" :rules="['date']">
                    <template v-slot:append>
                        <q-icon name="event" class="cursor-pointer">
                            <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                <q-date v-model="startTime">
                                    <div class="row items-center justify-end">
                                        <q-btn v-close-popup label="Close" color="primary" flat />
                                    </div>
                                </q-date>
                            </q-popup-proxy>
                        </q-icon>
                    </template>
                </q-input>
                <q-input  class="input-style" filled v-model="endTime" mask="date" :rules="['date']">
                    <template v-slot:append>
                        <q-icon name="event" class="cursor-pointer">
                            <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                <q-date v-model="endTime">
                                    <div class="row items-center justify-end">
                                        <q-btn v-close-popup label="Close" color="primary" flat />
                                    </div>
                                </q-date>
                            </q-popup-proxy>
                        </q-icon>
                    </template>
                </q-input>
            </div>

        <div class="row extra-mar">
            <div class="checkListInput">
                <p style="font-weight: bold;">Fachabteilung</p>
                <q-select class="" outlined v-model="department" :options="departmentValues" label="Fachabteilung"></q-select>
            </div>
            <div class="checkListInput">
                <p style="font-weight: bold;">Messdaten</p>
                <q-select class="" outlined v-model="inspectionData" :options="inspectionDataValues" label="Messdaten"></q-select>
            </div>
            <div class="">
                <p style="font-weight: bold;">Priorität</p>
                <q-checkbox v-model="priorityLow" val="niedrig" label="niedrig" @update:model-value="updateCheckbox('niedrig')"/>
                <q-checkbox v-model="priorityMiddle" val="mittel" label="mittel" @update:model-value="updateCheckbox('mittel')"/>
                <q-checkbox v-model="priorityHigh" val="hoch" label="hoch" @update:model-value="updateCheckbox('hoch')"/>

            </div>
        </div>
        <div>
            <p style="font-weight: bold;">Bemerkungen</p>
            <q-input class="input-bem text-with-input" outlined color="primary" rounded v-model="remarks" label="Bemerkungen" />
        </div>
            <q-btn style="width: 100%; max-width: 218px; margin-top: 20px" size="16px" no-caps rounded label="Speichern" color="primary" @click=sendData></q-btn>

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

.extra-mar {
    margin-bottom: 20px;
}

.checkListInput {
    width: 100%;
    max-width: 288px;
    margin-bottom: 20px;
    margin-right: 20px;
}
</style>
