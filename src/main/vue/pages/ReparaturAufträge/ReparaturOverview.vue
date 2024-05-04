<script>
import {onMounted, ref} from 'vue'
import repair from "@/main/vue/api/reparatur";
import router from "@/main/vue/router";

const columns = [
    {
        name: 'desc',
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

const rows = [
    {
        name: 'RepAuftrag 1',
        von: '20.04.2024',
        bis: '24.04.2024',
        freigabe: "Herr Müller",
        strecke: "strecke",
        status: "storniert"
    },
    {
        name: 'RepAuftrag 1',
        von: '20.04.2024',
        bis: '24.04.2024',
        freigabe: "Herr Müller",
        strecke: "strecke",
        status: "storniert"
    }
]

export default {
    setup () {
        onMounted(async () => {
            const response = await repair()
            console.log(response)
        })
        function goCreate() {
            router.push("repair/create")
        }
        return {
            filter: ref(''),
            columns,
            rows,
            goCreate
        }
    }
}
</script>

<template>
    <div class="q-pa-md">
        <q-table
            grid
            flat bordered
            card-class="bg-blue text-white"
            title="Reparaturaufträge"
            :rows="rows"
            :columns="columns"
            row-key="name"
            :filter="filter"
            hide-header>
            <template v-slot:top-right>
                <q-input borderless dense debounce="300" v-model="filter" placeholder="Search">
                    <template v-slot:append>
                        <q-icon name="search" />
                    </template>
                </q-input>
            </template>
        </q-table>
        <q-btn label="Neu erstellen" @click="goCreate" color="primary" class=""></q-btn>
    </div>
</template>

<style scoped>

</style>
