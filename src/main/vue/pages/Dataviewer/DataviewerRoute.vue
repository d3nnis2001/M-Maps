<script>
import Dataviewer from "@/main/vue/pages/Dataviewer/Dataviewer.vue";
import {getTrackLayoutData} from "@/main/vue/api/dataviewer";
import VueApexCharts from 'vue3-apexcharts';

import { Bar } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js'
import {ref, onMounted, reactive} from "vue";
import {getUserData} from "@/main/vue/api/admin";
import {useRoute} from "vue-router";

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

export default {
    components: {
        Dataviewer,
        apexchart: VueApexCharts,
    },
    setup() {
        const loaded = ref(false);
        const route = useRoute();
        const routeId = ref('')

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
                    rotate: -90
                },
                title: {
                    text: 'Streckenkilometer [km]'
                }
            },
            annotations: {
                yaxis: [
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
                        }*/
                    },
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
                    }
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
            console.log(routeId.value)
            const data = await getTrackLayoutData(routeId.value)
            let i = 0
            //const categories = []
            //const seriesData = []
            console.log(data)
            seriesRechts.value[0].data.push([127,0.0])
            seriesLinks.value[0].data.push([127,0.0])
            data.forEach(user => {
                    //console.log(user)
                if (i < 2000){
                    seriesRechts.value[0].data.push([user.str_km,user.z_rechts_railab_3p])
                    seriesLinks.value[0].data.push([user.str_km,user.z_links_railab_3p])
                    /*
                    if (user.str_km % 2 === 0) {
                        chartOptions.value.xaxis.categories.push(user.str_km)
                    }
                    else {
                        console.log(Math.round(user.str_km))
                        chartOptions.value.xaxis.categories.push('')
                    }*/
                    i += 1
                }

                    /*state.rows.push( {
                        id: user.id,
                        left: user.z_links_railab_3p,
                        right: user.z_rechts_railab_3p,
                        str_km: user.str_km,
                    })*/
            })
            //chartOptions.value.xaxis.categories = categories
            //series.value.data = seriesData
            loaded.value = true
            //console.log(chartOptions.value.xaxis.categories)
            console.log(seriesLinks.value[0].data)
            console.log(loaded.value)
        })

        return {chartOptions, seriesLinks, seriesRechts, loaded}
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
        <Dataviewer/>
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
            -->
            <div class="outline">
                <q-banner align="middle">Längenhöhe - z_links_railab_3p</q-banner>
                <q-banner v-if="!loaded">Loading</q-banner>
                <apexchart v-else type="bar" height="350" :options="chartOptions" :series="seriesLinks"></apexchart>
            </div>
            <q-space/>
            <div class="outline">
                <q-banner align="middle">Längenhöhe - z_rechts_railab_3p</q-banner>
                <q-banner v-if="!loaded">Loading</q-banner>
                <apexchart v-else type="bar" height="350" :options="chartOptions" :series="seriesRechts"></apexchart>
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
