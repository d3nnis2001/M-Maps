<script>
import {onMounted, reactive, ref} from 'vue'
import {repair} from "@/main/vue/api/reparatur";
import router from "@/main/vue/router";

export default {
    setup () {
        const state = reactive({
            filter: '',
            columns: [
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
            ],
            rows: []
        });

        onMounted(async () => {
            const response = await repair()
            for (let i = 0; i<response.length; i++) {
                state.rows.push({
                    name: response[i]["id"],
                    von: response[i]["from"],
                    bis: response[i]["till"],
                    freigabe: response[i]["freigabeberechtigter"],
                    strecke: response[i]["track"],
                    status: response[i]["status"]})
            }
        })
        function goCreate() {
            router.push("repair/create")
        }
        return {
            filter: ref(''),
            state,
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
            :rows="state.rows"
            :columns="state.columns"
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
