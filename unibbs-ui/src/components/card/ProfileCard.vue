<template>
    <!-- 用户昵称,用户账号 -->
    <VCard :title="props.profile.nickname"  max-width="500">
        <!-- 内容 -->
        <v-card-text class="text-h5 py-2">
            {{ props.addition }}
        </v-card-text>
    </VCard>

</template>

<script setup>

const props = defineProps({
    // 对应一个profilePo
    profile:Object,
    addition:String

})

</script>