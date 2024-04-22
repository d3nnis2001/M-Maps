<template>
    <div class="q-py-md" style="min-width: 80%">
        <div class="row  justify-start items-center">
            <div class="col-10">
                <q-input
                    bottom-slots
                    v-model="searchString"
                    label="Datensatz-ID"
                    :dense="dense"
                >
                    <template v-slot:prepend>
                        <q-icon name="search" />
                    </template>
                    <template v-slot:append>
                        <q-icon name="close" @click="text = ''" class="cursor-pointer" />
                    </template>
                </q-input>
            </div>
            <div class="col q-pl-md">
                <q-btn label="Suchen" @click="requestDataSets" />
            </div>
        </div>
        <div class="q-pa-md example-col-gutter-mixed">
            <div class="row q-col-gutter-x-xs q-col-gutter-y-lg">
                <div v-for="(set, index) in datasets" :key="index">
                    <q-card class="my-content">
                        <q-card-section horizontal>
                            <q-card-section avatar>
                                <q-icon name="description" size="50px" />
                            </q-card-section>
                            <q-card-section>
                                <div class="text-h6">{{ set.id }}</div>
                                <div class="text-subtitle2">{{ set.date }}</div>
                            </q-card-section>
                        </q-card-section>
                        <q-separator />
                        <q-card-actions align="right">
                            <q-checkbox v-model="set.checked" />
                        </q-card-actions>
                    </q-card>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col q-py-md">
                <q-checkbox
                    v-model="del"
                    label="Wollen Sie die Daten wirklich löschen?"
                />
                <div class="col q-py-md">
                    <q-btn
                        push
                        color="red"
                        text-color="white"
                        label="Löschen"
                        @click="deleteData"
                    />
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    name: "deleteComponent",
    data() {
        return {
            searchString: "",
            datasets: [],
            del: false,
        };
    },
    methods: {
        requestDataSets() {
            if (this.searchString.length == 0) {
                return;
            }
            //todo: adresse nur zum testen!
            axios
                .get("http://localhost:3000/data", {
                    params: {
                        search: this.searchString,
                    },
                })
                .then((response) => {
                    this.datasets = response.data;
                })
                .catch((error) => {
                    // todo: Fehler bearbeiten!
                    console.error("Error:", error);
                });
        },
        deleteData() {
            if (this.del == false) {
                this.$q.notify({
                    message: "Bitte Löschen bestätigen!",
                    timeout: 5000,
                });
                return;
            }
            const indicesToDelete = [];
            this.datasets.forEach((set, index) => {
                if (set.checked) {
                    indicesToDelete.push(index);
                }
            });
            this.datasets = this.datasets.filter((elem, idx) => {
                return !indicesToDelete.includes(idx);
            });

            const formData = new FormData();

            indicesToDelete.forEach((idx) => {
                formData.append("indices", idx);
            });
            axios
                .post("http://localhost:3000/delete", formData, {
                    headers: {
                        "Content-Type": "multipart/form-data",

                        // todo: für debugging, muss gelöscht werden
                        "Access-Control-Allow-Origin": "*",
                        "Access-Control-Allow-Headers": "*",
                        "Access-Control-Allow-Credentials": "true",
                    },
                })
                .then((response) => {
                    console.log("Response:", response.data);
                })
                .catch((error) => {
                    console.error("Error:", error);
                });
        },
    },
};
</script>

<style lang="sass">
.example-col-gutter-mixed
    .my-content
        padding: 10px 25px
        background: rgba(#999,.15)
        border: 1px solid rgba(#999,.2)
        width: 250px
</style>
