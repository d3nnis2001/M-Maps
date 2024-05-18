<script>
import {useQuasar} from "quasar";
import {useRouter} from "vue-router";
import {useLoginStore} from "@/main/vue/stores/LoginStore";
import {ref} from "vue";
import {getUserData} from "@/main/vue/api/admin";
import axios, {all} from "axios";

export default {
    setup() {
        const $q = useQuasar()
        const router = useRouter()
        const loginStore = useLoginStore()
        const columns = [
            {name: 'id', label: 'ID', align: 'left', field: row => row.id, format: val => `${val}`, sortable: true},
            {name: 'E-Mail', label: 'E-Mail', align: 'left', field: 'username', sortable: true},
            {name: 'First Name', label: 'First Name', align: 'left', field: 'firstname', sortable: true},
            {name: 'Last Name', label: 'Last Name', align: 'left', field: 'lastname', sortable: true},
            //{name: 'Roles', label: 'Roles', align: 'left', field: 'roles', sortable: true},
        ]
        const rows = ref([])

        /*
        function getData() {
            const data = getUserData()
            console.log(data)
            data.then(allUsers => {
                console.log(allUsers)
                allUsers.forEach(user => {
                    console.log(user)
                    rows.value.push( {
                        id: 'not in Database',
                        username: user[0],
                        firstname: user[1],
                        lastname: user[2],
                    })
                })
            })
        }

         */

        function getData() {
            const data = getUserData()
            console.log(data)
            data.then(allUsers => {
                console.log(allUsers)
                allUsers.forEach(user => {
                    console.log(user)
                    rows.value.push( {
                        id: 'not in Database',
                        username: user.username,
                        firstname: user.firstname,
                        lastname: user.lastname,
                    })
                })
            })
        }


        getData();
        return {getData, columns, rows}
    }
}
</script>

<template>
    <div class="q-pa-md">
        <q-btn label="Hol Nutzerdaten" color="primary" @click=getData class=""></q-btn>
        <q-table
            title="User Data"
            :rows="rows"
            :columns="columns"
            row-key="name"
        />
    </div>
</template>
<style scoped>

</style>

