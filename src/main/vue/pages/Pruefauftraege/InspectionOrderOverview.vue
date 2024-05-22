
<script>
import {onMounted, reactive, ref} from "vue";
import router from "@/main/vue/router";
import {getInspectionOrder} from "@/main/vue/api/inspection";


export default {
    setup () {
        const state = reactive ({
            filter: '',
            columns: [   // field: row => row.name
                { name: 'inspectionOrderId', required: true, label: 'ID', align: 'left', field: 'inspectionOrderId', format: val => `${val}`, sortable: true },
                { name: 'startLocation', label: 'Startort', align: 'left', field: 'startLocation', sortable: true },
                { name: 'endLocation', label: 'Zielort', align: 'left', field: 'endLocation', sortable: true },
                { name: 'startTime', label: 'von', align: 'left', field: 'startTime' },
                { name: 'endTime', label: 'bis', align: 'left', field: 'endTime' },
                { name: 'data', label: 'Messdaten', align: 'left', field: 'data' },
                { name: 'department', label: 'Fachabteilung', align: 'left', field: 'department' },
                { name: 'status', label: 'Status', align: 'left', field: 'status' },
            ],
            rows: []

        });
        onMounted(async () => {
            const response = await getInspectionOrder()
            console.log("API Response:", response);
            for (let i = 0; i < response.length; i++) {
                state.rows.push({
                    inspectionOrderId: response[i]["inspectionOrderId"],
                    startLocation: response[i]["startLocation"],
                    endLocation: response[i]["endLocation"],
                    startTime: response[i]["startTime"],
                    endTime: response[i]["endTime"],
                    data: response[i]["data"],
                    department: response[i]["department"],
                    status: response[i]["status"]
                })
            }
            console.log("State Rows:", state.rows);
        })

        const showDialog = ref(false);
        const currentRow = ref({});

        const rowClick = async (evt, rowData) => {
            currentRow.value = rowData;
            showDialog.value = true;
        };

        function createInspectionOrder() {
            router.push("/createInspectionOrder");
        }

        function editInspectionOrder() {
            const name = currentRow.value.name
            router.push(`/${name}/edit`)
        }

        function acceptInspectionOrder() {

        }
        return {
            filter: ref(''),
            state,
            createInspectionOrder,
            editInspectionOrder,
            acceptInspectionOrder,
            rowClick,
            currentRow,
            showDialog
        }

    }
}

</script>

<template>
    <div class="q-pa-md">
        <q-table
            class="my-sticky-header-table"
            flat bordered
            title="Prüfaufträge"
            :rows="state.rows"
            :columns="state.columns"
            row-key="inspectionOrderId"
            :filter = "filter"
            @row-click="rowClick" />
    </div>

    <div class="q-pa-md">
        <q-btn label="Neuen Auftrag erstellen" @click="createInspectionOrder" color="primary"  class=""></q-btn>

        <q-dialog v-model="showDialog">
            <q-card>
                <q-card-section>
                    <div class="option-button" @click="editOrder">Bearbeiten</div>
                    <q-separator />
                    <div class="option-button" @click="deleteOrder">Löschen</div>
                    <q-separator />
                    <div class="option-button" @click="archiveOrder">Archivieren</div>
                    <q-separator />
                    <div class="option-button" @click="acceptInspectionOrder">Auftrag annehmen</div>
                </q-card-section>
                <q-card-section>
                    <q-btn flat label="Schließen" color="primary" @click="showDialog = false"></q-btn>
                </q-card-section>
            </q-card>
        </q-dialog>

    </div>

</template>

<style lang="sass">
.my-sticky-header-table
    /* height or max-height is important */
    height: 500px

    .q-table__top,
    .q-table__bottom,
    thead tr:first-child th
        /* bg color is important for th; just specify one */
        background-color: $blue-grey-5

    thead tr th
        position: sticky
        z-index: 1
    thead tr:first-child th
        top: 0

    /* this is when the loading indicator appears */
    &.q-table--loading thead tr:last-child th
        /* height of all previous header rows */
        top: 48px

    /* prevent scrolling behind sticky top row on focus */
    tbody
        /* height of all previous header rows */
        scroll-margin-top: 48px
</style>
