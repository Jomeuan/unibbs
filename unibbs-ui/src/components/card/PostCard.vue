

<template>
    <!-- 用户昵称,用户账号 -->
    <VCard v-if="post.thisPost">
        <v-card-title>{{ post.thisPost.profile.nickname }}</v-card-title>
        <v-card-subtitle>{{ post.thisPost.profile.account }}</v-card-subtitle>

        <!-- 内容 -->
        <v-card-text class="text-body-1">
            {{ post.thisPost.comment.content }}
        </v-card-text>
        <v-card-text class="text-caption opacity-40 my-0 py-0">
            {{ post.thisPost.action.time }}
        </v-card-text>

        <!-- target内容 -->
        <v-card v-if="post.targetPost" :title="post.targetPost.profile.nickname"
            :subtitle="post.targetPost.profile.account" @click="openPostDetail(post.targetPost.action.id)">
            <v-card-text class="text-body2">
                {{ post.targetPost.comment.content }}
            </v-card-text>
            <v-card-text class="text-caption">
                {{ post.targetPost.action.time }}
            </v-card-text>
        </v-card>

        <v-card-actions class="py-0">
            <!-- TODO: 点赞 -->
            <v-btn class="py-0">
                <v-icon icon="mdi-thumb-up-outline" class="px-4 my-0" @click="like()"></v-icon>
                {{ post.thisPost.comment.likesCount }}
            </v-btn>
            <!-- 打开详情页 -->
            <v-btn class="py-0" @click="openPostDetail(post.thisPost.action.id)">
                <v-icon icon="mdi-comment-eye-outline" class="px-4 my-0"></v-icon>
                {{ post.thisPost.comment.commentsCount }}
            </v-btn>
            <!-- 浏览数 TODO:后端实现 -->
            <v-btn class="py-0">
                <v-icon icon="mdi-eye-circle-outline" class="px-4 "></v-icon>
                {{ Math.ceil(Math.random() * 100) }}
            </v-btn>

            <!-- 额外功能 -->
            <v-menu>
                <template v-slot:activator="{ props }">
                    <v-btn icon="mdi-dots-vertical" variant="text" v-bind="props"></v-btn>
                </template>

                <v-list>
                    <!-- TODO: 删除评论 -->
                    <v-list-item>
                        <v-btn>
                            <v-icon icon="mdi-delete"></v-icon>
                            删除
                        </v-btn>

                    </v-list-item>
                </v-list>

            </v-menu>
        </v-card-actions>
    </VCard>

</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const props = defineProps({
    // 对应一个postVo{thisPost,targetPost}
    post: Object
})


// console.log(props.post)
const router = useRouter()
 
function openPostDetail(postId) {
    router.replace({ path: "/post/detail", query: { postId: postId } })
}
function like(){
}

</script>