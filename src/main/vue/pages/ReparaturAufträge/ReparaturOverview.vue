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
        const showDialog = ref(false);
        const currentRow = ref({});

        const rowClick = async (evt, rowData) => {
            currentRow.value = rowData;
            showDialog.value = true;
        };
        function goCreate() {
            router.push("/repair/create")
        }

        function editOrder() {
            const name = currentRow.value.name
            router.push(`/repair/${name}/edit`)
        }
        return {
            filter: ref(''),
            state,
            goCreate,
            rowClick,
            currentRow,
            showDialog,
            editOrder
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
        <q-btn label="Neu erstellen" @click="goCreate" color="primary" class=""></q-btn>
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

<style scoped>

.option-button {
    cursor: pointer;
    padding: 8px;
    font-size: 16px;
}

.option-button:hover {
    text-decoration: underline;
}

</style>
