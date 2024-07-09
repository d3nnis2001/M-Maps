<script>
import {ref} from 'vue'
import {useQuasar} from 'quasar'
import {resetPassword, setPasswordNew} from "@/main/vue/api/auth";
import {useRoute, useRouter} from "vue-router";
import StandardCard from "@/main/vue/pages/Login/StandardCard.vue";
import Impressum from "@/main/vue/pages/Login/ImpressumTemplate.vue";
import Description from "@/main/vue/pages/Login/Description.vue";
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";
import Heading from "@/main/vue/pages/Login/Heading.vue";

export default {
    components: {Heading, StandardInput, Description, Impressum, StandardCard},
    methods: {resetPassword},
    setup() {
        const $q = useQuasar()
        const password = ref('')
        const route = useRoute()
        const passAgain = ref('')
        const router = useRouter()

        async function setNewPassword() {
            if (password !== null && passAgain !== null &&
                password.value !== "" && password.value === passAgain.value) {
                const token = route.query.token;
                const email = route.query.email;
                console.log(password.value, token)
                const response = setPasswordNew(email, password.value, token)
                if (response) {
                    setTimeout(2000);
                    await router.push("login")
                    $q.notify({
                        type: 'positive',
                        message: 'Successfully reseted your password',
                        caption: 'You can now login with your new password'
                    });
                } else {
                    $q.notify({
                        type: 'negative',
                        message: 'Something went wrong when reseting your password',
                        caption: 'We apologize. Contact our support'
                    });
                }
            } else {
                $q.notify({
                    type: 'negative',
                    message: 'Wrong input',
                    caption: 'You have to fill in both password inputs with the same password'
                });
            }
        }
        return {password, passAgain, setNewPassword}
    }
}

</script>

<template>
    <StandardCard>
        <Heading>Passwort neu setzen</Heading>
        <Description>Bitte geben Sie Ihr neues Passwort ein.</Description>
        <div class="row-auto text-align padding-md">
            <StandardInput type="password" v-model="password" label="Passwort"></StandardInput>
            <StandardInput type="password" v-model="passAgain" label="Passwort wiederholen"></StandardInput>
            <div class="padding-top">
                <q-btn label="Passwort neu setzen" @click="setNewPassword" color="primary" class=""></q-btn>
            </div>
        </div>
        <Impressum></Impressum>
    </StandardCard>
</template>

<style scoped>
.padding-top {
    padding-top: 16px;
}

.padding-md {
    padding-bottom: 32px;
}

.text-align {
    text-align: center;
}
</style>
