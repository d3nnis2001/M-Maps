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
                        await router.push('password?email='+email.value);
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
    <div class="full-height-center">
        <div class="content-container">
            <div class="items-center">
                <q-card class="q-pa-md">
                    <q-card-section class="inner-card">
                    <div class="row-auto text-align extra-padding">
                        <img src="../../../resources/db-logo.png" alt="Nicht verfügbar">
                    </div>
                    <div class="row-auto extra-padding">
                        <div class="text-h4 text-align ">Einloggen</div>
                    </div>
                    <div class="row-auto extra-padding">
                        <div class="rectangle"></div>
                    </div>
                    <div class="row-auto extra-padding">
                        <div class="text-align">
                            Bitte geben Sie Ihre E-Mail-Adresse ein
                        </div>
                    </div>
                    <div class="row-auto text-align extra-padding">
                        <q-input class="email-input extra-padding" v-model="email" label="E-Mail Adresse"/>
                        <div class="text-align extra-padding" @click="navigateRegister">
                            Registrieren
                        </div>
                        <q-btn label="Anmelden" @click="login()" color="primary" class=""></q-btn>
                    </div>
                    <div class="bg-grey-4 impressum-padding">
                        <div class="text-black text-align justify-center">Impressum</div>
                    </div>
                    </q-card-section>
                </q-card>
            </div>
        </div>
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

.impressum-padding {
    padding-bottom: 200px;
}

.extra-padding {
    padding-bottom: 20px;
}

.content-container {
    width: calc(100% - 40px);
    max-width: 500px;
    padding: 20px;
    box-shadow: 0 0 10px rgba(0,0,0,0.6);
    background: white;
}

.email-input {
    width: 100%;
    max-width: 300px;
    margin: 0 auto;
}

.text-align {
    text-align: center;
}

.rectangle {
    width: 72px;
    height: 7px;
    background-color: rgba(236, 0, 22, 1);
    border-radius: 20px;
    margin: auto;
}
</style>
