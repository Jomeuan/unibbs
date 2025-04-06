<template>
    <v-app>
        <v-layout>

            <v-navigation-drawer v-if="!mobile" floating />

            <v-navigation-drawer v-model="drawer" class="position-fixed">
                <v-list>
                    <v-list-item>
                        <RouterLink to="/" :key="$route.fullPath">热榜</RouterLink>
                    </v-list-item>
                    <v-list-item>
                        <RouterLink to="/search">搜索</RouterLink>
                    </v-list-item>
                    <!-- 临时社区dev用 -->
                    <v-list-item>
                        <RouterLink to="/community/index?communityId=1887803077897834497">社区首页dev</RouterLink>
                    </v-list-item>
                    <v-list-item>
                        <RouterLink to="/post/detail?postId=1894225184777150466">帖子详情dev</RouterLink>
                    </v-list-item>
                    <v-list-item>
                        <RouterLink :to="getToken() ? '/profile?userId=' + auth.user.id : '/login'">个人中心</RouterLink>
                    </v-list-item>
                    <v-list-item>
                        <v-btn @click="quickLogin">快速登录(作弊)</v-btn>
                    </v-list-item>
                </v-list>
            </v-navigation-drawer>
            <v-navigation-drawer v-if="!mobile" location="right" floating />
            <!-- <v-navigation-drawer v-if="!mobile" location="right" floating /> -->

            <v-app-bar density="compact">
                <template v-slot:prepend>
                    <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
                </template>
                <v-app-bar-title>
                    <v-icon icon="mdi-alpha-u" />
                    Uni-BBS
                </v-app-bar-title>
            </v-app-bar>

            <v-main>
                <RouterView :key="$route.fullPath" />
            </v-main>

        </v-layout>
    </v-app>

</template>
<script setup>
import { ref } from 'vue';
import { useDisplay } from 'vuetify'
import { useRoute, useRouter } from 'vue-router';
import { getAuth, getToken } from './util';

const route = useRoute()
const router = useRouter()
const { mobile } = useDisplay()
const drawer = ref(!mobile.value)
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
// console.log(getToken())
// 填充auth
getAuth().then(data => auth.value = data)
//登录一个nicknamee的账号
function quickLogin() {
    localStorage.setItem("token", "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyQXV0aGVudGljYXRpb24iOnsidXNlciI6eyJpZCI6MTg4Nzc1MzA3ODQ0MjgyMzY4MiwiYWNjb3VudCI6IjEyNDYiLCJwYXNzd29yZCI6bnVsbCwic3RhdGUiOm51bGwsImV4aXByYXRpb24iOm51bGx9LCJyb2xlcyI6W3siaWQiOjIsIm5hbWUiOiJWSVNJVE9SIn0seyJpZCI6MywibmFtZSI6Ik1PREVSQVRPUiJ9XX19.j7FbMCne4ZdCrloWkQ0ORsBAOkqHwGKEvbULnu5jQEI")
    getAuth().then(data=>auth.value=data)
    router.push("/")
}

</script>