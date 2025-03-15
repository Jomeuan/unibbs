import { createMemoryHistory, createRouter, createWebHistory } from 'vue-router'

import CommunityIndex from '@/components/page/CommunityIndex.vue'
import Home from '@/components/page/Home.vue'
import Login from '@/components/page/Login.vue'

const routes = [
    //   { path: '/about', component: AboutView },
    { path: "/community/index", component: CommunityIndex },
    { path: "/", component: Home },
    { path: "/login", component: Login },
]

const router = createRouter({
    // history: createMemoryHistory(),
    history: createWebHistory(),
    routes,
})

export default router