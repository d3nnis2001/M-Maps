<script>
import Dataviewer from "@/main/vue/pages/Dataviewer/Dataviewer.vue";
import {getTrackLayoutData} from "@/main/vue/api/dataviewer";

import { Bar } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js'
import {ref, onMounted, reactive} from "vue";
import {getUserData} from "@/main/vue/api/admin";

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

export default {
    components: {
        Dataviewer
    },
    setup() {
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
                {name: 'E-Mail', label: 'E-Mail', align: 'left', field: 'username', sortable: true},
                {name: 'First Name', label: 'First Name', align: 'left', field: 'firstname', sortable: true},
                {name: 'Last Name', label: 'Last Name', align: 'left', field: 'lastname', sortable: true},
            ],
            rows: []
        });

        function getData() {}

        onMounted( () => {
            const data = getTrackLayoutData()
            console.log(data)
            data.then(allUsers => {
                console.log(allUsers)
                allUsers.forEach(user => {
                    console.log(user)
                    state.rows.push( {
                        id: 'not in Database',
                        username: user.username,
                        firstname: user.firstname,
                        lastname: user.lastname,
                    })
                })
            })
        })

        return {getData, state}
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
            <div class="q-pa-md">
                <q-table
                    title="User Data"
                    :rows="state.rows"
                    :columns="state.columns"
                    row-key="name"
                />
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
