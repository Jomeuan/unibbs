<template>
    <v-container v-if="posts" class="pa-0">
        <v-row v-for="post in posts">
            <v-col cols="12">
                <PostCard :post="post"></PostCard>
            </v-col>
        </v-row>
    </v-container>


</template>
<script setup>

const props = defineProps({
    posts: {
        type: Object,
        default: []
    }
})

</script>