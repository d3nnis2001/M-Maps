<script setup>
</script>

<template>
    <div class="bg-grey-4">
        <RouterLink to="/impressum">
            <div class="padding-all">
                <div class="text-black text-align justify-center">Impressum</div>
            </div>
        </RouterLink>
        <div class="divider bg-grey-6"></div>
        <div class="padding-all">
            <div class="text-black text-align justify-center">DB Systemtechnik GmbH</div>
        </div>
    </div>
</template>

<style scoped>
.padding-all {
    padding: 32px;
}

.divider {
    width: calc(100% - 32px);
    height: 2px;
    border-radius: 16px;
    margin: auto;
}

.text-align {
    text-align: center;
}
</style>
