<script setup>
import {ref} from "vue";

const smallScreen = ref(false)
const largeScreen = ref(false)

</script>

<template>
    <div>
        <q-table
            v-show="!smallScreen && largeScreen"
            class="my-sticky-header-table"
            flat bordered
            title="Prüfaufträge"
            :rows="rows.value"
            :columns="columns"
            row-key="id"
        >
            <template v-slot:body="props">
                <q-tr :props="props" @click="onRowClick(props.row)">
                    <q-td key="id" :props="props">
                        {{ props.row.name }}
                    </q-td>
                    <q-td key="start" :props="props">
                        {{ props.row.start }}
                    </q-td>
                    <q-td key="destination" :props="props">
                        {{ props.row.ziel }}
                    </q-td>
                    <q-td key="timeStart" :props="props">
                        {{ props.row.von }}
                    </q-td>
                    <q-td key="timeDestination" :props="props">
                        {{ props.row.bis }}
                    </q-td>
                    <q-td key="data" :props="props">
                        {{ props.row.data }}
                    </q-td>
                    <q-td key="department" :props="props">
                        {{ props.row.abteilung }}
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
            :rows="rows.value"
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

</style>
