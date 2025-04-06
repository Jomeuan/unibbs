<template>
    <!-- 用户昵称,用户账号 -->
    <VCard v-if="post.thisPost">
        <v-card-title>{{ post.thisPost.profile.nickname }}</v-card-title>
        <v-card-subtitle>{{ post.thisPost.profile.account }}</v-card-subtitle>

        <!-- 内容 -->
        <!-- 如果是评论或广播 -->
        <v-card-text v-if="post.thisPost.action.type == 'COMMENT' || post.thisPost.action.type == 'COMMENT_BROADCAST'"
            class="text-body-1">
            {{ post.thisPost.comment.content }}
        </v-card-text>
        <!-- 或者是点赞 -->
        <v-card-text v-else-if="post.thisPost.action.type == 'LIKE'" class="text-caption-1">
            {{ post.thisPost.profile.nickname }}
            <v-icon icon="mdi-thumb-up-outline" class="px-4 my-0 text-red"> </v-icon>
            {{ post.targetPost.profile.nickname }}
        </v-card-text>

        <!-- 时间 -->
        <v-card-text class="text-caption opacity-40 my-0 py-0">
            {{ post.thisPost.action.time }}
        </v-card-text>

        <!-- target内容 -->
        <v-card v-if="post.targetPost" class="ma-3 text-grey" :title="post.targetPost.profile.nickname"
            :subtitle="post.targetPost.profile.account" @click="openPostDetail(post.targetPost.action.id)">
            <!-- TODO:修改的显示 -->
            <v-card-text v-if="post.targetPost.comment" class="text-body2">
                {{ post.targetPost.comment.content }}
            </v-card-text>
            <v-card-text class="text-caption opacity-40 my-0 py-0">
                {{ post.targetPost.action.time }}
            </v-card-text>
        </v-card>

        <!--  -->
        <v-card-actions class="py-0"
            v-if="post.thisPost.action.type == 'COMMENT' || post.thisPost.action.type == 'COMMENT_BROADCAST'">
            <!-- 点赞 -->
            <v-btn class="py-0 ">
                <v-icon icon="mdi-thumb-up-outline" :class="liked ? 'px-4 my-0 text-red' : 'px-4 my-0 text-white'"
                    @click="like(post.thisPost.action.id)"></v-icon>
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
                    <v-list-item>
                        <v-btn @click="deletePost()">
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
import { getAuth, getToken } from '@/util';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const props = defineProps({
    // 对应一个postVo{thisPost,targetPost}
    post: {
        type: Object,
        default(rawProps) {
            return {
                "thisPost": {
                    "action": {
                        "id": "",
                        "userId": "",
                        "targetId": "",
                        "type": "COMMENT",
                        "contentId": "",
                        "time": ""
                    },
                    "comment": {
                        "id": "",
                        "content": "",
                        "likesCount": 0,
                        "commentsCount": 0,
                        "collectionsCount": 0,
                        "pullCount": 0
                    },
                    "profile": {
                        "id": "",
                        "nickname": ""
                    }
                },
                "targetPost": {
                    "action": {
                        "id": "",
                        "userId": "",
                        "targetId": "",
                        "type": "COMMENT",
                        "contentId": "",
                        "time": ""
                    },
                    "comment": {
                        "id": "",
                        "content": "",
                        "likesCount": 0,
                        "commentsCount": 0,
                        "collectionsCount": 0,
                        "pullCount": 0
                    },
                    "profile": {
                        "id": "",
                        "nickname": "",
                    }
                }
            }
        }
    },
})


// console.log(props.post)
const router = useRouter()

function openPostDetail(postId) {
    router.push({ path: "/post/detail", query: { postId: postId } })
}
// 用户发布点赞
function like(postId) {
    if (!getToken())
        alert("请先登录")
    else {
        var likeResponse = fetch("http://localhost:9999/post/like", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                token: getToken()
            },
            body: JSON.stringify({
                action: {
                    userId: auth.value.user.id,
                    targetId: postId,
                    type: "LIKE",
                    contentId: 1
                }
            })
        }).then(response => {
            if (!response.ok) {
                alert("请稍后重试")
            }
            return response.json()
        }).then(data => {
            props.post.thisPost.comment.likesCount += (liked.value ? -1 : 1)
            liked.value = data.data.action.contentId
            isLiked(props.post.thisPost.action.id).then(data => liked.value = data)
        })
    }
}

const liked = ref(false)
if (props.post.thisPost) isLiked(props.post.thisPost.action.id).then(data => liked.value = data)

const auth = ref({
    user: {
        id: "",
        account: null,
        password: null,
        state: null,
        exipration: null
    },
    roles: [
        {
            id: null,
            name: null
        },
    ]
})

getAuth().then(data => auth.value = data)

function isLiked(postId) {
    return new Promise((resolve) => {
        if (!getToken()) {
            resolve(false)
        } else {
            fetch("http://localhost:9999/post/like?postId=" + postId, {
                method: "GET",
                headers: {
                    token: getToken()
                }
            })
                .then(response => response.text())
                .then(data => resolve(data % 2 == 1))
        }
    })
}

function deletePost() {
    if (!getToken()) {
        alert("请先登录")
    } else {
        fetch("http://localhost:9999/post", {
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json',
                token: getToken()

            },
            body: JSON.stringify({
                "action": {
                    "userId": auth.value.user.id,
                    "targetId": props.post.thisPost.action.id,
                }
            })
        }).then(response => response.ok ? response.json() : alert(response.json().message))
            .then(data => location.reload())
    }
}

</script>