<script>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import {
    getDetailsByID,
    getTickedItems,
    sendRepair,
    setTerminated,
    updateRepChecklist,
    updateStatus
} from "@/main/vue/api/reparatur";
import router from "@/main/vue/router";

export default {
    setup() {
        const route = useRoute();
        const repairDetails = ref({});
        const ticked = ref([]);
        const date = ref(getDate())
        const options = ref([]);
        const terminationDate = ref('');

        onMounted(async () => {
            const name = route.params.name;
            repairDetails.value = await getDetailsByID(name);

            if (repairDetails.value && repairDetails.value.checklist) {
                for (let i = 0; i < repairDetails.value.checklist.checkSel.length; i++) {
                    ticked.value.push({label: i, value: repairDetails.value.checklist.checkSel.get(i)});
                }
                for (let i = 0; i < repairDetails.value.checklist.checkPoints.items.length; i++) {
                    options.value.push({label: repairDetails.value.checklist.checkPoints.items[i], value: i});
                }
            }
            const tickedData = await getTickedItems(repairDetails.value.id);
            ticked.value = tickedData.data.map(item => parseInt(item, 10));
        });

        function allChecked() {
            if (repairDetails.value && repairDetails.value.checklist && repairDetails.value.checklist.checkPoints) {
                return ticked.value.length === repairDetails.value.checklist.checkPoints.items.length;
            }
            return false;
        }
        function getDate() {
            const today = new Date();
            const year = today.getFullYear();
            const month = String(today.getMonth() + 1).padStart(2, '0');
            const day = String(today.getDate()).padStart(2, '0');
            return `${year}/${month}/${day}`;
        }
        async function saveChanges() {
            if (allChecked()) {
                await updateStatus(repairDetails.value.id, "terminiert");
                await setTerminated(repairDetails.value.id, date.value);
            }
            await updateRepChecklist(repairDetails.value.id, ticked.value);
            await router.push({ path: "/repair" });
        }

        return {
            repairDetails,
            ticked,
            options,
            saveChanges,
            terminationDate,
            allChecked,
            date
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
            <div v-if="allChecked()">
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
            <q-btn label="Speichern" color="grey" @click="saveChanges" class=""></q-btn>
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
