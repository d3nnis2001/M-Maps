<script setup>

import {onMounted, onUnmounted, ref} from "vue";
import {deletePruef, getArchivedPruef, undoPruef} from "@/main/vue/api/archiv";

const smallScreen = ref(false)
const largeScreen = ref(false)
const showDialog = ref(false)
var selectedRow = null
const rows = ref([])

onMounted(async () => {
    rows.value = await getArchivedPruef();
    checkScreenSize();
    window.addEventListener('resize', checkScreenSize);
})

onUnmounted(() => {
    window.removeEventListener('resize', checkScreenSize);
})

const checkScreenSize = () => {
    const screenSize = window.innerWidth;
    smallScreen.value = screenSize <= 500;
    largeScreen.value = screenSize > 500;
}

const onRowClick = (row) => {
    selectedRow = row
    showDialog.value = true
}

const showColumn = (columnName) => {
    return this.visibleColumns.includes(columnName);
}

const undo = async () => {
    await undoPruef(selectedRow.inspectionOrderId)
    rows.value = await getArchivedPruef();
}

const deleteInsp = async () => {
    await deletePruef(selectedRow.inspectionOrderId)
    rows.value = await getArchivedPruef()
}

const visibleColumns = ["id", "start", "destination"]

const columns =  [
    { name: 'inspectionOrderId', required: true, label: 'ID', align: 'left', field: row => row.inspectionOrderId, format: val => `${val}`, sortable: true },
    { name: 'courseId', label: 'StreckenId', align: 'left', field: 'courseId', sortable: true },
    { name: 'startLocation', label: 'Startort', align: 'left', field: 'startLocation', sortable: true },
    { name: 'endLocation', label: 'Zielort', align: 'left', field: 'endLocation', sortable: true },
    { name: 'startTime', label: 'von', align: 'left', field: 'startTime' },
    { name: 'endTime', label: 'bis', align: 'left', field: 'endTime' },
    { name: 'inspectionData', label: 'Messdaten', align: 'left', field: 'inspectionData' },
    { name: 'department', label: 'Fachabteilung', align: 'left', field: 'department' },
    { name: 'status', label: 'Status', align: 'left', field: 'status' },
]

</script>

<template>
    <div class="q-pa-md">
        <q-dialog
            v-model="showDialog"
        >
            <q-card flat square bordered>
                <div class="row">
                    Prüfauftrag: {{selectedRow.inspectionOrderId}}
                </div>
                <div class="row">
                    <div class="col-6">
                        <q-btn
                            style="align-items: end; justify-content: end"
                            flat
                            no-caps
                            @click="undo"
                            label=""
                            color="primary"
                            v-close-popup
                        >
                            <q-icon name="undo" style="margin-left: 8px;" />
                        </q-btn>
                    </div>
                    <div class="col-6">
                        <q-btn
                            style="align-items: end; justify-content: end"
                            flat
                            no-caps
                            @click="deleteInsp"
                            label=""
                            color="primary"
                            v-close-popup
                        >
                            <q-icon name="delete" style="margin-left: 8px;" />
                        </q-btn>
                    </div>
                </div>
            </q-card>
        </q-dialog>

        <q-table
            v-show="!smallScreen && largeScreen"
            class="my-sticky-header-table"
            flat bordered
            title="Prüfaufträge"
            :rows="rows"
            :columns="columns"
            row-key="id"
        >
            <template v-slot:body="props">
                <q-tr :props="props" @click="onRowClick(props.row)">
                    <q-td key="inspectionOrderId" :props="props">
                        {{ props.row.inspectionOrderId }}
                    </q-td>
                    <q-td key="courseId" :props="props">
                        {{ props.row.courseId }}
                    </q-td>
                    <q-td key="startLocation" :props="props">
                        {{ props.row.startLocation }}
                    </q-td>
                    <q-td key="endLocation" :props="props">
                        {{ props.row.endLocation }}
                    </q-td>
                    <q-td key="startTime" :props="props">
                        {{ props.row.startTime }}
                    </q-td>
                    <q-td key="endTime" :props="props">
                        {{ props.row.endTime }}
                    </q-td>
                    <q-td key="inspectionData" :props="props">
                        {{ props.row.inspectionData }}
                    </q-td>
                    <q-td key="department" :props="props">
                        {{ props.row.department }}
                    </q-td>
                    <q-td key="status" :props="props">
                        {{ props.row.status }}
                    </q-td>

                </q-tr>
            </template>
        </q-table>
        <q-table
            v-show="!largeScreen && smallScreen"
            flat bordered
            grid
            grid-header
            title="Prüfaufträge"
            :rows="rows"
            :columns="columns"
            row-key="id"
            hide-header
            class="my-sticky-header-table2"
        >
            <template v-slot:header="props">
                <q-tr :props="props">
                    <q-th
                        :key="props.cols[0].name"
                        :props="props"
                    >
                        {{props.cols[0].label}}
                    </q-th>
                    <q-th
                        :key="props.cols[1].name"
                        :props="props"
                    >
                        {{props.cols[1].label}}
                    </q-th>
                    <q-th
                        :key="props.cols[2].name"
                        :props="props"
                    >
                        {{props.cols[2].label}}
                    </q-th>
                </q-tr>
            </template>
            <template v-slot:item="props">
                <div
                    class="q-pa-xs col-xs-12 col-sm-6 col-md-4 col-lg-3"
                >
                    <q-card bordered flat @click="onRowClick(props.row)">
                        <q-list dense>
                            <q-item v-for="col in props.cols" :key="col.name">
                                <q-item-section>
                                    <q-item-label caption>{{ col.label }}</q-item-label>
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ col.value }}</q-item-label>
                                </q-item-section>
                            </q-item>
                        </q-list>
                    </q-card>
                </div>
            </template>
        </q-table>
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
        z-index: 0
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
.my-sticky-header-table2
    .q-table__top,
    .q-table__bottom,
    thead tr:first-child th
        /* bg color is important for th; just specify one */
        background-color: $blue-grey-5

.centered-section
    display: flex
    justify-content: center
    align-items: center

.text-section
    display: flex
    align-items: center
    padding: 5px
    font-size: 16px
    color: #333
</style>
