<script setup>
import {onMounted, onUnmounted, ref} from "vue";
import {deletePruef, deleteRep, getArchivedRep, undoRep} from "@/main/vue/api/archiv";

const smallScreen = ref(false)
const largeScreen = ref(false)
const showDialog = ref(false)
var selectedRow = null
const rows = ref([])

onMounted(async () => {
    rows.value = await getArchivedRep()
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
    // Check if the column should be shown based on the visibleColumns array
    return this.visibleColumns.includes(columnName);
}

const undo = async () => {
    await undoRep(selectedRow.id);
    rows.value = await getArchivedRep()
}

const del = async () => {
    await deleteRep(selectedRow.id);
    rows.value = await getArchivedRep()
}

const visibleColumns = ["ID", "Von", "Bis"]

const columns =  [
    {
        name: 'id',
        required: true,
        label: 'Reparaturauftrag',
        align: 'left',
        field: 'name',
        format: val => `${val}`,
        sortable: true
    },
    { name: 'from', align: 'center', label: 'Von', field: 'von', sortable: true },
    { name: 'till', label: 'Bis', field: 'bis', sortable: true },
    { name: 'freigabeberechtigter', label: 'Freigabeberechtigter', field: 'freigabe', sortable: true},
    { name: 'track', label: 'Strecke', field: 'strecke', sortable: true},
    { name: 'status', label: 'Status', field: 'status', sortable: true},
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
                            @click="del"
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
            title="Reparaturaufträge"
            :rows="rows"
            :columns="columns"
            row-key="ID"
        >
            <template v-slot:body="props">
                <q-tr :props="props" @click="onRowClick(props.row)">
                    <q-td key="id" :props="props">
                        {{ props.row.id }}
                    </q-td>
                    <q-td key="from" :props="props">
                        {{ props.row.from }}
                    </q-td>
                    <q-td key="till" :props="props">
                        {{ props.row.till }}
                    </q-td>
                    <q-td key="freigabeberechtigter" :props="props">
                        {{ props.row.freigabeberechtigter }}
                    </q-td>
                    <q-td key="track" :props="props">
                        {{ props.row.track }}
                    </q-td>
                    <q-td key="status" :props="props">
                        {{ props.row.status }}
                    </q-td>
                </q-tr>
            </template>
        </q-table>

        <q-table
            v-show="!largeScreen && smallScreen"
            class="my-sticky-header-table2"
            flat bordered
            grid
            grid-header
            title="Reparaturaufträge"
            :rows="rows"
            :columns="columns"
            row-key="ID"
            hide-header
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
