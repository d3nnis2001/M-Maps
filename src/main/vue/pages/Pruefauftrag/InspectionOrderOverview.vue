<script>
import {onMounted, onUnmounted, reactive, ref} from "vue";
import router from "@/main/vue/router";
import {
    deleteInspectionOrder,
    getDataById,
    getInspectionOrder,
    sendNewStatus,
    sendReview
} from "@/main/vue/api/inspection";
import {useQuasar} from "quasar";
import axios from "axios";
import {sendImage} from "@/main/vue/api/image";
import {getUserByToken} from "../../api/admin";
import {sendUsername} from "../../api/inspection";
import {useUserStore} from "@/main/vue/stores/UserStore";


export default {
    setup () {
        const $q = useQuasar();

        const files = ref([]);
        const base64String = ref('');
        const imageName = ref('')
        const base64Strings = ref([]);
        const errormsg = ref([])
        const imagemsg = ref([])

        const showDialog = ref(false);
        const showConfirmDialog = ref(false);
        const showPictureUploadDialog = ref(false);
        const showFurtherInformationDialog = ref(false);
        const currentRow = ref({});
        const rowToDelete = ref(null);

        const userId = ref('');
        const remarks = ref('');
        const review = ref('');
        const finishedDate = ref('')

        const userStore = useUserStore();
        const bearbeiter = ref(false);
        const pruefer = ref(false);

        const state = reactive ({
            filter: '',
            columns: [
                { name: 'inspectionOrderId', required: true, label: 'ID', align: 'left', field: row => row.inspectionOrderId, format: val => `${val}`, sortable: true },
                { name: 'courseId', label: 'StreckenId', align: 'left', field: 'courseId', sortable: true },
                { name: 'startLocation', label: 'Startort', align: 'left', field: 'startLocation', sortable: true },
                { name: 'endLocation', label: 'Zielort', align: 'left', field: 'endLocation', sortable: true },
                { name: 'startTime', label: 'von', align: 'left', field: 'startTime' },
                { name: 'endTime', label: 'bis', align: 'left', field: 'endTime' },
                { name: 'inspectionData', label: 'Messdaten', align: 'left', field: 'inspectionData', sortable: true },
                { name: 'department', label: 'Fachabteilung', align: 'left', field: 'department', sortable: true},
                { name: 'status', label: 'Status', align: 'left', field: 'status' },
                { name: 'priority', label: 'Priorität', align: 'left', field: 'priority', sortable: true }
            ],
            rows: []

        });

        const smallScreen = ref(false);
        const largeScreen = ref(true);

        onMounted(async () => {
            const authenticated = axios.defaults.headers['Authorization'] !== null;
            console.log(authenticated)
            checkScreenSize();
            window.addEventListener('resize', checkScreenSize);
            bearbeiter.value = userStore.hasRole('Bearbeiter')
            console.log("Bearbeiter: ", bearbeiter.value);
            pruefer.value = userStore.hasRole('Prüfer');
            console.log("Prüfer: ", pruefer.value)

            await fetchData();

        })

        onUnmounted(() => {
            window.removeEventListener('resize', checkScreenSize);
        });


        const fetchData = async () => {
            const response = await getInspectionOrder()
            console.log("API Response:", response);
            state.rows = response
                .filter(row => row.status !== 'archiviert')
                .map(row => ({
                    inspectionOrderId: row.inspectionOrderId,
                    courseId: row.courseId,
                    startLocation: row.startLocation,
                    endLocation: row.endLocation,
                    startTime: row.startTime,
                    endTime: row.endTime,
                    inspectionData: row.inspectionData,
                    department: row.department,
                    status: row.status,
                    priority: row.priority
                }));
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
            fetchData();
        }
        function editInspectionOrder() {
            const inspectionOrderId = currentRow.value["inspectionOrderId"]
            console.log(currentRow.value["inspectionOrderId"])
            router.push(`inspectionOrder/${inspectionOrderId}/edit`)
        }

        async function acceptInspectionOrder() {
            const token = localStorage.getItem('token')
            const id = currentRow.value.inspectionOrderId;
            console.log(token)
            if (token != null) {
                const userId = await getUserByToken(token)
                console.log(userId)
                await sendUsername(id, userId.data)
            }
            showDialog.value = false;
            await sendNewStatus(id, "in Bearbeitung");
            updateRowStatus(id, "in Bearbeitung");

        }

        async function markArchived() {
            showDialog.value = false;
            const id = currentRow.value.inspectionOrderId;
            await sendNewStatus(id, "archiviert");
            updateRowStatus(id, "archiviert");
            await fetchData();
        }

        async function markFinished() {
            showPictureUploadDialog.value = false;
            showDialog.value = false;
            const id = currentRow.value.inspectionOrderId;
            await sendNewStatus(id, "abgeschlossen");
            updateRowStatus(id, "abgeschlossen");

        }

        async function markCancelled() {
            showDialog.value = false;
            const id = currentRow.value.inspectionOrderId;
            await sendNewStatus(id, "storniert");
            updateRowStatus(id, "storniert");
        }

        async function markOrdered() {
            showDialog.value = false;
            const id = currentRow.value.inspectionOrderId;
            await sendNewStatus(id, "beauftragt");
            updateRowStatus(id, "beauftragt");
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
            await fetchData();
        };

        const removeRow = (name) => {
            const index = state.rows.findIndex(row => row.name === name);
            if (index !== -1) {
                state.rows.splice(index, 1);
            }
        };

        const updateRowStatus = (inspectionOrderId, status) => {
            const row = state.rows.find(row => row.inspectionOrderId === inspectionOrderId);
            if (row) {
                row.status = status;
            }
        };

        async function showFurtherInformation() {
            showFurtherInformationDialog.value = true;
            const inspecOrder = await getDataById(currentRow.value.inspectionOrderId);
            userId.value = inspecOrder.userId;
            console.log("userId: ", userId);
            remarks.value = inspecOrder.remarks;
            console.log("remarks: ", remarks);
        }

        router.afterEach((to, from) => {
            if (from.name === 'editInspectionOrder') {
                fetchData();
            }
        });

        // Foto Upload:


        const fileToBase64 = (file) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = () => {
                imageName.value = file.name;
                console.log("name: ", imageName.value);
                base64String.value = reader.result;
                if (base64String.value !== null) {
                    console.log("success in fileToBase64");
                }
                uploadImage(base64String.value, imageName.value);

            };
            reader.onerror = (error) => {
                console.log('Error: ', error);
            };
        };

        async function uploadImage(imageString, name) {
            const res = await sendImage(currentRow.value.inspectionOrderId, imageString, name);
            if (res === false) {
                errormsg.value.push("There has been an error while uploading your image. Please try again or contact our support.");
                console.log("error")
            }
            if (res === true) {
                imagemsg.value.push("Your image has been successfully uploaded!")
                console.log("success")
            }

        }

        const onFileRemoved = (removedFile) => {
            files.value = files.value.filter(f => f !== removedFile);
            base64Strings.value = base64Strings.value.filter(base64 => base64.file !== removedFile);
            console.log("onFileRemoved", files.value);
        };

        const onFileAdded = (newFiles) => {
            newFiles.forEach(file => {
                files.value.push(file);
            });
            console.log("onFileAdded", files.value)
        };

        function upload() {
            uploadImages();
            uploadReview();
        }

        const uploadImages = async () => {
            files.value.forEach(file => {
                console.log("uploadImages: ", file.name);
                fileToBase64(file);

            });
            if (errormsg.value.length > 0) {
                for (let i = 0; i < errormsg.value.length; i++) {
                    $q.notify({
                        type: 'negative',
                        message: errormsg[i]
                    })
                    console.log("errormsg")
                }
            }

            if (imagemsg.value.length > 0) {
                for (let i = 0; i < imagemsg.value.length; i++) {
                    $q.notify({
                        type: 'positive',
                        message: imagemsg[i]
                    })
                    console.log("imagemsg")
                }
                await markFinished()
            }
        };

        async function uploadReview() {
            console.log(review.value)
            console.log(finishedDate.value)
            await sendReview(currentRow.value.inspectionOrderId, review.value, finishedDate.value)
            await markFinished();

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
            markCancelled,
            markOrdered,
            smallScreen,
            largeScreen,
            showPictureUploadDialog,
            showFurtherInformation,
            showFurtherInformationDialog,
            userId,
            remarks,
            onFileAdded,
            onFileRemoved,
            files,
            review,
            base64String,
            uploadImages,
            upload,
            finishedDate,
            bearbeiter,
            pruefer
        }
    },
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
            :filter = "state.filter"
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
            :filter = "state.filter"
            hide-header
            @row-click="rowClick" />
    </div>

    <div class="q-pa-md">
        <q-btn class="handleButton" style="width: 100%; max-width: 218px" size="16px" no-caps rounded label="Auftrag erstellen" @click="createInspectionOrder" color="primary"></q-btn>

        <q-dialog v-model="showDialog">
            <q-card>
                <q-card-section>
                    <div class="option-button" v-if="bearbeiter" @click="editInspectionOrder">Bearbeiten</div>
                    <q-separator  v-if="currentRow.status === 'storniert' && bearbeiter"/>
                    <div class="option-button" v-if="currentRow.status === 'storniert' && bearbeiter" @click="confirmDeleteOrder(currentRow)">Löschen</div>
                    <q-separator v-if="currentRow.status === 'abgeschlossen' && currentRow.status !== 'archiviert' && bearbeiter"/>
                    <div class="option-button" v-if="currentRow.status === 'abgeschlossen' && currentRow.status !== 'archiviert' && bearbeiter" @click="archiveOrder">Archivieren</div>
                    <q-separator v-if="currentRow.status !== 'abgeschlossen' && pruefer"/>
                    <div class="option-button" v-if="currentRow.status !== 'abgeschlossen' && pruefer" @click="showPictureUploadDialog = true">Auftrag abschließen</div>
                    <q-separator v-if="currentRow.status !== 'in Bearbeitung' && pruefer"/>
                    <div class="option-button" v-if="currentRow.status !== 'in Bearbeitung' && pruefer" @click="acceptInspectionOrder">Auftrag annehmen </div>
                    <q-separator v-if="currentRow.status === 'beauftragt' && currentRow.status !== 'storniert' && bearbeiter"/>
                    <div class="option-button" v-if="currentRow.status === 'beauftragt' && currentRow.status !== 'storniert' && bearbeiter" @click="markCancelled">Stornieren</div>
                    <q-separator v-if="currentRow.status === 'storniert' && currentRow.status !== 'beauftragt' && bearbeiter" />
                    <div class="option-button" v-if="currentRow.status === 'storniert' && currentRow.status !== 'beauftragt' && bearbeiter" @click="markOrdered">Beauftragen</div>
                    <q-separator />
                    <div class="option-button"  @click="showFurtherInformation">Weitere Informationen </div>
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
                    <div class="text-h6">Bewertung</div>
                </q-card-section>
                <q-card-section>
                    Wollen Sie noch Fotos für die Dokumentation hochladen?
                </q-card-section>
                <q-card-section>
                    <q-uploader
                        v-model="files"
                        label="Laden Sie Ihre Fotos hoch"
                        @added="onFileAdded"
                        @removed="onFileRemoved"
                        no-auto-upload="true"
                        multiple
                        no-thumbnails
                    />
                    <q-card-section>
                        <q-input class="input-style mar-right" filled v-model="finishedDate" label="Abschlussdatum" mask="date" :rules="['date']">
                            <template v-slot:append>
                                <q-icon name="event" class="cursor-pointer">
                                    <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                        <q-date v-model="finishedDate">
                                            <div class="row items-center justify-end">
                                                <q-btn v-close-popup label="Close" color="primary" flat />
                                            </div>
                                        </q-date>
                                    </q-popup-proxy>
                                </q-icon>
                            </template>
                        </q-input>
                        <q-input class="input-bem text-with-input" outlined color="primary" v-model="review" label="Bewertung" />

                    </q-card-section>
                    <q-btn flat label="Abbrechen" color="negative" @click="showPictureUploadDialog = false"></q-btn>
                    <q-btn flat label="Prüfauftrag abschließen" color="positive" @click="upload"></q-btn>
                </q-card-section>
            </q-card>
        </q-dialog>
        <q-dialog v-model="showFurtherInformationDialog">
            <q-card>
                <q-card-section>
                    <div class="text-h6">Weitere Informationen</div>
                </q-card-section>
                <div>
                    <div class="row">
                        <p style="font-weight: bold; margin-left: 5px; margin-right: 5px"> UserId: </p>
                        <p style="">{{ userId }}</p>
                    </div>
                </div>
                <div>
                    <div class="row info-row">
                        <p style="margin-left: 5px; font-weight: bold; margin-right: 5px"> Bemerkungen: </p>
                        <p style="margin-left: 5px">{{ remarks }}</p>
                    </div>
                </div>
                <q-card-section>
                    <q-btn flat label="Schließen" color="primary" @click="showFurtherInformationDialog = false"></q-btn>
                </q-card-section>3
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

.q-uploader
    margin-left: 20px


</style>
