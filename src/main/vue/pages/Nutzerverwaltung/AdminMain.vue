<script>
import {onMounted, onUnmounted, ref} from "vue";
import axios, {all} from "axios";
import EditUser from "@/main/vue/pages/Nutzerverwaltung/EditUser.vue";
import {useQuasar} from "quasar";
import {useRouter} from "vue-router";
import {useLoginStore} from "@/main/vue/stores/LoginStore";
import {getUserData, deleteUser} from "@/main/vue/api/admin";


const columns = [
    {
        name: 'username',
        required: true,
        label: 'E-Mail',
        align: 'left',
        field: row => row.username,
        format: val => `${val}`,
        sortable: true
    },
    {name: 'firstname', label: 'First Name', align: 'left', field: 'firstname', sortable: true},
    {name: 'firstname', label: 'Last Name', align: 'left', field: 'lastname', sortable: true},
    //{name: 'Roles', label: 'Roles', align: 'left', field: 'roles', sortable: true},
]
export const rows = ref([])
const selected = ref([])


export default {
    computed: {

    },
    components: {EditUser},
    setup() {
        const $q = useQuasar()
        const router = useRouter()
        const loginStore = useLoginStore()
        const smallScreen = ref(false);
        const largeScreen = ref(true);

        async function getData() {
            const data = getUserData()
            console.log(data)
            data.then(allUsers => {
                console.log(allUsers)
                allUsers.forEach(user => {
                    console.log(user)
                    rows.value.push({
                        username: user.username,
                        firstname: user.firstname,
                        lastname: user.lastname,
                    })
                })
            })
        }

        const checkScreenSize = () => {
            const screenSize = window.innerWidth;
            smallScreen.value = screenSize <= 500;
            largeScreen.value = screenSize > 500;
        };

        onMounted(async () => {
            checkScreenSize();
            window.addEventListener('resize', checkScreenSize);
            await getData();
        });

        onUnmounted(() => {
            window.removeEventListener('resize', checkScreenSize);
        });

        return {columns, rows, selected, router, smallScreen, largeScreen}
    },
    data() {

    },
    methods: {
        async deleteUsername(selectedUser) {
            if (selectedUser[0] === undefined) {
                this.$q.notify({
                    message: "Wähle einen Nutzer aus!",
                    timeout: 5000,
                });
            } else {
                deleteUser(selectedUser);
                deleteRow(selectedUser);
                console.log(selectedUser[0].username)
            }
        },
        unlockUser(selectedUser) {
            if (selectedUser[0] === undefined) {
                this.$q.notify({
                    message: "Wähle einen Nutzer aus!",
                    timeout: 5000,
                });
            } else {
                this.$q.notify({
                    message: `Nutzer mit der ID: '${selectedUser[0].username}' wurde freigeschaltet!`,
                    timeout: 5000,
                });
            }
        },
        async editUser(selectedUser, router) {
            if (selectedUser[0] === undefined) {
                this.$q.notify({
                    message: "Wähle einen Nutzer aus!",
                    timeout: 5000,
                });
            } else {
                const username = selectedUser[0].username;
                await router.push(`/admin/${username}/editUser`)
            }
        }
    }
}
</script>

<template>
    <div class="q-pa-md">
        <q-btn label="Freischalten" color="primary" @click=unlockUser(selected) class=""></q-btn>
        <q-btn label="Editieren" color="primary" @click=editUser(selected,router) class=""></q-btn>
        <q-btn label="Löschen" color="primary" @click="deleteUsername(selected)" class=""></q-btn>
        <q-table
            title="User Data"
            :rows="rows"
            :columns="columns"
            row-key="username"
            selection="single"
            v-model:selected="selected"
        />
    </div>
</template>
<style scoped>
tr.q-table__row--selected {
    background-color: #42b983 !important;
}
</style>

