/**
 * main.js
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

// Plugins
import { registerPlugins } from '@/plugins'

// Components
import App from './App.vue'

// Composables
import { createApp } from 'vue'
import router from './plugins/router'

const app = createApp(App)

// 注册路由
app.use(router)

//注册veritify
registerPlugins(app)

app.mount('#app')
