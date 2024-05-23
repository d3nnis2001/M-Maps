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


            <q-btn-dropdown color="red" label="Status" dropdown-icon="change_history">
                <q-list>
                    <q-item clickable v-close-popup @click="onItemClick">
                        <q-item-section>
                            <q-item-label>beauftragt</q-item-label>
                        </q-item-section>
                    </q-item>

                    <q-item clickable v-close-popup @click="onItemClick">
                        <q-item-section>
                            <q-item-label>in Bearbeitung</q-item-label>
                        </q-item-section>
                    </q-item>

                    <q-item clickable v-close-popup @click="onItemClick">
                        <q-item-section>
                            <q-item-label>abgeschlossen</q-item-label>
                        </q-item-section>
                    </q-item>

                    <q-item clickable v-close-popup @click="onItemClick">
                        <q-item-section>
                            <q-item-label>storniert</q-item-label>
                        </q-item-section>
                    </q-item>
                </q-list>
            </q-btn-dropdown>


            <q-btn label="Speichern" @click="$router.push('/inspectionOrderOverview')" color="primary" class=""></q-btn>
        </div>
    </div>

</template>


<style scoped>

</style>
