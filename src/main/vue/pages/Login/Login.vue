<script>
import {ref} from 'vue'
import {useLoginStore} from "../../stores/LoginStore"
import {useQuasar} from 'quasar'
import {useRouter} from 'vue-router'

export default {
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
                <div class="row-auto text-align padding-md">
                    <img src="../../../resources/db-logo.png" alt="Nicht verfügbar">
                </div>
                <h1 class="text-h4 text-align text-bold">Einloggen</h1>
                <div class="row-auto padding-md">
                    <div class="rectangle"></div>
                </div>
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
                <div class="bg-grey-4">
                    <div class="pading-all">
                        <div class="text-black text-align justify-center">Impressum</div>
                    </div>
                    <div class="divider bg-grey-6"></div>
                    <div class="pading-all">
                        <div class="text-black text-align justify-center">DB Systemtechnik GmbH</div>
                    </div>
                </div>
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

.pading-all {
    padding: 32px;
}

.divider {
    width: calc(100% - 32px);
    height: 2px;
    border-radius: 16px;
    margin: auto;
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

.rectangle {
    width: 72px;
    height: 8px;
    background-color: rgba(236, 0, 22, 1);
    border-radius: 16px;
    margin: auto;
}
</style>
