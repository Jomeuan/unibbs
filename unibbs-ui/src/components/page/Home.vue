<template>
    <VContainer class="mx-0">
        <v-row justify="center" dense>
            <VCol cols="12">
                helloworld!
            </VCol>
        </v-row>
    </VContainer>
    <PostList :posts="posts"></PostList>
</template>

<script setup>
import PostList from '../card/PostList.vue';
import { ref } from 'vue';

const posts = ref([])

fetch("http://localhost:9999/post/rank?size=20", {
    method: "GET"
}).then((response) => {
    if (!response.ok) {
        alert("获取失败,请重试")
    }
    return response.json();
}).then((data) => {
    posts.value = data
})

</script>