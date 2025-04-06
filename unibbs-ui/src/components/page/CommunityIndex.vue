<template>

    <v-container class="mx-0">
        <v-row>
            <VCol cols="12">
                <v-card color="blue">
                    <v-card-title>
                        {{ communityContent.title }}
                    </v-card-title>
                    <v-card-subtitle>
                        {{ communityContent.introduction }}
                    </v-card-subtitle>
                    <v-card-actions>
                        <v-btn @click="setDisplayType('index')">帖子 </v-btn>
                        <v-btn @click="setDisplayType('rule')">信息 </v-btn>
                        <v-btn @click="setDisplayType('mods')">管理员</v-btn>
                    </v-card-actions>
                </v-card>
            </VCol>
        </v-row>

        <!-- 帖子们 -->
        <v-row v-if="displayType == 'index'">
            <!-- 发帖子 -->
            <v-col cols="12">
                <v-textarea v-model="newCommentContent" label="发表你的看法吧" variant="filled" auto-grow counter></v-textarea>
                <v-btn block @click="comment()">发表</v-btn>
            </v-col>
            <VCol cols="12">
                <PostList :posts="posts"></PostList>
            </VCol>
        </v-row>

        <!-- 信息页 -->
        <v-row v-if="displayType == 'rule'">
            <v-col cols="12">
                <v-text-field v-model="newCommunityContent.title" label="名称" :disable="isModerator"></v-text-field>
            </v-col>
        </v-row>
        <v-row v-if="displayType == 'rule'">
            <v-col cols="12">
                <v-textarea v-model="newCommunityContent.introduction" label="介绍" :disable="isModerator"></v-textarea>
            </v-col>
        </v-row>
        <v-row>
            <v-col cols="12" v-if="displayType == 'rule'">
                <v-textarea v-model="newCommunityContent.rule" label="规则" :disable="isModerator"></v-textarea>
            </v-col>
        </v-row v-if="displayType == 'rule'">
        <v-row v-if="displayType == 'rule' && isModerator">
            <v-col>
                <v-btn @click="updateCommunityContent()">修改</v-btn>
            </v-col>
            <v-col>
                <v-btn @click="resetNewCommunityContent()">重置修改信息</v-btn>
            </v-col>
        </v-row>

        <!-- 管理员页 -->
        <v-row v-if="displayType == 'mods'">
            <!-- 管理员列表 -->
            <v-col cols="6" v-for="mod in moderators">
                <v-card :title="mod.profile.nickname" :subtitle="mod.moderator.title">
                </v-card>
            </v-col>
        </v-row>
        <v-row v-if="displayType == 'mods' && isModerator">
            <!-- 添加管理员 -->
            <v-col cols="6">
                <v-btn @click="addDialog = true" class="text-center"> 添加管理员
                </v-btn>
                <!-- 里面是搜索并选择用户 -->
                <v-dialog v-model="addDialog" max-width="500px">
                    <v-card title="添加管理员">
                        <!-- 搜索框 -->
                        <v-card-text>
                            <v-text-field v-model="keyword" append-inner-icon="mdi-magnify" density="compact"
                                label="Search user" hide-details @click:append-inner="searchUser()"></v-text-field>
                        </v-card-text>
                        <v-card-text>
                            <v-card v-for="user in searchResult" @click="selectedAdd = user"
                                :color="selectedAdd == user ? 'primary' : 'black'">
                                <v-card-text class="text-caption">{{ user.nickname }} </v-card-text>
                                <v-card-text class="text-caption opacity-40">{{ user.id }} </v-card-text>
                            </v-card>

                            <!-- <ProfileCard v-for="user in searchResult" :profile="user" :addition="user.id"></ProfileCard> -->
                            <!-- 搜索结果 -->
                            <!-- <v-checkbox v-model="selectedAdd" v-for="user in searchResult" :label="user.nickname"
                                :value="user.nickname"></v-checkbox> -->
                        </v-card-text>
                        <v-car-action-actions>
                            <!-- 添加 -->
                            <v-btn @click="addMod()"> 确认</v-btn>
                            <!-- 取消 -->
                            <v-btn @click="addDialog = false"> 取消 </v-btn>
                        </v-car-action-actions>
                    </v-card>
                </v-dialog>
            </v-col>
            <!-- 删除管理员 -->
            <v-col cols="6" >
                <v-btn @click="delDialog = true" class="text-center"> 删除管理员
                </v-btn>
                <v-dialog v-model="delDialog" max-width="500px">
                    <v-card title="删除管理员">
                        <v-card-text>
                            <v-card v-for="mod in moderators" :profile="user" @click="selectedDel = mod"
                                :color="selectedDel == mod ? 'primary' : 'black'">
                                <v-card-text class="text-caption">{{ mod.profile.nickname }} </v-card-text>
                                <v-card-text class="text-caption opacity-40">{{ mod.profile.id }} </v-card-text>
                            </v-card>

                            <!-- <ProfileCard v-for="user in searchResult" :profile="user" :addition="user.id"></ProfileCard> -->
                            <!-- 搜索结果 -->
                            <!-- <v-checkbox v-model="selectedAdd" v-for="user in searchResult" :label="user.nickname"
                                :value="user.nickname"></v-checkbox> -->
                        </v-card-text>
                        <v-car-action-actions>
                            <!-- 添加 -->
                            <v-btn @click="delMod()"> 确认</v-btn>
                            <!-- 取消 -->
                            <v-btn @click="delDialog = false"> 取消 </v-btn>
                        </v-car-action-actions>
                    </v-card>
                </v-dialog>
            </v-col>

        </v-row>
    </v-container>

</template>

<script setup>
import { getAuth, getToken } from '@/util';
import { computed, ref } from 'vue';
import { useRoute } from 'vue-router';
import PostList from '../card/PostList.vue';
import ProfileCard from '../card/ProfileCard.vue';

const route = useRoute()
// auth
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
getAuth().then(data => {
    auth.value = data
})


// 初始化数据
const communityContent = ref({
    title: "",
    introduction: "",
    rule: ""
})
const moderators = ref([])
const posts = ref([])
const communityId = ref(route.query.communityId)
const page = ref(1);
const limit = ref(30);
ref(fetch("http://localhost:9999/community/index?communityId=" + communityId.value
    + "&page=" + page.value + "&limit=" + limit.value, {
    method: "GET",
    headers: {
        token: getToken()
    }
}).then((response) => {
    return response.json()
}).then((data) => {
    communityContent.value = data.data.communityContent
    moderators.value = data.data.moderators
    posts.value = data.data.posts
    newCommunityContent.value = JSON.parse(JSON.stringify(communityContent.value))
    // console.log(json.data)
})
    .catch((err) => console.log(err)))

// 展示的页面:帖子,规则,管理员
const displayType = ref("index")
function setDisplayType(type) {
    displayType.value = type
}

//发帖子 
const newCommentContent = ref("")
async function comment() {
    if (!getToken()) {
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
                userId: auth.value.user.id,
                targetId: communityId.value,
                type: "COMMENT_BROADCAST"
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
// 判断是否是管理员
const isModerator = computed(() => {
    if (!getToken()) return false
    // 判断管理员id数组中包含userID
    return moderators.value.map((moderator) => moderator.moderator.userId).indexOf(auth.value.user.id) != -1
})

// 添加管理员
const keyword = ref("")
const addDialog = ref(false)
// profilePo
const selectedAdd = ref({})
const searchResult = ref([])
function searchUser() {
    fetch("http://localhost:9999/profile/find?keyword=" + keyword.value + "&page=1&limit=100", {
        method: "GET"
    }).then(response => response.json())
        .then(data => {
            searchResult.value = data
            console.log(searchResult.value)
        })
}
async function addMod() {
    // TODO;要在此处确认管理员身份吗
    fetch("http://localhost:9999/community/moderator", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            token: getToken()
        },
        body: JSON.stringify({
            "action": {
                "userId": (await getAuth()).user.id,
                "targetId": communityId.value
            },
            "moderators": [
                {
                    "moderator": {
                        userId: selectedAdd.value.id
                    }
                }
            ]
        })
    }).then(response => response.json())
        .then(data => {
            if (data.code == "ERROR") alert(data.msg)
            else moderators.value = data.data.moderators
        })
}
// 删除管理员
const delDialog = ref(false)
const selectedDel = ref({})
async function delMod() {
    // TODO;要在此处确认管理员身份吗
    fetch("http://localhost:9999/community/moderator", {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            token: getToken()
        },
        body: JSON.stringify({
            "action": {
                "userId": auth.value.user.id,
                "targetId": communityId.value
            },
            "moderators": [
                {
                    "moderator": {
                        userId: selectedDel.value.profile.id
                    }
                }
            ]
        })
    }).then(response => response.json())
        .then(data => {
            if (data.code == "ERROR") alert(data.msg)
            else moderators.value = data.data.moderators
        })
}
// 修改板块信息
const newCommunityContent = ref(JSON.parse(JSON.stringify(communityContent.value)))
function updateCommunityContent() {
    fetch("http://localhost:9999/community", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            token: getToken()
        },
        body: JSON.stringify({
            "action": {
                "userId": auth.value.user.id,
                "targetId": communityId.value
            },
            "communityContent": newCommunityContent.value
        })
    }).then(response => response.json())
        .then(data => {
            if (data.code == "ERROR") alert(data.msg)
            else location.reload()
        })
}

//重置修改的板块信息
function resetNewCommunityContent() {
    newCommunityContent.value = JSON.parse(JSON.stringify(communityContent.value))
}

</script>