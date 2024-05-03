// vite.config.mjs
import { fileURLToPath, URL } from "url";
import { defineConfig } from "file:///Users/jt/GPSE/gp-se-ss-2024-team1-2/node_modules/vite/dist/node/index.js";
import vue from "file:///Users/jt/GPSE/gp-se-ss-2024-team1-2/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import { quasar, transformAssetUrls } from "file:///Users/jt/GPSE/gp-se-ss-2024-team1-2/node_modules/@quasar/vite-plugin/src/index.js";
import { VitePWA } from "file:///Users/jt/GPSE/gp-se-ss-2024-team1-2/node_modules/vite-plugin-pwa/dist/index.js";
var __vite_injected_original_import_meta_url = "file:///Users/jt/GPSE/gp-se-ss-2024-team1-2/vite.config.mjs";
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
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcubWpzIl0sCiAgInNvdXJjZXNDb250ZW50IjogWyJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiL1VzZXJzL2p0L0dQU0UvZ3Atc2Utc3MtMjAyNC10ZWFtMS0yXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCIvVXNlcnMvanQvR1BTRS9ncC1zZS1zcy0yMDI0LXRlYW0xLTIvdml0ZS5jb25maWcubWpzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9Vc2Vycy9qdC9HUFNFL2dwLXNlLXNzLTIwMjQtdGVhbTEtMi92aXRlLmNvbmZpZy5tanNcIjtpbXBvcnQge2ZpbGVVUkxUb1BhdGgsIFVSTH0gZnJvbSAndXJsJ1xuXG5pbXBvcnQge2RlZmluZUNvbmZpZ30gZnJvbSAndml0ZSdcbmltcG9ydCB2dWUgZnJvbSAnQHZpdGVqcy9wbHVnaW4tdnVlJ1xuaW1wb3J0IHtxdWFzYXIsIHRyYW5zZm9ybUFzc2V0VXJsc30gZnJvbSAnQHF1YXNhci92aXRlLXBsdWdpbidcbmltcG9ydCB7Vml0ZVBXQX0gZnJvbSBcInZpdGUtcGx1Z2luLXB3YVwiO1xuXG4vLyBodHRwczovL3ZpdGVqcy5kZXYvY29uZmlnL1xuZXhwb3J0IGRlZmF1bHQgZGVmaW5lQ29uZmlnKHtcbiAgICBwbHVnaW5zOiBbXG4gICAgICAgIHZ1ZSh7XG4gICAgICAgICAgICAgICAgdGVtcGxhdGU6IHRyYW5zZm9ybUFzc2V0VXJsc1xuICAgICAgICAgICAgfVxuICAgICAgICApLCBWaXRlUFdBKHtcbiAgICAgICAgICAgIHJlZ2lzdGVyVHlwZTogXCJhdXRvVXBkYXRlXCIsXG4gICAgICAgICAgICBtYW5pZmVzdDoge1xuICAgICAgICAgICAgICAgIG5hbWU6IFwiZXhhbXBsZS1hcHBsaWNhdGlvblwiLFxuICAgICAgICAgICAgICAgIHNob3J0X25hbWU6IFwiZXhhbXBsZVwiLFxuICAgICAgICAgICAgICAgIGRlc2NyaXB0aW9uOiBcIlRoaXMgaXMgYW4gZXhhbXBsZSBhcHBsaWNhdGlvblwiLFxuICAgICAgICAgICAgICAgIHRoZW1lX2NvbG9yOiBcIiNmZmZmZmZcIixcbiAgICAgICAgICAgICAgICBpY29uczogW1xuICAgICAgICAgICAgICAgICAgICB7XG4gICAgICAgICAgICAgICAgICAgICAgICBzcmM6IFwiYW5kcm9pZC1jaHJvbWUtNTEyeDUxMi5wbmdcIixcbiAgICAgICAgICAgICAgICAgICAgICAgIHNpemVzOiBcIjUxMng1MTJcIixcbiAgICAgICAgICAgICAgICAgICAgICAgIHR5cGU6IFwiaW1hZ2UvcG5nXCIsXG4gICAgICAgICAgICAgICAgICAgICAgICBwdXJwb3NlOiBcImFueSBtYXNrYWJsZVwiXG4gICAgICAgICAgICAgICAgICAgIH1cbiAgICAgICAgICAgICAgICBdXG4gICAgICAgICAgICB9LFxuICAgICAgICAgICAgZGV2T3B0aW9uczoge1xuICAgICAgICAgICAgICAgIGVuYWJsZWQ6IHRydWVcbiAgICAgICAgICAgIH1cbiAgICAgICAgfSksXG4gICAgICAgIHF1YXNhcih7XG4gICAgICAgICAgICBzYXNzVmFyaWFibGVzOiAnc3JjL21haW4vdnVlL3F1YXNhci12YXJpYWJsZXMuc2FzcydcbiAgICAgICAgfSldXG4gICAgLCByZXNvbHZlOiB7XG4gICAgICAgIGFsaWFzOiB7XG4gICAgICAgICAgICAnQCc6IGZpbGVVUkxUb1BhdGgobmV3IFVSTCgnLi9zcmMnLCBpbXBvcnQubWV0YS51cmwpKVxuICAgICAgICB9XG4gICAgfSwgc2VydmVyOiB7XG4gICAgICAgIHByb3h5OiB7XG4gICAgICAgICAgICAvKlxuICAgICAgICAgICAgICAgIHByb3h5IGFsbCB3ZWJwYWNrIGRldi1zZXJ2ZXIgcmVxdWVzdHMgc3RhcnRpbmcgd2l0aCAvYXBpXG4gICAgICAgICAgICAgICAgdG8gb3VyIFNwcmluZyBCb290IGJhY2tlbmQgKGxvY2FsaG9zdDo4MDg4KSB1c2luZyBodHRwLXByb3h5LW1pZGRsZXdhcmVcbiAgICAgICAgICAgICAgICBzZWUgaHR0cHM6Ly92aXRlanMuZGV2L2NvbmZpZy8jc2VydmVyLXByb3h5XG4gICAgICAgICAgICAqL1xuICAgICAgICAgICAgJy9hcGknOiB7XG4gICAgICAgICAgICAgICAgdGFyZ2V0OiAnaHR0cDovL2xvY2FsaG9zdDo4MDg4Jywgd3M6IHRydWUsIGNoYW5nZU9yaWdpbjogdHJ1ZVxuICAgICAgICAgICAgfVxuICAgICAgICB9XG4gICAgfSwgYnVpbGQ6IHtcbiAgICAgICAgb3V0RGlyOiAnc3JjL21haW4vcmVzb3VyY2VzL3B1YmxpYycsIGFzc2V0c0RpcjogJ3N0YXRpYydcbiAgICB9XG59KVxuIl0sCiAgIm1hcHBpbmdzIjogIjtBQUFnUyxTQUFRLGVBQWUsV0FBVTtBQUVqVSxTQUFRLG9CQUFtQjtBQUMzQixPQUFPLFNBQVM7QUFDaEIsU0FBUSxRQUFRLDBCQUF5QjtBQUN6QyxTQUFRLGVBQWM7QUFMMkosSUFBTSwyQ0FBMkM7QUFRbE8sSUFBTyxzQkFBUSxhQUFhO0FBQUEsRUFDeEIsU0FBUztBQUFBLElBQ0w7QUFBQSxNQUFJO0FBQUEsUUFDSSxVQUFVO0FBQUEsTUFDZDtBQUFBLElBQ0o7QUFBQSxJQUFHLFFBQVE7QUFBQSxNQUNQLGNBQWM7QUFBQSxNQUNkLFVBQVU7QUFBQSxRQUNOLE1BQU07QUFBQSxRQUNOLFlBQVk7QUFBQSxRQUNaLGFBQWE7QUFBQSxRQUNiLGFBQWE7QUFBQSxRQUNiLE9BQU87QUFBQSxVQUNIO0FBQUEsWUFDSSxLQUFLO0FBQUEsWUFDTCxPQUFPO0FBQUEsWUFDUCxNQUFNO0FBQUEsWUFDTixTQUFTO0FBQUEsVUFDYjtBQUFBLFFBQ0o7QUFBQSxNQUNKO0FBQUEsTUFDQSxZQUFZO0FBQUEsUUFDUixTQUFTO0FBQUEsTUFDYjtBQUFBLElBQ0osQ0FBQztBQUFBLElBQ0QsT0FBTztBQUFBLE1BQ0gsZUFBZTtBQUFBLElBQ25CLENBQUM7QUFBQSxFQUFDO0FBQUEsRUFDSixTQUFTO0FBQUEsSUFDUCxPQUFPO0FBQUEsTUFDSCxLQUFLLGNBQWMsSUFBSSxJQUFJLFNBQVMsd0NBQWUsQ0FBQztBQUFBLElBQ3hEO0FBQUEsRUFDSjtBQUFBLEVBQUcsUUFBUTtBQUFBLElBQ1AsT0FBTztBQUFBO0FBQUE7QUFBQTtBQUFBO0FBQUE7QUFBQSxNQU1ILFFBQVE7QUFBQSxRQUNKLFFBQVE7QUFBQSxRQUF5QixJQUFJO0FBQUEsUUFBTSxjQUFjO0FBQUEsTUFDN0Q7QUFBQSxJQUNKO0FBQUEsRUFDSjtBQUFBLEVBQUcsT0FBTztBQUFBLElBQ04sUUFBUTtBQUFBLElBQTZCLFdBQVc7QUFBQSxFQUNwRDtBQUNKLENBQUM7IiwKICAibmFtZXMiOiBbXQp9Cg==
