<template>
    <div id="app">
        <div :class="{'nav-div':true, 'nav-div-black':wheel.now == 0 || wheel.now == 1 || wheel.now == 2 || wheel.now == 4, 'nav-div-white': wheel.now == 3}">
            <div v-for="(item, i) in wheel.mao" class="nav-box" :key="key" @click="scoll(i, 1000)">{{item.name}}</div>
        </div>
        <div class="pre-title-div">
            <div class="title-div animated fadeIn"
                 :style="{'background-image': 'url('+require('@/assets/img/title.jpg')+')'}">
                <span class="title animated fadeInRight">基于语音识别的视频处理</span>
                <span class="team-name animated fadeIn">||{{" "}}{{" "}}帅男靓女</span>
            </div>
        </div>

        <div class="pre-srt-div">
            <div class="srt-div">
                <div :style="{display: wheel.now == 1? '': 'none'}">
                    <p class="gold fadeInDown animated">字幕生成</p>
                    <p class="gold2 fadeInUp animated">
                        采用国际领先的流式端到端语音语言一体化建模算法，将视频语音快速准确识别为文字，支持多个应用场景，自动提取并生成字幕文件。</p>
                    <el-upload
                            class="upload-demo fadeInUp animated" :action="url.srt_hand" :limit="1"
                            :on-success="download_srt" accept=".mp4">
                        <span style="color: #a79938;font-weight: bold"> Upload </span>
                        <div slot="tip" class="el-upload__tip">*上传后处理较慢，请耐心等待</div>
                    </el-upload>
                </div>
                <div class="srt-img" :style="{'background-image': 'url('+require('@/assets/img/srt.jpg')+')'}"></div>
            </div>
        </div>

        <div class="pre-compose-div">
            <div class="compose-div">
                <div class="fadeInLeftBig animated" :style="{display: wheel.now == 2? '': 'none'}">
                    <p class="white fadeInRight animated" style="">视频合成字幕</p>
                    <p class="white2 fadeInRight animated" style="right: 24%">上传字幕文件及视频文</p>
                    <p class="white2 fadeInRight animated" style="right: 26%">件，采用图像处理对</p>
                    <p class="white2 fadeInRight animated" style="right: 28%">视频进行逐帧添加字幕，可以</p>
                    <p class="white2 fadeInRight animated" style="right: 30%">自定义字体颜色，大小</p>
                    <p class="white2 fadeInRight animated" style="right: 32%">及样式！</p>
                    <div class="compose-form fadeInUp animated">
                        <!--<button @click="test">555</button>-->
                        <el-form :model="compose" class="demo-form-inline" label-width="120px">
                            <el-form-item label="字体颜色">
                                <el-color-picker v-model="compose.fontcolor"></el-color-picker>
                            </el-form-item>
                            <el-form-item label="字体类型">
                                <el-select v-model="compose.fontfamily" placeholder="请选择">
                                    <el-option
                                            v-for="item in compose.fontfamilies" :key="item.id"
                                            :value="item.id" :label="item.name">
                                        <span :style="{float: left, 'font-family': 'font'+item.id, 'font-size': '25px', padding: '50px 0px'}">{{ item.name }}</span>
                                        <!--<span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>-->
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="字体大小">
                                <el-select v-model="compose.fontsize" placeholder="请选择">
                                    <el-option
                                            v-for="item in compose.fontsizes" :key="item.size" :label="item.label"
                                            :value="item.size">
                                        <span :style="{float: left,'font-size': (item.size - 30)+'px'}">{{ item.label }}</span>
                                        <!--<span style="float: right; color: #8492a6; font-size: 13px">{{ item.value }}</span>-->
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <!--<button @click="test">55</button>-->
                            <el-form-item label="视频及字幕文件">
                                <el-upload style="float: left;" class="upload-demo" :limit="2" accept=".mp4,.srt"
                                           :http-request="addfilecompose" :file-list="compose.filelist"
                                           :auto-upload="false" ref="composeupload" multiple>
                                    <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                                    <el-button style="border-radius: 0px;margin-left: 20px" size="small" type="primary"
                                               @click="submitcompose">点击上传
                                    </el-button>
                                </el-upload>
                                <!--<el-button style="margin-left: 10px;" size="small" type="success" @click="submitcompose">上传到服务器</el-button>-->
                                <div style="clear: both"></div>
                            </el-form-item>
                        </el-form>
                    </div>
                </div>
            </div>
        </div>

        <div class="pre-water-div">
            <div class="water-div" :style="{'background-image': 'url('+require('@/assets/img/water.png')+')'}">
                <p class="blue-green fadeIn animated" :style="{display: wheel.now == 3? '': 'none'}">去水印</p>
                <div class="water-form fadeIn animated" :style="{display: wheel.now == 3? '': 'none'}">
                    <el-form :model="water" class="demo-form-inline" label-width="120px">
                        <el-form-item label="水印位置">
                            <el-input v-model="water.x" placeholder="X"
                                      style="width: 150px;margin-right: 20px"></el-input>
                            <el-input v-model="water.y" placeholder="Y" style="width: 150px"></el-input>
                            <p>* 水印左上角相对于视频左上角坐标</p>
                        </el-form-item>
                        <el-form-item label="水印大小">
                            <el-input v-model="water.w" placeholder="W"
                                      style="width: 150px;margin-right: 20px"></el-input>
                            <el-input v-model="water.h" placeholder="H" style="width: 150px"></el-input>
                        </el-form-item>
                        <el-form-item label="视频文件">
                            <el-upload
                                    style="float: left;width: 100%" class="upload-demo" :limit="1"
                                    :on-success="download_water"
                                    accept=".mp4" :action="url.water_hand"
                                    :data="{'x': water.x, 'y':water.y, 'w':water.w, 'h':water.h}">
                                <el-button style="border-radius: 0px" size="small" type="primary">点击上传</el-button>
                            </el-upload>
                            <div style="clear: both"></div>
                        </el-form-item>
                    </el-form>
                </div>

            </div>
        </div>
        <div class="pre-logo-div">
            <div class="logo-div" :style="{'background-image': 'url('+require('@/assets/img/logo.jpg')+')'}">
                <div class="fadeInUpBig animated" :style="{display: wheel.now == 4? '': 'none'}">
                    <p class="red-blue fadeInDown animated">添加logo</p>
                    <p class="red-blue2 fadeIn animated">采用视频帧处理技术，提交处理视频与logo图片，在视频左上角添加logo水印。</p>
                    <el-upload
                            class="upload-demo fadeInUp animated" :limit="2" accept=".mp4,.jpg"
                            :http-request="addfilelogo" :file-list="logo.filelist"
                            :auto-upload="false" ref="logoupload" multiple
                            style="left: 9vw;top: 28vh;position: relative;display: inline-block;width: 30vw;animation-delay: 1s;">
                        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitlogo">上传到服务器
                        </el-button>
                    </el-upload>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    /* eslint vue/valid-v-for: "off"*/
    import ElementUI from 'element-ui';
    import 'element-ui/lib/theme-chalk/index.css';
    import Vue from 'vue'
    import VueResource from 'vue-resource'
    import '@/assets/css/animate.min.css'
    import '@/assets/css/font.css'
    import $ from 'jquery'
    import fun from "@/assets/js/info.js"

    Vue.use(VueResource);
    Vue.use(ElementUI);

    export default {
        name: 'App',
        data() {
            return {
                url: {
                    srt_hand: "",
                    srt_download: "",
                    compose_hand: "",
                    compose_download: "",
                    water_hand: "",
                    water_download: "",
                    logo_hand: "",
                    logo_download: "",
                },
                srt: {
                },
                compose: {
                    forms: "",
                    filelist: [],
                    fontcolor: '#000000',
                    fontfamily: null,
                    fontsize: null,
                    fontfamilies: [{
                        name: "龙吟手体",
                        id: 55
                    }, {
                        name: "镇魂手书",
                        id: 24
                    }, {
                        name: "霸燃手书",
                        id: 138
                    }, {
                        name: "白鸽天行体",
                        id: 50
                    }, {
                        name: "武林江湖体",
                        id: 110
                    }, {
                        name: "御守锦书",
                        id: 71
                    }, {
                        name: "逍遥行书",
                        id: 49
                    }],
                    fontsizes: [{
                        label: "40px",
                        size: 40
                    }, {
                        label: "45px",
                        size: 45
                    }, {
                        label: "48px",
                        size: 48
                    }, {
                        label: "50px",
                        size: 50
                    }, {
                        label: "53px",
                        size: 53
                    }, {
                        label: "58px",
                        size: 58
                    }, {
                        label: "62px",
                        size: 62
                    }]
                },
                water: {
                    x: null,
                    y: null,
                    w: null,
                    h: null
                },
                logo: {
                    forms: "",
                    filelist: []
                },
                wheel: {
                    mao: [{
                        id: ".pre-title-div",
                        offset: 0,
                        name: "首页"
                    }, {
                        id: ".pre-srt-div",
                        offset: 0,
                        name: "字幕生成"
                    }, {
                        id: ".pre-compose-div",
                        offset: 0,
                        name: "视频合成字幕"
                    }, {
                        id: ".pre-water-div",
                        offset: 0,
                        name: "除水印"
                    }, {
                        id: ".pre-logo-div",
                        offset: 0,
                        name: "添加logo"
                    }],
                    scolling: false,
                    now: null
                }
            }
        },
        methods: {

            addfilecompose(param) {
                this.compose.forms.append("file", param.file);
            },
            addfilelogo(param) {
                this.logo.forms.append("file", param.file);
            },
            /* 提交视频合成字母 */
            submitcompose() {
                this.compose.forms = new FormData();
                this.$refs.composeupload.submit();
                this.compose.forms.append("fontcolor", this.compose.fontcolor)
                this.compose.forms.append("fontfamily", this.compose.fontfamily)
                this.compose.forms.append("fontsize", this.compose.fontsize)
                this.$http.post(this.url.compose_hand,
                    this.compose.forms, {'Content-Type': 'multipart/form-data'}).then(function (res) {
                    this.download_overmp4(res.body.submission_id)
                }, function () {
                    console.log('请求失败处理');
                });
            },

            /* 添加logo */
            submitlogo() {
                this.logo.forms = new FormData();
                this.$refs.logoupload.submit();
                this.$http.post(this.url.logo_hand,
                    this.logo.forms, {'Content-Type': 'multipart/form-data'}).then(function (res) {
                    this.download_logo(res.body.submission_id)
                }, function () {
                    console.log('请求失败处理');
                });
            },

            /* 下载字幕文件 */
            download_srt: function (data) {
                let submit_id = data.submission_id
                window.location.href = this.url.srt_download+"?submission_id="+submit_id;
            },

            /* 下载字幕合成后的mp4文件 */
            download_overmp4: function (submit_id) {
                window.location.href = this.url.compose_download+"?submission_id="+submit_id;
            },

            /* 下载去水印mp4文件 */
            download_water: function (data) {
                let submit_id = data.submission_id
                window.location.href = this.url.water_download+"?submission_id="+submit_id;
            },

            /* 下载添加logo的mp4文件 */
            download_logo: function (submit_id) {
                window.location.href = this.url.logo_download+"?submission_id="+submit_id;
            },

            /* 检查当前页面位置 */
            checkscoll() {
                var id = 0
                for (let i = 0; i < this.wheel.mao.length; i++) {
                    if (Math.abs($('html,body').scrollTop() - this.wheel.mao[i].offset) < Math.abs($('html,body').scrollTop() - this.wheel.mao[id].offset)) id = i;
                }
                return id;
            },

            /* 滚动到指定页面位置 */
            scoll: function (id, t) {
                $('html,body').animate({scrollTop: this.wheel.mao[id].offset}, t, 'swing')
                var _this = this;
                if (t != 0) {
                    setTimeout(function () {
                        _this.wheel.scolling = false;
                        _this.wheel.now = id;
                    }, 1000)
                } else {

                    this.wheel.scolling = false;
                    this.wheel.now = id;
                }

            },
            handscroll(e) {
                var direction = e.deltaY > 0 ? 1 : -1
                if (this.wheel.scolling == false) {

                    this.wheel.scolling = true;
                    let nxt = this.wheel.now + direction;
                    if (nxt < 0) this.scoll(0, 1000)
                    else if (nxt >= this.wheel.mao.length) this.scoll(this.wheel.mao.length - 1, 1000)
                    else this.scoll(nxt, 1000)
                }
            },

            /* 更新页面的offset */
            updatewindows() {
                for (let i = 0; i < this.wheel.mao.length; i++) {
                    this.wheel.mao[i].offset = $(this.wheel.mao[i].id).offset().top;
                }
            }
        },
        mounted() {
            this.url.srt_hand = fun.srt_hand
            this.url.srt_download = fun.srt_download
            this.url.compose_hand = fun.compose_hand
            this.url.compose_download = fun.compose_download
            this.url.water_hand = fun.water_hand
            this.url.water_download = fun.water_download
            this.url.logo_hand = fun.logo_hand
            this.url.logo_download = fun.logo_download
            this.updatewindows()
            var id = this.checkscoll();
            this.scoll(id, 1000)
            window.addEventListener('mousewheel', this.handscroll, false)
            window.onresize = () => {
                return (() => {
                    this.updatewindows()
                    this.scoll(this.wheel.now, 0)
                })()
            }
        }
    }
</script>

<style>
    .nav-div{
        transition: background-color 0.5s;
        transition: color 0.5s;
    }
    .nav-div-black {
        z-index: 15;
        position: fixed;
        cursor: pointer;
        width: 100vw;
        background-color: rgba(0, 0, 0, 0);
        color: #696969;
    }

    .nav-div-black:hover {
        color: white;
        border-bottom: 1.5px solid rgba(80, 80, 80, 0.1);
        background-color: rgba(177, 177, 177, 0.1);
    }

    .nav-div-white {
        z-index: 15;
        position: fixed;
        cursor: pointer;
        width: 100vw;
        background-color: rgba(0, 0, 0, 0);
        color: #e7e7e7;
    }

    .nav-div-white:hover {
        color: black;
        border-bottom: 1.5px solid rgba(232, 232, 232, 0.1);
        background-color: rgba(195, 195, 195, 0.1);
    }

    .nav-box {
        transition: background-color 0.25s;
        float: left;
        padding: 18px 30px;
        font-size: 15px;
    }

    .nav-box:hover {
        /*border-right: 1.5px solid rgba(177, 177, 177, 0.5);*/
        /*border-left: 1.5px solid rgba(177, 177, 177, 0.5);*/
        /*border-bottom: 1.5px solid rgba(177, 177, 177, 0.5);*/
        background-color: rgba(177, 177, 177, 0.5);;
    }

    .water-form {
        display: inline-block;
        position: relative;
        left: 45vw;
        top: 180px;
    }

    .compose-form {
        width: 45%;
        animation-delay: 1s;
        display: inline-block;
        position: relative;
        left: 10vw;
        top: 20px;
    }

    .compose-form label {
        color: white !important;
    }

    .blue-green {
        position: relative;
        left: 70%;
        top: 15%;
        background-image: -webkit-linear-gradient(bottom, #4af006, #ffe200, #e40300);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        font-family: gold;
        font-size: 50px;
    }

    .red-blue {
        animation-delay: 1s;
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        font-family: gold;
        font-size: 50px;
        background-image: -webkit-linear-gradient(bottom, #f00300, #ff00cf, #0007e4);
        display: inline-block;
        position: relative;
        left: 37%;
        top: 15vh;
    }

    .red-blue2 {
        animation-delay: 1s;
        width: 30vw;
        font-size: 18px;
        font-family: gold;
        color: white;
        top: 25vh;
        position: relative;
        left: -7%;
        display: inline-block;
    }

    .gold {
        background-image: -webkit-linear-gradient(left, #f0f0da, #a79938, #887b21);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        font-family: gold;
        font-size: 50px;
    }

    .gold2 {
        width: 40vw;
        margin-top: 5vh;
        color: white;
        line-height: 2;
    }

    .white2 {
        animation-delay: 1s;
        /*top: 70px;*/
        text-align: center;
        color: white;
        line-height: 1 .;
        /*right: 21%;*/
        position: relative;
        width: 10px;
        float: right;
        text-align: center;
        font-family: gold;
        font-size: 20px;
    }

    .white {
        animation-delay: 1s;
        color: white;
        /*background-image: -webkit-linear-gradient(left, #00e0ff, #00d261, #3fcd00);*/
        /*-webkit-background-clip: text;*/
        /*-webkit-text-fill-color: transparent;*/
        word-wrap: break-word;
        font-family: gold;
        font-size: 40px;
        width: 50px;
        line-height: 1;
        text-align: right;
        position: relative;
        float: right;
        right: 300px;
        /*margin-top: 8vh;*/
    }

    .srt-img {
        float: right;
        width: 50vw;
        height: 80vh;
        margin-top: 20vh;
        background-size: 100% 100%;
        background-repeat: repeat-x;
    }

    .pre-title-div, .pre-srt-div, .pre-compose-div, .pre-water-div, .pre-logo-div {
        width: 100vw;
        height: 100vh
    }

    .pre-water-div {
        background-image: -webkit-linear-gradient(bottom, black, white, white, white, white, white, white, white, white, white);
    }

    .compose-div {
        width: 100vw;
        height: 100vh;
        background-image: -webkit-linear-gradient(bottom, white, black);
    }

    .compose-div > div:first-child {
        position: relative;
        background-color: rgb(0, 0, 0, 0.1);
        height: 50vh;
        top: 25vh;
        padding-top: 8vh
    }

    .title-div {
        background-size: 100% 100%;
        background-repeat: repeat-x;
        background-position-x: -260px;
        width: 100vw;
        height: 100vh
    }

    .logo-div {
        background-size: 100% 100%;
        background-repeat: repeat-x;
        background-position-x: 80px;
        width: 100vw;
        height: 100vh
    }

    .logo-div > div:first-child {
        left: 5vw;
        position: relative;
        background-color: rgba(130, 130, 130, 0.5);
        height: 100vh;
        /* top: 25vh; */
        /* padding-top: 8vh; */
        width: 50vw;
        /*filter: blur(15px);*/
    }

    .water-div {
        background-size: 100% 100%;
        background-repeat: no-repeat;
        background-position-x: -260px;
        width: 80vw;
        height: 100vh
    }

    .srt-div {
        background-color: black;
        height: 100vh
    }

    .srt-div > div:first-child {
        float: left;
        margin-top: 20vh;
        margin-left: 10vw;
    }

    .title {
        position: relative;
        font-family: title;
        text-align: center;
        top: 7%;
        color: white;
        font-size: 50px;
        left: 85%;
        width: 10px;
        word-wrap: break-word;
        line-height: 1;
        display: inline-block;
        animation-delay: 1s;
    }

    .team-name {
        font-family: team;
        position: relative;
        text-align: center;
        top: 7%;
        color: white;
        font-size: 35px;
        left: 76%;
        width: 10px;
        word-wrap: break-word;
        line-height: 1;
        display: inline-block;
        animation-delay: 1.5s;
    }

    body {
        margin: 0px;
        overflow: hidden;
    }

    p {
        margin: 0px;
    }

    #app {
        font-family: Avenir, Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        /*text-align: center;*/
        color: #2c3e50;
    }
</style>
