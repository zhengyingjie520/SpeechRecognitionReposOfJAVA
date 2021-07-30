const webpack = require('webpack')
module.exports = {
    configureWebpack: {
        performance: {
            hints:'warning',
            //入口起点的最大体积
            maxEntrypointSize: 50000000,
            //生成文件的最大体积
            maxAssetSize: 30000000,
            //只给出 js 文件的性能提示
            assetFilter: function(assetFilename) {
                return assetFilename.endsWith('.js');
            }
        },
        resolve: {
            alias: {
                vue$: "vue/dist/vue.esm.js", //加上这一句
            }
        },
        plugins: [
            new webpack.ProvidePlugin({
                $:"jquery",
                jQuery:"jquery",
                "windows.jQuery":"jquery",
                Popper: ["popper.js", "default"]
            })
        ],
    },
    pages: {
        // 只有entry属性时，直接用字符串表示模块入口
		index: 'src/main.js'
    },
    publicPath:"./",
    outputDir:'dist',
    assetsDir: 'static',
    devServer: {
        port: 8020,
        proxy: {
            '/api': {
                target: 'http://60.205.187.87:8089/ctguacm_OnlineJudge', //对应自己的接口
                changeOrigin: true,
                ws: true,
                pathRewrite: {
                    '^/api': ''
                }
            }
        }
    }
};