<template>
    <div class="q-pa-md">
        <div>
            <q-file
                outlined
                v-model="selectedFiles"
                style="width: 80%"
                @update:model-value="onFilesSelected()"
                multiple
                append
            >
                <template v-slot:prepend>
                    <q-icon name="attach_file" />
                </template>
            </q-file>
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
                                        <div :style="{overflow: 'hidden'}">{{ f.file.name }}</div>
                                        <div v-if="f.error" style="color: red">
                                            {{ f.errorMsg }}
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
                                <q-btn
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

export default {
    name: "importComponent",
    data() {
        return {
            selectedFiles: [],
            selectedFilesWithText: [],
            loading: false,
        };
    },
    methods: {
        onFilesSelected() {
            this.selectedFiles.forEach((f) =>
                this.selectedFilesWithText.push({
                    file: f,
                    textInput: "",
                    error: false,
                    errorMsg: ""
                })
            );
            this.selectedFiles.splice(0);
            console.log(this.selectedFilesWithText[0].file.name);
        },
        removeFile(index) {
            this.selectedFiles.splice(index, 1);
            this.selectedFilesWithText.splice(index, 1);
        },
        handleClick() {
            if (this.selectedFilesWithText.length === 0) {
                this.$q.notify({
                    message: "Bitte Dateien zum Importieren auswählen!",
                    timeout: 5000,
                });
                return;
            }
            this.loading = true;
            const formData = new FormData();

            this.selectedFilesWithText.forEach((f) => {
                formData.append("files[]", f.file);
                formData.append("additionalData[]", f.textInput === "" ? "missing" : f.textInput);
            });

            axios
                .post("http://localhost:8088/api/files/uploadFiles", formData, {
                    headers: {
                        "Content-Type": "multipart/form-data",
                    },
                })
                .then((response) => {
                    const  files  = response.data;
                    console.log("Response:", files);

                    files.forEach((file) => {
                        console.log(`File: ${file.name}, Accepted: ${file.accepted}, Reason: ${file.reason}`);
                        const index = this.selectedFilesWithText.findIndex(
                            (f) => f.file.name === file.name
                        );
                        console.log("------ " + index);
                        if (file.accepted === false) {
                            this.selectedFilesWithText[index].error = true;
                            this.selectedFilesWithText[index].errorMsg = file.reason;
                        } else {
                            this.selectedFilesWithText.splice(index, 1);
                        }
                    });
                    this.loading = false;
                })
                .catch((error) => {
                    console.error("Error uploading files:", error);
                    this.loading = false;
                    this.$q.notify({
                        message: error.toString(),
                        timeout: 5000,
                    });
                });
        },
    },
};
</script>

