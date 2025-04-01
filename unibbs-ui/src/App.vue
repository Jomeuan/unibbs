<template>
  <v-app class="rounded rounded-md ">
    <v-main class="d-flex justify-center">

      <v-list  class="position-absolute top-0 left-0 ">
        <v-list-item >
          <v-icon icon="mdi-alpha-u"></v-icon>
          Uni-BBS
        </v-list-item>
        <v-list-item>
          <RouterLink to="/">首页dev</RouterLink>
        </v-list-item>
        <v-list-item>
          <RouterLink to="/search">搜索dev</RouterLink>
        </v-list-item>
        <!-- 临时社区dev用 -->
        <v-list-item>
          <RouterLink to="/community/index">社区首页dev</RouterLink>
        </v-list-item>
        <v-list-item>
          <RouterLink to="/login">登录</RouterLink>
        </v-list-item>

      </v-list>

      <RouterView />

    </v-main>

  </v-app>

</template>

<script setup>

</script>
