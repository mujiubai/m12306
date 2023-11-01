import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import * as Icons from '@ant-design/icons-vue'
import axios from 'axios'
import { notification } from 'ant-design-vue';

const app=createApp(App);
app.use(Antd).use(store).use(router).mount('#app');

const icons=Icons;
for(const i in icons){
    app.component(i,icons[i]);
}


// axios拦截器
axios.interceptors.request.use(function(config){
    console.log('请求参数',config);
    if(store.state.member.token){
        config.headers.token=store.state.member.token;
        console.log("请求参数添加token：",store.state.member.token);
    }
    return config;
}, error =>{
    return Promise.reject(error);
});


axios.interceptors.response.use(function(response){
    console.log('返回结果',response);
    return response;
}, error =>{
    console.log('返回错误',error);
    const status=error.response.status;
    if(status==401){
        console.log("未登录或请求超时,跳转登录页");
        store.commit("setMember",{});
        notification.error({ description: "未登录或登录超时" });
        router.push('/login');
    }
    return Promise.reject(error);
});

axios.defaults.baseURL=process.env.VUE_APP_SERVER

console.log('启动环境',process.env.NODE_ENV)
console.log('服务器',process.env.VUE_APP_SERVER)