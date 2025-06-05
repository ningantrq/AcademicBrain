import { createApp } from "vue";
import "./style.css";
import App from "./App.vue";



import router from "./router/index";

import ElementPlus from "element-plus";
import "element-plus/dist/index.css";


import { createPinia } from "pinia";


// 引入动画
import 'animate.css';
import "@/assets/font_4394635_lwuldvb474/iconfont.css";

const app = createApp(App);

const pinia = createPinia();
app.use(pinia);

app.use(router);
app.use(ElementPlus);
app.mount("#app");

