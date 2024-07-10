<script setup>
import {onMounted, onUnmounted, ref} from 'vue';
import { useRoute } from 'vue-router';
import {getUserByUsername} from "@/main/vue/api/userprofile";
import {useUserStore} from "@/main/vue/stores/UserStore";

const userStore = useUserStore()
const smallScreen = ref(false);
const largeScreen = ref(true);
const route = useRoute();
const currentUser = ref({user: []})
const username = userStore.username;
const firstname = ref('')
const lastname = ref('')
const userRegion = ref({regions:[]})
const regions = [''];
const roles = ref({selected_roles: []})
const allRoles = ['Administrator', 'Datenverwalter', 'Bearbeiter', 'Prüfer'];

const checkScreenSize = () => {
    const screenSize = window.innerWidth;
    smallScreen.value = screenSize <= 500;
    largeScreen.value = screenSize > 500;
};

onMounted(async () => {
    checkScreenSize();
    window.addEventListener('resize', checkScreenSize);
    currentUser.value = await getUserByUsername(username);
    console.log(currentUser)
    if (currentUser.value) {
        firstname.value = currentUser.value.firstname;
        lastname.value = currentUser.value.lastname;
        console.log("Region: " + currentUser.value.region)
        if (currentUser.value.region !== undefined) {
            currentUser.value.region.forEach((region) => {
                console.log(region)
                userRegion.value.regions.push(region)
            })
        }
        if (currentUser.value.roles !== null && currentUser.value.roles !== undefined) {
            currentUser.value.roles.forEach((role) => {
                console.log(role)
                roles.value.selected_roles.push(role)
            })
        }
    }
});

onUnmounted(() => {
    window.removeEventListener('resize', checkScreenSize);
});
</script>

<template>
    <q-page
        padding
        v-show="!smallScreen && largeScreen"
        flat bordered
    >
        <div class="row justify-center">
            <div class="col-12 col-md-8">
                <q-card>
                    <q-card-section>
                        <div class="text-h6">Nutzer Informationen</div>
                    </q-card-section>
                    <q-card-section>
                        <q-list>
                            <q-item>
                                <q-item-section>
                                    <q-item-label>Nutzer ID</q-item-label>
                                </q-item-section>
                                <q-item-section side>
                                    <q-item-label>{{ username }}</q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item>
                                <q-item-section>
                                    <q-item-label>Vorname</q-item-label>
                                </q-item-section>
                                <q-item-section side>
                                    <q-item-label>{{ firstname }}</q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item>
                                <q-item-section>
                                    <q-item-label>Nachname</q-item-label>
                                </q-item-section>
                                <q-item-section side>
                                    <q-item-label>{{ lastname }}</q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item>
                                <q-item-section>
                                    <q-item-label>Rollen</q-item-label>
                                </q-item-section>
                                <q-item-section side>
                                    <q-item-label>{{ roles.selected_roles.sort().join(', ') }}</q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item>
                                <q-item-section>
                                    <q-item-label>Regionen</q-item-label>
                                </q-item-section>
                                <q-item-section side>
                                    <q-item-label>{{ regions }}</q-item-label>
                                </q-item-section>
                            </q-item>
                        </q-list>
                    </q-card-section>
                </q-card>
            </div>
        </div>
    </q-page>

    <q-page
        padding
        v-show="!largeScreen && smallScreen"
        flat bordered
    >
        <q-card bordered>
            <q-card-section>
                <q-list class="q-pa-xs col-xs-12 col-sm-6 col-md-4 col-lg-3">
                    <q-item>
                        <q-item-section>
                            <q-item-label>Nutzer ID</q-item-label>
                        </q-item-section>
                        <q-item>
                            <q-item-section side>
                                <q-item-label>{{ username }}</q-item-label>
                            </q-item-section>
                        </q-item>
                    </q-item>
                    <q-item>
                        <q-item-section>
                            <q-item-label>Vorname</q-item-label>
                        </q-item-section>
                        <q-item>
                            <q-item-section side>
                                <q-item-label>{{ firstname }}</q-item-label>
                            </q-item-section>
                        </q-item>
                    </q-item>
                    <q-item>
                        <q-item-section>
                            <q-item-label>Nachname</q-item-label>
                        </q-item-section>
                        <q-item>
                            <q-item-section side>
                                <q-item-label>{{ lastname }}</q-item-label>
                            </q-item-section>
                        </q-item>
                    </q-item>
                    <q-item class="q-pt-md">
                        <q-item-section>
                            <q-item-label>Rollen</q-item-label>
                        </q-item-section>
                        <q-item>
                            <q-item-section side  style="max-width: 100px">
                                <q-item-label>{{ roles.selected_roles.sort().join(', ') }}</q-item-label>
                            </q-item-section>
                        </q-item>
                    </q-item>
                    <q-item>
                        <q-item-section>
                            <q-item-label>Regionen</q-item-label>
                        </q-item-section>
                        <q-item>
                            <q-item-section side>
                                <q-item-label>{{ regions.join(', ') }}</q-item-label>
                            </q-item-section>
                        </q-item>
                    </q-item>
                </q-list>
            </q-card-section>
        </q-card>
    </q-page>
</template>

<style>

</style>
