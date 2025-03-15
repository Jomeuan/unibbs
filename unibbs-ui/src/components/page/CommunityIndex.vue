<template>

    <v-container width="500" min-height="3001211" class="mx-0">
        <v-row justify="center" dense>
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
        <!-- 帖子们 -->
        <v-row v-if="displayType == 'index'" justify="center" dense>
            <VCol cols="12" v-for="item in items">
                <PostCard :post="item"></PostCard>
            </VCol>
        </v-row>
        <!-- 规则 -->
        <v-row v-if="displayType == 'rule'" justify="center" dense>
            <VCol cols="12"> {{ communityContent.rule }}</VCol>
        </v-row>
        <!-- 管理员页 -->
        <v-row v-if="displayType == 'mods'" justify="center" dense>
            <VCol cols="6" v-for="mod in moderators">
                <v-card :title="mod.profile.nickname" :subtitle="mod.profile.account">
                </v-card>
            </VCol>
        </v-row>

    </v-container>

</template>

<script setup>

import { ref } from 'vue';
const community = ref(fetch("http://localhost:9999/community"))
const communityContent = community.value.communityContent
const moderators = community.value.moderators

const displayType = ref("index")

function setDisplayType(type) {
    displayType.value = type
}

</script>