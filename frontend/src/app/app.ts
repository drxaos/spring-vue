import { createApp } from 'vue'
import App from './App.vue'
import router from './app.route'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/lib/theme-chalk/index.css'
import locale from 'element-plus/lib/locale/lang/ru'
import 'dayjs/locale/ru'

createApp(App).use(store).use(router).use(ElementPlus, {locale}).mount('#app')
