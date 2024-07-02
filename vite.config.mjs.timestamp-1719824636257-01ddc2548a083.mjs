// vite.config.mjs
import { fileURLToPath, URL } from "url";
import { defineConfig } from "file:///C:/Users/smrah/gp-se-ss-2024-team1-2/node_modules/vite/dist/node/index.js";
import vue from "file:///C:/Users/smrah/gp-se-ss-2024-team1-2/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import { quasar, transformAssetUrls } from "file:///C:/Users/smrah/gp-se-ss-2024-team1-2/node_modules/@quasar/vite-plugin/src/index.js";
import { VitePWA } from "file:///C:/Users/smrah/gp-se-ss-2024-team1-2/node_modules/vite-plugin-pwa/dist/index.js";
var __vite_injected_original_import_meta_url = "file:///C:/Users/smrah/gp-se-ss-2024-team1-2/vite.config.mjs";
var vite_config_default = defineConfig({
  plugins: [
    vue(
      {
        template: transformAssetUrls
      }
    ),
    VitePWA({
      registerType: "autoUpdate",
      manifest: {
        name: "example-application",
        short_name: "example",
        description: "This is an example application",
        theme_color: "#ffffff",
        icons: [
          {
            src: "android-chrome-512x512.png",
            sizes: "512x512",
            type: "image/png",
            purpose: "any maskable"
          }
        ]
      },
      devOptions: {
        enabled: true
      }
    }),
    quasar({
      sassVariables: "src/main/vue/quasar-variables.sass"
    })
  ],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", __vite_injected_original_import_meta_url))
    }
  },
  server: {
    proxy: {
      /*
          proxy all webpack dev-server requests starting with /api
          to our Spring Boot backend (localhost:8088) using http-proxy-middleware
          see https://vitejs.dev/config/#server-proxy
      */
      "/api": {
        target: "http://localhost:8088",
        ws: true,
        changeOrigin: true
      }
    }
  },
  build: {
    outDir: "src/main/resources/public",
    assetsDir: "static"
  }
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcubWpzIl0sCiAgInNvdXJjZXNDb250ZW50IjogWyJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcc21yYWhcXFxcZ3Atc2Utc3MtMjAyNC10ZWFtMS0yXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFxzbXJhaFxcXFxncC1zZS1zcy0yMDI0LXRlYW0xLTJcXFxcdml0ZS5jb25maWcubWpzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9DOi9Vc2Vycy9zbXJhaC9ncC1zZS1zcy0yMDI0LXRlYW0xLTIvdml0ZS5jb25maWcubWpzXCI7aW1wb3J0IHtmaWxlVVJMVG9QYXRoLCBVUkx9IGZyb20gJ3VybCdcclxuXHJcbmltcG9ydCB7ZGVmaW5lQ29uZmlnfSBmcm9tICd2aXRlJ1xyXG5pbXBvcnQgdnVlIGZyb20gJ0B2aXRlanMvcGx1Z2luLXZ1ZSdcclxuaW1wb3J0IHtxdWFzYXIsIHRyYW5zZm9ybUFzc2V0VXJsc30gZnJvbSAnQHF1YXNhci92aXRlLXBsdWdpbidcclxuaW1wb3J0IHtWaXRlUFdBfSBmcm9tIFwidml0ZS1wbHVnaW4tcHdhXCI7XHJcblxyXG4vLyBodHRwczovL3ZpdGVqcy5kZXYvY29uZmlnL1xyXG5leHBvcnQgZGVmYXVsdCBkZWZpbmVDb25maWcoe1xyXG4gICAgcGx1Z2luczogW1xyXG4gICAgICAgIHZ1ZSh7XHJcbiAgICAgICAgICAgICAgICB0ZW1wbGF0ZTogdHJhbnNmb3JtQXNzZXRVcmxzXHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICApLCBWaXRlUFdBKHtcclxuICAgICAgICAgICAgcmVnaXN0ZXJUeXBlOiBcImF1dG9VcGRhdGVcIixcclxuICAgICAgICAgICAgbWFuaWZlc3Q6IHtcclxuICAgICAgICAgICAgICAgIG5hbWU6IFwiZXhhbXBsZS1hcHBsaWNhdGlvblwiLFxyXG4gICAgICAgICAgICAgICAgc2hvcnRfbmFtZTogXCJleGFtcGxlXCIsXHJcbiAgICAgICAgICAgICAgICBkZXNjcmlwdGlvbjogXCJUaGlzIGlzIGFuIGV4YW1wbGUgYXBwbGljYXRpb25cIixcclxuICAgICAgICAgICAgICAgIHRoZW1lX2NvbG9yOiBcIiNmZmZmZmZcIixcclxuICAgICAgICAgICAgICAgIGljb25zOiBbXHJcbiAgICAgICAgICAgICAgICAgICAge1xyXG4gICAgICAgICAgICAgICAgICAgICAgICBzcmM6IFwiYW5kcm9pZC1jaHJvbWUtNTEyeDUxMi5wbmdcIixcclxuICAgICAgICAgICAgICAgICAgICAgICAgc2l6ZXM6IFwiNTEyeDUxMlwiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICB0eXBlOiBcImltYWdlL3BuZ1wiLFxyXG4gICAgICAgICAgICAgICAgICAgICAgICBwdXJwb3NlOiBcImFueSBtYXNrYWJsZVwiXHJcbiAgICAgICAgICAgICAgICAgICAgfVxyXG4gICAgICAgICAgICAgICAgXVxyXG4gICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICBkZXZPcHRpb25zOiB7XHJcbiAgICAgICAgICAgICAgICBlbmFibGVkOiB0cnVlXHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICB9KSxcclxuICAgICAgICBxdWFzYXIoe1xyXG4gICAgICAgICAgICBzYXNzVmFyaWFibGVzOiAnc3JjL21haW4vdnVlL3F1YXNhci12YXJpYWJsZXMuc2FzcydcclxuICAgICAgICB9KV1cclxuICAgICwgcmVzb2x2ZToge1xyXG4gICAgICAgIGFsaWFzOiB7XHJcbiAgICAgICAgICAgICdAJzogZmlsZVVSTFRvUGF0aChuZXcgVVJMKCcuL3NyYycsIGltcG9ydC5tZXRhLnVybCkpXHJcbiAgICAgICAgfVxyXG4gICAgfSwgc2VydmVyOiB7XHJcbiAgICAgICAgcHJveHk6IHtcclxuICAgICAgICAgICAgLypcclxuICAgICAgICAgICAgICAgIHByb3h5IGFsbCB3ZWJwYWNrIGRldi1zZXJ2ZXIgcmVxdWVzdHMgc3RhcnRpbmcgd2l0aCAvYXBpXHJcbiAgICAgICAgICAgICAgICB0byBvdXIgU3ByaW5nIEJvb3QgYmFja2VuZCAobG9jYWxob3N0OjgwODgpIHVzaW5nIGh0dHAtcHJveHktbWlkZGxld2FyZVxyXG4gICAgICAgICAgICAgICAgc2VlIGh0dHBzOi8vdml0ZWpzLmRldi9jb25maWcvI3NlcnZlci1wcm94eVxyXG4gICAgICAgICAgICAqL1xyXG4gICAgICAgICAgICAnL2FwaSc6IHtcclxuICAgICAgICAgICAgICAgIHRhcmdldDogJ2h0dHA6Ly9sb2NhbGhvc3Q6ODA4OCcsIHdzOiB0cnVlLCBjaGFuZ2VPcmlnaW46IHRydWVcclxuICAgICAgICAgICAgfVxyXG4gICAgICAgIH1cclxuICAgIH0sIGJ1aWxkOiB7XHJcbiAgICAgICAgb3V0RGlyOiAnc3JjL21haW4vcmVzb3VyY2VzL3B1YmxpYycsIGFzc2V0c0RpcjogJ3N0YXRpYydcclxuICAgIH1cclxufSlcclxuIl0sCiAgIm1hcHBpbmdzIjogIjtBQUF3UyxTQUFRLGVBQWUsV0FBVTtBQUV6VSxTQUFRLG9CQUFtQjtBQUMzQixPQUFPLFNBQVM7QUFDaEIsU0FBUSxRQUFRLDBCQUF5QjtBQUN6QyxTQUFRLGVBQWM7QUFMa0ssSUFBTSwyQ0FBMkM7QUFRek8sSUFBTyxzQkFBUSxhQUFhO0FBQUEsRUFDeEIsU0FBUztBQUFBLElBQ0w7QUFBQSxNQUFJO0FBQUEsUUFDSSxVQUFVO0FBQUEsTUFDZDtBQUFBLElBQ0o7QUFBQSxJQUFHLFFBQVE7QUFBQSxNQUNQLGNBQWM7QUFBQSxNQUNkLFVBQVU7QUFBQSxRQUNOLE1BQU07QUFBQSxRQUNOLFlBQVk7QUFBQSxRQUNaLGFBQWE7QUFBQSxRQUNiLGFBQWE7QUFBQSxRQUNiLE9BQU87QUFBQSxVQUNIO0FBQUEsWUFDSSxLQUFLO0FBQUEsWUFDTCxPQUFPO0FBQUEsWUFDUCxNQUFNO0FBQUEsWUFDTixTQUFTO0FBQUEsVUFDYjtBQUFBLFFBQ0o7QUFBQSxNQUNKO0FBQUEsTUFDQSxZQUFZO0FBQUEsUUFDUixTQUFTO0FBQUEsTUFDYjtBQUFBLElBQ0osQ0FBQztBQUFBLElBQ0QsT0FBTztBQUFBLE1BQ0gsZUFBZTtBQUFBLElBQ25CLENBQUM7QUFBQSxFQUFDO0FBQUEsRUFDSixTQUFTO0FBQUEsSUFDUCxPQUFPO0FBQUEsTUFDSCxLQUFLLGNBQWMsSUFBSSxJQUFJLFNBQVMsd0NBQWUsQ0FBQztBQUFBLElBQ3hEO0FBQUEsRUFDSjtBQUFBLEVBQUcsUUFBUTtBQUFBLElBQ1AsT0FBTztBQUFBO0FBQUE7QUFBQTtBQUFBO0FBQUE7QUFBQSxNQU1ILFFBQVE7QUFBQSxRQUNKLFFBQVE7QUFBQSxRQUF5QixJQUFJO0FBQUEsUUFBTSxjQUFjO0FBQUEsTUFDN0Q7QUFBQSxJQUNKO0FBQUEsRUFDSjtBQUFBLEVBQUcsT0FBTztBQUFBLElBQ04sUUFBUTtBQUFBLElBQTZCLFdBQVc7QUFBQSxFQUNwRDtBQUNKLENBQUM7IiwKICAibmFtZXMiOiBbXQp9Cg==
