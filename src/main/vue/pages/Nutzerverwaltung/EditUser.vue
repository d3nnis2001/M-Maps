<script>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import router from "@/main/vue/router";
import {getUserById} from "@/main/vue/api/admin";

export default {
    setup () {
        const route = useRoute();
        const username = route.params.username;
        const firstname = ref('')
        const lastname = ref('')
        const allRoles = ref(['Admin', 'Datenverwalter', 'Bearbeiter', 'Prüfer']);
        const roles = ref({selected_roles: []})

        onMounted( async  () => {
            console.log(username)
            const user = await getUserById(username);
            firstname.value = user.firstname
            console.log(firstname.value)
            lastname.value =user.lastname
        })

        const abort = () => {
            router.push(`/admin`);
        };

        const saveRoles = (roles) => {

        }

        return {
            route,
            username,
            firstname,
            lastname,
            allRoles,
            roles,
            abort,
            saveRoles,
            /*
            group: ref([]),
            options: [
                { label: 'Admin', value: 'admin' },
                { label: 'Datenverwalter', value: 'datenverwalter', },
                { label: 'Bearbeiter', value: 'bearbeiter', },
                { label: 'Prüfer', value: 'prüfer', },
            ]

             */
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

            <div class="q-pa-lg">
                <p style="margin-right: 5px">Rollen: </p>
                <!--
                <q-option-group
                    :options="options"
                    type="checkbox"
                    v-model="roles.selected_roles"
                    :val="allRoles"
                />
                -->
                {{roles}}
                <div v-for="allRoles in allRoles">
                    <q-checkbox
                        :label="allRoles"
                        v-model="roles.selected_roles"
                        :val="allRoles"
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


<style scoped>

</style>

