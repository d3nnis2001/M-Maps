<script>
import {onMounted, onUnmounted, reactive, ref} from "vue";
import router from "@/main/vue/router";
import {deleteInspectionOrder, getInspectionOrder, sendNewStatus} from "@/main/vue/api/inspection";
import {useQuasar} from "quasar";


export default {
    setup () {
        const $q = useQuasar();
        const state = reactive ({
            filter: '',
            columns: [
                { name: 'inspectionOrderId', required: true, label: 'ID', align: 'left', field: row => row.inspectionOrderId, format: val => `${val}`, sortable: true },
                { name: 'courseId', label: 'StreckenId', align: 'left', field: 'courseId', sortable: true },
                { name: 'startLocation', label: 'Startort', align: 'left', field: 'startLocation', sortable: true },
                { name: 'endLocation', label: 'Zielort', align: 'left', field: 'endLocation', sortable: true },
                { name: 'startTime', label: 'von', align: 'left', field: 'startTime' },
                { name: 'endTime', label: 'bis', align: 'left', field: 'endTime' },
                { name: 'inspectionData', label: 'Messdaten', align: 'left', field: 'inspectionData' },
                { name: 'department', label: 'Fachabteilung', align: 'left', field: 'department' },
                { name: 'status', label: 'Status', align: 'left', field: 'status' },
            ],
            rows: []

        });

        const smallScreen = ref(false);
        const largeScreen = ref(true);

        onMounted(async () => {
            checkScreenSize();
            window.addEventListener('resize', checkScreenSize);
            await fetchData();

        })

        onUnmounted(() => {
            window.removeEventListener('resize', checkScreenSize);
        });

        const showDialog = ref(false);
        const showConfirmDialog = ref(false);
        const showPictureUploadDialog = ref(false);
        const currentRow = ref({});
        const rowToDelete = ref(null);


        const fetchData = async () => {
            const response = await getInspectionOrder()
            console.log("API Response:", response);
            for (let i = 0; i < response.length; i++) {
                state.rows.push({
                    inspectionOrderId: response[i]["inspectionOrderId"],
                    courseId: response[i]["courseId"],
                    startLocation: response[i]["startLocation"],
                    endLocation: response[i]["endLocation"],
                    startTime: response[i]["startTime"],
                    endTime: response[i]["endTime"],
                    inspectionData: response[i]["inspectionData"],
                    department: response[i]["department"],
                    status: response[i]["status"]
                })
            }
            console.log("State Rows:", state.rows);
        };
        const checkScreenSize = () => {
            const screenSize = window.innerWidth;
            smallScreen.value = screenSize <= 500;
            largeScreen.value = screenSize > 500;
        };


        const rowClick = async (evt, rowData) => {
            currentRow.value = rowData;
            showDialog.value = true;
        };

        function createInspectionOrder() {
            router.push("inspectionOrder/create");
        }
        function editInspectionOrder() {
            const inspectionOrderId = currentRow.value["inspectionOrderId"]
            console.log(currentRow.value["inspectionOrderId"])
            router.push(`inspectionOrder/${inspectionOrderId}/edit`)
        }

        function acceptInspectionOrder() {
            // UserId Änderung!!!!
            showDialog.value = false;
            const id = currentRow.value.inspectionOrderId;
            sendNewStatus(id, "in Bearbeitung");
            // Statusänderung auf 'in Bearbeitung'
        }

        function markArchived() {
            showDialog.value = false;
            const id = currentRow.value.inspectionOrderId;
            sendNewStatus(id, "archiviert");
            // Statusänderung auf 'archiviert'
        }

        async function markFinished() {
            // Bild speichern

            showPictureUploadDialog.value = false;
            showDialog.value = false;
            const id = currentRow.value.inspectionOrderId;
            sendNewStatus(id, "abgeschlossen");
            // Statusänderung auf 'abgeschlossen'

        }

        function markCancelled() {
            showDialog.value = false;
            const id = currentRow.value.inspectionOrderId;
            sendNewStatus(id, "storniert");
            // Statusänderung auf 'storniert'
        }

        function markOrdered() {
            showDialog.value = false;
            const id = currentRow.value.inspectionOrderId;
            sendNewStatus(id, "beauftragt");
            // Statusänderung auf 'beauftragt'
        }

        const confirmDeleteOrder = (row) => {
            rowToDelete.value = row;
            showConfirmDialog.value = true;
        };

        const deleteOrder = async () => {
            const id = rowToDelete.value.inspectionOrderId;
            await deleteInspectionOrder(id);
            removeRow(id);
            showConfirmDialog.value = false;
            showDialog.value = false;
        };

        const removeRow = (name) => {
            const index = state.rows.findIndex(row => row.name === name);
            if (index !== -1) {
                state.rows.splice(index, 1);
            }
        };

        function onRejected (rejectedEntries) {
            $q.notify({
                type: 'negative',
                message: `${rejectedEntries.length} file(s) did not pass validation constraints`
            })
        }


        return {
            state,
            filter: ref(''),
            createInspectionOrder,
            editInspectionOrder,
            acceptInspectionOrder,
            deleteOrder,
            archiveOrder: markArchived,
            rowClick,
            currentRow,
            showDialog,
            showConfirmDialog,
            confirmDeleteOrder,
            markFinished,
            markCancelled,
            markOrdered,
            smallScreen,
            largeScreen,
            showPictureUploadDialog,
            onRejected,
        }
    }
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
            title="Prüfaufträge"
            :rows="state.rows"
            :columns="state.columns"
            row-key="inspectionOrderId"
            :filter = "filter"
            @row-click="rowClick" />

        <q-table
            v-show="!largeScreen && smallScreen"
            class="my-sticky-header-table2"
            flat bordered
            grid
            title="Prüfaufträge"
            :rows="state.rows"
            :columns="state.columns"
            row-key="inspectionOrderId"
            :filter = "filter"
            hide-header
            @row-click="rowClick" />
    </div>

    <div class="q-pa-md">
        <q-btn class="handleButton" style="width: 100%; max-width: 218px" size="16px" no-caps rounded label="Auftrag erstellen" @click="createInspectionOrder" color="primary"></q-btn>

        <q-dialog v-model="showDialog">
            <q-card>
                <q-card-section>
                    <div class="option-button" @click="editInspectionOrder">Bearbeiten</div>
                    <q-separator  />
                    <div class="option-button" v-if="currentRow.status === 'storniert'" @click="confirmDeleteOrder(currentRow)">Löschen</div>
                    <q-separator />
                    <div class="option-button" v-if="currentRow.status === 'abgeschlossen' && currentRow.status !== 'archiviert'" @click="archiveOrder">Archivieren</div>
                    <q-separator />
                    <div class="option-button" v-if="currentRow.status !== 'abgeschlossen'" @click="showPictureUploadDialog = true">Auftrag abschließen</div>
                    <q-separator />
                    <div class="option-button"  @click="acceptInspectionOrder">Auftrag annehmen </div>
                    <q-separator />
                    <div class="option-button" v-if="currentRow.status === 'beauftragt' && currentRow.status !== 'storniert'" @click="markCancelled">Stornieren</div>
                    <q-separator />
                    <div class="option-button" v-if="currentRow.status === 'storniert' && currentRow.status !== 'beauftragt'" @click="markOrdered">Beauftragen</div>
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
                    Sind Sie sich sicher, dass sie diesen Prüfautrag löschen wollen?
                </q-card-section>
                <q-card-section>
                    <q-btn flat label="Abbrechen" color="positive" @click="showConfirmDialog = false"></q-btn>
                    <q-btn flat label="Löschen" color="negative" @click="deleteOrder"></q-btn>
                </q-card-section>
            </q-card>
        </q-dialog>
        <q-dialog v-model="showPictureUploadDialog">
            <q-card>
                <q-card-section>
                    <div class="text-h6">Bestätigung</div>
                </q-card-section>
                <q-card-section>
                    Wollen Sie noch Fotos für die Dokumentation hochladen?
                </q-card-section>
                <q-card-section>
                    <q-uploader
                        field-name="file"
                        style="max-width: 300px"
                        url="/api/inspection/upload"
                        label="Restricted to images"
                        multiple
                        accept=".jpg, .png, image/*"
                        @rejected="onRejected"
                        with-credentials
                    />
                    <q-btn flat label="Abbrechen" color="negative" @click="showPictureUploadDialog = false"></q-btn>
                    <q-btn flat label="Prüfauftrag abschließen" color="positive" @click="markFinished"></q-btn>
                </q-card-section>
            </q-card>
        </q-dialog>

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
