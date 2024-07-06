<script>
import {onMounted, ref, watch} from 'vue';
import { useRoute } from 'vue-router';
import router from "@/main/vue/router";
import {getUserByUsername, getUserData, updateRoles} from "@/main/vue/api/admin";
import {useQuasar} from "quasar";

export default {
    setup () {
        const route = useRoute();
        const currentUser = ref({user: []})
        const username = route.params.username.toString();
        const firstname = ref('')
        const lastname = ref('')
        const userRegion = ref({regions:[]})
        const allRoles = ref(['Admin', 'Datenverwalter', 'Bearbeiter', 'Prüfer']);
        const updatedRoles = ref({selected_roles: []})
        const {notify} = useQuasar();

        onMounted( async  () => {
            currentUser.value = await getUserByUsername(username);
            if (currentUser.value) {
                firstname.value = currentUser.value.firstname;
                lastname.value = currentUser.value.lastname;
                console.log(currentUser.value.region)
                if (currentUser.value.region !== undefined) {
                    currentUser.value.region.forEach((region) => {
                        console.log(region)
                        userRegion.value.regions.push(region)
                    })
                }
                if (currentUser.value.roles !== null) {
                    currentUser.value.roles.forEach((role) => {
                        console.log(role)
                        updatedRoles.value.selected_roles.push(role)
                    })
                }
            }
        })

        const abort = () => {
            router.push(`/admin`);
        };

        async function saveRoles() {
            console.log(updatedRoles.value.selected_roles)
            updatedRoles.value.selected_roles.forEach((data) => {
                console.log(data)
            })
            const update = await updateRoles(username, updatedRoles.value.selected_roles)
            if (update) {
                router.push(`/admin`);
                notify({
                    message: "Die Rollen wurden erfolgreich gespeichert!",
                    timeout: 5000,
                })
            } else {
                notify({
                    message: "Es gab ein Problem mit dem Update der Rollen!",
                    timeout: 5000,
                });
            }
        }

        return {
            route,
            username,
            firstname,
            lastname,
            allRoles,
            updatedRoles,
            abort,
            saveRoles,
        }
    }
}
</script>

<template>
    <div class="outline">
        <div class="outer-layer">
            <div class="row justify-center" >
                <h4 style="margin-right: 10px">Nutzerkonfiguration </h4>
            </div>
        </div>

        <q-separator size="2px" color="primary" style="margin-top: 20px "></q-separator>

        <div class="row">
            <div class="col">
                <div class="row" >
                    <h6 style="margin-right: 10px" >Nutzer ID: </h6>
                    <h6 style="font-weight: bold">{{ username }}</h6>
                </div>
                <div class="row">
                    <h6 style="margin-right: 10px">Vorname: </h6>
                    <h6 style="font-weight: bold">{{ firstname }}</h6>
                </div>
                <div class="row">
                    <h6 style="margin-right: 10px">Nachname: </h6>
                    <h6 style="font-weight: bold">{{ lastname }}</h6>
                </div>
            </div>

            <div class="col">
                <div class="row" >
                    <h6 style="margin-right: 10px" >Region: </h6>
                    <!--
                    <div class="column" v-for="region in userRegion">
                        <q-item>
                            {{ region }}
                        </q-item>
                    </div>
                        -->
                </div>
            </div>

            <div class="q-pa-lg">
                <div class="checkbox-container" v-for="role in allRoles">
                    <q-checkbox
                        :label="role"
                        v-model="updatedRoles.selected_roles"
                        :val="role"
                        size="lg"
                    />
                </div>
                <p></p>
            </div>
        </div>



        <div class="q-gutter-sm row justify-end">
            <q-btn
                class="handleButton"
                style="width: 100%; max-width: 218px"
                size="16px"
                no-caps rounded label="Abbrechen"
                color="primary"
                @click=abort >
            </q-btn>
            <q-btn
                class="handleButton"
                style="width: 100%; max-width: 218px"
                size="16px"
                no-caps rounded label="Speichern"
                color="primary"
                @click=saveRoles >
            </q-btn>
        </div>
    </div>
</template>


<style>
.checkbox-container .q-checkbox {
    font-size: 20px;
}
.extra-mar {
    margin-right: 20px;
}
</style>

