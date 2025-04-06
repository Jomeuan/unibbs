<template>
    <v-container class="mx-0">
        <v-row v-if="getToken()" justify="center" dense>
            <v-col cols="12">
                <v-card>已经登录</v-card>
            </v-col>
        </v-row>

        <v-row v-else justify="center">
            <v-col>
                <v-text-field v-model="account" :readonly="loading" :rules="[required]"
                    label="账号Account"></v-text-field>
            </v-col>
        </v-row>
        <v-row>
            <v-col>
                <v-text-field v-model="password" type="password" :readonly="loading" :rules="[required]"
                    label="密码Password"></v-text-field>
            </v-col>
        </v-row>
        <v-row>
            <v-col>
                <v-btn :loading="loading" @click="login()">
                    登录
                </v-btn>
            </v-col>
            <v-col>
                <v-btn :loading="loading" @click="register()">
                    注册
                </v-btn>
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

// 注册
function login() {
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

// 注册兼登录
function register() {
    fetch("http://localhost:9999/security/auth/register", {
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
            alert("注册失败,请重试")
        }
        return response.json();
    }).then((data) => {
        if (data.code != "SUCCESS")
            alert(data.msg)
        else {
            alert("注册成功")
            localStorage.setItem("token", data.data.token);
            router.push({ path: "/profile", query: { userId: data.user.id } })
        }
    })
}

function required(v) {
    return !!v || 'Field is required'
}


</script>