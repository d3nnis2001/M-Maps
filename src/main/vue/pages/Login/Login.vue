<script>
import {ref} from 'vue'
import {useLoginStore} from "../../stores/LoginStore"
import {useUserStore} from "../../stores/UserStore"
import {useQuasar} from 'quasar'
import {useRouter} from 'vue-router'
import Impressum from "@/main/vue/pages/Login/Impressum.vue";
import Heading from "@/main/vue/pages/Login/Heading.vue";
import Description from "@/main/vue/pages/Login/Description.vue";
import StandardInput from "@/main/vue/pages/Login/StandardInput.vue";
import StandardCard from "@/main/vue/pages/Login/StandardCard.vue";

export default {
    components: {StandardCard, StandardInput, Description, Heading, Impressum},
    setup() {
        const $q = useQuasar()
        const router = useRouter()
        const loginStore = useLoginStore()
        const userStore = useUserStore()
        const email = ref('')

        async function login() {
            console.log("TEST: login")
            if (!(email.value).includes("@")) {
                $q.notify({
                    type: 'negative',
                    message: 'Mistake',
                    caption: 'This seems not to be a valid email!'
                });
            } else {
                try {
                    const emailExists = await loginStore.checkEmail(email.value);
                    if (emailExists) {
                        await router.push('password?email=' + email.value);
                    } else {
                        $q.notify({
                            type: 'negative',
                            message: 'Login failed',
                            caption: 'Email is not registered'
                        });
                    }
                } catch (error) {
                    console.error('Error during login:', error);
                    $q.notify({
                        type: 'negative',
                        message: 'Login failed',
                        caption: 'An unexpected error occurred'
                    });
                }
            }
        }

        function navigateRegister() {
            router.push("/register")
        }

        return {email, login, navigateRegister}
    }
}

</script>
<template>
    <StandardCard>
        <Heading>Anmelden</Heading>
        <Description>Bitte geben Sie Ihre E-Mail Adresse ein.</Description>
        <div class="row-auto text-align padding-xl">
            <StandardInput label="E-Mail Adresse" v-model="email"></StandardInput>
            <span class="padding-right">
                        <q-btn label="Registrieren" outline color="primary" @click="navigateRegister"></q-btn>
                    </span>
            <q-btn label="Anmelden" @click="login()" color="primary" class=""></q-btn>
        </div>
        <Impressum></Impressum>
    </StandardCard>
</template>

<style scoped>

.padding-xl {
    padding-bottom: 64px;
}

.padding-right {
    padding-right: 8px;
}

.text-align {
    text-align: center;
}
</style>
