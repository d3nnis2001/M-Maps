
<script>

export default {
    setup () {
        return {
            columns,
            rows,
        }
    }
}

const columns = [
    { name: 'id', required: true, label: 'ID', align: 'left', field: row => row.name, format: val => `${val}`, sortable: true },
    { name: 'start', label: 'Startort', align: 'left', field: 'start', sortable: true },
    { name: 'destination', label: 'Zielort', align: 'left', field: 'ziel', sortable: true },
    { name: 'timeStart', label: 'bis', align: 'left', field: 'von' },
    { name: 'timeDestination', label: 'bis', align: 'left', field: 'von' },
    { name: 'data', label: 'Messdaten', align: 'left', field: 'data' },
    { name: 'department', label: 'Fachabteilung', align: 'left', field: 'abteilung' },
    { name: 'status', label: 'Status', align: 'left', field: 'status' },
]

const rows = [
    { name: 'Prüfauftrag 1', start: 'Bielefeld', ziel: 'Hannover', von: '07-04-2024', bis: '08-04-2024', abteilung: ' ', status: "storniert" },
    { name: 'Prüfauftrag 2', start: 'Bielefeld', ziel: 'Berlin', von: '10-05-2023', bis: '12-05-2023', abteilung: ' ', status: "storniert"  },
    { name: 'Prüfauftrag 3', start: 'Bielefeld', ziel: 'Berlin', von: '10-05-2023', bis: '12-05-2023', abteilung: ' ', status: "storniert"  },
]



</script>

<template>
    <div class="q-pa-md">
        <q-table
            class="my-sticky-header-table"
            flat bordered
            title="Prüfaufträge"
            :rows="rows"
            :columns="columns"
            row-key="id"
        />
    </div>
    <div class="q-pa-md">
        <q-btn label="Neuen Auftrag erstellen" @click="$router.push('/createInspectionOrder')" color="primary"  class=""></q-btn>
        <q-btn label="Auftrag annehmen" @click="" color="primary" class=""></q-btn>
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
