<script>
    import {useQuasar} from "quasar";
    import {useRouter} from "vue-router";
    import {useLoginStore} from "@/main/vue/stores/LoginStore";
    import {ref} from "vue";
    import {getUserData} from "@/main/vue/api/admin";

    export default {
        setup() {
            const $q = useQuasar()
            const router = useRouter()
            const loginStore = useLoginStore()
            const columns = [
                {
                    name: 'name',
                    label: 'Users',
                    align: 'left',
                    field: row => row.name,
                    format: val => `${val}`,
                    sortable: true
                },
                { name: 'E-Mail', align: 'center', label: 'E-Mail', field: 'username', sortable: true },
                { name: 'First Name', label: 'First Name', field: 'firstname', sortable: true },
                { name: 'Last Name', label: 'Last Name', field: 'lastname', sortable: true },
                { name: 'Service', label: 'Service', field: 'service', sortable: true},
            ]
            function getData() {
                const data = getUserData()
                console.log(data)
                return data
            }

            const rows = getData()
            return {getData}
        }
    }
</script>

<template>
    <div class="q-pa-md">
        <q-btn label="Hol Nutzerdaten" color="primary" @click=getData class=""></q-btn>
        <q-table
            title="Treats"
            :rows="rows"
            :columns="columns"
            row-key="name"
        />
    </div>
</template>

<style scoped>

</style>
