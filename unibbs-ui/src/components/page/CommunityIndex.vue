<template>

    <v-container width="500"class="mx-0">
        <v-row >
            <VCol cols="12">
                <v-card :title="communityContent.title" :subtitle="communityContent.introduction" color="blue">
                    <v-card-actions>
                        <v-btn @click="setDisplayType('index')">帖子 </v-btn>
                        <v-btn @click="setDisplayType('rule')">规则 </v-btn>
                        <v-btn @click="setDisplayType('mods')">管理员</v-btn>
                    </v-card-actions>
                </v-card>
            </VCol>
        </v-row>
        <!-- 发帖文本框 -->
        <!-- 帖子们 -->
        <v-row v-if="displayType == 'index'">
            <VCol cols="12" v-for="post in posts">
                <PostCard :post="post"></PostCard>
            </VCol>
        </v-row>
        <!-- 规则 -->
        <v-row v-if="displayType == 'rule'">
            <VCol cols="12"> {{ communityContent.rule }}</VCol>
        </v-row>
        <!-- 管理员页 -->
        <v-row v-if="displayType == 'mods'">
            <VCol cols="6" v-for="mod in moderators">
                <v-card :title="mod.profile.nickname" :subtitle="mod.moderator.title">
                </v-card>
            </VCol>
        </v-row>

    </v-container>

</template>

<script setup>

import { getToken } from '@/util';
import { ref } from 'vue';
import { useRoute } from 'vue-router';
// TODO: 
const props = defineProps({
    communityId: Number
})
// 初始化数据
const communityContent = ref({
    title: "community",
    introduction: "introdction"
})
const moderators = ref([])
const posts = ref([])

const communityId = ref("1887803077897834497")
// const communityId = ref(route.query.communityId)
// const page = ref(route.query.page);
const page = ref(1);
const limit = ref(10);
const community = ref(fetch("http://localhost:9999/community/index?communityId=" + communityId.value
    + "&page=" + page.value + "&limit=" + limit.value, {
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
    communityContent.value = data.data.communityContent
    moderators.value = data.data.moderators
    posts.value = data.data.posts
    // console.log(json.data)
})
    .catch((err) => console.log(err)))


const displayType = ref("index")

function setDisplayType(type) {
    displayType.value = type
}

</script>