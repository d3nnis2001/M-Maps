<template>
    <div class="q-pa-md">
        <div class="row q-pa-sm justify-start items-center">
            <div class="col-xs-7 col-md-10">
                <q-input
                    bottom-slots
                    v-model="path"
                    label="Dateipfad"
                >
                    <template v-slot:prepend>
                        <q-icon name="search" />
                    </template>
                    <template v-slot:append>
                        <q-icon name="close" @click="path = ''" class="cursor-pointer" />
                    </template>
                </q-input>
            </div>
            <div class="col-xs-5 col-md-2 q-pl-md">
                <q-btn label="Suchen" @click="onSearch" />
            </div>
        </div>
            <div v-if="selectedFilesWithText.length">
                <q-list separator>
                    <q-item v-for="(f, index) in selectedFilesWithText" :key="index" style="width: 100%">
                        <div class="row" style="width: 100%">
                            <div class="col-xs-3 col-md-1">
                                <q-icon v-if="!f.error" name="description" size="xl" />
                                <q-icon v-else name="error" size="xl" color="red" />
                            </div>
                            <div class="col-xs-7 col-md-10">
                                <div class="row">
                                    <div class="col-12 col-md-6">
                                        <div :style="{overflow: 'auto'}">{{ f.name }}</div>
                                        <div v-if="f.error" style="color: red">
                                            {{ f.errorMsg }}
                                        </div>
                                        <div v-if="f.imported" style="color: green">
                                            Datei erfolgreich importiert!
                                        </div>
                                    </div>
                                    <div class="col-12 col-md-6">
                                        <q-input
                                            v-model="f.textInput"
                                            filled
                                            label="Strecken-Id"
                                            stack-label
                                        />
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-2 col-md-1">
                                <q-checkbox
                                    v-if="!f.imported"
                                    v-model="f.selected"
                                />
                                <q-btn
                                    v-if="f.imported"
                                    size="l"
                                    flat
                                    round
                                    icon="delete"
                                    @click="removeFile(index)"
                                />
                            </div>
                        </div>
                    </q-item>
                </q-list>
            </div>
        <div class="q-pa-md">
            <q-btn :loading="loading" color="red" text-color="white" @click="handleClick" label="Importieren"></q-btn>
        </div>
        </div>
</template>

<script>
import axios from "axios";
import {search, importFiles} from "@/main/vue/api/dataimport";

export default {
    name: "importComponent",
    data() {
        return {
            selectedFiles: [],
            selectedFilesWithText: [],
            loading: false,
            path: "",
        };
    },
    methods: {
        async onSearch() {
            if(this.path === ""){
                this.$q.notify({
                    message: "Bitte Dateipfad eingeben!",
                    timeout: 5000,
                });
                return;
            }
            this.selectedFilesWithText.splice(0);

            const data = await search(this.path);
            console.log(data)
            if(data == null) {
                this.$q.notify({
                    message: "Directory existiert nicht oder ist leer!",
                    timeout: 5000,
                });
                return;
            }
            data.forEach((d) => {
                this.selectedFilesWithText.push({
                    path: d[0],
                    name: d[1],
                    error: false,
                    errorMsg: "",
                    textInput: "",
                    imported: false,
                    selected: false,
                })
            });
        },
        removeFile(index) {
            this.selectedFiles.splice(index, 1);
            this.selectedFilesWithText.splice(index, 1);
        },
        async handleClick() {
            const selectedIndex = this.selectedFilesWithText.map((f, index) => f.selected ? index : -1).filter(index => index !== -1);
            if (selectedIndex.length === 0) {
                this.$q.notify({
                    message: "Bitte Dateien zum Importieren auswählen!",
                    timeout: 5000,
                });
                return;
            }
            this.loading = true;
            const selectedPaths = [];
            const streckenIds = [];
            selectedIndex.forEach(i => selectedPaths.push(this.selectedFilesWithText[i].path));
            selectedIndex.forEach(i => streckenIds.push(this.selectedFilesWithText[i].textInput === "" ? "missing" : this.selectedFilesWithText[i].textInput));
            const response = await importFiles(selectedPaths, streckenIds);
            if(response == null) {
                this.$q.notify({
                    message: "Server Error",
                    timeout: 5000,
                });
                return;
            }
            this.loading = false;

            response.forEach((r, index) => {
                if(r.accepted) {
                    this.selectedFilesWithText[selectedIndex[index]].imported = true;
                    this.selectedFilesWithText[selectedIndex[index]].error = false;
                    this.selectedFilesWithText[selectedIndex[index]].errorMsg = "";
                }
                else {
                    this.selectedFilesWithText[selectedIndex[index]].error = true;
                    this.selectedFilesWithText[selectedIndex[index]].errorMsg = r.reason;
                }
            });
        },
    },
};
</script>

