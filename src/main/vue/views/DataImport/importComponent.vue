<template>
    <div class="q-py-md" style="min-width: 80%">
        <q-file
            outlined
            v-model="selectedFiles"
            style="max-width: 75%"
            @update:model-value="onFilesSelected()"
            multiple
            append
        >
            <template v-slot:prepend>
                <q-icon name="attach_file" />
            </template>
        </q-file>

        <div v-if="selectedFilesWithText.length" style="max-width: 75%">
            <q-list separator>
                <q-item v-for="(f, index) in selectedFilesWithText" :key="index">
                    <q-item-section avatar class="gt-xs">
                        <q-icon v-if="!f.missingId" name="description" size="xl" />
                        <q-icon v-else name="error" size="xl" color="red" />
                    </q-item-section>
                    <q-item-section>
                        <q-item-label class="full-width ellipsis">
                            {{ f.file.name }}
                        </q-item-label>
                        <q-item-label v-if="f.missingId" style="color: red">
                            Strecken-Id fehlt!
                        </q-item-label>
                    </q-item-section>

                    <q-item-section middle center>
                        <q-input
                            v-model="f.textInput"
                            filled
                            label="Strecken-Id"
                            stack-label
                            :dense="dense"
                        />
                    </q-item-section>

                    <q-item-section top side>
                        <q-btn
                            class="gt-xs"
                            size="12px"
                            flat
                            dense
                            round
                            icon="delete"
                            @click="removeFile(index)"
                        />
                    </q-item-section>
                </q-item>
            </q-list>
        </div>
    </div>
    <div>
        <q-btn :loading="loading" color="red" text-color="white" @click="handleClick" label="Importieren"></q-btn>
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
                    missingId: false,
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
                            if(file.reason === "Missing Track_ID") {
                                this.selectedFilesWithText[index].missingId = true;
                            }
                            else {
                                //todo: Grund überprüfen, wenn strecken id fehlt diese rot markieren in der list
                            }
                        } else {
                            this.selectedFilesWithText.splice(index, 1);
                        }
                    });
                    this.loading = false;
                })
                .catch((error) => {
                    console.error("Error uploading files:", error);
                    this.loading = false;
                    // todo: error bearbeiten
                });
        },
    },
};
</script>
