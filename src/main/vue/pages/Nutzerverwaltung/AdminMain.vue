<script setup>
import {onMounted, onUnmounted, reactive, ref} from "vue";
import axios, {all} from "axios";
import EditUser from "@/main/vue/pages/Nutzerverwaltung/EditUser.vue";
import {useQuasar} from "quasar";
import {useRouter} from "vue-router";
import {useLoginStore} from "@/main/vue/stores/LoginStore";
import {getUserData, deleteUser, unlockUser} from "@/main/vue/api/admin";


const table = reactive( {
    filter: '',
    columns:[
        {
            name: 'username',
            required: true,
            label: 'E-Mail',
            align: 'left',
            field: 'username',
            format: val => `${val}`,
            sortable: true
        },
        {name: 'firstname', label: 'First Name', align: 'left', field: 'firstname', sortable: true},
        {name: 'lastname', label: 'Last Name', align: 'left', field: 'lastname', sortable: true},
        {name: 'roles', label: 'Roles', align: 'left', field: 'roles', sortable: true},
        {name: 'unlocked', label: 'Unlocked', align: 'left', field: 'unlocked', sortable: true},
    ],
    rows: []
});

const router = useRouter()
const smallScreen = ref(false);
const largeScreen = ref(true);
const showDialog = ref(false);
const showConfirmDialog = ref(false);
const currentRow = reactive({});
const rowToDelete = ref(null);
const {notify} = useQuasar();
const allRoles = ['Administrator', 'Datenverwalter', 'Bearbeiter', 'Prüfer', 'Freigabeberechtigter'];

async function getData() {
    const data = await getUserData()
    console.log(data)
    data.forEach((user) => {
        console.log("Region: " + user.region)
        if (!user.unlocked) {
            table.rows.push({
                username: user.username,
                firstname: user.firstname,
                lastname: user.lastname,
                roles: user.roles,
                unlocked: "Needs Activation!"
            })
        } else {
            table.rows.push({
                username: user.username,
                firstname: user.firstname,
                lastname: user.lastname,
                roles: user.roles,
            })
        }
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

const confirmDeleteUsername = (row) => {
    rowToDelete.value = row;
    showConfirmDialog.value = true;
}

async function deleteUsername(selectedUser) {
    const deleteSuccessful = await deleteUser(selectedUser);
    if (deleteSuccessful) {
        removeRow(selectedUser);
        notify({
            message: "Nutzer wurde erfolgreich entfernt!",
            timeout: 5000,
        })
    } else {
        notify({
            message: "Es gab ein Problem beim entfernen des Nutzer!",
            timeout: 5000,
        })
    }
    showConfirmDialog.value = false;
    showDialog.value = false;
}

async function unlockCurrentUser(selectedUser) {
    console.log(selectedUser)
    const unlockSuccessful = await unlockUser(selectedUser);
    if (unlockSuccessful) {
        router.go(0);
    } else {
        showDialog.value = false;
        console.log("Problem")
        notify({
            message: "Es gab ein Problem mit dem freischalten des Nutzer!",
            timeout: 5000,
        });
    }
}

async function editUser() {
    const username = currentRow.username;
    await router.push(`/admin/${username}/editUser`)
    showDialog.value = false;
}

const rowClick = (evt, rowData) => {
    console.log(typeof rowData)
    console.log(rowData)
    console.log(currentRow)
    Object.assign(currentRow, rowData);
    showDialog.value = true;
}

const removeRow = (selectedUser) => {
    const index = table.rows.findIndex(row => row.username === selectedUser);
    if (index !== -1) {
        table.rows.splice(index, 1);
    }
}

</script>

<template>
    <div class="q-pa-md extra-margin">
        <q-input borderless dense debounce="300" v-model="table.filter" placeholder="Search">
            <template v-slot:append>
                <q-icon name="search" />
            </template>
        </q-input>
        <q-table
            v-show="!smallScreen && largeScreen"
            class="my-sticky-header-table"
            flat bordered
            title="User Data"
            :rows="table.rows"
            :columns="table.columns"
            row-key="username"
            :filter="table.filter"
            @row-click="rowClick"
        >
            <template v-slot:body="props">
                <q-tr :props="props" @click="rowClick($event, props.row)">
                    <q-td key="username" :props="props">
                        {{ props.row.username }}
                    </q-td>
                    <q-td key="firstname" :props="props">
                        {{ props.row.firstname }}
                    </q-td>
                    <q-td key="lastname" :props="props">
                        {{ props.row.lastname }}
                    </q-td>
                    <q-td key="roles" :props="props">
                        {{ props.row.roles }}
                    </q-td>
                    <q-td key="unlocked" :props="props">
                        {{ props.row.unlocked }}
                    </q-td>
                </q-tr>
            </template>
        </q-table>

        <q-table
            v-show="!largeScreen && smallScreen"
            class="my-sticky-header-table2"
            flat bordered
            grid
            grid-header
            hide-header
            title="User Data"
            :rows="table.rows"
            :columns="table.columns"
            row-key="username"
            :filter="table.filter"
            @row-click="rowClick"
        >
            <template v-slot:header="props">
                <q-tr :props="props">
                    <q-th :key="props.cols[0].name" :props="props">
                        {{ props.cols[0].label }}
                    </q-th>
                    <q-th :key="props.cols[2].name" :props="props">
                        {{ props.cols[2].label }}
                    </q-th>
                    <q-th :key="props.cols[4].name" :props="props">
                        {{ props.cols[4].label }}
                    </q-th>
                </q-tr>
            </template>
            <template v-slot:item="props">
                <div class="q-pa-xs col-xs-12 col-sm-6 col-md-4 col-lg-3">
                    <q-card bordered flat @click="rowClick($event, props.row)">
                        <q-list dense>
                            <q-item v-for="col in props.cols" :key="col.name">
                                <q-item-section>
                                    <q-item-label caption>{{ col.label }}</q-item-label>
                                </q-item-section>
                                <q-item-section>
                                    <q-item-label>{{ props.row[col.field] }}</q-item-label>
                                </q-item-section>
                            </q-item>
                        </q-list>
                    </q-card>
                </div>
            </template>
        </q-table>

        <q-dialog v-model="showDialog">
            <q-card>
                <q-card-section>
                    <div class="option-button" @click="editUser">Editieren</div>
                    <div v-if="currentRow.unlocked" class="option-button" @click="unlockCurrentUser(currentRow.username)">Freischalten</div>
                    <div v-else-if="!currentRow.unlocked" class="option-button" :class="{ 'disabled-button': true }">Freischalten</div>
                    <div class="option-button" @click="confirmDeleteUsername(currentRow)">Löschen</div>
                </q-card-section>

                <q-card-section>
                    <q-btn flat label="Schließen" color="primary" @click="showDialog = false"></q-btn>
                </q-card-section>
            </q-card>
        </q-dialog>

        <q-dialog v-model="showConfirmDialog">
            <q-card>
                <q-card-section>
                    <div class="text-h6">Bestätigung</div>
                </q-card-section>
                <q-card-section>
                    Wollen Sie diesen Nutzer wirklich entfernen?
                </q-card-section>
                <q-card-section>
                    <q-btn flat label="Abbrechen" color="positive" @click="showConfirmDialog = false"></q-btn>
                    <q-btn flat label="Löschen" color="negative" @click="deleteUsername(currentRow.username)"></q-btn>
                </q-card-section>
            </q-card>
        </q-dialog>
    </div>
</template>

<style scoped>
.option-button {
    cursor: pointer;
    padding: 8px;
    font-size: 16px;
    transition-duration: 0.4s;
}
.extra-margin {
    margin-top: 20px;
    margin-left: 5px;
    margin-right: 5px;
}

.option-button:hover {
    background-color: #e6e4e4;
}

.handleButton {
    margin-top: 20px;

}

.disabled-button {
    pointer-events: none;
    opacity: 0.5;
    cursor: not-allowed;
}
</style>

<style lang="sass">
.my-sticky-header-table
    height: 500px
    .q-table__top,
    .q-table__bottom,
    thead tr:first-child th
        background-color: $blue-grey-5
    thead tr th
        position: sticky
        z-index: 0
    thead tr:first-child th
        top: 0
    &.q-table--loading thead tr:last-child th
        top: 48px
    tbody
        scroll-margin-top: 48px

.my-sticky-header-table2
    .q-table__top,
    .q-table__bottom,
    thead tr:first-child th
        background-color: $blue-grey-5
</style>

