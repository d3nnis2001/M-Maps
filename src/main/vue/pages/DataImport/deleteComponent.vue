<template>
    <div class="q-py-md" style="width: 100%">
        <div class="row q-pa-sm justify-start items-center">
            <div class="col-xs-7 col-md-10">
                <q-input
                    bottom-slots
                    v-model="searchString"
                    label="Datensatz-ID"
                >
                    <template v-slot:prepend>
                        <q-icon name="search" />
                    </template>
                    <template v-slot:append>
                        <q-icon name="close" @click="searchString = ''" class="cursor-pointer" />
                    </template>
                </q-input>
            </div>
            <div class="col-xs-5 col-md-2 q-pl-md">
                <q-btn label="Suchen" @click="requestDataSets" />
            </div>
        </div>
        <div class="q-pa-md">
            <div class="row q-col-gutter-x-xs q-col-gutter-y-lg">
                <div class="col-md-4 col-sm-12" v-for="(set, index) in datasets" :key="index">
                    <q-card style="width: 80%">
                        <q-card-section horizontal class="justify-around q-px-md">
                            <q-icon name="description" size="xl" />
                            <div class="text-bold">{{ set.id }}</div>
                            <q-checkbox v-model="set.checked" />
                        </q-card-section>
                        <q-separator />
                        <q-card-section>
                            <div>{{ set.streckenid }}</div>
                            <div style="width: 80%; overflow: auto;">{{ set.fileName }}</div>
                            <div>{{ set.date }}</div>
                        </q-card-section>
                    </q-card>
                </div>
            </div>
        </div>
        <div class="row q-pb-xl q-pl-md">
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
import {deleteDataSets} from "@/main/vue/api/dataimport";

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
            this.datasets.splice(0, this.datasets.length);
            axios
                .get("http://localhost:8088/api/files/getFiles", {
                    params: {
                        search: this.searchString,
                    },
                })
                .then((response) => {
                    response.data.forEach((set) => {
                        this.datasets.push( {
                            id: set.id,
                            streckenid: set.streckenId,
                            fileName: set.fileName,
                            date: set.uploadDate,
                            checked: false,
                        });
                    });
                })
                .catch((error) => {
                    this.$q.notify({
                        message: error.toString(),
                        timeout: 5000,
                    });
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
            const idToDelete = [];
            this.datasets.forEach((set, index) => {
                if (set.checked) {
                    indicesToDelete.push(index);
                    idToDelete.push(set.id);
                }
            });
            const formData = new FormData();
            idToDelete.forEach((id) => {
                formData.append("ids[]", id);
            });

            this.datasets = this.datasets.filter((elem, idx) => {
                return !indicesToDelete.includes(idx);
            });
            deleteDataSets(formData);
        },
    },
};
</script>

