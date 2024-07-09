<script>
import {onMounted, onUnmounted, ref} from 'vue';
import { useRoute } from 'vue-router';
import {useQuasar} from "quasar";
import {sendImage} from "../../api/image";
import {getTickedItems, updateStatus, getDetailsByID, getTerminationDate} from "../../api/reparatur";


export default {
    setup() {
        const route = useRoute();
        const repairDetails = ref({});
        const ticked = ref([]);
        const date = ref(getDate());
        const options = ref([]);
        const terminationDate = ref('');
        const id = ref('')
        const $q = useQuasar();

        const files = ref([]);
        const base64String = ref('');
        const imageName = ref('')
        const base64Strings = ref([]);
        const errormsg = ref([])
        const imagemsg = ref([])
        const smallScreen = ref(false);
        const largeScreen = ref(true);

        onMounted(async () => {
            checkScreenSize();
            window.addEventListener('resize', checkScreenSize);

            id.value = route.query.id;
            console.log(route.query.id);
            repairDetails.value = await getDetailsByID(id.value)

            terminationDate.value = await getTerminationDate(id.value);
            console.log(terminationDate)
            console.log(terminationDate.value)

            if (repairDetails.value && repairDetails.value.checklist && repairDetails.value.checklist.checkSel !== undefined) {
                for (let i = 0; i < repairDetails.value.checklist.checkSel.length; i++) {
                    ticked.value.push({label: repairDetails.value.checklist.checkSel.get(i), value: i});
                }
                let i = 0;
                for (i; i < repairDetails.value.checklist.checkPoints.tasks.length; i++) {
                    options.value.push({label: repairDetails.value.checklist.checkPoints.tasks[i], value: i});
                }
                console.log(i)
                for (let j = i; j < repairDetails.value.checklist.checkPoints.material.length + i; j++) {
                    options.value.push({label: repairDetails.value.checklist.checkPoints.material[j - i], value: j});
                }
            }
            /*
            if (repairDetails.value && repairDetails.value.checklist) {
                for (let i = 0; i < repairDetails.value.checklist.checkSel.length; i++) {
                    ticked.value.push({label: repairDetails.value.checklist.checkSel.get(i), value: i});
                }
                for (let i = 0; i < repairDetails.value.checklist.checkPoints.items.length; i++) {
                    options.value.push({label: repairDetails.value.checklist.checkPoints.items[i], value: i});
                }
            }

             */

        });

        onUnmounted(() => {
            window.removeEventListener('resize', checkScreenSize);
        });

        const checkScreenSize = () => {
            const screenSize = window.innerWidth;
            smallScreen.value = screenSize <= 500;
            largeScreen.value = screenSize > 500;
        };

        function getDate() {
            const today = new Date();
            const year = today.getFullYear();
            const month = String(today.getMonth() + 1).padStart(2, '0');
            const day = String(today.getDate()).padStart(2, '0');
            return `${year}/${month}/${day}`;
        }

        async function confirmRepairOrder() {
            await updateStatus(route.query.id, "abgeschlossen");
            $q.notify ({
                type: 'positive',
                message: 'The repair order has been successfully updated'
            })
        }


        // Foto upload:
        async function uploadImage(imageString, name) {
            const res = await sendImage(id, imageString, name);
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
            }
        };

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

        return {
            repairDetails,
            ticked,
            options,
            terminationDate,
            confirmRepairOrder,
            date,
            onFileRemoved,
            onFileAdded,
            uploadImages,
            files
        };
    }
}
</script>

<template>
    <q-layout view="hHh lpR fFf">
        <q-header class="bg-white text-dark q-px-md q-py-sm">
            <q-toolbar class="justify-center">
                <q-img src="/src/main/resources/db-logo.png" style="width: 50px; height: auto; margin-right: 10px;"/>
                <q-toolbar-title class="text-center">Gleisbauer</q-toolbar-title>
            </q-toolbar>
        </q-header>

        <q-page-container>
            <div class="outline">
                <div class="outer-layer">
                    <div>
                        <div class="row">
                            <p style="margin-right: 5px">ID: </p>
                            <p style="font-weight: bold">{{ repairDetails.id }}</p>
                        </div>
                        <div class="row">
                            <p style="margin-right: 5px">Zeitraum: </p>
                            <p style="font-weight: bold">
                                {{ "   von   " + repairDetails.from + "   bis   " + repairDetails.till }}</p>
                        </div>
                        <div class="row">
                            <p style="margin-right: 5px">Freigabeberechtigter: </p>
                            <p style="font-weight: bold">{{ repairDetails.freigabeberechtigter }}</p>
                        </div>
                        <div class="row">
                            <p style="margin-right: 5px">Status: </p>
                            <p style="font-weight: bold">{{"terminiert zum   " + terminationDate.data}}</p>
                        </div>
                    </div>
                    <q-separator size="2px" color="primary" style="margin-top: 30px"></q-separator>
                    <p class="checklist">Checkliste</p>
                    <div class="row extra-mar">
                    <div class="outline-nomar">
                        <div class="q-pa-md">
                            <div v-for="item in options" :key="item.value" class="checklist-item">
                                {{ item.label }}
                            </div>
                        </div>
                    </div>
                    <div class="q-pa-md">

                            <q-uploader
                                v-model="files"
                                label="Laden Sie Ihre Fotos hoch"
                                @added="onFileAdded"
                                @removed="onFileRemoved"
                                no-auto-upload="true"
                                multiple
                                no-thumbnails
                            />
                            <q-btn flat label="Upload" color="primary" @click="uploadImages"></q-btn>
                    </div>
                    </div>
                    <q-btn style="width: 100%; max-width: 218px" size="16px" no-caps rounded label="Bestätigen" color="primary" @click=confirmRepairOrder></q-btn>
                </div>
            </div>
        </q-page-container>
    </q-layout>
</template>

<style lang="scss">
.outer-layer {
    margin: 20px;
}

.outline {
    border: 2px solid $primary;
    padding: 20px;
    margin: 10px;
    border-radius: 15px;
    background-color: #F7F7F7;
    box-shadow: 0 6px 10px rgba(0, 0, 0, 0.3);
}

.checklist {
    font-weight: bold;
    font-size: 30px;
    margin-top: 30px;
}

.checklist-item {
    margin-bottom: 5px;
}

p {
    font-weight: normal;
}

.outline-nomar {
    border: 2px solid $primary;
    padding: 10px;
    margin-bottom: 20px;
    border-radius: 15px;
    flex: 1;
}

.q-header {
    background-color: white;
    padding: 8px 16px;
}

.q-toolbar-title {
    flex: 1;
    text-align: center;
}

.q-layout__page {
    padding: 0;
    margin: 0;
}

.row.extra-mar {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
}

.upload-container {
    display: flex;
    justify-content: flex-end;
    align-items: center;
}

.btn {
    margin-top: 40px;
    margin-left: 60px
}
</style>
