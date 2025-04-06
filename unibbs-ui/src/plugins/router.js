import { createMemoryHistory, createRouter, createWebHistory } from 'vue-router'

import CommunityIndex from '@/components/page/CommunityIndex.vue'
import Home from '@/components/page/Home.vue'
import Login from '@/components/page/Login.vue'
import Search from '@/components/page/Search.vue'
import PostDetail from '@/components/page/PostDetail.vue'
import ProfilePage from '@/components/page/ProfilePage.vue'

const routes = [
    //   { path: '/about', component: AboutView },
    { path: "/community/index", component: CommunityIndex },
    { path: "/", component: Home },
    { path: "/login", component: Login },
    { path: "/search", component: Search },
    { path: "/post/detail", component: PostDetail },
    {path:"/profile",component:ProfilePage},
]

const router = createRouter({
    // history: createMemoryHistory(),
    history: createWebHistory(),
    routes,
})

export default router