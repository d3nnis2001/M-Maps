<script setup>

</script>

<template>
    <div class="full-height-center bg-grey-2 padding-xl">
        <q-card class="q-pa-md content-container items-center" flat>
            <q-card-section class="inner-card">
                <slot></slot>
            </q-card-section>
        </q-card>
    </div>
</template>

<style scoped>

.full-height-center {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    width: 100%;
}

.padding-xl {
    padding-bottom: 64px;
}

.content-container {
    width: calc(100% - 48px);
    max-width: 384px;
    padding: 16px;
    height: 100%
}

</style>
