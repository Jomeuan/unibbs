<template>
    <v-container>
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
                <v-textarea v-model="newCommentContent" label="发表你的看法吧" variant="filled" auto-grow counter></v-textarea>
                <v-btn block @click="comment">发表</v-btn>
            </v-col>
        </v-row>

        <!-- 评论的帖子 -->
        <PostList :posts="comments"></PostList>

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
import { useRoute, useRouter } from 'vue-router';
import { getAuth, getToken } from '@/util';

const route = useRoute()
const router = useRouter()

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
    // console.log(data.data)
    return data.data
}))
// 发表
const newCommentContent = ref("")

async function comment() {
    if(!getToken()){
        alert("请先登录")
        return
    }
    fetch("http://localhost:9999/robot", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
            token: getToken()
        },
        body: JSON.stringify({
            action: {
                userId: (await getAuth()).user.id,
                targetId: post.value.thisPost.action.id,
                type:"COMMENT"
            }, comment: {
                content: newCommentContent.value,
            }
        })
    }).then((response) => {
        if (!response.ok) {
            alert("网络错误请重试,")
            throw new Error('Network response was not ok ' + response.statusText);
        }
        location.reload()
    })
}




</script>