<script>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import {getDetailsByID, getTickedItems} from "@/main/vue/api/reparatur";
import router from "@/main/vue/router";
import {useQuasar} from "quasar";

export default {
    setup() {
        const route = useRoute();
        const repairDetails = ref({});
        const ticked = ref([]);
        const date = ref(getDate());
        const options = ref([]);
        const terminationDate = ref('');
        const $q = useQuasar();

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

        function getDate() {
            const today = new Date();
            const year = today.getFullYear();
            const month = String(today.getMonth() + 1).padStart(2, '0');
            const day = String(today.getDate()).padStart(2, '0');
            return `${year}/${month}/${day}`;
        }

        function onRejected (rejectedEntries) {
            $q.notify({
                type: 'negative',
                message: `${rejectedEntries.length} file(s) did not pass validation constraints`
            })
        }

        return {
            repairDetails,
            ticked,
            options,
            terminationDate,
            onRejected,
            date
        };
    }
}
</script>

<template>
    <q-layout view="hHh lpR fFf">
        <q-header class="bg-white text-dark q-px-md q-py-sm">
            <q-toolbar class="justify-center">
                <q-img src="/src/main/resources/db-logo.png" style="width: 50px; height: auto; margin-right: 10px;"/>
                <q-toolbar-title class="text-center">Gleisbauer</q-toolbar-title>
            </q-toolbar>
        </q-header>

        <q-page-container>
            <div class="outline">
                <div class="outer-layer">
                    <div>
                        <div class="row">
                            <p style="margin-right: 5px">ID: </p>
                            <p style="font-weight: bold">{{ repairDetails.id }}</p>
                        </div>
                        <div class="row">
                            <p style="margin-right: 5px">Zeitraum: </p>
                            <p style="font-weight: bold">
                                {{ "   von   " + repairDetails.from + "   bis   " + repairDetails.till }}</p>
                        </div>
                        <div class="row">
                            <p style="margin-right: 5px">Freigabeberechtigter: </p>
                            <p style="font-weight: bold">{{ repairDetails.freigabeberechtigter }}</p>
                        </div>
                        <div class="row">
                            <p style="margin-right: 5px">Status: </p>
                            <p style="font-weight: bold">{{repairDetails.status + "   zum   " }}</p>
                        </div>
                    </div>
                    <q-separator size="2px" color="primary" style="margin-top: 30px"></q-separator>
                    <p class="checklist">Checkliste</p>
                    <div class="row extra-mar">
                    <div class="outline-nomar">
                        <div class="q-pa-md">
                            <div v-for="item in options" :key="item.value" class="checklist-item">
                                {{ item.label }}
                            </div>
                        </div>
                    </div>
                    <div class="q-pa-md">
                        <div class="q-gutter-md row items-start">
                            <q-uploader
                                field-name="file"
                                style="max-width: 300px"
                                url="/api/repair/upload"
                                label="Restricted to images"
                                multiple
                                accept=".jpg, image/*"
                                @rejected="onRejected"
                            />
                        </div>
                    </div>
                    </div>
                </div>
            </div>
        </q-page-container>
    </q-layout>
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

.checklist-item {
    margin-bottom: 5px;
}

p {
    font-weight: normal;
}

.outline-nomar {
    border: 2px solid $primary;
    padding: 10px;
    margin-bottom: 20px;
    border-radius: 15px;
    flex: 1;
}

.q-header {
    background-color: white;
    padding: 8px 16px;
}

.q-toolbar-title {
    flex: 1;
    text-align: center;
}

.q-layout__page {
    padding: 0;
    margin: 0;
}

.row.extra-mar {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
}

.upload-container {
    display: flex;
    justify-content: flex-end;
    align-items: center;
}
</style>
