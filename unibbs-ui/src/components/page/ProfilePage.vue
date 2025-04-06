<template>
    <v-container>
        <!-- 个人信息 -->
        <v-row>
            <v-col>
                <!-- 昵称 -->
                <v-text-field v-model="profile.nickname" label="昵称"></v-text-field>
            </v-col>
            <v-col>
                <!-- 性别 -->
                <!-- <v-text-field v-model="profile.gender" label="性别"></v-text-field> -->
            </v-col>
            <v-col>
                <!-- 生日 -->
                <!-- <v-text-field v-model="profile.birthday" label="生日"></v-text-field> -->
            </v-col>
        </v-row>
        <v-row>
            <v-col>
                <!-- 电话 -->
                <v-text-field v-model="profile.phone" label="电话"></v-text-field>
            </v-col>
            <v-col>
                <!-- 邮件 -->
                <v-text-field v-model="profile.email" label="email"></v-text-field>
            </v-col>
        </v-row>
        <v-row>
            <v-col>
                <!-- 地址 -->
                <v-text-field v-model="profile.detail_address" label="地址"></v-text-field>
            </v-col>
        </v-row>
        <!-- 修改:非该用户不显示 -->
         <v-row v-if="auth.user.id == route.query.userId ">
            <v-col>
                <v-btn @click="saveProfile()">保存</v-btn>
            </v-col>
            <v-col>
                <v-btn @click="logout()">登出</v-btn>
            </v-col>
         </v-row>
         
        <!-- 分界线 -->
        <v-row >
            <v-col cols="12">
                <v-divider> </v-divider>
            </v-col>
        </v-row>
            <!-- 历史发言 -->

        <v-row>
            <v-col>
                <PostList :posts="posts"></PostList>
            </v-col>
        </v-row>

    </v-container>

    </template>

<script setup>
import { ref } from 'vue';
import PostList from '../card/PostList.vue';
import { getAuth, getToken } from '@/util';
import { useRoute, useRouter } from 'vue-router';
import Login from './Login.vue';

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
getAuth().then(data=>auth.value=data)

// 获取profile
const profile = ref({
    "id": null,
    "nickname": null,
    "avatar": null,
    "phone": null,
    "email": null,
    "join_date": null,
    "last_login_time": null,
    "birthday": null,
    "gender": null,
    "country": null,
    "address1": null,
    "address2": null,
    "address3": null,
    "detail_address": null
})
const route = useRoute()
fetch("http://localhost:9999/profile?userId=" + route.query.userId, {
    method: "GET",
    headers: {
        token: getToken()
    }
})
    .then(response => response.json())
    .then(data => profile.value = data)

// 获取用户的历史发言
const posts = ref([])
fetch("http://localhost:9999/post?userId=" + route.query.userId + "&page=1" + "&limit=100", {
    method: "GET",
    headers: {
        token: getToken()
    }
})
    .then(response => response.json())
    .then(data => posts.value = data.data)
    .then(data => {
        // for(var post of data){
        //     if(post.targetPost&&!post.targetPost.comment)
        //         console.log(post)
        // }
    })

    const router=useRouter()
// 登出
function logout(){
    localStorage.removeItem('token')
    router.push("/login")
    
}
// 修改profile
function saveProfile(){
    fetch("http://localhost:9999/profile",{
        method: "PUT",
        headers: {
            'Content-Type': 'application/json',
            token: getToken()
        },
        body: JSON.stringify(profile.value)
    }).then(response => response.json())
    .then(data => profile.value = data.data)
}

</script>