<script>
import Dataviewer from "@/main/vue/pages/Dataviewer/Dataviewer.vue";

import { Bar } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js'

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)

export default {
    name: 'BarChart',
    components: { Bar, Dataviewer },
    data() {
        return {
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
    beforeMount() {
        this.generateData()
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
};

</script>

<template>
    <q-page>
        <Dataviewer/>
        <div class="outline">
            <div id="app">
                <Bar
                    :options="chartOptions"
                    :data="chartData"
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
