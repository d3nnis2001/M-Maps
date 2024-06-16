<script>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import {
    getDetailsByID,
    getTickedItems,
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
                <div class="row">
                    <p style="margin-right: 5px">ID: </p>
                    <p style="font-weight: bold">{{ repairDetails.id }}</p>
                </div>
                <div class="row">
                    <p style="margin-right: 5px">Zeitraum: </p>
                    <p style="font-weight: bold">{{ repairDetails.from }}</p>
                </div>
                <div class="row">
                    <p style="margin-right: 5px">Freigabeberechtigter: </p>
                    <p style="font-weight: bold">{{ repairDetails.freigabeberechtigter}}</p>
                </div>
                <div class="row">
                    <p style="margin-right: 5px">Status: </p>
                    <p style="font-weight: bold">{{ repairDetails.status }}</p>
                </div>
            </div>
            <q-separator size="2px" color="primary" style="margin-top: 30px"></q-separator>
            <p class="checklist">Checkliste</p>
            <div class="outline-nomar">
                <div class="q-pa-md">
                    <q-option-group
                        :options="options"
                        type="checkbox"
                        v-model="ticked"
                    />
                </div>
            </div>
            <q-btn v-if="allChecked() === false" style="width: 100%; max-width: 218px" size="16px" no-caps rounded label="Speichern" color="primary" @click=saveChanges></q-btn>
            <div v-if="allChecked()">
                <div class="column">
                    <q-input filled style="width: 218px" v-model="date" mask="date" :rules="['date']">
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
                    <q-btn style="width: 100%; max-width: 218px" size="16px" no-caps rounded label="Terminieren" color="primary" @click=saveChanges></q-btn>
                </div>
            </div>
        </div>
    </div>
</template>

<style lang="scss">
.outer-layer {
    margin: 20px;
}
.outline {
    border: 2px solid $primary;
    padding: 20px;
    border-radius: 15px;
}

.checklist {
    font-weight: bold;
    font-size: 30px;
    margin-top: 30px;
}

.q-option-group {
    max-height: 175px;
    overflow-y: auto;
}


p {
    font-weight: normal;
}

.outline-nomar {
    border: 2px solid $primary;
    padding: 10px;
    margin-bottom: 20px;
    border-radius: 15px;
}
</style>
