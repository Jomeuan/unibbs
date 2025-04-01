<template>
    <!-- 用户昵称,用户账号 -->
    <VCard :title="props.communityContent.title"  max-width="500" @click="openCommunityIndex">
        <!-- 内容 -->
         <v-card-text class="text-h5 py-2">
            {{  props.communityContent.introduction }}
            {{ props.communityContent.id }}
        </v-card-text>


        <v-card-text class="text-body-1">
            {{ props.addition }}
        </v-card-text>
    </VCard>

</template>

<script setup>
import { useRouter } from 'vue-router';


const router=useRouter()


const props = defineProps({
    // 对应一个CommunityContentPo
    communityContent:Object,
    addition: String
})

function openCommunityIndex(){
    router.replace({path:"community/index",query:{communityId:props.communityContent.id,page:1}})
}

</script>