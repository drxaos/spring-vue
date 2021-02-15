const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    // publicPath: "/app",
    // indexPath: "/app/index.html",
    devServer: {
        historyApiFallback: {
            verbose: true,
            // index: "/landing.html",
            rewrites: [
                { from: /^\/app/, to: "/app/index.html" },
                { from: /^\/admin/, to: "/admin/index.html" },
                { from: /^\/$/, to: "/app/index.html" },
            ],
        }
    },
    chainWebpack: config => {
        // delete default entry point 'app'
        config.entryPoints.delete("app").end();
        //delete default 'html' plugin - in case you don't want default index.html file
        //delete 'prefetch' and 'preload' plugins which are dependent on 'html' plugin
        config.plugins
            .delete("html")
            .delete("prefetch")
            .delete("preload");
    },
    configureWebpack: {
        entry: {
            admin: "./src/admin/admin.ts",
            app: "./src/app/app.ts"
        },
        output: {
            filename: "[name]/[name].bundle.js"
        },
        plugins: [
            new HtmlWebpackPlugin({
                template: "public/admin.html",
                filename: "admin/index.html",
                chunks: ["admin", "chunk-common", "chunk-vendors"]
            }),
            new HtmlWebpackPlugin({
                template: "public/app.html",
                filename: "app/index.html",
                chunks: ["app", "chunk-common", "chunk-vendors"]
            })]
    }
}
