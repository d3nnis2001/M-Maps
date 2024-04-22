<script setup>
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import {onMounted} from 'vue';

onMounted(() => {
    const map = L.map('map').setView([52.0368609707235, 8.49525289846377], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);
})

</script>

<template>
    <div id="map"></div>
</template>

<style>
#map { height: 560px; width: 720px; margin: 48px }
</style>
