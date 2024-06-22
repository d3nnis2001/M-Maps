<script setup>
import {onMounted, onUnmounted, ref} from "vue";
import {getArchivedRep, undoRep} from "@/main/vue/api/archiv";

const smallScreen = ref(false)
const largeScreen = ref(false)
const showDialog = ref(false)
var selectedRow = null
const rows = ref("")

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
    await undoRep(selectedRow.name);
    rows.value = await getArchivedRep()
}

const visibleColumns = ["ID", "Von", "Bis"]

const columns =  [
    {
        name: 'ID',
        required: true,
        label: 'Reparaturauftrag',
        align: 'left',
        field: row => row.name,
        format: val => `${val}`,
        sortable: true
    },
    { name: 'Von', align: 'center', label: 'Von', field: 'von', sortable: true },
    { name: 'Bis', label: 'Bis', field: 'bis', sortable: true },
    { name: 'Freigabeberechtigter', label: 'Freigabeberechtigter', field: 'freigabe' },
    { name: 'Strecke', label: 'Strecken', field: 'strecke' },
    { name: 'Status', label: 'Status', field: 'status' },
]
/*
const rows = [
    { name: 'Reparaturauftrag 1', von: '07-04-2024', bis: '08-04-2024', freigabe: "Klaus Peter", strecke:"6100", status:"done" },
    { name: 'Reparaturauftrag 2', von: '07-04-2024', bis: '08-04-2024', freigabe: "Klaus Peter", strecke:"6100", status:"done" },
    { name: 'Reparaturauftrag 3', von: '07-04-2024', bis: '08-04-2024', freigabe: "Klaus Peter", strecke:"6100", status:"done" },
    { name: 'Reparaturauftrag 4', von: '07-04-2024', bis: '08-04-2024', freigabe: "Klaus Peter", strecke:"6100", status:"done" },
]
*/
</script>

<template>
    <div class="q-pa-md">
        <q-dialog
            v-model="showDialog"
        >
            <q-card style="width: 300px">
                <q-card-section>
                    <div class="centered-section ">
                        <q-icon name="unarchive" size="xl"></q-icon>
                    </div>
                    <div class="text-section">
                        Archivieren von {{selectedRow.name}}  rückgängig machen?
                    </div>
                </q-card-section>

                <q-separator />

                <q-card-actions vertical>
                    <q-btn flat @click="undo">Ja</q-btn>
                </q-card-actions>
            </q-card>
        </q-dialog>

        <q-table
            v-show="!smallScreen && largeScreen"
            class="my-sticky-header-table"
            flat bordered
            title="Reparaturaufträge"
            :rows="rows.value"
            :columns="columns"
            row-key="ID"
        >
            <template v-slot:body="props">
                <q-tr :props="props" @click="onRowClick(props.row)">
                    <q-td key="ID" :props="props">
                        {{ props.row.name }}
                    </q-td>
                    <q-td key="Von" :props="props">
                        {{ props.row.von }}
                    </q-td>
                    <q-td key="Bis" :props="props">
                        {{ props.row.ziel }}
                    </q-td>
                    <q-td key="Freigabeberechtigter" :props="props">
                        {{ props.row.freigabe }}
                    </q-td>
                    <q-td key="Strecke" :props="props">
                        {{ props.row.strecke }}
                    </q-td>
                    <q-td key="Status" :props="props">
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
            :rows="rows.value"
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
