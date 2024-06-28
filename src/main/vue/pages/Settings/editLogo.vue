<script setup>

</script>

<template>
    <q-uploader
        style="max-width: 300px"
        url="http://localhost:8088/api/settings/"
        label="Restricted to images"
        multiple
        accept=".jpg, image/*"
        @rejected="onRejected"
    />
</template>

<style scoped>

</style>
