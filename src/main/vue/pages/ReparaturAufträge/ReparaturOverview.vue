<script>
import { onMounted, reactive, ref } from 'vue';
import { deleteRepairOrder, repair, updateStatus } from "@/main/vue/api/reparatur";
import router from "@/main/vue/router";

export default {
    setup() {
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
            const response = await repair();
            for (let i = 0; i < response.length; i++) {
                state.rows.push({
                    name: response[i]["id"],
                    von: response[i]["from"],
                    bis: response[i]["till"],
                    freigabe: response[i]["freigabeberechtigter"],
                    strecke: response[i]["track"],
                    status: response[i]["status"]
                });
            }
        });

        const showDialog = ref(false);
        const showConfirmDialog = ref(false);
        const currentRow = reactive({});
        const rowToDelete = ref(null);

        const rowClick = (evt, rowData) => {
            Object.assign(currentRow, rowData);
            showDialog.value = true;
        };

        function goCreate() {
            router.push(`/repair/${name}/edit`);
        }

        async function editOrder() {
            const name = currentRow.name;
            router.push(`/repair/${name}/edit`);
        }

        async function cancelOrder() {
            const name = currentRow.name;
            await updateStatus(name, "storniert");
            currentRow.status = "storniert";
            updateRowStatus(name, "storniert");
            showDialog.value = false;
        }

        async function archiveOrder() {
            const name = currentRow.name;
            await updateStatus(name, "archiviert");
            currentRow.status = "archiviert";
            updateRowStatus(name, "archiviert");
            showDialog.value = false;
        }

        async function reapplyOrder() {
            const name = currentRow.name;
            await updateStatus(name, "neu beauftragt");
            currentRow.status = "neu beauftragt";
            updateRowStatus(name, "neu beauftragt");
            showDialog.value = false;
        }

        function confirmDeleteOrder(row) {
            rowToDelete.value = row;
            showConfirmDialog.value = true;
        }

        async function deleteOrder() {
            const name = rowToDelete.value.name;
            await deleteRepairOrder(name);
            removeRow(name);
            showConfirmDialog.value = false;
            showDialog.value = false;
        }

        function updateRowStatus(name, status) {
            const row = state.rows.find(row => row.name === name);
            if (row) {
                row.status = status;
            }
        }

        function removeRow(name) {
            const index = state.rows.findIndex(row => row.name === name);
            if (index !== -1) {
                state.rows.splice(index, 1);
            }
        }

        return {
            filter: ref(''),
            state,
            goCreate,
            rowClick,
            currentRow,
            showDialog,
            showConfirmDialog,
            confirmDeleteOrder,
            deleteOrder,
            editOrder,
            cancelOrder,
            archiveOrder,
            reapplyOrder
        };
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
                    <q-separator v-if="currentRow.status !== 'abgeschlossen'" />
                    <div class="option-button" v-if="currentRow.status === 'storniert'" @click="confirmDeleteOrder(currentRow)">Löschen</div>
                    <q-separator v-if="currentRow.status === 'storniert'" />
                    <div class="option-button" v-if="currentRow.status === 'storniert'" @click="archiveOrder">Archivieren</div>
                    <q-separator v-if="currentRow.status !== 'abgeschlossen'" />
                    <div class="option-button" v-if="currentRow.status !== 'storniert'" @click="cancelOrder">Stornieren</div>
                    <q-separator v-if="currentRow.status === 'abgeschlossen'" />
                    <div class="option-button" v-if="currentRow.status === 'storniert'" @click="reapplyOrder">Neu beantragen</div>
                </q-card-section>
                <q-card-section>
                    <q-btn flat label="Schließen" color="primary" @click="showDialog = false"></q-btn>
                </q-card-section>
            </q-card>
        </q-dialog>
        <q-dialog v-model="showConfirmDialog">
            <q-card>
                <q-card-section>
                    <div class="text-h6">Bestätigung</div>
                </q-card-section>
                <q-card-section>
                    Sind Sie sich sicher, dass sie diesen Reparaturauftrag löschen wollen?
                </q-card-section>
                <q-card-section>
                    <q-btn flat label="Abbrechen" color="positive" @click="showConfirmDialog = false"></q-btn>
                    <q-btn flat label="Löschen" color="negative" @click="deleteOrder"></q-btn>
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
