<template>
    <v-container width="500" min-height="3001211" class="mx-0">
        <v-row v-if="isLogin()" justify="center" dense>
            <v-col cols="12">
                <v-card>已经登录</v-card>
            </v-col>
        </v-row>

        <v-row v-else justify="center">
            <v-col cols="12">
                <v-form v-model="form" @submit.prevent="onSubmit">

                    <!-- loading作用:点击登录按钮用户不能再交互, -->
                    <v-text-field v-model="account" :readonly="loading" :rules="[required]" class="mb-2"
                        label="账号Account" clearable></v-text-field>

                    <v-text-field v-model="password" :readonly="loading" :rules="[required]" label="密码Password"
                        placeholder="Enter your password" clearable></v-text-field>

                    <v-btn :loading="loading" type="submit">
                        登录
                    </v-btn>
                </v-form>
            </v-col>
        </v-row>
    </v-container>
</template>

<script setup>

import router from '@/plugins/router';
import { ref } from 'vue';
import { getToken } from "@/util"


const form = ref(false);
const account = ref(null);
const password = ref(null);
const loading = ref(false);

function onSubmit() {
    fetch("http://localhost:9999/login/jwt", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            user: {
                account: account.value,
                password: password.value
            }
        })
    }).then((response) => {
        if (!response.ok) {
            alert("登录失败,请检查用户名或密码,首次登录前需先注册")
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return response.json();
    }).then((data) => {
        console.log(data.data.token)
        localStorage.setItem("token", data.data.token);
        router.push({ path: "/" })
    }).catch((err) => console.log(err))
}

function required(v) {
    return !!v || 'Field is required'
}

function isLogin() {
    var token = getToken();
    if (token == "undefined") {
        return false;
    } else {
        return true;
    }
}


</script>