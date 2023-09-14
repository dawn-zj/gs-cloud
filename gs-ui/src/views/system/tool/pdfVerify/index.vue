<template>

  <el-row>
    <div>
      <el-upload
        ref="upload"
        class="pdf-upload"
        name="fileList"
        accept=".pdf"
        action="#"
        :auto-upload="false"
        :file-list="fileList"
        :show-file-list="false"
        :on-remove="handleFileRemove"
        :on-change="handleFileChange"
      >
        <el-button type="info" size="small" icon="el-icon-folder-opened" />
      </el-upload>
    </div>
    <el-col :span="24">
      <div id="pageContent" class="pageContent_2lAGg">
        <div id="left" class="leftContent_2NoKi">
          <div class="doc-list-context">
            <div>
              <el-tabs v-model="activeNameLeft" :stretch="true">
                <el-tab-pane label="验签章" name="1" />
              </el-tabs>

              <template v-if="activeNameLeft == 1">
                <div class="seal-list-title">验签结果
                  <div v-show="form.stampType === 1 && form.stampParam.locations.length > 0" class="list">
                    <div v-if="verifyResult" style="color: limegreen">成功</div>
                    <div v-else style="color: red">失败</div>
                  </div>
                </div>

                <div v-show="form.stampType === 1" class="doc-list-title">签署列表
                  <div class="list">
                    <div v-for="(item) in form.stampParam.locations">
                      <span v-if="item.result" class="el-icon-check" style="color: limegreen; font-weight: bold" />
                      <span v-else class="el-icon-close" style="color: red; font-weight: bold" />
                      <el-link class="el-icon-s-check" @click="viewToPageNum(item.pageNum)">{{ item.name }}</el-link>
                    </div>
                  </div>
                </div>
              </template>

            </div>
          </div>
        </div>

        <div class="pdfContent_1PW2f">
          <div class="pdf-header">
            <div class="pageNo">
              <div class="page-name">
                <button
                  style="background-color: Transparent; margin-top:10px;cursor:pointer;"
                  type="button"
                  @click="viewToPageNum(1)"
                ><span style="color: #4D4D4D" class="el-icon-d-arrow-left" /></button>
              </div>
              <div class="page-name">
                <button
                  style="background-color: Transparent; margin-top:10px;cursor:pointer;"
                  type="button"
                  @click="pagePrev"
                ><span style="color: #4D4D4D" class="el-icon-arrow-left" /></button>
              </div>
              <div class="page-name">
                <button style="background-color: Transparent; margin-top:10px;" type="button"><span
                  id="pageNo"
                  style="color: #4D4D4D"
                >{{
                  pageNum
                }}</span><span id="pageCount" style="color: #4D4D4D">/{{ page_count }}</span></button>
              </div>
              <div class="page-name">
                <button
                  style="background-color: Transparent; margin-top:10px; cursor:pointer;"
                  type="button"
                  @click="pageNext"
                ><span style="color: #4D4D4D;" class="el-icon-arrow-right" /></button>
              </div>
              <div class="page-name">
                <button
                  style="background-color: Transparent; margin-top:10px; cursor:pointer;"
                  type="button"
                  @click="viewToPageNum(page_count)"
                ><span style="color: #4D4D4D;" class="el-icon-d-arrow-right" /></button>
              </div>
              <span style="margin-top:6px; font-size: 13px;line-height: 28px;color: #606266;">
                <div class="el-input el-pagination__editor is-in-pagination"><input
                  v-model="pageNum"
                  type="number"
                  min="1"
                  max="4"
                  class="el-input__inner"
                ></div>
                <el-button size="small" @click="viewToPageNum(Number(pageNum))">跳转</el-button>
              </span>
            </div>
          </div>
          <div
            ref="pdfBox"
            class="pdf-container"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.1)"
          >
            <div class="pdf-box" :style="{width: pdfWidth+'px', left: showEdges? '-60px': ''}">
              <div v-show="this.form.stampType == 1" id="signArea" />
              <div class="pdfPage_1yRne">
                <canvas id="the-canvas" class="pdf-item" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>

import { getBase64, getFileType } from '@/utils/file'
import { base642blob } from '@/utils/base64/base64'
import { verify } from '@/api/system/genStamp'

// 2.7.542版本
const PDFJS = window['pdfjs-dist/build/pdf']
PDFJS.GlobalWorkerOptions.workerSrc = 'pdfjs-dist/build/pdf.worker'
// const PDFJS = require('pdfjs-dist')
// PDFJS.GlobalWorkerOptions.workerSrc = './pdf.worker.js'
export default {
  // name: "signFieldConfig",
  data() {
    return {
      isShow: false,
      signEdges: false,
      showEdges: false,
      // 遮罩层
      loading: true,
      activeNameLeft: '1',
      activeName: '1',
      fileList: [],
      pdfUrl: '',
      pdfDoc: null, // pdfjs 生成的对象
      pageNum: 1, //
      page_count: 0, // 总页数
      pdfWidth: 0,
      pdfHeight: 0,
      scale: 1.0,
      templateInfo: {},
      verifyResult: null,
      sealInfo: {
        name: '',
        dpi: 96,
        width: 160, // 单位像素，拖拽显示时转换成磅
        height: 160,
        base64: ''
      },
      form: {
        id: '',
        stampType: 1, // 签章方式
        sealId: null,
        stampParam: {
          locations: [], // 拖拽签章坐标位置x,y,页码,印章
          useQfz: 1, // 使用骑缝章
          qfzType: 1, // 骑缝章方式
          qfzSealId: null,
          qfzSealName: ''
        }

      },
      sealList: []
    }
  },
  // 监听一个值的变化,然后执行相对应的函数。
  watch: {
    pdfUrl: function(val) {
      this.$nextTick(() => {
        this._loadFile(val)
      })
    }
  },
  mounted() {
    this.$nextTick(function() {
      this.getFileInfo()
    })
  },
  methods: {
    // 上传文件变化
    handleFileChange(file, fileList) {
      // 限制为指定类型
      const fileType = getFileType(file.raw.name)
      const isTrue = fileType === '.pdf'
      if (!isTrue) {
        this.msgError('请选择pdf类型文件')
        this.$set(this, 'fileList', [])
        return false
      }
      this.fileList = [fileList[fileList.length - 1]]
      // 获取base64，并转URL
      getBase64(file.raw).then(res => {
        this.pageNum = 1
        this.pdfUrl = window.URL.createObjectURL(base642blob(res))
      })
      // 发送后端请求，验签章
      this.verify(file.raw)
    },
    // 删除文件
    handleFileRemove(file, fileList) {
      // this.$set(this, 'fileBase64', '')
    },
    verify(file) {
      const formData = new FormData()
      formData.append('file', file)

      verify(formData).then(res => {
        this.verifyResult = res.data.result

        this.form.stampParam.locations = []
        // for循环写入form.stampParam.locations
        const signList = res.data.signList
        signList.forEach(item => {
          const location = {
            result: item.result,
            name: item.certDn,
            pageNum: item.signLocation[0].page,
            x: item.signLocation[0].position.left,
            y: item.signLocation[0].position.bottom,
            sealInfo: {
              dpi: 96,
              width: item.signLocation[0].position.width,
              height: item.signLocation[0].position.height
            }
          }
          this.form.stampParam.locations.push(location)
        })

        // 手动跳转至当前页，渲染悬浮手型div至PDF表层
        this._renderPage(this.pageNum)
      })
    },
    // 获取文件信息
    getFileInfo() {
      this.pdfUrl = this.publicPath + '/show.pdf'
      this._loadFile(this.pdfUrl)
    },
    // 预览渲染模板文件
    _renderPage(num) {
      this.pdfDoc.getPage(num).then((page) => {
        const canvas = document.getElementById('the-canvas')
        const ctx = canvas.getContext('2d')
        // 引用js文件，注意getViewport写法，和required引用写法不同
        const viewport = page.getViewport({ scale: this.scale })
        // pdfjs返回的PDF宽高单位是pt，但我们渲染直接用了px，视觉上pdf比实际小一圈，但清晰
        // 需要注意，拖拽图片时，图片宽高单位和渲染单位，与PDF渲染逻辑保持一致
        this.pdfHeight = canvas.height = viewport.height
        this.pdfWidth = canvas.width = viewport.width
        const renderContext = {
          canvasContext: ctx,
          viewport: viewport
        }
        page.render(renderContext)

        // 渲染所有签署区
        var signArea = document.getElementById('signArea')
        signArea.innerHTML = ''

        const locations = this.form.stampParam.locations
        if (locations.length > 0) {
          this.$nextTick(function() {
            for (var i = 0; i < locations.length; i++) {
              const location = locations[i]
              this.reviewSignArea(location.x, location.y, location.sealInfo, true)
            }
          })
        }
      })
    },
    // 加载模板文件
    _loadFile(url) {
      const CMAP_URL = 'https://cdn.jsdelivr.net/npm/pdfjs-dist@2.0.943/cmaps/'
      PDFJS.getDocument({ url: url, cMapUrl: CMAP_URL, cMapPacked: true }).promise.then((pdf) => {
        this.pdfDoc = pdf
        this.page_count = this.pdfDoc.numPages
        this.$nextTick(() => {
          this.isShow = true
          this._renderPage(1)
        })
      })
    },
    // 接收后台返回的流文件，赋值模板地址
    getTemplateData() {
      const baseURL = process.env.VUE_APP_BASE_API
      const baseUrl = baseURL + '/common/download?filePath=' + encodeURI(this.templateInfo.path) + '&remotePath=' + encodeURI(this.templateInfo.remotePath) + '&fileName=' + encodeURI(this.templateInfo.name)
      const xhr = new XMLHttpRequest()
      xhr.open('get', baseUrl, true) // get、post都可
      xhr.responseType = 'blob' // 转换流
      const that = this
      xhr.onload = function() {
        if (this.status == 200) {
          const blob = this.response
          that.pdfUrl = window.URL.createObjectURL(blob)
          that.loading = false
          that._loadFile(that.pdfUrl)
        }
      }
      xhr.send()
    },
    queueRenderPage(num) {
      this._renderPage(num)
    },
    // 放大缩小
    handleChange(val) {
      this.scale = val
      this.queueRenderPage(this.pageNum)
    },
    // 前进翻页
    pagePrev() {
      if (this.pageNum !== 1) {
        this.pageNum = this.pageNum - 1
      }
      this._renderPage(this.pageNum)
    },
    // 后退翻页
    pageNext() {
      if (this.pageNum !== this.page_count) {
        this.pageNum = this.pageNum + 1
      }
      this._renderPage(this.pageNum)
    },
    viewToPageNum(num) {
      this.pageNum = num
      this.$nextTick(function() {
        this._renderPage(this.pageNum)
      })
    },
    stampTypeChange(val) {
      this.$nextTick(function() {
        if (val === 1) {
          this.drag()
        }
      })
    },
    createSealDiv(left, top, sealInfo) {
      const cloneTemp = document.createElement('div')
      // 磅
      const cloneTempWidth = sealInfo.width
      const cloneTempHeight = sealInfo.height
      cloneTemp.className = 'sign-area'
      cloneTemp.style.border = '0px dotted red'
      cloneTemp.style.backgroundSize = '100% 100%'
      cloneTemp.style.position = 'absolute' // 相对于pdf图片的绝对位置
      cloneTemp.style.zIndex = '8' // footer是9
      cloneTemp.style.width = cloneTempWidth + 'px'
      cloneTemp.style.height = cloneTempHeight + 'px'
      cloneTemp.style.left = left + 'px'
      cloneTemp.style.top = top + 'px'
      cloneTemp.style.cursor = 'grab'

      return cloneTemp
    },
    reviewSignArea(left, bottom, sealInfo, isShow) {
      var _this = this

      // 签署区
      var signArea = document.getElementById('signArea')
      const cloneTemp = _this.createSealDiv(0, 0, sealInfo)
      if (isShow) {
        cloneTemp.style.display = 'block'
      } else {
        cloneTemp.style.display = 'none'
      }
      signArea.appendChild(cloneTemp)

      var pdfBox = document.getElementsByClassName('pdfPage_1yRne')[0]
      var top = pdfBox.scrollHeight - bottom - cloneTemp.scrollHeight

      cloneTemp.style.left = left + 'px'
      cloneTemp.style.top = top + 'px'

      // 给签署方框加 点击事件，弹出验章信息
      cloneTemp.onclick = function(e) {
        _this.msgInfo('验章')

        // 阻止默认事件
        return false
      }

      // 签署域方框鼠标悬浮，显示删除小红叉
      cloneTemp.onmouseover = function(event) {
        cloneTemp.style.cursor = 'grab'
      }
    }

  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
.pageContent_2lAGg {
  display: flex;
  /*max-width: 1200px;*/
  height: 95vh;
  /*margin: 10px auto 0 auto;*/
  overflow: auto;
  user-select: none;
  /*firefox浏览器*/
  -moz-user-select: none;
  /*safari、chrome浏览器*/
  -webkit-user-select: none; /*Safari中不支持该属性值，只能使用none或者text，或者是在html的标签属性中使用*/
  .input {
    border: none;
    outline: none;
    width: 100%;
  }
}

.leftContent_2NoKi {
  display: flex;
  flex-direction: column;
  flex: 0 0 240px;
  width: 280px;
  height: 100%;

  .seal-list-title {
    font-size: 14px;
    font-weight: 800;
    color: #333;
    line-height: 40px;
  }

  .doc-list-title {
    font-size: 14px;
    font-weight: 800;
    color: #333;
    line-height: 40px;
    height: 40px;
    width: 100%;
    /*border-bottom: 1px solid #eaeaea;*/
    span {
      color: #777;
      font-size: 12px;
      line-height: 40px;
      font-weight: normal;
    }
  }

  .doc-list-context {
    height: 100%;
    width: 280px;
    display: inline-block;
    padding: 0 15px;
  }

  .doc-list {
    overflow-y: auto;
    width: 100%;
    height: calc(100% - 100px);
  }

  .doc-list::-webkit-scrollbar {
    display: none
  }

  .doc-item {
    margin: 0;
    padding: 20px 45px 0 45px;
    cursor: pointer;
  }

  .doc-img {
    border: 1px solid #333;
    position: relative;
    width: 121px;
    height: 161px;
    margin-bottom: 5px;

    img {
      width: 100%;
      height: 100%;
    }
  }

  .doc-page-num {
    z-index: 100;
    position: absolute;
    bottom: 0;
    right: 0;
    width: 20px;
    height: 20px;
    line-height: 20px;
    font-size: 12px;
    text-align: center;
    background-color: #333333;
    color: #fff;
  }
}

.pdfContent_1PW2f {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: auto;
  background: #f5f5f5;

  .pdf-header {
    /*position: fixed;*/
    top: 55px;
    left: 240px;
    right: 240px;
    height: 40px;
    border-bottom: 1px solid #eee;
    background-color: #fff;
    z-index: 100;
    text-align: center;
    //display: flex;
    .block {
      display: flex;
      margin-left: 350px;

      .demonstration, .slider {
        flex: 0 0 150px;
        line-height: 40px;
        width: 150px;
        text-align: right;
      }

      .demonstration {
        flex: 1;
        margin-right: 20px;
      }
    }

    .pageNo {
      display: flex;
      flex: 0 0 150px;
      margin-left: 35%;
      z-index: 2;
      text-align: center;

      .page-name {
        float: left;
      }

      .page-name button {
        border: none;
      }
    }

    .input {
      flex: 1;
      text-align: left;
      line-height: 40px;
      margin-left: 20px;

      .input-text {
        border: none;
        outline: none;
        border-bottom: 1px solid #e4e7ed;
        margin-left: 10px;
        width: 25px;
        text-align: center;
      }
    }
  }

  .pdf-container {
    display: inline-block;
    overflow: auto;
    height: 100%;
    width: 100%;

    .pdf-box {
      position: relative;
      margin: 0 auto;
    }
  }

  .pdfPage_1yRne {
    /*transition: left .3s;*/
    position: relative;
    margin: 10px 0;
  }

  .es-pdf-page__edges--visible {
    border: 2px dashed #2981d9;
  }

  .dragLayer_3ccsq {
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
  }
}

.taskInfoArea_3ThxW {
  display: flex;
  flex-direction: column;
  flex: 0 0 270px;
  width: 270px;
  height: 100%;

  .tab-bar {
    width: 100%;
    padding: 0 15px;
    height: 40px;
    font-size: 14px;
    line-height: 38px;
    display: flex;
    border-bottom: 1px solid #eaeaea;
    color: #999;
    overflow: hidden;
    justify-content: space-around;
  }

  .tab-context {
    /*padding: 0 15px;*/
    height: calc(100% - 51px);
    overflow-y: auto;
    overflow-x: hidden;
    text-align: center;
    font-weight: bold;
  }

  .item {
    margin-top: 25px;
  }

  .title {
    margin: 10px 0;
  }
}

.add-widget-module {

}

.add-widget-module-title {
  color: #333;
  font-size: 14px;
  font-weight: 500;
  margin-top: 15px;
  padding-left: 15px;
  font-size: 14px;
}

.add-widget-module-tips {
  padding: 5px 15px;
  color: #666;
  font-size: 12px;
  line-height: 20px;
}

.added-widget {
  margin-left: 10px;
  margin-top: 20px;
  width: 210px;
  height: 28px;
  box-sizing: border-box;
  border: 1px dashed rgba(0, 0, 0, .3);
  padding: 5px 0 0 5px;
  cursor: pointer;
}

.added-widget-type-name {
  font-size: 12px;
  color: #333;
  line-height: 1.2;
}

</style>
