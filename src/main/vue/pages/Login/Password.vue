<script setup>
import {ref} from 'vue'
import {useLoginStore} from "../../stores/LoginStore"
import {useQuasar} from 'quasar'
import {useRouter} from 'vue-router'
import {useRoute} from 'vue-router';
import StandardCard from "@/main/vue/pages/Login/StandardCard.vue";
import Heading from "@/main/vue/pages/Login/Heading.vue";
import Description from "@/main/vue/pages/Login/Description.vue";
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";
import Impressum from "@/main/vue/pages/Login/Impressum.vue";
import {useUserStore} from "../../stores/UserStore";

const $q = useQuasar()
const router = useRouter()
const route = useRoute();
const loginStore = useLoginStore()
const userStore = useUserStore()

const passwordVar = ref('')

async function password() {
    const email = route.query.email;
    const success = await loginStore.checkPassword(email, passwordVar.value)
    if (success) {
        userStore.requestToken( {
            username: email,
            password: passwordVar.value
        }).then( () => {
            console.log("Success")
            userStore.decodeToken();
            console.log("Test")
            router.push("map")
        }).catch( () => {
            $q.notify({
                type: 'negative',
                message: 'Something went wrong with the token',
                caption: 'Password does not match email'
            });
        })

    } else {
        $q.notify({
            type: 'negative',
            message: 'Wrong password',
            caption: 'Password does not match email'
        });
    }

}

function navigateForgotPassword() {
    const email = route.query.email;
    router.push({path: "/forgotPassword", query: {email: email}})
}

// https://www.figma.com/file/KE0rSr4lUzKzWQp7nY2jDZ/M-Maps-Prototyp?type=design&node-id=2-8&mode=design&t=atOavQXYG9PWr9Zr-4
</script>

<template>
    <StandardCard>
        <Heading>Passwort eingeben</Heading>
        <Description>Bitte geben Sie Ihr Passwort ein.</Description>
        <div class="row-auto text-align padding-xl">
            <StandardInput type="password" v-model="passwordVar" label="Passwort"></StandardInput>
            <div class="padding-xs">
                <q-btn class="text-align" flat color="primary" @click=navigateForgotPassword()>
                Passwort Vergessen?
                </q-btn>
            </div>
            <q-btn label="Anmelden" @click="password()" color="primary" class=""></q-btn>
        </div>
        <Impressum></Impressum>
    </StandardCard>
</template>

<style scoped>

.padding-xl {
    padding-bottom: 64px;
}

.padding-xs {
    padding-bottom: 8px;
}

.text-align {
    text-align: center;
}
</style>
