<template>
    <v-container width="600">
        <!-- 楼主的帖子 -->
        <v-row>
            <v-col cols="12">

                <PostCard :post="post"></PostCard>
                <!-- <div v-if="post">{{ "postDetail != null" }}</div> -->

            </v-col>
        </v-row>
        <!-- 写评论 -->
        <v-row>
            <v-col cols="12">
                <PublishCard></PublishCard>
            </v-col>
        </v-row>

        <!-- 评论的帖子 -->
        <v-row>
            <!-- 一级评论的post -->
            <v-col cols="12" v-for="comment in comments">
                <PostCard :post="comment.post"></PostCard>
                <!-- 一级评论的评论 -->
                <PostCard v-for="commentPost in comment.comments" :post="commentPost"></PostCard>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import PublishCard from '../card/PublishCard.vue';
import { getToken } from '@/util';

const route = useRoute()

const post = ref({})
const comments = ref([])

const postId = ref(route.query.postId)
const postDetail = ref(fetch("http://localhost:9999/post/detail?actionId=" + postId.value, {
    method: "GET",
    headers: {
        token: getToken()
    }
}).then((response) => {
    if (!response.ok) {
        alert("网络错误请重试,")
        throw new Error('Network response was not ok ' + response.statusText);
    }
    return response.json()
}).then((data) => {
    post.value = data.data.post
    comments.value = data.data.comments
    console.log(data.data)
    return data.data
}))




</script>