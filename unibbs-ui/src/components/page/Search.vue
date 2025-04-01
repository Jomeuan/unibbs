<!-- 用来搜索帖子和板块(社区) -->
<template>
  <v-container width="500" class="mx-0">
    <!-- 搜索框 -->
    <v-row justify="center" >
      <v-col cols="12">
        <v-text-field v-model="keyword" :loading="loading" append-inner-icon="mdi-magnify" density="compact"
          label="Search templates" variant="solo" hide-details single-line @click:append-inner="onClick"></v-text-field>
      </v-col>
    </v-row>
    <!-- 板块搜索结果 -->
    <v-row v-if="communityResult!=null" >
      <v-col cols="12">
        <v-divider> 搜索结果:板块 </v-divider>
      </v-col>
    </v-row>
    <v-row justify="center" >
      <v-col cols="12" v-for="communityContentPo in communityResult">
        <CommunityCard :communityContent="communityContentPo"></CommunityCard>
      </v-col>
    </v-row>

    <!-- 用户搜索结果 -->
    <v-row v-if="profileResult!=null" >
      <v-col cols="12">
        <v-divider> 搜索结果:用户 </v-divider>
      </v-col>
    </v-row>
    <v-row justify="center" >
      <v-col cols="12" v-for="profilePo in profileResult">
        <ProfileCard :profile="profilePo"></ProfileCard>
      </v-col>
    </v-row>

    <!-- 帖子搜索结果 -->
    <v-row v-if="postResult!=null">
      <v-col cols="12">
        <v-divider> 搜索结果:帖子 </v-divider>
      </v-col>
    </v-row>
    <v-row justify="center" >
      <v-col cols="12" v-for="postVo in postResult">
        <PostCard :post="postVo"></PostCard>
      </v-col>
    </v-row>
  </v-container>

</template>

<script setup>
import { ref } from 'vue'
import PostCard from '../card/PostCard.vue'
import CommunityCard from '../card/CommunityCard.vue'

const loaded = ref(false)
const loading = ref(false)
const keyword = ref("")

const communityResult = ref()
const postResult = ref()
const profileResult = ref()



function onClick() {
  loading.value = true
  fetch("http://localhost:9999/community/find?keyword=" + keyword.value + "&page=1&limit=100")
    .then(response => response.json())
    .then(data => {
      console.log(data)
      communityResult.value = data
    })

  fetch("http://localhost:9999/post/find?keyword=" + keyword.value + "&page=1&limit=100")
    .then(response => response.json())
    .then(data => {
      console.log(data)
      postResult.value = data
    }
    )

  fetch("http://localhost:9999/profile/find?keyword=" + keyword.value + "&page=1&limit=100")
    .then(response => response.json())
    .then(data => {
      console.log(data)
      profileResult.value = data
    })

  // 加载动画
  setTimeout(() => {
    loading.value = false
    loaded.value = true
  }, 2000)
}



</script>