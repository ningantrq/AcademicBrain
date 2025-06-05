import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import { resolve } from "path";

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      "@": resolve(__dirname, "./src"),
    },
    extensions: [".mjs", ".js", ".ts", ".jsx", ".tsx", ".json", ".vue"],
  },
  server: {
    open: true,
    proxy: {
      // 代理 /api 路径，转发到后端 http://localhost:8080
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '/api'), // 实际没改，可以省略rewrite
      },

      // 你原先的 /oss 代理示例，这里我帮你修正了 rewrite
      '/oss': {
        target: 'http://localhost:88/api/platform/',
        ws: true,
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/oss/, '/oss'), // 这里一般是替换成空或者其他路径，看你后端接口具体
      },
    },
  },
});
