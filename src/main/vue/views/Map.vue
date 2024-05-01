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

    new L.TileLayer('http://{s}.tiles.openrailwaymap.org/standard/{z}/{x}/{y}.png',
        {
            attribution: '<a href="https://www.openstreetmap.org/copyright">© OpenStreetMap contributors</a>, Style: <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA 2.0</a> <a href="http://www.openrailwaymap.org/">OpenRailwayMap</a> and OpenStreetMap',
            minZoom: 2,
            maxZoom: 19,
            tileSize: 256
        }).addTo(map);

    L.marker([52.0368609707235, 8.49525289846377]).addTo(map)
        .bindPopup('This is an example popup<br> for a marker.')
        .openPopup();
})

</script>

<template>
    <div id="map"></div>
</template>

<style>
#map { height: 720px; width: 95%; margin: 48px }
</style>
