<template>
    <q-page>
        <importComponent v-if="route === 0" />
        <deleteComponent v-if="route === 1" />
            <q-page-sticky position="bottom-right" :offset="[10, 10]" >
                <div class="q-mt-lg">
                    <q-fab
                        label="Actions"
                        vertical-actions-align="left"
                        color="red"
                        icon="keyboard_arrow_left"
                        direction="left"
                    >
                        <q-fab-action
                            v-model="route"
                            color="red"
                            @click="onClickAdd"
                            icon="add"
                        />
                        <q-fab-action
                            color="red"
                            @click="onClickDelete"
                            icon="delete"
                        />
                        <q-fab-action
                            color="red"
                            @click="onClickAnalytics"
                            icon="analytics"
                        />
                    </q-fab>
                </div>
            </q-page-sticky>
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
