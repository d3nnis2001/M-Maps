<script>
import Dataviewer from "@/main/vue/pages/Dataviewer/Dataviewer.vue";
import {getTrackLayoutData} from "@/main/vue/api/dataviewer";
import VueApexCharts from 'vue3-apexcharts';

import {ref, onMounted, reactive, nextTick, watch} from "vue";
import {useRoute, useRouter} from "vue-router";
import Plotly from 'plotly.js-dist';
import {useQuasar} from "quasar";

export default {
    components: {
        Dataviewer,
        apexchart: VueApexCharts,
    },
    setup() {
        const plotlyChartLeft = ref(null);
        const plotlyChartRight = ref(null);
        const loaded = ref(false);
        const route = useRoute();
        const routeId = ref('')
        const from = ref('')
        const to = ref('')
        const upper = ref(null)
        const lower = ref(null)
        const fromStrKm = ref('')
        const toStrKm = ref('')
        const router = useRouter()
        const data = ref([])
        const isProfile = ref(false)
        const leftEmpty = ref(false)
        const rightEmpty = ref(false)
        const isChanging = ref(false)
        const $q = useQuasar()
        const isFail = ref(false)
        const rangeValues = ref({min: 10, max: 50})
        const isTimeoutActive = ref(false);
        const newestDate = ref('')

        const NOTIFY_OPTIONS = {
            fromHigherThanTo: {
                type: 'negative',
                message: 'The From value should be less then or equal to the To value '
            },
        };

        // -------------- 3D GRAPH --------------

        const xLeft = ref([])
        const yLeft = ref([])
        const zLeft = ref([])

        const xRight = ref([])
        const yRight = ref([])
        const zRight = ref([])

        const plot3DLeft = () => {
            const trace = {
                x: xLeft.value,
                y: yLeft.value,
                z: zLeft.value,
                mode: 'markers',
                marker: {
                    size: 5,
                    color: zLeft.value,
                    colorscale: 'Viridis',
                    opacity: 0.8,
                },
                //type: 'surface',
                //type: 'mesh3d',
                type: 'scatter3d',
            };

            const layout = {
                scene: {
                    xaxis: {title: 'Streckenkm'},
                    yaxis: {title: 'mm'},
                    zaxis: {title: 'Time'},
                },
            };

            if (plotlyChartLeft.value) {
                Plotly.newPlot(plotlyChartLeft.value, [trace], layout);
            }
        };

        const plot3DRight = () => {
            const trace = {
                x: xRight.value,
                y: yRight.value,
                z: zRight.value,
                mode: 'markers',
                marker: {
                    size: 5,
                    color: zRight.value,
                    colorscale: 'Viridis',
                    opacity: 0.8,
                },
                type: 'scatter3d',
            };

            const layout = {
                scene: {
                    xaxis: {title: 'Streckenkm'},
                    yaxis: {title: 'mm'},
                    zaxis: {title: 'Time'},
                },
            };

            if (plotlyChartRight.value) {
                Plotly.newPlot(plotlyChartRight.value, [trace], layout);
            }
        };

        // -------------- CHART --------------

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
                enabled: false
            },
            yaxis: {
                forceNiceScale: true,
                labels: {
                    formatter: (value) => {
                        return value
                    }
                },
                title: {
                    text: 'Längenhöhe [mm]'
                }
            },
            xaxis: {
                type: 'numeric',
                tickAmount: Math.ceil((toStrKm.value - fromStrKm.value) / 0.5),
                min: parseFloat(fromStrKm.value),
                max: parseFloat(toStrKm.value),
                labels: {
                    rotate: -90,
                    formatter: (value) => parseFloat(value).toFixed(3)
                },
                title: {
                    text: 'Streckenkilometer [km]'
                }
            }
        });

        const seriesLinks = ref([{
            name: 'Längenhöhe',
            data: []
        }]);

        const seriesRechts = ref([{
            name: 'Längenhöhe',
            data: []
        }]);

        // -------------- DATASET --------------

        function buildDataset(from, to, id) {
            seriesLinks.value[0].data = []
            seriesRechts.value[0].data = []
            xLeft.value = []
            yLeft.value = []
            zLeft.value = []
            xRight.value = []
            yRight.value = []
            zRight.value = []
            data.value.forEach(user => {
                if (user.str_km >= from && user.str_km <= to) {
                    const timestamp = user.time_unix
                    const date = new Date(timestamp * 1000)
                    const formattedDate = date.toISOString()
                    if (formattedDate.includes(newestDate.value)) {
                        seriesLinks.value[0].data.push([user.str_km, user.z_links_railab_3p])
                        seriesRechts.value[0].data.push([user.str_km, user.z_rechts_railab_3p])
                    }
                    formattedDate.slice(11, 16)
                    xLeft.value.push(user.str_km)
                    yLeft.value.push(user.z_links_railab_3p)
                    zLeft.value.push(formattedDate)
                    xRight.value.push(user.str_km)
                    yRight.value.push(user.z_rechts_railab_3p)
                    zRight.value.push(formattedDate)
                }
            })
            if (seriesLinks.value[0].data.length === 0) {
                leftEmpty.value = true
            }
            if (seriesRechts.value[0].data.length === 0) {
                rightEmpty.value = true
            }

            nextTick(() => {
                plot3DLeft();
                plot3DRight();
            })
        }

        function reduceDataSet() {
            const maxValues = 2000
            const length = seriesLinks.value[0].data.length
            const step = Math.ceil(length / maxValues);
            const listLeft = []
            const listRight = []
            console.log(length)
            if (length > maxValues) {
                for (let i = 0; i < length; i += step) {
                    listLeft.push(seriesLinks.value[0].data[i]);
                    listRight.push(seriesRechts.value[0].data[i]);
                }
                seriesRechts.value[0].data = listRight
                seriesLinks.value[0].data = listLeft
                console.log(seriesRechts.value[0].data.length, xRight.value.length)
            }
            console.log("not")
        }

        function findNewestDate() {
            data.value.forEach(user => {
                if (newestDate.value === '') {
                    newestDate.value = user.time_unix
                } else {
                    if (newestDate.value < user.time_unix) {
                        newestDate.value = user.time_unix
                    }
                }
            })
            const date = new Date(newestDate.value * 1000)
            const formattedDate = date.toISOString().slice(0, 10)
            newestDate.value = formattedDate
            console.log(formattedDate)
        }

        // -------------- ROUTE --------------

        function refreshRoute2() {
            console.log(fromStrKm.value)
            const routeId2 = routeId.value
            const fromStrKm2 = rangeValues.value.min
            const toStrKm2 = rangeValues.value.max
            if (fromStrKm2 <= toStrKm2) {
                isFail.value = false
                buildDataset(fromStrKm2, toStrKm2, routeId2)
                reduceDataSet()
                router.push(`/dataviewer/route/${routeId2}/from/${fromStrKm2}/to/${toStrKm2}`)
                isChanging.value = false;
            } else {
                $q.notify(NOTIFY_OPTIONS.fromHigherThanTo)
                isFail.value = true
            }
            console.log(seriesLinks.value[0].data)
        }

        // -------------- BUTTONS --------------

        function switchDataviewer() {
            isProfile.value = false
            console.log(seriesLinks.value[0].data.length)
        }

        function switchProfil() {
            isProfile.value = true
            console.log(xLeft.value.length)
            nextTick(() => {
                plot3DLeft();
                plot3DRight();
            })
        }

        // -------------- START --------------

        function findMinAndMax() {
            data.value.forEach(user => {
                const valueMin = Math.floor(user.str_km)
                const valueMax = Math.ceil(user.str_km)
                if (lower.value === null) {
                    lower.value = valueMin
                } else if (upper.value === null) {
                    if (lower.value < valueMax) {
                        upper.value = valueMax
                    }
                } else {
                    if (lower.value > valueMin) {
                        lower.value = valueMin
                    }
                    if (upper.value < valueMax) {
                        upper.value = valueMax
                    }
                }
            })
            console.log(lower.value, upper.value)
        }

        onMounted(async () => {
            console.log(loaded.value)
            routeId.value = route.params.id
            from.value = route.params.fromId
            to.value = route.params.toId
            if (from.value > to.value) {
                toStrKm.value = from.value
                fromStrKm.value = to.value
            } else {
                fromStrKm.value = from.value
                toStrKm.value = to.value
            }
            console.log(routeId.value, from.value, to.value)
            const data2 = await getTrackLayoutData(routeId.value)
            data.value = data2
            findMinAndMax()
            findNewestDate()
            console.log(data2)
            if (from.value === 'null') {
                from.value = lower.value
                fromStrKm.value = from.value
                console.log(1)
            }
            if (to.value === 'null') {
                to.value = upper.value
                toStrKm.value = to.value
                console.log(2)
            }
            rangeValues.value.min = parseFloat(fromStrKm.value)
            rangeValues.value.max = parseFloat(toStrKm.value)
            console.log(from.value, to.value)
            buildDataset(rangeValues.value.min, rangeValues.value.max, routeId.value)
            console.log(data.value)
            loaded.value = true
            console.log(seriesLinks.value[0].data)
            console.log(loaded.value)
        })

        // -------------- WATCHERS --------------

        watch([fromStrKm, toStrKm], () => {
            isChanging.value = true
            if (!isTimeoutActive.value) {
                isTimeoutActive.value = true;

                setTimeout(() => {
                    console.log(rangeValues.value.max, rangeValues.value.min);
                    refreshRoute2();
                    isProfile.value = false
                    isTimeoutActive.value = false;
                }, 5000);
            }

            const minVal = parseFloat(fromStrKm.value);
            const maxVal = parseFloat(toStrKm.value);
            const diff = maxVal - minVal;

            const x = Math.floor(diff / 10)
            const y = Math.ceil(diff / x)
            console.log(diff, x, y)

            chartOptions.value.xaxis.tickAmount = diff > 10 ? Math.ceil(diff / Math.floor(diff / 10)) : Math.ceil(diff / 0.5);
            chartOptions.value.xaxis.min = minVal;
            chartOptions.value.xaxis.max = maxVal;
        });

        watch(rangeValues, () => {
            isChanging.value = true

            if (!isTimeoutActive.value) {
                isTimeoutActive.value = true;

                setTimeout(() => {
                    console.log(rangeValues.value.max, rangeValues.value.min);
                    refreshRoute2();
                    isProfile.value = false
                    isTimeoutActive.value = false;
                }, 5000);
            }

            const minVal = rangeValues.value.min;
            const maxVal = rangeValues.value.max;
            const diff = maxVal - minVal;

            const x = Math.floor(diff / 10)
            const y = Math.ceil(diff / x)
            console.log(diff, x, y)

            chartOptions.value.xaxis.tickAmount = diff > 10 ? Math.ceil(diff / Math.floor(diff / 10)) : Math.ceil(diff / 0.5);
            chartOptions.value.xaxis.min = minVal;
            chartOptions.value.xaxis.max = maxVal;
        });

        // -------------- RETURN --------------

        return {
            chartOptions, seriesLinks, seriesRechts, loaded,
            plotlyChartLeft, fromStrKm, toStrKm,
            switchDataviewer, switchProfil, isProfile, plot3DLeft,
            leftEmpty, rightEmpty, plotlyChartRight, routeId,
            isChanging, rangeValues, upper, lower
        }
    }
}

</script>
<template>
    <q-page>
        <div class="align-mult q-pa-xs">
            <div class="q-pa-xs">
                <div>
                    <p>Strecken ID: {{ routeId }}</p>
                </div>
            </div>
            <q-linear-progress indeterminate color="black" v-if="!loaded" class="q-mt-sm">Loading</q-linear-progress>
            <div v-else class="q-pa-lg">
                <q-space/>
                <q-range
                    v-model="rangeValues"
                    :min="lower !== null ? lower : 0"
                    :max="upper !== null ? upper : 1000"
                    step="0.5"
                    dense
                    label-always
                />
            </div>
        </div>
        <div class="q-mt-lg q-ml-md q-mr-md">
            <div class="q-gutter-y-md">
                <q-btn label="Profil" @click="switchDataviewer" class=""></q-btn>
                <q-btn label="Zeitreihe" @click="switchProfil" class=""></q-btn>
            </div>
        </div>
        <div>
            <div v-if="!isProfile" class="outline">
                <q-banner align="middle">Längenhöhe - z_links_railab_3p</q-banner>
                <q-linear-progress indeterminate color="black" v-if="!loaded || isChanging" class="q-mt-sm">Loading
                </q-linear-progress>
                <apexchart v-else type="bar" height="350" :options="chartOptions" :series="seriesLinks"></apexchart>
            </div>
            <q-space/>
            <div v-if="!isProfile" class="outline">
                <q-banner align="middle">Längenhöhe - z_rechts_railab_3p</q-banner>
                <q-linear-progress indeterminate color="black" v-if="!loaded || isChanging" class="q-mt-sm">Loading
                </q-linear-progress>
                <apexchart v-else type="bar" height="350" :options="chartOptions" :series="seriesRechts"></apexchart>
            </div>
            <q-space/>
            <div>
                <div v-if="isProfile" class="outline">
                    <q-banner align="middle">Längenhöhe - z_rechts_railab_3p</q-banner>
                    <q-linear-progress indeterminate color="black" v-if="!loaded || isChanging" class="q-mt-sm">
                        Loading
                    </q-linear-progress>
                    <div v-else ref="plotlyChartLeft" style="width: 100%; height: 100%;"></div>
                </div>
            </div>
            <q-space/>
            <div>
                <div v-if="isProfile" class="outline">
                    <q-banner align="middle">Längenhöhe - z_links_railab_3p</q-banner>
                    <q-linear-progress indeterminate color="black" v-if="!loaded || isChanging" class="q-mt-sm">
                        Loading
                    </q-linear-progress>
                    <div v-else ref="plotlyChartRight" style="width: 100%; height: 100%;"></div>
                </div>
            </div>
        </div>
    </q-page>
</template>
<style scoped>
#app {
    width: auto;
    height: 400px;
}
</style>
