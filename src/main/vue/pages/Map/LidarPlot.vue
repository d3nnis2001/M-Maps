<script setup>
import { ref, onMounted, onBeforeUnmount, watchEffect } from 'vue';
import * as THREE from 'three';
import {getVelodynPointsForTrackId} from "@/main/vue/api/map";
import {useQuasar} from "quasar";

const index = ref(0)
const velodynPoints = ref([])
const next = ref(true)
const $q = useQuasar()
var stop = false

const props = defineProps({
    streckenid: Number
})

const loadLidarData = async () => {
    console.log(props.streckenid)
    velodynPoints.value = await getVelodynPointsForTrackId(props.streckenid, index.value)
}


const target = ref();
var scene;
var camera;
var renderer;

const createGeometry = () => {
    var min = 100.0;
    var max = 0.0;

    const positions = []
    const intensities = [];
    const colors = []
    const color = new THREE.Color();

    velodynPoints.value[0].forEach((p) => {
        positions.push(p.x, p.y, p.z);
        intensities.push(p.intensity);
        if(p.intensity < min) min = p.intensity
        if(p.intensity > max) max = p.intensity
    });

    intensities.forEach((i) => {
        const l = intensityToViridisRGB(i, min, max)
        color.set(l.r / 255, l.g /255, l.b /255, THREE.SRGBColorSpace)
        colors.push(color.r, color.g, color.b)
    })

    const geometry = new THREE.BufferGeometry();

    geometry.setAttribute( 'position', new THREE.Float32BufferAttribute( positions, 3 ) );
    geometry.setAttribute( 'color', new THREE.Float32BufferAttribute( colors, 3 ) );
    geometry.computeBoundingSphere();
    const material = new THREE.PointsMaterial( { size: 0.05, vertexColors: true } );
    let points = new THREE.Points( geometry, material );
    scene.add( points );
}


const init = () => {
    scene = new THREE.Scene();
    scene.background = new THREE.Color(0x000000);

    camera = new THREE.PerspectiveCamera(25, window.innerWidth / window.innerHeight, 1, 1000);
    camera.up = new THREE.Vector3(0,0,1)

    let axes = new THREE.AxesHelper(25);

    scene.add(axes);

    createGeometry();

    renderer = new THREE.WebGLRenderer();
    renderer.setSize(window.innerWidth, window.innerHeight);
    target.value.appendChild(renderer.domElement);
}

const animate = (t) => {
    let time = t / 10000;
    camera.position.set(
        Math.sin(time) * 70,
        Math.cos(time) * 70,
        30);
    camera.lookAt(0,0,0);
    renderer.render(scene , camera);
    if(!stop)
        requestAnimationFrame(animate);
}

function normalizeIntensity(intensity, minIntensity, maxIntensity) {
    if (minIntensity === maxIntensity) {
        return 0;
    }
    return (intensity - minIntensity) / (maxIntensity - minIntensity);
}
function interpolateColor(t, color1, color2) {
    const r = Math.round(color1[0] + t * (color2[0] - color1[0]));
    const g = Math.round(color1[1] + t * (color2[1] - color1[1]));
    const b = Math.round(color1[2] + t * (color2[2] - color1[2]));
    return [r, g, b];
}
function viridisColormap(t) {
    const viridisColors = [
        [68, 1, 84],
        [59, 82, 139],
        [33, 145, 140],
        [94, 201, 97],
        [253, 231, 36]
    ];

    const steps = viridisColors.length - 1;
    const step = Math.floor(t * steps);

    const nextStep = Math.min(step + 1, steps);

    const localT = (t * steps) - step;

    const color1 = viridisColors[step];
    const color2 = viridisColors[nextStep];

    return interpolateColor(localT, color1, color2);
}

function intensityToViridisRGB(intensity, minIntensity, maxIntensity) {
    const normalizedIntensity = normalizeIntensity(intensity, minIntensity, maxIntensity);

    const clampedIntensity = Math.max(0, Math.min(1, normalizedIntensity));

    const [red, green, blue] = viridisColormap(clampedIntensity);

    return { r:red, g:green, b:blue };
}


onMounted(async () => {
    await loadLidarData();
    if(velodynPoints.value.length === 0) {
        $q.notify({
            message: "Keine Lidar-Daten verfügbar!",
            timeout: 5000,
        });
    }
    else if(velodynPoints.value.length > 0) {
        init();
        animate(0);
        next.value = false
    }

});

const incIndex = async () => {
    index.value = index.value + 1;
    const data = await getVelodynPointsForTrackId(props.streckenid, index.value)
    if(data.length > 0) {
        stop = false
        velodynPoints.value = data
        target.value.removeChild(renderer.domElement)
        init()
        animate()
    }
    else {
        index.value = index-value - 1;
        next.value = true
    }
}

const decIndex = async ()  => {
    if(index.value > 0) {
        index.value = index.value - 1;
        const data = await getVelodynPointsForTrackId(props.streckenid, index.value)
        stop = false
        velodynPoints.value = data
        target.value.removeChild(renderer.domElement)
        init()
        animate()
    }
}

const handleClick = () => {
    if(stop === true) {
        stop = false
        animate(0);
    }
    else {
        stop = true
    }
}

</script>

<template>
    <div>
        <div ref="target" @click="handleClick"></div>
        <q-card flat square bordered>
            <div class="row">
                <q-btn
                    :disable="index === 0"
                    flat
                    no-caps
                    @click="decIndex"
                    color="primary"
                    icon="arrow_back"
                    label="Zurück"
                />
                <q-btn
                    flat
                    no-caps
                    @click="incIndex"
                    color="primary"
                    icon="arrow_forward"
                    label="Vor"
                    :disable="next"
                />
            </div>
        </q-card>
    </div>
</template>

<style scoped>
</style>
