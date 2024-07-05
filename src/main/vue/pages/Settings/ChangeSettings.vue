<script setup>

</script>

<template>
    <div class="justify-center">
        <div class="padding">
            <RouterLink to="/settings/edit/impressum">
                <q-btn label="Impressum bearbeiten" color="primary" outline />
            </RouterLink>
        </div>
        <div class="padding">
            <RouterLink to="/settings/edit/logo" class="padding">
                <q-btn label="Logo bearbeiten" color="primary" outline />
            </RouterLink>
        </div>
        <div class="padding">
            <RouterLink to="/settings/edit/colors">
                <q-btn label="Farben bearbeiten" color="primary" outline />
            </RouterLink>
        </div>
    </div>
</template>

<style scoped>
.padding {
    padding: 16px;
}
.justify-center {
    justify-content: center;
}
</style>
