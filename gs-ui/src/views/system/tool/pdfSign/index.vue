<template>
  <div id="app">
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
    <el-row>
      <el-col :span="4">
        <div>
          <div class="left-title">我的印章</div>
          <draggable v-model="mainImagelist" :group="{name:'itext',pull:'clone'}" :sort="false" @end="end">
            <transition-group type="transition">
              <li v-for="item in mainImagelist" :key="item" class="item">
                <img :src="item" width="100%;" height="100%" class="imgstyle">
              </li>
            </transition-group>
          </draggable>
        </div>
      </el-col>

      <el-col :span="14">
        <!-- pdf的预览 -->
        <div style="overflow: scroll">
          <div class="block">
            <span class="demonstration">{{ scale*100 }}%</span>
            <el-slider v-model="scale" :min="0.5" :max="2" :step="0.25" @change="queueRenderPage(pageNum)" />
          </div>
          <div class="page">
            <button class="btn-outline-dark" @click="prevPage">上一页</button>
            <button class="btn-outline-dark" @click="nextPage">下一页</button>
            <button class="btn-outline-dark">{{ pageNum }}/{{ numPages }}页</button>
            <input id="page-input" ref="getPages" class="btn-outline-dark" :value="pageNum" type="number" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
            <button class="btn-outline-dark" @click="cutover">跳转</button>
          </div>

          <canvas id="the-canvas" />
        </div>
      </el-col>

      <el-col :span="5" :offset="1">
        <div>
          <div class="left-title">任务信息</div>
          <div>
            <div>
              <div class="right-item">
                <span class="right-item-title">文件主题</span>
                <span class="detail-item-desc">{{ taskInfo.title }}</span>
              </div>
              <div class="right-item">
                <span class="right-item-title">发起方</span>
                <span class="detail-item-desc">{{ taskInfo.uname }}</span>
              </div>
              <div class="right-item">
                <span class="right-item-title">截止时间</span>
                <span class="detail-item-desc">{{ taskInfo.endtime }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

  </div>

</template>

<script>
import '@/assets/pdfview/index.css'
import { base642blob, getBase64, getFileType } from '@/utils/file'

const storage = window.localStorage

const pdfjsLib = require('pdfjs-dist')
pdfjsLib.GlobalWorkerOptions.workerSrc = require('pdfjs-dist/build/pdf.worker.js')

export default {
  name: 'IndexVue',
  data() {
    return {
      fileList: [],
      pdfUrl: '',
      pdfDoc: null,
      numPages: 1,
      pageNum: 1,
      scale: 1.0, // 放大倍数
      pageRendering: false,
      pageNumPending: null,
      sealUrl: '',
      signUrl: '',
      canvas: null,
      ctx: null,
      canvasEle: null,
      whDatas: null,
      isSpinShow: false,
      mainImagelist: [],
      taskInfo: {}
    }
  },
  // 监听一个值的变化,然后执行相对应的函数。
  watch: {
    pdfUrl: function(val) {
      console.log(val)
      this.$nextTick(() => {
        this.showpdf(val)
      })
    },
    pageNum: function(pageNum) {
      // this.commonSign(this.pageNum);
      this.queueRenderPage(this.pageNum)
    }
  },
  // 未渲染html页面时执行
  created: function() {
    var that = this
    that.pdfUrl = that.publicPath + '/show.pdf'
    that.mainImagelist = [`http://${window.location.host}/` + that.publicPath + 'sign.png', `http://${window.location.host}/` + that.publicPath + 'seal.png']
    that.taskInfo = { 'title': '测试盖章', uname: '张三', endtime: '2021-09-01 17:59:59' }
  },
  // 事件方法
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
    },
    // 删除文件
    handleFileRemove(file, fileList) {
      // this.$set(this, 'fileBase64', '')
    },

    /**
     *  翻页部分开始----------------------------------------------------------------
     */
    renderPage(num) {
      const _this = this
      this.pageRendering = true
      // Using promise to fetch the page
      return this.pdfDoc.getPage(num).then((page) => {
        const viewport = page.getViewport({ scale: _this.scale })
        _this.canvas.height = viewport.height
        _this.canvas.width = viewport.width

        // Render PDF page into canvas context
        const renderContext = {
          canvasContext: _this.ctx,
          viewport: viewport
        }
        const renderTask = page.render(renderContext)

        // Wait for rendering to finish
        renderTask.promise.then(() => {
          _this.pageRendering = false
          if (_this.pageNumPending !== null) {
            // New page rendering is pending
            this.renderPage(_this.pageNumPending)
            _this.pageNumPending = null
          }
          this.isSpinShow = false
        })
      })
    },
    queueRenderPage(pageNums) {
      if (this.pageRendering) { // 渲染中，终止用户翻页行为
        this.pageNumPending = pageNums
      } else {
        this.renderPage(pageNums)
      }
    },
    prevPage() {
      // this.confirmSignature()
      if (this.pageNum <= 1) {
        return
      }
      this.pageNum--
    },
    nextPage() {
      // this.confirmSignature()
      if (this.pageNum >= this.numPages) {
        return
      }
      this.pageNum++
    },
    cutover() {
      // this.confirmSignature()
      const pageNums = Number(this.$refs.getPages.value)
      if (pageNums > this.numPages) {
        this.pageNum = this.numPages
        return
      }

      if (pageNums < 1) {
        this.pageNum = 1
        return
      }

      if (!(/(^[1-9]\d*$)/.test(pageNums))) {
        this.pageNum = 1
        return
      }
      this.pageNum = pageNums
    },
    /**
     *  翻页部分结束----------------------------------------------------------------
     */

    /**
     *  展示PDF部分开始----------------------------------------------------------------
     */
    showpdf(pdfUrl) {
      this.canvas = document.getElementById('the-canvas')
      this.ctx = this.canvas.getContext('2d')
      pdfjsLib.getDocument({ url: pdfUrl, rangeChunkSize: 65536, disableAutoFetch: false }).promise.then((pdfDoc_) => {
        this.pdfDoc = pdfDoc_
        this.numPages = this.pdfDoc.numPages
        this.renderPage(this.pageNum).then((res) => {
          this.renderPdf({
            width: this.canvas.width,
            height: this.canvas.height
          })
          // this.isSpinShow = false;
        })
        // this.commonSign(this.pageNum, true)
      })
    },

    // 设置绘图区域宽高
    renderPdf(data) {
      this.whDatas = data
      // document.querySelector('.elesign').style.width = data.width + 'px'
    },
    /**
     *  展示PDF部分结束----------------------------------------------------------------
     */

    /**
     *  盖章部分开始----------------------------------------------------------------
     */
    // 添加公章
    addSeal(sealUrl, left, top, index) {
      fabric.Image.fromURL(
        sealUrl,
        (oImg) => {
          oImg.set({
            left: left,
            top: top,
            // angle: 10,
            scaleX: 0.8,
            scaleY: 0.8,
            index: index
          })
          // oImg.scale(0.5); //图片缩小一
          this.canvasEle.add(oImg)
        }
      )
    },

    // 确认签章位置并保存到缓存
    confirmSignature() {
      const data = this.canvasEle.getObjects() // 获取当前页面内的所有签章信息
      let caches = JSON.parse(storage.getItem('signs')) // 获取缓存字符串后转换为对象
      const signDatas = {} // 存储当前页的所有签章信息
      let i = 0
      const sealUrl = ''
      for (var val of data) {
        signDatas[i] = {
          width: val.width,
          height: val.height,
          top: val.top,
          left: val.left,
          angle: val.angle,
          translateX: val.translateX,
          translateY: val.translateY,
          scaleX: val.scaleX,
          scaleY: val.scaleY,
          pageNum: this.pageNum,
          sealUrl: this.mainImagelist[val.index],
          index: val.index
        }
        i++
      }
      if (caches == null) {
        caches = {}
        caches[this.pageNum] = signDatas
      } else {
        caches[this.pageNum] = signDatas
      }
      storage.setItem('signs', JSON.stringify(caches)) // 对象转字符串后存储到缓存
    },

    /**
     *  盖章部分结束----------------------------------------------------------------
     */
    end(e) {
      this.addSeal(this.mainImagelist[e.newDraggableIndex], e.originalEvent.layerX, e.originalEvent.layerY, e.newDraggableIndex)
    }
  }
}
</script>

<style scoped>

</style>
