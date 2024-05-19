
<script>
import {onMounted, reactive, ref} from "vue";
import router from "@/main/vue/router";
//import {inspection} from "@/main/vue/api/inspection";


export default {
    setup () {
        const state = reactive ({
            filter: '',
            columns: [
                { name: 'courseId', required: true, label: 'ID', align: 'left', field: row => row.name, format: val => `${val}`, sortable: true },
                { name: 'startLocation', label: 'Startort', align: 'left', field: 'start', sortable: true },
                { name: 'endLocation', label: 'Zielort', align: 'left', field: 'ziel', sortable: true },
                { name: 'startTime', label: 'von', align: 'left', field: 'von' },
                { name: 'endTime', label: 'bis', align: 'left', field: 'von' },
                { name: 'data', label: 'Messdaten', align: 'left', field: 'data' },
                { name: 'department', label: 'Fachabteilung', align: 'left', field: 'abteilung' },
                { name: 'status', label: 'Status', align: 'left', field: 'status' },
            ],
            rows: []

        });
        onMounted(async () => {
            const response = await inspection()
            for (let i = 0; i < response.length; i++) {
                state.rows.push( {
                    name: response[i]["courseId"],
                    start: response[i]["startLocation"],
                    destination: response[i]["endLocation"],
                    timeStart: response[i]["startTime"],
                    timeDestination: response[i]["endTime"],
                    data: response[i]["data"],
                    department: response[i]["department"],
                    status: response[i]["status"]
                })
            }
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
        <q-table>
            class="my-sticky-header-table"
            flat bordered
            title="Prüfaufträge"
            :rows="state.rows"
            :columns="state.columns"
            row-key="id"
            :filter = "filter"
            hide-header
            @row-click="rowClick">
            <template v-slot:top-right>
                <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
                    <template v-slot:append>
                        <q-icon name="search" />
                    </template>
                </q-input>
            </template>
        </q-table>
    </div>
    <div class="q-pa-md">
        <q-btn label="Neuen Auftrag erstellen" @click="createInspectionOrder" color="primary"  class=""></q-btn>
        <q-btn label="Auftrag annehmen" @click="acceptInspectionOrder" color="primary" class=""></q-btn>

        <q-dialog v-model="showDialog">
            <q-card>
                <q-card-section>
                    <div class="option-button" @click="editOrder">Bearbeiten</div>
                    <q-separator />
                    <div class="option-button" @click="deleteOrder">Löschen</div>
                    <q-separator />
                    <div class="option-button" @click="archiveOrder">Archivieren</div>
                    <q-separator />
                    <div class="option-button" @click="cancelOrder">Stornieren</div>
                    <q-separator />
                    <div class="option-button" @click="reapplyOrder">Neu beantragen</div>
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
