<script>
import {ref} from 'vue'
import {useLoginStore} from "../../stores/LoginStore"
import {useQuasar} from 'quasar'
import {useRouter} from 'vue-router'
import Impressum from "@/main/vue/pages/Login/Impressum.vue";
import Heading from "@/main/vue/pages/Login/Heading.vue";

export default {
    components: {Heading, Impressum},
    setup() {
        const $q = useQuasar()
        const router = useRouter()
        const loginStore = useLoginStore()

        const email = ref('')

        async function login() {
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
    <div class="full-height-center bg-grey-2 padding-xl">
        <q-card class="q-pa-md content-container items-center" flat>
            <q-card-section class="inner-card">
                <Heading>Einloggen</Heading>
                <div class="row-auto padding-sm">
                    <div class="text-align text-grey-9">
                        Bitte geben Sie Ihre E-Mail-Adresse ein.
                    </div>
                </div>
                <div class="row-auto text-align padding-xl">
                    <q-input class="email-input padding-md" bg-color="bg-grey-1" filled color="black" v-model="email"
                             label="E-Mail Adresse"/>
                    <q-btn label="Registrieren" outline color="dark" @click="navigateRegister">
                    </q-btn>
                    <span class="padding-right"></span>
                    <q-btn label="Anmelden" @click="login()" color="dark" class=""></q-btn>
                </div>
                <Impressum></Impressum>
            </q-card-section>
        </q-card>
    </div>
</template>

<style scoped>

.full-height-center {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    width: 100%;
}

.padding-sm {
    padding-bottom: 16px;
}

.padding-md {
    padding-bottom: 32px;
}

.padding-xl {
    padding-bottom: 64px;
}

.padding-right {
    padding-right: 8px;
}

.content-container {
    width: calc(100% - 48px);
    max-width: 384px;
    padding: 16px;
    height: 100%
}

.email-input {
    width: 100%;
    max-width: 288px;
    margin: 0 auto;
}

.text-align {
    text-align: center;
}
</style>
