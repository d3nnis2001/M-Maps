<template>
    <div>
        <canvas ref="canvas"></canvas>
    </div>
</template>

<script>
import { Bar } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, registerables } from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, registerables);

export default {
    name: 'BarChart',
    extends: Bar,
    props: {
        chartData: {
            type: Object,
            required: true
        },
        options: {
            type: Object,
            required: true
        }
    }
}
</script>

<style scoped>

</style>
