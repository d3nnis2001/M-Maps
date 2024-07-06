<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import {deleteRepairOrder, repair, trackBuilderPathAxios, updateStatus} from "@/main/vue/api/reparatur";
import router from "@/main/vue/router";
import {useQuasar} from "quasar";

const smallScreen = ref(false);
const largeScreen = ref(true);

const state = reactive({
    filter: '',
    columns: [
        {
            name: 'name',
            required: true,
            label: 'Reparaturauftrag',
            align: 'left',
            field: 'name',
            format: val => `${val}`,
            sortable: true
        },
        { name: 'von', align: 'center', label: 'Von', field: 'von', sortable: true },
        { name: 'bis', label: 'Bis', field: 'bis', sortable: true },
        { name: 'freigabe', label: 'Freigabeberechtigter', field: 'freigabe', sortable: true},
        { name: 'strecke', label: 'Strecke', field: 'strecke', sortable: true},
        { name: 'status', label: 'Status', field: 'status', sortable: true},
    ],
    rows: []
});

const fetchData = async () => {
    const response = await repair();
    state.rows = response.map(item => ({
        name: item.id,
        von: item.from,
        bis: item.till,
        freigabe: item.freigabeberechtigter,
        strecke: item.track,
        status: item.status
    }));
};

const checkScreenSize = () => {
    const screenSize = window.innerWidth;
    smallScreen.value = screenSize <= 500;
    largeScreen.value = screenSize > 500;
};

onMounted(async () => {
    checkScreenSize();
    window.addEventListener('resize', checkScreenSize);
    await fetchData();
});

onUnmounted(() => {
    window.removeEventListener('resize', checkScreenSize);
});

const showDialog = ref(false);
const showConfirmDialog = ref(false);
const showConfirmDialogtwo = ref(false);
const currentRow = reactive({});
const rowToDelete = ref(null);
const emailTrackBuilder = ref('');
const $q = useQuasar();

const rowClick = (evt, rowData) => {
    Object.assign(currentRow, rowData);
    showDialog.value = true;
};

const goCreate = () => {
    router.push(`/repair/create/`);
};

const editOrder = async () => {
    const name = currentRow.name;
    router.push(`/repair/${name}/edit`);
};

const cancelOrder = async () => {
    const name = currentRow.name;
    await updateStatus(name, "storniert");
    currentRow.status = "storniert";
    updateRowStatus(name, "storniert");
    showDialog.value = false;
};

const archiveOrder = async () => {
    const name = currentRow.name;
    await updateStatus(name, "archiviert");
    currentRow.status = "archiviert";
    updateRowStatus(name, "archiviert");
    showDialog.value = false;
};

const reapplyOrder = async () => {
    const name = currentRow.name;
    await updateStatus(name, "neu beauftragt");
    currentRow.status = "neu beauftragt";
    updateRowStatus(name, "neu beauftragt");
    showDialog.value = false;
};

const sendEmailToTrackBuilder = async () => {
    const res = trackBuilderPathAxios(emailTrackBuilder.value, currentRow.name)
    console.log(currentRow.name)
    if (res) {
        $q.notify({
            type: 'positive',
            message: 'An email with the link has been sent to the track builder!'
        });
    } else {
        $q.notify({
            type: 'negative',
            message: 'Error',
            caption: 'Something went wrong. Please send again!'
        });
    }
    showConfirmDialogtwo.value = false;
};

const isValidEmail = async (email) => {
    const regex = /^[A-Za-z0-9+_.-]+@[A-Za-z+_]+\.[A-Za-z+_.-]+$/;
    return regex.test(email);
};


const confirmDeleteOrder = (row) => {
    rowToDelete.value = row;
    showConfirmDialog.value = true;
};

const deleteOrder = async () => {
    const name = rowToDelete.value.name;
    await deleteRepairOrder(name);
    removeRow(name);
    showConfirmDialog.value = false;
    showDialog.value = false;
};

const updateRowStatus = (name, status) => {
    const row = state.rows.find(row => row.name === name);
    if (row) {
        row.status = status;
    }
};

const removeRow = (name) => {
    const index = state.rows.findIndex(row => row.name === name);
    if (index !== -1) {
        state.rows.splice(index, 1);
    }
};

const finishRepairOrder = async (name) => {
    await updateStatus(name, "bestätigt");
    updateRowStatus(name, "bestätigt")
    showDialog.value = false;
}
</script>

<template>
    <div class="q-pa-md extra-margin">
        <q-input borderless dense debounce="300" v-model="state.filter" placeholder="Search">
            <template v-slot:append>
                <q-icon name="search" />
            </template>
        </q-input>

        <q-table
            v-show="!smallScreen && largeScreen"
            class="my-sticky-header-table"
            flat bordered
            title="Reparaturaufträge"
            :rows="state.rows"
            :columns="state.columns"
            row-key="name"
            :filter="state.filter"
            @row-click="rowClick"
        >
            <template v-slot:body="props">
                <q-tr :props="props" @click="rowClick($event, props.row)">
                    <q-td key="name" :props="props">
                        {{ props.row.name }}
                    </q-td>
                    <q-td key="von" :props="props">
                        {{ props.row.von }}
                    </q-td>
                    <q-td key="bis" :props="props">
                        {{ props.row.bis }}
                    </q-td>
                    <q-td key="freigabe" :props="props">
                        {{ props.row.freigabe }}
                    </q-td>
                    <q-td key="strecke" :props="props">
                        {{ props.row.strecke }}
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
            title="Reparaturaufträge"
            :rows="state.rows"
            :columns="state.columns"
            row-key="name"
            hide-header
            class="my-sticky-header-table2"
        >
            <template v-slot:header="props">
                <q-tr :props="props">
                    <q-th :key="props.cols[0].name" :props="props">
                        {{props.cols[0].label}}
                    </q-th>
                    <q-th :key="props.cols[1].name" :props="props">
                        {{props.cols[1].label}}
                    </q-th>
                    <q-th :key="props.cols[2].name" :props="props">
                        {{props.cols[2].label}}
                    </q-th>
                </q-tr>
            </template>
            <template v-slot:item="props">
                <div class="q-pa-xs col-xs-12 col-sm-6 col-md-4 col-lg-3">
                    <q-card bordered flat @click="rowClick($event, props.row)">
                        <q-list dense>
                            <q-item v-for="col in props.cols" :key="col.name">
                                <q-item-section>
                                    <q-item-label caption>{{ col.label }}</q-item-label>
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ props.row[col.field] }}</q-item-label>
                                </q-item-section>
                            </q-item>
                        </q-list>
                    </q-card>
                </div>
            </template>
        </q-table>
        <q-btn class="handleButton" style="width: 100%; max-width: 218px" size="16px" no-caps rounded label="Neu Erstellen" color="primary" @click=goCreate></q-btn>

        <q-dialog v-model="showDialog">
            <q-card>
                <q-card-section>
                    <div class="option-button" @click="editOrder">Bearbeiten</div>
                    <q-separator v-if="currentRow.status !== 'abgeschlossen'" />
                    <div class="option-button" v-if="currentRow.status === 'storniert'" @click="confirmDeleteOrder(currentRow)">Löschen</div>
                    <q-separator v-if="currentRow.status === 'bestätigt'" />
                    <div class="option-button" v-if="currentRow.status === 'bestätigt'" @click="archiveOrder">Archivieren</div>
                    <q-separator v-if="currentRow.status !== 'abgeschlossen'" />
                    <div class="option-button" v-if="currentRow.status !== 'storniert'" @click="cancelOrder">Stornieren</div>
                    <q-separator v-if="currentRow.status === 'abgeschlossen'" />
                    <div class="option-button" v-if="currentRow.status === 'storniert'" @click="reapplyOrder">Neu beantragen</div>
                    <q-separator v-if="currentRow.status === 'terminiert'" />
                    <div class="option-button" v-if="currentRow.status === 'terminiert'" @click="showConfirmDialogtwo = true">Link an Gleisbauer</div>
                    <q-separator v-if="currentRow.status === 'abgeschlossen'" />
                    <div class="option-button" v-if="currentRow.status === 'abgeschlossen'" @click="finishRepairOrder(currentRow.name)">Bestätigen</div>
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

        <q-dialog v-model="showConfirmDialogtwo">
            <q-card>
                <q-card-section>
                    <div class="text-h6">Reparaturauftrag verschicken</div>
                </q-card-section>
                <q-card-section>
                    <q-input v-model="emailTrackBuilder" label="E-Mail Eingabe"
                             :rules="[val => isValidEmail(val) || 'Invalid email address']"
                    />
                </q-card-section>
                <q-card-section>
                    <q-btn flat label="Abbrechen" @click="showConfirmDialogtwo = false"></q-btn>
                    <q-btn flat label="Senden" color="positive" @click="sendEmailToTrackBuilder"></q-btn>
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
.extra-margin {
    margin-top: 20px;
    margin-left: 5px;
    margin-right: 5px;
}

.option-button:hover {
    text-decoration: underline;
}

.handleButton {
    margin-top: 20px;

}
</style>

<style lang="sass">
.my-sticky-header-table
    height: 500px
    .q-table__top,
    .q-table__bottom,
    thead tr:first-child th
        background-color: $blue-grey-5
    thead tr th
        position: sticky
        z-index: 0
    thead tr:first-child th
        top: 0
    &.q-table--loading thead tr:last-child th
        top: 48px
    tbody
        scroll-margin-top: 48px

.my-sticky-header-table2
    .q-table__top,
    .q-table__bottom,
    thead tr:first-child th
        background-color: $blue-grey-5
</style>
