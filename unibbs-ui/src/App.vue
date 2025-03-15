<template>
  <v-app class="rounded rounded-md ">
    <v-main class="d-flex justify-center">

      <v-list>
        <v-list-item>
          <RouterLink to="/">首页dev</RouterLink>
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
