<script setup>
import { ref } from 'vue';

const props = defineProps({
    // 对应一个postVo{thisPost,targetPost}
    post: Object
})
const thisPost = props.post.thisPost
const targetPost = props.post.targetPost

</script>

<template>
    <!-- 用户昵称,用户账号 -->
    <VCard :title="thisPost.profile.nickname" :subtitle="thisPost.profile.account" max-width="500">
        <!-- 内容 -->
        <v-card-text class="text-h5 py-2">
            {{ thisPost.comment.content }}
        </v-card-text>

        <v-card-actions>
            <!-- TODO: 点赞 -->
            <v-btn>
                <v-icon icon="mdi-thumb-up-outline"></v-icon>
                {{ thisPost.comment.likeCounts }}
            </v-btn>
            <!-- 评论数, TODO: 点击跳转详情页 -->
            <v-btn>
                <v-icon icon="mdi-comment-eye-outline"></v-icon>
                {{ thisPost.comment.commentCounts }}
            </v-btn>
            <v-btn>
                <v-icon icon="mdi-eye-circle-outline"></v-icon>
                {{ Math.ceil(Math.random()*100) }}
            </v-btn>
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