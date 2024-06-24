<script>
import Dataviewer from "@/main/vue/pages/Dataviewer/Dataviewer.vue";
import {getTrackLayoutData} from "@/main/vue/api/dataviewer";
import VueApexCharts from 'vue3-apexcharts';

import { Bar } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js'
import {ref, onMounted, reactive, nextTick} from "vue";
import {getUserData} from "@/main/vue/api/admin";
import {useRoute, useRouter} from "vue-router";
import Plotly from 'plotly.js-dist';

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

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
        const fromStrKm = ref('')
        const toStrKm = ref('')
        const router = useRouter()
        const data = ref([])
        const isProfile = ref(false)
        const leftEmpty = ref(false)
        const rightEmpty = ref(false)
        /*
        const x = ref([20,25,23,24,19,21,22,20,25,23,24,19,21,22])
        const y = ref([0.5,0.2,0.3,0.4,0,1,-0.2,-0.5,-0.2,-0.3,-0.4,0,-1,0.2])
        const z = ref([1,2,3,4,5,6,7,8,9,10,11,12,13,14])
         */

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

        const chartOptions = ref({
            chart: {
                type: 'bar',
                height: '350',
                id: 'vuechart-example'
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
                    formatter: (value) => {return value}
                },
                title: {
                    text: 'Längenhöhe [mm]'
                }
            },
            xaxis: {
                type: 'numeric',
                //categories: [],
                labels: {
                    rotate: -90,
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

        function buildDataset(from, to, id) {
            seriesLinks.value[0].data = []
            seriesRechts.value[0].data = []
            xLeft.value = []
            yLeft.value = []
            zLeft.value = []
            data.value.forEach(user => {
                if (user.str_km >= from && user.str_km <= to){
                    console.log("1")
                    seriesLinks.value[0].data.push([user.str_km,user.z_links_railab_3p])
                    seriesRechts.value[0].data.push([user.str_km,user.z_rechts_railab_3p])
                    const timestamp = user.time_unix
                    const date = new Date(timestamp * 1000)
                    const formattedDate = date.toISOString().slice(11, 16)
                    //console.log(formattedDate)
                    //const formattedDate = date.toISOString().slice(0, 19).replace('T', ' ')
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
            //plot3D()
        }

        function refreshRoute() {
            console.log(fromStrKm.value)
            const routeId2 = routeId.value
            const fromStrKm2 = fromStrKm.value
            const toStrKm2 = toStrKm.value
            //const vst = seriesLinks.value[0].data
            /*data.value.forEach(value => {
                console.log(value.str_km)
            })*/
            buildDataset(fromStrKm2, toStrKm2, routeId2)
            //console.log(vst)
            //vst.push([1,1])
            //seriesLinks.value[0].data = vst
            console.log(seriesLinks.value[0].data)
            router.push(`/dataviewer/route/${routeId2}/from/${fromStrKm2}/to/${toStrKm2}`)
        }

        function switchDataviewer() { isProfile.value = false }

        function switchProfil() {
            isProfile.value = true
            nextTick(() => {
                plot3DLeft();
                plot3DRight();
            })
        }

        /*
        const state = reactive({
            filter: '',
            columns: [
                {
                    name: 'desc',
                    required: true,
                    label: 'Reparaturauftrag',
                    align: 'left',
                    field: row => row.name,
                    format: val => `${val}`,
                    sortable: true
                },
                {name: 'id', label: 'ID', align: 'left', field: row => row.id, format: val => `${val}`, sortable: true},
                {name: 'E-Mail', label: 'Left Railab', align: 'left', field: 'left', sortable: true},
                {name: 'First Name', label: 'Rigth Railab', align: 'left', field: 'right', sortable: true},
                {name: 'Last Name', label: 'Str km', align: 'left', field: 'str_km', sortable: true},
            ],
            rows: []
        });

        function transformData() {}
         */
        onMounted( async () => {
            console.log(loaded.value)
            routeId.value = route.params.id
            from.value = route.params.fromId
            to.value = route.params.toId
            console.log(routeId.value, from.value, to.value)
            const data2 = await getTrackLayoutData(routeId.value)
            data.value = data2
            let i = 0
            //const categories = []
            //const seriesData = []
            console.log(data2)
            //seriesRechts.value[0].data.push([127,0.0])
            //seriesLinks.value[0].data.push([127,0.0])
            //buildDataset(from, to, routeId)

            data2.forEach(user => {
                    //console.log(user)
                //data.value.push([user])
                if (/*i < 2000 &&*/ user.str_km >= from.value && user.str_km <= to.value){
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
                    //i += 1*/
                }

                    /*state.rows.push( {
                        id: user.id,
                        left: user.z_links_railab_3p,
                        right: user.z_rechts_railab_3p,
                        str_km: user.str_km,
                    })*/
            });
            nextTick(() => {
                plot3DLeft();
                plot3DRight();
            });
            if (seriesLinks.value[0].data.length === 0) {
                leftEmpty.value = true
            }
            if (seriesRechts.value[0].data.length === 0) {
                rightEmpty.value = true
            }

            //plot3D()
            //chartOptions.value.xaxis.categories = categories
            //series.value.data = seriesData
            console.log(data.value)
            loaded.value = true
            //console.log(chartOptions.value.xaxis.categories)
            console.log(seriesLinks.value[0].data)
            console.log(loaded.value)
        })

        return {
            chartOptions, seriesLinks, seriesRechts, loaded,
            plotlyChartLeft, refreshRoute, fromStrKm, toStrKm,
            switchDataviewer, switchProfil, isProfile, plot3DLeft,
            leftEmpty, rightEmpty, plotlyChartRight
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
        <div class="align-mult">
            <Dataviewer/>
            <div>
                <div class="align-mult">
                    <div>
                        <p>von</p>
                        <q-input class="q-pa-xs" outlined v-model="fromStrKm"></q-input>
                    </div>
                    <div>
                        <p>bis</p>
                        <q-input class="q-pa-xs" outlined v-model="toStrKm"></q-input>
                    </div>
                </div>
                <div>
                    <div class="q-pa-xs">
                        <q-btn label="Strecken km aktualisieren" @click=refreshRoute class=""></q-btn>
                    </div>
                </div>
            </div>
        </div>
        <div class="align-mult">
            <div>
                <q-btn label="Dataviever" @click="switchDataviewer" class=""></q-btn>
            </div>
            <div>
                <q-btn label="Profil" @click="switchProfil" class=""></q-btn>
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
                <q-banner v-if="!loaded">Loading</q-banner>
                <apexchart v-else type="bar" height="350" :options="chartOptions" :series="seriesLinks"></apexchart>
            </div>
            <q-space/>
            <div v-if="!isProfile" class="outline">
                <q-banner align="middle">Längenhöhe - z_rechts_railab_3p</q-banner>
                <q-banner v-if="!loaded">Loading</q-banner>
                <apexchart v-else type="bar" height="350" :options="chartOptions" :series="seriesRechts"></apexchart>
            </div>
            <q-space/>
            <div>
                <div v-if="isProfile" class="outline">
                    <q-banner align="middle">Längenhöhe - z_rechts_railab_3p</q-banner>
                    <q-banner v-if="!loaded">Loading</q-banner>
                    <div ref="plotlyChartLeft" style="width: 100%; height: 100%;"></div>
                </div>
            </div>
            <q-space/>
            <div>
                <div v-if="isProfile" class="outline">
                    <q-banner align="middle">Längenhöhe - z_links_railab_3p</q-banner>
                    <q-banner v-if="!loaded">Loading</q-banner>
                    <div ref="plotlyChartRight" style="width: 100%; height: 100%;"></div>
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
