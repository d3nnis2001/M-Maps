<script>
import Dataviewer from "@/main/vue/pages/Dataviewer/Dataviewer.vue";
import {getTrackLayoutData} from "@/main/vue/api/dataviewer";
import VueApexCharts from 'vue3-apexcharts';

import {ref, onMounted, reactive, nextTick, watch} from "vue";
import {getUserData} from "@/main/vue/api/admin";
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
                //title: '3D Scatter Plot',
                scene: {
                    xaxis: {title: 'Streckenkm'},
                    yaxis: {title: 'mm'},
                    zaxis: {title: 'Time'},
                },
            };

            if (plotlyChartLeft.value) {
                Plotly.newPlot(plotlyChartLeft.value, [trace], layout);
            }

            //Plotly.newPlot(plotlyChart.value, [trace], layout);
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
                //title: '3D Scatter Plot',
                scene: {
                    xaxis: {title: 'Streckenkm'},
                    yaxis: {title: 'mm'},
                    zaxis: {title: 'Time'},
                },
            };

            if (plotlyChartRight.value) {
                Plotly.newPlot(plotlyChartRight.value, [trace], layout);
            }

            //Plotly.newPlot(plotlyChart.value, [trace], layout);
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
                //categories: [],
                labels: {
                    rotate: -90,
                    formatter: (value) => parseFloat(value).toFixed(3)
                    /*
                    formatter: function (value) {
                        return value.toFixed(0);
                    }*/
                },
                title: {
                    text: 'Streckenkilometer [km]'
                }
            },
            annotations: {
                yaxis: [
                    /*
                    {
                        y: 10.5,
                        borderColor: '#e30000',
                        strokeDashArray: 0
                        /*
                        label: {
                            borderColor: '#00E396',
                            style: {
                                color: '#fff',
                                background: '#e3001a'
                            },
                            text: ''
                        }
                    },
                    /*
                    {
                        y: 7.5,
                        borderColor: '#e3c500',
                        strokeDashArray: 0
                    },
                    {
                        y: 3.5,
                        borderColor: '#00e30b',
                        strokeDashArray: 0
                    },
                    {
                        y: -10.5,
                        borderColor: '#e30000',
                        strokeDashArray: 0
                    },
                    {
                        y: -7.5,
                        borderColor: '#e3c500',
                        strokeDashArray: 0
                    },
                    {
                        y: -3.5,
                        borderColor: '#00e30b',
                        strokeDashArray: 0
                    }*/
                ]
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
                        //console.log(true)
                    }
                    formattedDate.slice(11, 16)
                    xLeft.value.push(user.str_km)
                    yLeft.value.push(user.z_links_railab_3p)
                    zLeft.value.push(formattedDate)
                    xRight.value.push(user.str_km)
                    yRight.value.push(user.z_rechts_railab_3p)
                    zRight.value.push(formattedDate)
                    //console.log(formattedDate)
                    //const formattedDate = date.toISOString().slice(0, 19).replace('T', ' ')
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
            //plot3D()


            /*chartOptions.value.xaxis.min = parseFloat(from);
            chartOptions.value.xaxis.max = parseFloat(to);
            chartOptions.value.xaxis.tickAmount = Math.ceil((to - from) / 0.5);*/
        }

        function reduceDataSet() {
            //filterDouble()
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

        /*function refreshRoute() {
            console.log(fromStrKm.value)
            const routeId2 = routeId.value
            //const [fromStrKm2, toStrKm2] = rangeValues.value;
            const fromStrKm2 = fromStrKm.value
            const toStrKm2 = toStrKm.value
            //const vst = seriesLinks.value[0].data
            /*data.value.forEach(value => {
                console.log(value.str_km)
            })
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
            //console.log(vst)
            //vst.push([1,1])
            //seriesLinks.value[0].data = vst
            console.log(seriesLinks.value[0].data)
            //isChanging.value = true
        }*/

        // -------------- ROUTE --------------

        function refreshRoute2() {
            console.log(fromStrKm.value)
            const routeId2 = routeId.value
            //const [fromStrKm2, toStrKm2] = rangeValues.value;
            const fromStrKm2 = rangeValues.value.min
            const toStrKm2 = rangeValues.value.max
            //const vst = seriesLinks.value[0].data
            /*data.value.forEach(value => {
                console.log(value.str_km)
            })*/
            if (fromStrKm2 <= toStrKm2) {
                isFail.value = false
                buildDataset(fromStrKm2, toStrKm2, routeId2)
                reduceDataSet()
                router.push(`/dataviewer/route/${routeId2}/from/${fromStrKm2}/to/${toStrKm2}`)
                isChanging.value = false;
            } else {
                $q.notify(NOTIFY_OPTIONS.fromHigherThanTo)
                isFail.value = true
                /*isFail.value = false
                buildDataset(fromStrKm2, toStrKm2, routeId2)
                reduceDataSet()
                router.push(`/dataviewer/route/${routeId2}/from/${toStrKm2}/to/${fromStrKm2}`)
                isChanging.value = false;*/
            }
            //console.log(vst)
            //vst.push([1,1])
            //seriesLinks.value[0].data = vst
            console.log(seriesLinks.value[0].data)
            //isChanging.value = true
        }

        // -------------- BUTTONS --------------

        function switchDataviewer() {
            isProfile.value = false
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
            //let i = 0
            //const categories = []
            //const seriesData = []
            console.log(data2)
            //seriesRechts.value[0].data.push([127,0.0])
            //seriesLinks.value[0].data.push([127,0.0])
            //buildDataset(from, to, routeId)
            if (from.value === 'null') {
                //from.value = '129'
                from.value = lower.value
                fromStrKm.value = from.value
                console.log(1)
            }
            if (to.value === 'null') {
                //to.value = '130'
                to.value = upper.value
                toStrKm.value = to.value
                console.log(2)
            }
            rangeValues.value.min = parseFloat(fromStrKm.value)
            rangeValues.value.max = parseFloat(toStrKm.value)
            //rangeValues.value = [parseFloat(fromStrKm.value), parseFloat(toStrKm.value)]
            console.log(from.value, to.value)
            buildDataset(rangeValues.value.min, rangeValues.value.max, routeId.value)
            /*data2.forEach(user => {
                    //console.log(user)
                //data.value.push([user])
                if (user.str_km >= from.value && user.str_km <= to.value){
                    console.log(user.str_km >= from.value)
                    seriesRechts.value[0].data.push([user.str_km,user.z_rechts_railab_3p])
                    seriesLinks.value[0].data.push([user.str_km,user.z_links_railab_3p])
                    const timestamp = user.time_unix
                    const date = new Date(timestamp * 1000)
                    const formattedDate = date.toISOString().slice(11, 16)
                    xLeft.value.push(user.str_km)
                    yLeft.value.push(user.z_links_railab_3p)
                    zLeft.value.push(formattedDate)
                    xRight.value.push(user.str_km)
                    yRight.value.push(user.z_rechts_railab_3p)
                    zRight.value.push(formattedDate)
                    /*
                    if (user.str_km % 2 === 0) {
                        chartOptions.value.xaxis.categories.push(user.str_km)
                    }
                    else {
                        console.log(Math.round(user.str_km))
                        chartOptions.value.xaxis.categories.push('')
                    }
                    //i += 1
                }

                    /*state.rows.push( {
                        id: user.id,
                        left: user.z_links_railab_3p,
                        right: user.z_rechts_railab_3p,
                        str_km: user.str_km,
                    })
            });
            reduceDataSet()
            nextTick(() => {
                plot3DLeft();
                plot3DRight();
            });
            if (seriesLinks.value[0].data.length === 0) {
                leftEmpty.value = true
            }
            if (seriesRechts.value[0].data.length === 0) {
                rightEmpty.value = true
            }*/


            /*chartOptions.value.xaxis.min = parseFloat(from);
            chartOptions.value.xaxis.max = parseFloat(to);
            chartOptions.value.xaxis.tickAmount = Math.ceil((to - from) / 0.5);*/

            //plot3D()
            //chartOptions.value.xaxis.categories = categories
            //series.value.data = seriesData
            console.log(data.value)
            loaded.value = true
            //console.log(chartOptions.value.xaxis.categories)
            console.log(seriesLinks.value[0].data)
            console.log(loaded.value)
        })

        // -------------- WATCHERS --------------

        watch([fromStrKm, toStrKm], () => {
            isChanging.value = true
            /*setTimeout(() => {
                refreshRoute();
            }, 5000);*/

            if (!isTimeoutActive.value) {
                isTimeoutActive.value = true;

                setTimeout(() => {
                    console.log(rangeValues.value.max, rangeValues.value.min);
                    refreshRoute2();
                    isProfile.value = false
                    isTimeoutActive.value = false;
                    //isChanging.value = false;
                }, 5000);
            }
            /*
            const [minVal, maxVal] = rangeValues.value;
            const diff = maxVal - minVal;

            chartOptions.value.xaxis.tickAmount = diff > 10 ? Math.floor(diff) : Math.ceil(diff / 0.5);
            chartOptions.value.xaxis.min = minVal;
            chartOptions.value.xaxis.max = maxVal;*/

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
                    //isChanging.value = false;
                }, 5000);
            }
            /*
            setTimeout(() => {
                console.log(rangeValues.value.max, rangeValues.value.min)
                refreshRoute2()
                //isChanging.value = false
            }, 5000);*/

            const minVal = rangeValues.value.min;
            const maxVal = rangeValues.value.max;
            const diff = maxVal - minVal;

            const x = Math.floor(diff / 10)
            const y = Math.ceil(diff / x)
            console.log(diff, x, y)

            //chartOptions.value.xaxis.tickAmount = diff > 10 ? diff : Math.ceil(diff / 0.5);
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

/*
export default {
    name: 'BarChart',
    components: { Bar, Dataviewer },
    data: () => ({
        loaded: false,
        chartData: null
    }),
    async mounted() {
        this.loaded = false

        try {
            const userlist = getTrackLayoutData()
            this.chartData = userlist

            this.loaded = true
            console.log(this.loaded)
        } catch (e) {
            console.error(e)
        }
    },
    /*
    data() {
        return {
            loaded: false,
            dataPoints: [],
            chartData: {
                labels: [],
                datasets: [
                    {
                        barPercentage: 1.0,
                        categoryPercentage: 1.0,
                        label: 'My Dataset',
                        backgroundColor: '#00345c',
                        data: []
                    }
                ]
            },
            chartOptions: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: false,
                        min: -20,
                        max: 20,
                        ticks: {
                            autoSkip: false,
                            stepSize: 5
                        }
                    },
                    x: {
                        ticks: {
                            autoSkip: false,
                            font: {
                                size: 10
                            },
                            callback: (value, index, values) => {
                                const groupSize = 10;
                                if (index % groupSize === 0) {
                                    return value;
                                }
                                return '';
                            }
                        }
                    }
                }
            }
        };
    },

    methods: {
        generateData() {
            // Flatten the data points and group every 10 points
            this.dataPoints = this.generateDatapoint();
            const groupSize = 10;
            const groupedPoints = this.dataPoints.reduce((acc, point, index) => {
                const groupIndex = Math.floor(index / groupSize);
                if (!acc[groupIndex]) acc[groupIndex] = [];
                acc[groupIndex].push(point);
                return acc;
            }, []);
            // Extract labels and data from grouped points
            groupedPoints.forEach((group, groupIndex) => {
                group.forEach(point => {
                    this.chartData.labels.push(`x: ${point.x}, y: ${point.y}`);
                    this.chartData.datasets[0].data.push(point.y);
                });
            });
        },
        generateDatapoint() {
            const points = []
            for (let i = 0; i <= 100; i += 0.5) {
                const z = Math.floor(Math.random() * 3) - 1
                if (!(z === 0)) {
                    points.push({x: i, y: z})
                } else {
                    points.push({x: i, y: -0.23})
                }

            }
            return points
        }
    }
};*/

</script>
<!--:options="chartOptions"
<template>
    <q-page>
        <Dataviewer/>
        <div class="outline">
            <div id="app">
                <Bar
                    v-if="loaded"
                    :data="chartData"
                />
            </div>
        </div>
    </q-page>
</template>
-->
<template>
    <q-page>
        <div class="align-mult q-pa-xs">
            <!--<Dataviewer/>-->
            <div class="q-pa-xs">
                <div>
                    <p>Strecken ID: {{ routeId }}</p>
                    <!--<q-badge color="secondary">
                    von {{ rangeValues.min }} km bis {{ rangeValues.max }} km
                    </q-badge>
                    <q-input class="q-pa-xs" outlined v-model="routeId" ></q-input>
                </div>
                <div>
                    <p>von</p>
                    <q-input class="q-pa-xs" outlined v-model="fromStrKm"></q-input>
                </div>
                <div>
                    <p>bis</p>
                    <q-input class="q-pa-xs" outlined v-model="toStrKm"></q-input>-->
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
                <div>
                    <!--
                    <div class="q-pa-xs">
                        <q-btn label="Strecken km aktualisieren" @click=refreshRoute class=""></q-btn>
                    </div>
                    -->
                </div>
            </div>
        </div>
        <div class="q-mt-lg q-ml-md q-mr-md">
            <div class="q-gutter-y-md">
                <q-btn label="Profil" @click="switchDataviewer" class=""></q-btn>
                <q-btn label="Zeitreihe" @click="switchProfil" class=""></q-btn>
            </div>
        </div>
        <div>
            <!--
            <div class="q-pa-md">
                <q-table
                    title="User Data"
                    :rows="state.rows"
                    :columns="state.columns"
                    row-key="name"
                />
            </div>
            <q-banner v-else-if="rightEmpty">There is no Data for this Part</q-banner>
            -->
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
