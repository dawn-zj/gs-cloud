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

          <!-- PDF预览区域 -->
          <canvas id="the-canvas" />
        </div>
        <!-- 拖拽盖章区域 -->
        <canvas id="ele-canvas" />
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

const pdfjsLib = window['pdfjs-dist/build/pdf']

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
    whDatas: {
      handler() {
        if (this.whDatas) {
          this.renderFabric()
          this.canvasEvents()
        }
      }
    },
    pdfUrl: function(val) {
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
    that.mainImagelist = [`http://${window.location.host}/` + that.publicPath + '/sign.png', `http://${window.location.host}/` + that.publicPath + '/seal.png']
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
    // 监控预览区变化，生成拖拽绘图区域
    renderFabric() {
      const canvaEle = document.querySelector('#ele-canvas')

      // 拖拽区域与PDF预览区域保持同样大小
      canvaEle.width = this.whDatas.width
      canvaEle.height = this.whDatas.height

      // 使用绝对定位，拖拽区与预览区定在同一位置，实现视觉上的盖章
      this.canvasEle = new fabric.Canvas(canvaEle)
      const container = document.querySelector('.canvas-container')
      container.style.position = 'absolute'
      container.style.top = '90px'
    },

    // 相关事件操作哟
    canvasEvents() {
      // 拖拽边界 不能将图片拖拽到绘图区域外
      this.canvasEle.on('object:moving', function(e) {
        var obj = e.target
        // if object is too big ignore
        if (obj.currentHeight > obj.canvas.height || obj.currentWidth > obj.canvas.width) {
          return
        }
        obj.setCoords()
        // top-left  corner
        if (obj.getBoundingRect().top < 0 || obj.getBoundingRect().left < 0) {
          obj.top = Math.max(obj.top, obj.top - obj.getBoundingRect().top)
          obj.left = Math.max(obj.left, obj.left - obj.getBoundingRect().left)
        }
        // bot-right corner
        if (obj.getBoundingRect().top + obj.getBoundingRect().height > obj.canvas.height || obj.getBoundingRect().left + obj.getBoundingRect().width > obj.canvas.width) {
          obj.top = Math.min(obj.top, obj.canvas.height - obj.getBoundingRect().height + obj.top - obj.getBoundingRect().top)
          obj.left = Math.min(obj.left, obj.canvas.width - obj.getBoundingRect().width + obj.left - obj.getBoundingRect().left)
        }
      })
    },
    // 拖拽触发
    end(e) {
      alert('x,y： ' + e.originalEvent.layerX + ',' + e.originalEvent.layerY)
      console.log(e)
      this.addSeal(this.mainImagelist[e.newIndex], e.originalEvent.layerX, e.originalEvent.layerY, e.newIndex)
    },
    // 拖拽添加公章
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
    }

    /**
     *  盖章部分结束----------------------------------------------------------------
     */

  }
}
</script>

<style scoped>

</style>
