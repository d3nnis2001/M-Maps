<script>
import {onMounted, onUnmounted, ref} from 'vue';
import { useRoute } from 'vue-router';
import router from "@/main/vue/router";
import {getUserByUsername, updateRoles} from "@/main/vue/api/admin";
import {useQuasar} from "quasar";

export default {
    setup () {
        const smallScreen = ref(false);
        const largeScreen = ref(true);
        const route = useRoute();
        const currentUser = ref({user: []})
        const username = route.params.username.toString();
        const firstname = ref('')
        const lastname = ref('')
        const userRegion = ref({regions:[]})
        const allRoles = ref(['Administrator', 'Datenverwalter', 'Bearbeiter', 'Prüfer']);
        const updatedRoles = ref({selected_roles: []})
        const {notify} = useQuasar();

        const checkScreenSize = () => {
            const screenSize = window.innerWidth;
            smallScreen.value = screenSize <= 500;
            largeScreen.value = screenSize > 500;
        };

        onMounted( async  () => {
            checkScreenSize();
            window.addEventListener('resize', checkScreenSize);
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

        onUnmounted(() => {
            window.removeEventListener('resize', checkScreenSize);
        });

        const abort = () => {
            router.push(`/admin`);
        };

        async function saveRoles() {
            updatedRoles.value.selected_roles.forEach((data) => {
                console.log(data)
            })
            const update = await updateRoles(username, updatedRoles.value.selected_roles)
            if (update) {
                await router.push(`/admin`);
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
            smallScreen,
            largeScreen,
        }
    }
}
</script>

<template>
    <q-page
        padding
        v-show="!smallScreen && largeScreen"
        flat bordered
    >

        <div class="outline">
            <div class="outer-layer">
                <div class="row justify-center" >
                    <h4 style="margin-right: 10px">Nutzerkonfiguration </h4>
                </div>
            </div>

            <q-separator size="2px" color="primary" style="margin-top: 35px "></q-separator>

            <div class="row">
                <div class="col">
                    <div class="row" >
                        <h6 style="margin-right: 40px" >Nutzer ID: </h6>
                        <h6 style="font-weight: bold">{{ username }}</h6>
                    </div>
                    <div class="row">
                        <h6 style="margin-right: 40px">Vorname: </h6>
                        <h6 style="font-weight: bold">{{ firstname }}</h6>
                    </div>
                    <div class="row">
                        <h6 style="margin-right: 25px">Nachname: </h6>
                        <h6 style="font-weight: bold">{{ lastname }}</h6>
                    </div>
                </div>

                <div class="q-pa-lg" style="display: grid; grid-auto-flow: row; margin-top: 25px">
                    <div class="checkbox-container" style="margin-right: 50px" v-for="role in allRoles">
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
    </q-page>

    <q-page
        padding
        v-show="!largeScreen && smallScreen"
        flat bordered
    >
        <div class="outline">
            <div class="outer-layer">
                <div class="row justify-center q-pt-md">
                    <h4 style="margin-right: 10px">Nutzerkonfiguration</h4>
                </div>
            </div>

            <q-separator size="2px" color="primary" style="margin-top: 35px"></q-separator>

            <div class="row q-col-gutter-md">
                <div class="col-12 col-md-6">
                    <div class="row">
                        <h6 style="margin-right: 40px">Nutzer ID:</h6>
                        <h6 style="font-weight: bold">{{ username }}</h6>
                    </div>
                    <div class="row">
                        <h6 style="margin-right: 40px">Vorname:</h6>
                        <h6 style="font-weight: bold">{{ firstname }}</h6>
                    </div>
                    <div class="row">
                        <h6 style="margin-right: 25px">Nachname:</h6>
                        <h6 style="font-weight: bold">{{ lastname }}</h6>
                    </div>
                </div>

                <div class="col-12 col-md-6 q-pl-md">
                    <div class="checkbox-container" v-for="role in allRoles">
                        <q-checkbox
                            :label="role"
                            v-model="updatedRoles.selected_roles"
                            :val="role"
                            size="lg"
                        />
                    </div>
                </div>
            </div>

            <div class="q-gutter-sm row justify-end q-pt-md">
                <q-btn
                    class="handleButton"
                    size="16px"
                    no-caps rounded label="Abbrechen"
                    color="primary"
                    @click=abort
                />
                <q-btn
                    class="handleButton"
                    size="16px"
                    no-caps rounded label="Speichern"
                    color="primary"
                    @click=saveRoles
                />
            </div>
        </div>
    </q-page>
</template>


<style>
.checkbox-container {
    font-size: 20px;
}
</style>

