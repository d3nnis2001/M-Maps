<script setup>
import {onMounted, onUnmounted, ref} from 'vue';
import { useRoute } from 'vue-router';
import {getUserByUsername} from "@/main/vue/api/userprofile";
import router from "@/main/vue/router";
import {useQuasar} from "quasar";

const smallScreen = ref(false);
const largeScreen = ref(true);
const route = useRoute();
const currentUser = ref({user: []})
const username = route.params.username.toString();
const firstname = ref('')
const lastname = ref('')
const userRegion = ref({regions:[]})
const regions = [''];

const checkScreenSize = () => {
    const screenSize = window.innerWidth;
    smallScreen.value = screenSize <= 500;
    largeScreen.value = screenSize > 500;
};

onMounted(async () => {
    console.log(username)
    checkScreenSize();
    window.addEventListener('resize', checkScreenSize);
    currentUser.value = await getUserByUsername(username);
    console.log(currentUser.value)
    if (currentUser.value) {
        firstname.value = currentUser.value.firstname;
        lastname.value = currentUser.value.lastname;
        if (currentUser.value.region !== undefined) {
            currentUser.value.region.forEach((region) => {
                console.log(region)
                userRegion.value.regions.push(region)
            })
        }
        if (currentUser.value.roles !== null && currentUser.value.roles !== undefined) {
            currentUser.value.roles.forEach((role) => {
                console.log(role)
                updatedRoles.value.selected_roles.push(role)
            })
        }
    }
});

onUnmounted(() => {
    window.removeEventListener('resize', checkScreenSize);
});
</script>

<template>
    <q-page padding>
        <div class="row justify-center">
            <div class="col-12 col-md-8">
                <q-card>
                    <q-card-section>
                        <div class="text-h6">User Data Overview</div>
                    </q-card-section>
                    <q-card-section>
                        <q-list>
                            <q-item>
                                <q-item-section>
                                    <q-item-label>Username</q-item-label>
                                </q-item-section>
                                <q-item-section side>
                                    <q-item-label>{{ username }}</q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item>
                                <q-item-section>
                                    <q-item-label>Firstname</q-item-label>
                                </q-item-section>
                                <q-item-section side>
                                    <q-item-label>{{ firstname }}</q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item>
                                <q-item-section>
                                    <q-item-label>Lastname</q-item-label>
                                </q-item-section>
                                <q-item-section side>
                                    <q-item-label>{{ lastname }}</q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item>
                                <q-item-section>
                                    <q-item-label>Roles</q-item-label>
                                </q-item-section>
                                <q-item-section side>
                                    <q-item-label>{{ roles }}</q-item-label>
                                </q-item-section>
                            </q-item>
                            <q-item>
                                <q-item-section>
                                    <q-item-label>Regions</q-item-label>
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
</template>

<style scoped>

</style>
