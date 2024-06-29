<script>
import Dataviewer from "@/main/vue/pages/Dataviewer/Dataviewer.vue";
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {getPointData} from "@/main/vue/api/dataviewer";

export default {
    components: {
      Dataviewer
    },
    setup(){
        const pointId = ref('')
        const lon = ref('')
        const lat = ref('')
        const trackId = ref('')
        const route = useRoute();
        const tab = ref('mails')

        onMounted(async () => {
            pointId.value = route.params.pointId
            const response = await getPointData(pointId.value)
            console.log(response[0])
            lon.value = response[0].longitude
            lat.value = response[0].latitude
            trackId.value = response[0].strecken_id
        })

        return {
            lon,
            lat,
            pointId,
            trackId,
            tab
        }
    }
}
</script>

<template>
    <q-page>
        <Dataviewer/>
        <div class="outline">
            <p>Point ID: {{pointId}}</p>
            <p>Strecke: {{trackId}}</p>
            <p>Longitude: {{lon}}</p>
            <p>Latitude: {{lat}}</p>
            <div class="q-pa-md">
                <div class="q-gutter-y-md" style="max-width: 600px">
                    <q-card>
                        <q-tabs
                            v-model="tab"
                            dense
                            class="text-grey"
                            active-color="primary"
                            indicator-color="primary"
                            align="justify"
                            narrow-indicator
                        >
                            <q-tab name="mails" label="Mails" />
                            <q-tab name="alarms" label="Alarms" />
                            <q-tab name="movies" label="Movies" />
                        </q-tabs>

                        <q-separator />

                        <q-tab-panels v-model="tab" animated>
                            <q-tab-panel name="mails">
                                <div class="text-h6">Mails</div>
                                Lorem ipsum dolor sit amet consectetur adipisicing elit.
                            </q-tab-panel>

                            <q-tab-panel name="alarms">
                                <div class="text-h6">Alarms</div>
                                Lorem ipsum dolor sit amet consectetur adipisicing elit.
                            </q-tab-panel>

                            <q-tab-panel name="movies">
                                <div class="text-h6">Movies</div>
                                Lorem ipsum dolor sit amet consectetur adipisicing elit.
                            </q-tab-panel>
                        </q-tab-panels>
                    </q-card>
                </div>
            </div>
        </div>
    </q-page>
</template>

<style scoped>

</style>
