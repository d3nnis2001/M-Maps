<script>
import {useQuasar} from "quasar";
import {useRouter} from "vue-router";
import {useLoginStore} from "@/main/vue/stores/LoginStore";
import {ref} from "vue";
import {getUserData} from "@/main/vue/api/admin";
import axios, {all} from "axios";
const columns = [
    {name: 'username', required: true, label: 'E-Mail', align: 'left', field: row => row.username, format: val => `${val}`,sortable: true},
    {name: 'firstname', label: 'First Name', align: 'left', field: 'firstname', sortable: true},
    {name: 'firstname', label: 'Last Name', align: 'left', field: 'lastname', sortable: true},
    //{name: 'Roles', label: 'Roles', align: 'left', field: 'roles', sortable: true},
]
const rows = ref([])
const selected = ref([])

export default {
    setup() {
        const $q = useQuasar()
        const router = useRouter()
        const loginStore = useLoginStore()

        function getData() {
            const data = getUserData()
            console.log(data)
            data.then(allUsers => {
                console.log(allUsers)
                allUsers.forEach(user => {
                    console.log(user)
                    rows.value.push( {
                        username: user.username,
                        firstname: user.firstname,
                        lastname: user.lastname,
                    })
                })
            })
        }

        getData();
        return {columns, rows, selected}
    },
    data() {
        return {columns, rows, selected, }
    },
    methods: {
        deleteUser(selectedUser) {

            if (selectedUser[0] === undefined) {
                this.$q.notify({
                    message: "Wähle einen Nutzer aus!",
                    timeout: 5000,
                });
                return;
            } else {
                console.log(selectedUser[0].username)
            }
        }

}
}
</script>

<template>
    <div class="q-pa-md">
        <!--
        <q-btn label="Freischalten" color="primary" @click= class=""></q-btn>
        <q-btn label="Editieren" color="primary" @click= class=""></q-btn>
        -->
        <q-btn label="Löschen" color="primary" @click="deleteUser(selected)" class=""></q-btn>
        <q-table
            title="User Data"
            :rows="rows"
            :columns="columns"
            row-key="username"
            selection="single"
            v-model:selected="selected"
        />
        <div class="q-mt-md">
            Selected: {{ JSON.stringify(selected) }}
        </div>
    </div>
</template>
<style scoped>
tr.q-table__row--selected {
    background-color: #42b983 !important;
}
</style>

