<template>
    <q-page class="window-height window-width row">
        <div class="col-xs-2">
            <q-page-sticky position="top-left" :offset="[10, 10]">
                <div class="q-mt-md">
                    <q-fab
                        label="Actions"
                        vertical-actions-align="left"
                        color="red"
                        icon="keyboard_arrow_down"
                        direction="down"
                    >
                        <q-fab-action
                            v-model="route"
                            color="red"
                            @click="onClickAdd"
                            icon="add"
                            label="add"
                        />
                        <q-fab-action
                            color="red"
                            @click="onClickDelete"
                            icon="delete"
                            label="delete"
                        />
                        <q-fab-action
                            color="red"
                            @click="onClickAnalytics"
                            icon="analytics"
                            label="configure"
                        />
                    </q-fab>
                </div>
            </q-page-sticky>
        </div>
        <div class="col">
            <importComponent v-if="route == 0" />
            <deleteComponent v-if="route == 1" />
        </div>
    </q-page>
</template>

<script>
import importComponent from "./importComponent.vue";
import deleteComponent from "./deleteComponent.vue";

export default {
    name: "DataImport",
    data() {
        return {
            route: 0,
        };
    },
    methods: {
        onClickAdd() {
            this.route = 0;
        },
        onClickDelete() {
            this.route = 1;
        },
    },
    components: { deleteComponent, importComponent },
    setup() {},
};
</script>
