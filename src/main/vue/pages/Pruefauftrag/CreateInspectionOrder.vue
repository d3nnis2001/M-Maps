<script>
import { ref } from 'vue'

export default {
    setup () {
        return {
            text: ref(''),
            ph: ref(''),
            dense: ref(false)
        }
    }
}
</script>

<template>
    <div class="q-pa-md">
        <div class="q-gutter-y-md column" style="max-width: 300px">
            <q-toggle v-model="dense" label="Dense QInput" />

            <q-input outlined v-model="text" label="StreckenID" :dense="dense" />

            <q-input outlined v-model="text" label="Startort" :dense="dense" />

            <q-input outlined v-model="text" label="Endort" :dense="dense" />

            <q-input outlined v-model="text" label="Von" :dense="dense">
                <template v-slot:prepend>
                    <q-icon name="event" />
                </template>
            </q-input>

            <q-input outlined v-model="text" label="Bis" :dense="dense">
                <template v-slot:prepend>
                    <q-icon name="event" />
                </template>
            </q-input>

            <q-input outlined v-model="text" label="Fachabteilung" :dense="dense" />

            <q-input outlined v-model="text" label="Zu überprüfende Messdaten" :dense="dense" />

            <q-input
                v-model="text" label="Bemerkungen" :dense="dense"
                filled
                autogrow
            />
            <q-btn label="Erstellen" @click="$router.push('/inspectionOrderOverview')" color="primary" class=""></q-btn>
            <q-btn label="Abbrechen" @click="$router.push('/inspectionOrderOverview')" color="primary" class=""></q-btn>
        </div>
    </div>

</template>


<style scoped>

</style>

<!--
<div class="basic-center">
        <div class="rec1">
            <div class="align-basic">
                <h4>Prüfkoordinaten/Streckenabschnitt</h4>
                <q-input class="extra-mar" outlined v-model="streckenabschnitt" label="Streckenabschnitt" />
            </div>
            <div class="align-basic">
                <h4>Zeitraum</h4>
                <div class="align-mult">
                    <q-input filled v-model="date" mask="date" :rules="['date']">
                        <template v-slot:append>
                            <q-icon name="event" class="cursor-pointer">
                                <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                    <q-date v-model="date">
                                        <div class="row items-center justify-end">
                                            <q-btn v-close-popup label="Close" color="primary" flat />
                                        </div>
                                    </q-date>
                                </q-popup-proxy>
                            </q-icon>
                        </template>
                    </q-input>
                    <h4 class="extra-mar">bis</h4>
                    <q-input filled v-model="date2" mask="date" :rules="['date']">
                        <template v-slot:append>
                            <q-icon name="event" class="cursor-pointer">
                                <q-popup-proxy cover transition-show="scale" transition-hide="scale">
                                    <q-date v-model="date2">
                                        <div class="row items-center justify-end">
                                            <q-btn v-close-popup label="Close" color="primary" flat />
                                        </div>
                                    </q-date>
                                </q-popup-proxy>
                            </q-icon>
                        </template>
                    </q-input>
                </div>
            </div>
            <div class="align-basic">
                <div>
                    <h4 class="mar-align">Freigabeberechtigter</h4>
                    <h4>Checkliste</h4>
                </div>
                <div class="align-mult">
                    <q-input class="extra-mar" outlined v-model="freigabe" label="Freigabeberechtigter" />
                    <q-input class="extra-mar" outlined v-model="checkliste" label="Checkliste wählen" />
                </div>
            </div>
            <div class="align-basic">
                <h4>Bemerkungen</h4>
                <q-input class="extra-mar input-bem" outlined v-model="bem" label="Bemerkungen" />
            </div>
            <q-btn label="Reparaturauftrag anlegen" color="grey" @click=sendData class=""></q-btn>
        </div>
    </div>
-->
