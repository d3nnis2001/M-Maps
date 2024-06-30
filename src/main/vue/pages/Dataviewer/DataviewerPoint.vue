<script>
import Dataviewer from "@/main/vue/pages/Dataviewer/Dataviewer.vue";
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {getPointData, getPointInfo} from "@/main/vue/api/dataviewer";
import VueApexCharts from "vue3-apexcharts";

export default {
    components: {
        Dataviewer,
        apexchart: VueApexCharts
    },
    setup(){
        const pointId = ref('')
        const lon = ref('')
        const lat = ref('')
        const trackId = ref('')
        const route = useRoute();
        const tab = ref('mails')
        const loaded = ref(false)

        //-----------Graph-----------------

        const chartOptions = ref({
            chart: {
                type: 'bar',
                height: '350',
                id: 'vuechart'
            },
            plotOptions: {
                bar: {
                    colors: {
                        ranges: [{
                            from: -100,
                            to: -46,
                            color: '#F15B46'
                        }, {
                            from: -45,
                            to: 0,
                            color: '#FEB019'
                        }]
                    },
                    columnWidth: '100%',
                }
            },
            dataLabels: {
                enabled: true
            },
            yaxis: {
                forceNiceScale: true,
                labels: {
                    formatter: (value) => {return value}
                },
                title: {
                    text: 'Längenhöhe [mm]'
                }
            },
            xaxis: {
                categories: [],
                title: {
                    text: 'Aufnahmezeit'
                }
            }
        });

        const series = ref([
            {
                name: 'z_links_railab_3p',
                data: []
            },
            {
                name: 'z_rechts_railab_3p',
                data: []
            }
        ]);

        //--------Start-----------

        onMounted(async () => {
            pointId.value = route.params.pointId
            const [response, responseInfo] = await Promise.all([
                getPointData(pointId.value),
                getPointInfo(pointId.value)
            ])
            console.log(responseInfo)
            console.log(response)
            lon.value = responseInfo.longitude
            lat.value = responseInfo.latitude
            trackId.value = responseInfo.strecken_id
            response.forEach(data => {
                const timestamp = data.time_unix
                const date = new Date(timestamp * 1000)
                const formattedDate = String(date.getDate()).padStart(2, '0') + '-' +
                    String(date.getMonth() + 1).padStart(2, '0') + '-' +
                    date.getFullYear() + ' ' +
                    String(date.getHours()).padStart(2, '0') + ':' +
                    String(date.getMinutes()).padStart(2, '0');
                series.value[0].data.push(data.z_links_railab_3p)
                series.value[1].data.push(data.z_rechts_railab_3p)
                chartOptions.value.xaxis.categories.push(formattedDate)
            })
            loaded.value = true
        })

        return {
            lon,
            lat,
            pointId,
            trackId,
            tab,
            chartOptions,
            series,
            loaded
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
                            <q-tab name="mails" label="Gleislagedaten" />
                            <q-tab name="alarms" label="Fotos" />
                            <q-tab name="movies" label="Movies" />
                        </q-tabs>

                        <q-separator />

                        <q-tab-panels v-model="tab" animated>
                            <q-tab-panel name="mails">
                                <div class="outline">
                                    <q-linear-progress indeterminate color="black" v-if="!loaded" class="q-mt-sm">Loading</q-linear-progress>
                                    <apexchart v-else type="bar" height="350" :options="chartOptions" :series="series"></apexchart>
                                </div>
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
