<template>

  <el-row>
    <el-col :span="24">
      <div id="pageContent" class="pageContent_2lAGg">
        <div v-if="showLeft" id="left" class="leftContent">
          <slot name="left" />
        </div>
        <div class="pdfContent">
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
              <div id="signArea" />
              <div id="fieldArea" />
              <div class="pdfPage_1yRne">
                <canvas id="the-canvas" class="pdf-item" />
              </div>
            </div>
          </div>
        </div>
        <div v-if="showRight" id="right" class="leftContent">
          <slot name="right" />
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>

import { base642blob } from '@/utils/base64/base64'
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
      showEdges: false,
      showLeft: true,
      showRight: true,
      // 遮罩层
      loading: true,
      pdfUrl: '',
      pdfDoc: null, // pdfjs 生成的对象
      pageNum: 1, //
      page_count: 0, // 总页数
      pdfWidth: 0,
      pdfHeight: 0,
      scale: 1.0,
      sealInfo: {
        name: '',
        dpi: 96,
        width: 160, // 单位像素，拖拽显示时转换成磅，公式：像素 / 分辨率 * 72
        height: 160,
        base64: ''
      },
      // 默认可拖拽
      isDrag: true,
      // 拖拽文本域坐标位置x,y,页码，坐标起点左下角
      fieldParam: [
        {
          fx: 0,
          fy: 0,
          width: 100, // 单位像素，拖拽显示时转换成磅，公式：像素 / 分辨率 * 72
          height: 20,
          pageNum: 1,
          fname: '测试文本域'
        }
      ],
      // 拖拽签署域坐标位置x,y,页码
      locations: [
        {
          pageNum: 1,
          x: 0,
          y: 0,
          width: 160, // 单位像素，拖拽显示时转换成磅，公式：像素 / 分辨率 * 72
          height: 160,
          name: '测试签署域',
          // 印章信息，有数据时替掉宽高默认值
          sealInfo: {
            id: 1,
            name: '测试章',
            dpi: 96,
            width: 160, // 单位像素，拖拽显示时转换成磅，公式：像素 / 分辨率 * 72
            height: 160,
            base64: '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'
          }
        }
      ]
    }
  },
  // 监听一个值的变化,然后执行相对应的函数。
  watch: {
    pdfUrl: function(val) {
      this.$nextTick(() => {
        this._loadFile(val)
      })
    },
    // 监听pageNum
    pageNum: {
      handler(val, oldVal) {
        this.renderField(this.fieldParam, this.isDrag)
        this.renderStampField(this.locations, this.isDrag)
      },
      deep: true
    }
  },
  mounted() {
    this.$nextTick(function() {
      this.getFileInfo()
    })
  },
  methods: {
    getLocationWidth(location) {
      if (location.sealInfo != null && location.sealInfo.width) {
        return this.px2pt(location.sealInfo.width, location.sealInfo.dpi)
      }
      return location.width
    },
    getLocationHeight(location) {
      if (location.sealInfo != null && location.sealInfo.height) {
        return this.px2pt(location.sealInfo.height, location.sealInfo.dpi)
      }
      return location.height
    },
    px2pt(px, dpi) {
      return px / dpi * 72
    },
    // 获取文件信息
    getFileInfo(blob, fileBase64) {
      if (blob) {
        this.pdfUrl = window.URL.createObjectURL(blob)
        this.loading = false
        this.loadFile(this.pdfUrl)
      } else if (fileBase64) {
        this.pdfUrl = window.URL.createObjectURL(base642blob(fileBase64))
        this.loadFile(this.pdfUrl)
      } else {
        this.pdfUrl = this.publicPath + '/show.pdf'
        this._loadFile(this.pdfUrl)
      }
    },
    // 预览渲染模板文件
    _renderPage(num) {
      this.pdfDoc.getPage(num).then((page) => {
        const canvas = document.getElementById('the-canvas')
        const ctx = canvas.getContext('2d')
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

        // 渲染指定页时，清除所有文本域，重新渲染，处于当前页的文本域展示，否则隐藏
        this.renderField(this.fieldParam, this.isDrag)

        // 渲染指定页时，清除所有签署域，重新渲染，处于当前页的签署域展示，否则隐藏
        this.renderStampField(this.locations, this.isDrag)
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
    renderField(fieldParam, isDrag) {
      // 渲染指定页时，清除所有文本域，重新渲染，处于当前页的文本域展示，否则隐藏
      var fieldArea = document.getElementById('fieldArea')
      fieldArea.innerHTML = ''

      if (fieldParam.length > 0) {
        this.$nextTick(function() {
          for (var i = 0; i < fieldParam.length; i++) {
            const field = fieldParam[i]
            const isShow = field.pageNum === this.pageNum
            this.reviewFieldArea(field.fx, field.fy, field.width, field.height, field.fname, isShow, isDrag)
          }
        })
      }
    },
    renderStampField(locations, isDrag) {
      // 渲染指定页时，清除所有签署域，重新渲染，处于当前页的签署域展示，否则隐藏
      var signArea = document.getElementById('signArea')
      signArea.innerHTML = ''

      if (locations.length > 0) {
        this.$nextTick(function() {
          for (var i = 0; i < locations.length; i++) {
            const location = locations[i]
            console.log(location)
            // 像素转磅
            const width = this.getLocationWidth(location)
            const height = this.getLocationHeight(location)
            const isShow = location.pageNum === this.pageNum
            this.reviewSignArea(location.x, location.y, width, height, location.name, location.sealInfo.base64, isShow, isDrag)
          }
        })
      }
    },
    /**
     *
     * @param left 距离页面左边界距离
     * @param bottom 距离页面下边界距离
     * @param width 域宽，单位磅
     * @param height 域高，单位磅
     * @param name 域名称
     * @param isShow 是否展示
     * @param isDrag 是否允许拖拽
     */
    reviewSignArea(left, bottom, width, height, name, sealBase64, isShow, isDrag) {
      var _this = this

      // 签署域
      var signArea = document.getElementById('signArea')
      const cloneTemp = _this.createSealDiv(0, 0, width, height, name, sealBase64)
      if (isShow) {
        cloneTemp.style.display = 'block'
      } else {
        cloneTemp.style.display = 'none'
      }
      const deleteDiv = _this.createDeleteDiv()
      cloneTemp.appendChild(deleteDiv)
      signArea.appendChild(cloneTemp)

      var pdfBox = document.getElementsByClassName('pdfPage_1yRne')[0]
      var top = pdfBox.scrollHeight - bottom - cloneTemp.scrollHeight

      cloneTemp.style.left = left + 'px'
      cloneTemp.style.top = top + 'px'

      if (isDrag) {
        // 给签署方框加 点击/抬起/移动事件，起到拖拽效果
        cloneTemp.onmousedown = function(e) {
          document.onmousemove = function(event) {
            _this.move(event, e)
          }

          document.onmouseup = function(event) {
            document.onmousemove = null
            document.onmouseup = null
            _this.upSeal(event, e)
          }

          // 阻止默认事件
          return false
        }

        // 签署域方框鼠标悬浮，显示删除小红叉
        cloneTemp.onmouseover = function(event) {
          deleteDiv.style.display = 'block'
        }

        // 签署域方框鼠标离开，隐藏删除小红叉
        cloneTemp.onmouseleave = function(event) {
          deleteDiv.style.display = 'none'
        }

        // 点击删除小红叉，删除签署域方框
        deleteDiv.onclick = function(event) {
          var signDiv = event.target.parentNode

          // 删除坐标参数
          var index = [].indexOf.call(document.getElementById('signArea').children, signDiv)
          _this.form.stampParam.stampType = 1
          _this.locations.splice(index, 1)

          // 重新渲染当前页，或者删除该签署域dom节点
          _this.viewToPageNum(_this.pageNum)
        }
      }
    },
    /**
     *
     * @param width 域宽，单位像素
     * @param height 域高，单位像素
     * @param left 距离页面左边界距离
     * @param bottom 距离页面下边界距离
     * @param name 域名称
     * @param isShow 是否展示
     */
    reviewFieldArea(left, bottom, width, height, name, isShow, isDrag) {
      var _this = this

      // 签署域
      var fieldArea = document.getElementById('fieldArea')
      const cloneTemp = _this.createFieldDiv(0, 0, width, height, name)
      if (isShow) {
        cloneTemp.style.display = 'block'
      } else {
        cloneTemp.style.display = 'none'
      }
      const deleteDiv = _this.createDeleteDiv()
      cloneTemp.appendChild(deleteDiv)
      fieldArea.appendChild(cloneTemp)

      var pdfBox = document.getElementsByClassName('pdfPage_1yRne')[0]
      var top = pdfBox.scrollHeight - bottom - cloneTemp.scrollHeight

      cloneTemp.style.left = left + 'px'
      cloneTemp.style.top = top + 'px'

      if (isDrag) {
        // 给签署方框加 点击/抬起/移动事件，起到拖拽效果
        cloneTemp.onmousedown = function(e) {
          document.onmousemove = function(event) {
            _this.move(event, e)
          }

          document.onmouseup = function(event) {
            document.onmousemove = null
            document.onmouseup = null
            _this.upField(event, e)
          }

          // 阻止默认事件
          return false
        }

        // 签署域方框鼠标悬浮，显示删除小红叉
        cloneTemp.onmouseover = function(event) {
          deleteDiv.style.display = 'block'
        }

        // 签署域方框鼠标离开，隐藏删除小红叉
        cloneTemp.onmouseleave = function(event) {
          deleteDiv.style.display = 'none'
        }

        // 点击删除小红叉，删除签署域方框
        deleteDiv.onclick = function(event) {
          var fieldDiv = event.target.parentNode

          // 删除坐标参数
          var index = [].indexOf.call(document.getElementById('fieldArea').children, fieldDiv)
          _this.fieldParam.splice(index, 1)

          // 重新渲染当前页，或者删除该签署域dom节点
          _this.viewToPageNum(_this.pageNum)
        }
      }
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
    createFieldDiv(left, top, width, height, innerHTML) {
      const	cloneTemp = document.createElement('div')
      const cloneTempWidth = width
      const cloneTempHeight = height
      cloneTemp.className = 'field-area'
      cloneTemp.innerHTML = innerHTML
      cloneTemp.style.textAlign = 'left'
      cloneTemp.style.color = 'skyblue'
      cloneTemp.style.lineHeight = cloneTempHeight / Math.ceil(innerHTML.length / 7) + 'px'
      cloneTemp.draggable = true
      cloneTemp.style.backgroundColor = 'hsla(0,0%,100%,.85)'
      cloneTemp.style.border = '1px dotted skyblue'
      cloneTemp.style.position = 'absolute' // 相对于pdf图片的绝对位置
      cloneTemp.style.zIndex = '8' // footer是9
      cloneTemp.style.width = cloneTempWidth + 'px'
      cloneTemp.style.height = cloneTempHeight + 'px'
      cloneTemp.style.left = left + 'px'
      cloneTemp.style.top = top + 'px'
      cloneTemp.style.cursor = 'grab'
      cloneTemp.style.wordBreak = 'break-all'

      return cloneTemp
    },
    /**
     * 创建签署域div
     * @param left
     * @param top
     * @param width 单位磅
     * @param height 单位磅
     * @param innerHTML 占位名字
     * @param sealBase64 占位图片
     * @returns {HTMLDivElement}
     */
    createSealDiv(left, top, width, height, innerHTML, sealBase64) {
      const cloneTemp = document.createElement('div')
      // 像素转换成磅
      const cloneTempWidth = width
      const cloneTempHeight = height
      cloneTemp.className = 'sign-area'
      cloneTemp.innerHTML = innerHTML
      cloneTemp.draggable = true
      cloneTemp.style.backgroundColor = 'hsla(0,0%,100%,0)'
      cloneTemp.style.backgroundImage = 'url(data:image/png;base64,' + sealBase64 + ')'
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
    createDeleteDiv() {
      const deleteDiv = document.createElement('div')
      const deleteDivWidth = 20
      const deleteDivHeight = 20
      deleteDiv.innerHTML = '×'
      deleteDiv.style.textAlign = 'center'
      deleteDiv.style.color = 'skyblue'
      deleteDiv.style.lineHeight = deleteDivHeight + 'px'
      deleteDiv.style.width = deleteDivWidth + 'px'
      deleteDiv.style.height = deleteDivHeight + 'px'
      deleteDiv.style.borderRadius = deleteDivHeight / 2 + 'px'
      deleteDiv.style.position = 'absolute'
      deleteDiv.style.right = '0px'
      deleteDiv.style.top = '0px'
      deleteDiv.style.display = 'none'
      deleteDiv.style.cursor = 'pointer'

      return deleteDiv
    },
    move(documentEvent, divEvent) {
      var cloneTemp = divEvent.target

      var pdfBox = document.getElementsByClassName('pdfPage_1yRne')[0]

      // pdf在浏览器的绝对位置
      var pdfTop = pdfBox.getBoundingClientRect().top
      var pdfBottom = pdfTop + pdfBox.scrollHeight
      var pdfLeft = pdfBox.getBoundingClientRect().left
      var pdfRight = pdfLeft + pdfBox.scrollWidth

      // 鼠标在浏览器的绝对位置 event.clientX，event.clientY
      var event = documentEvent || window.event
      var top = event.clientY > pdfTop ? event.clientY : pdfTop
      var left = event.clientX > pdfLeft ? event.clientX : pdfLeft

      // 越界处理，减去框的宽高
      if (top > pdfBottom - cloneTemp.clientHeight) { top = pdfBottom - cloneTemp.clientHeight }

      if (left > pdfRight - cloneTemp.clientWidth) { left = pdfRight - cloneTemp.clientWidth }

      // 计算签署框相对于pdf左上角的相对位置
      left = left - pdfLeft
      top = top - pdfTop

      cloneTemp.style.left = left + 'px'
      cloneTemp.style.top = top + 'px'
      return false
    },

    upSeal(documentEvent, divEvent) {
      var _this = this
      // 移除移动事件
      document.onmousemove = function(e) {
      }

      var cloneTemp = divEvent.target

      // var cloneTemp = document.getElementsByClassName('sign-area')[0]
      var pdfBox = document.getElementsByClassName('pdfPage_1yRne')[0]

      var pdfTop = _this.offsetTop(pdfBox)
      var pdfBottom = pdfTop + pdfBox.scrollHeight

      // 计算签署框相对于pdf左下角的相对位置
      var x = _this.offsetLeft(cloneTemp) + cloneTemp.scrollLeft - _this.offsetLeft(pdfBox)
      var y = pdfBottom - _this.offsetTop(cloneTemp) - cloneTemp.scrollHeight

      // 自动修正，以保证不出边界
      x = x < 0 ? 0 : x
      x = x > pdfBox.scrollWidth ? pdfBox.scrollWidth : x
      y = y < 0 ? 0 : y
      y = y > pdfBox.scrollHeight ? pdfBox.scrollHeight : y

      var index = [].indexOf.call(document.getElementById('signArea').children, cloneTemp)
      if (index < 0) { return }

      if (_this.locations.length > index) { // 已添加过，修改坐标
        var location = _this.locations[index] // 点x删除也会触发这里，加一层判断防止出错
        if (location.x !== undefined) { _this.locations[index].x = x }

        if (location.y !== undefined) { _this.locations[index].y = y }
      }
    },
    upField(documentEvent, divEvent) {
      var _this = this
      // 移除移动事件
      document.onmousemove = function(e) {
      }

      var cloneTemp = divEvent.target

      // var cloneTemp = document.getElementsByClassName('sign-area')[0]
      var pdfBox = document.getElementsByClassName('pdfPage_1yRne')[0]

      var pdfTop = _this.offsetTop(pdfBox)
      var pdfBottom = pdfTop + pdfBox.scrollHeight

      // 计算签署框相对于pdf左下角的相对位置
      var x = _this.offsetLeft(cloneTemp) + cloneTemp.scrollLeft - _this.offsetLeft(pdfBox)
      var y = pdfBottom - _this.offsetTop(cloneTemp) - cloneTemp.scrollHeight

      // 自动修正，以保证不出边界
      x = x < 0 ? 0 : x
      x = x > pdfBox.scrollWidth ? pdfBox.scrollWidth : x
      y = y < 0 ? 0 : y
      y = y > pdfBox.scrollHeight ? pdfBox.scrollHeight : y

      var index = [].indexOf.call(document.getElementById('fieldArea').children, cloneTemp)
      if (index < 0) { return }

      if (_this.fieldParam.length > index) { // 已添加过，修改坐标
        var field = _this.fieldParam[index] // 点x删除也会触发这里，加一层判断防止出错
        if (field.fx !== undefined) { _this.fieldParam[index].fx = x }

        if (field.fy !== undefined) { _this.fieldParam[index].fy = y }
      }
    },
    offsetTop(elements) {
      var top = elements.offsetTop
      var parent = elements.offsetParent
      while (parent != null) {
        top += parent.offsetTop
        parent = parent.offsetParent
      }
      return top
    },
    offsetLeft(elements) {
      var left = elements.offsetLeft
      var parent = elements.offsetParent
      while (parent != null) {
        left += parent.offsetLeft
        parent = parent.offsetParent
      }

      return left
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

.leftContent {
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

.pdfContent {
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
