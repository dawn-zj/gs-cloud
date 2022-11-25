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
                <el-tab-pane label="签章方式" name="1" />
              </el-tabs>
            </div>
            <template v-if="activeNameLeft == 1">
              <div>
                <el-select v-model="form.sealId" placeholder="请选择印章" style="width:100%;" @change="sealChange">
                  <el-option
                    v-for="item in sealList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
              </div>

              <div v-show="form.sealId !== null" class="seal-list-title">印章缩略图
                <div class="add-widget-module-list">
                  <div id="sealImg" fit="fill" />
                  <el-button
                    v-show="form.stampParam.useQfz == 1"
                    type="text"
                    style="float: right"
                    @click="handleQfzSeal"
                  >设为骑缝章
                  </el-button>
                </div>
              </div>

              <div v-show="form.stampType === 1" class="doc-list-title">指定签署区

                <div v-if="form.stampType === 1" class="add-widget-module-list">
                  <div id="sealdivid1" draggable="true" class="added-widget" style="width:100%">
                    <div class="added-widget-type-name">点击添加签署区
                    </div>
                  </div>
                </div>
                <div
                  v-show="form.stampType === 1 && form.stampParam.locations.length > 0"
                  class="add-widget-module-list"
                >已添加签署区
                  <div class="list">
                    <div v-for="(item,index) in form.stampParam.locations">
                      <el-link class="el-icon-s-check" @click="viewToPageNum(item.pageNum)">{{ item.name }}</el-link>
                    </div>
                  </div>
                </div>
                <div>
                  <el-form
                    v-if="page_count >=2 && page_count <=32"
                    :key="1"
                    label-width="90px"
                    style="margin-top: 20px"
                  >
                    <el-form-item label="启用骑缝章">
                      <el-switch
                        v-model="form.stampParam.useQfz"
                        :active-value="1"
                        :inactive-value="0"
                      />
                    </el-form-item>
                    <el-form-item v-if="form.stampParam.useQfz == 1" label="骑缝位置">
                      <el-radio-group v-model="form.stampParam.qfzType" size="small">
                        <el-radio-button :label="0">左骑缝</el-radio-button>
                        <el-radio-button :label="1">右骑缝</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item v-if="form.stampParam.useQfz == 1" label="印章名称">
                      <span>{{ form.stampParam.qfzSealName }}</span>
                    </el-form-item>
                  </el-form>
                </div>
                <div slot="footer" style="text-align: center">
                  <el-button type="primary" @click="submitForm">签 署</el-button>
                </div>
              </div>

            </template>
          </div>
        </div>
        <div class="pdfContent_1PW2f">
          <div class="pdf-header">
            <div class="pageNo">
              <div style="width:140px;margin-top:5px;">
                <span>
                  <el-button size="small" @click="viewToPageNum(1)">首页</el-button>
                  <el-button size="small" @click="viewToPageNum(page_count)">尾页</el-button>
                </span>

              </div>
              <div class="page-name">
                <button
                  style="background-color: Transparent; margin-top:10px;cursor:pointer;"
                  type="button"
                  @click="pagePrev"
                ><span style="color: #4D4D4D">&lt;</span></button>
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
                ><span style="color: #4D4D4D;">&gt;</span></button>
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
                <canvas :id="'the-canvas'+pageNum" class="pdf-item" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>

import { base642blob, getBase64, getFileType } from '@/utils/file'

const PDFJS = require('pdfjs-dist')
PDFJS.GlobalWorkerOptions.workerSrc = './pdf.worker.js'

// import { getPdfFile, pdfStamp } from '@/api/seal/pdfFile'
// import { getSeal, listSeal } from '@/api/seal/seal'
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

    this.drag()
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
    },
    // 删除文件
    handleFileRemove(file, fileList) {
      // this.$set(this, 'fileBase64', '')
    },
    // 获取文件信息
    getFileInfo() {
      // this.form.id = this.$route.query.id
      // getPdfFile(this.form.id).then(res => {
      //   this.templateInfo = res.data
      //   this.getTemplateData()
      //   this.getSealList()
      // })
      this.pdfUrl = this.publicPath + '/show.pdf'
      this._loadFile(this.pdfUrl)
      this.getSealList()
    },
    /** 查询印章管理列表 */
    getSealList() {
      // const data = {
      //   pageNum: -1
      // }
      // listSeal(data).then(response => {
      //   this.sealList = response.data.rows
      // }).catch(() => {
      // })
      this.sealList = [
        {
          'id': 1,
          'name': '测试章'
        }
      ]
    },
    // 预览渲染模板文件
    _renderPage(num) {
      this.pdfDoc.getPage(num).then((page) => {
        const canvas = document.getElementById('the-canvas' + num)
        const ctx = canvas.getContext('2d')
        const viewport = page.getViewport(this.scale)
        // pdfjs返回的PDF宽高单位是pt，但我们渲染直接用了px，视觉上pdf比实际小一圈，但清晰
        // 需要注意，拖拽图片时，图片宽高单位和渲染单位，与PDF渲染逻辑保持一致
        this.pdfHeight = canvas.height = viewport.height
        this.pdfWidth = canvas.width = viewport.width
        const renderContext = {
          canvasContext: ctx,
          viewport: viewport
        }
        page.render(renderContext)

        // 渲染指定页时，清除所有签署区，重新渲染，处于当前页的签署区展示，否则隐藏
        var signArea = document.getElementById('signArea')
        signArea.innerHTML = ''

        const locations = this.form.stampParam.locations
        if (locations.length > 0) {
          this.$nextTick(function() {
            for (var i = 0; i < locations.length; i++) {
              const location = locations[i]
              if (location.pageNum == this.pageNum) {
                this.reviewSignArea(location.x, location.y, location.sealInfo, true)
              } else {
                this.reviewSignArea(location.x, location.y, location.sealInfo, false)
              }
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
    sealChange(sealId) {
      // getSeal(sealId).then(res => {
      var seal = {
        'name': '测试章',
        'photo': {
          'dpi': 96,
          'width': 161,
          'height': 161
        },
        'photoDataB64': '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'
      }
      this.sealInfo.name = seal.name
      this.sealInfo.dpi = seal.photo.dpi
      this.sealInfo.width = seal.photo.width
      this.sealInfo.height = seal.photo.height
      this.sealInfo.base64 = seal.photoDataB64

      // 印章缩略图预览
      const sealImgDiv = document.getElementById('sealImg')

      // 像素转换成磅
      const cloneTempWidth = this.sealInfo.width / this.sealInfo.dpi * 72
      const cloneTempHeight = this.sealInfo.height / this.sealInfo.dpi * 72

      // 若转换成磅之后图片还是很大，左侧缩略图缩小至150，只是一个大小界限，防止图片超大
      if (cloneTempWidth > 150) {
        sealImgDiv.style.width = 150 + 'px'
        sealImgDiv.style.height = cloneTempHeight / (cloneTempWidth / 150) + 'px'
      } else {
        sealImgDiv.style.width = cloneTempWidth + 'px'
        sealImgDiv.style.height = cloneTempHeight + 'px'
      }
      sealImgDiv.style.backgroundImage = 'url(data:image/png;base64,' + this.sealInfo.base64 + ')'
      sealImgDiv.style.backgroundSize = '100% 100%'
    },
    createSealDiv(left, top, sealInfo) {
      const cloneTemp = document.createElement('div')
      // 像素转换成磅
      const cloneTempWidth = sealInfo.width / sealInfo.dpi * 72
      const cloneTempHeight = sealInfo.height / sealInfo.dpi * 72
      cloneTemp.className = 'sign-area'
      cloneTemp.draggable = true
      cloneTemp.style.backgroundColor = 'hsla(0,0%,100%,0)'
      cloneTemp.style.backgroundImage = 'url(data:image/png;base64,' + sealInfo.base64 + ')'
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
    drag() {
      var _this = this
      divMove(document.getElementById('sealdivid1'), false)

      function divMove(div, isRemove) {
        // 签署区鼠标按下，增加一个签署区
        div.onmousedown = function(event) {
          // 选择印章
          if (_this.form.sealId == null) {
            _this.msgError('请选择印章')
            return
          }

          // 签署区
          var signArea = document.getElementById('signArea')

          // if (signArea.children.length >= 1) {
          //   _this.msgError('只能添加1个签署区')
          //   return
          // }

          if (isRemove) {
            div.style.display = 'none'
          }

          // 签署区创建在当前所在页
          const cloneTemp = _this.createSealDiv(0, 0, _this.sealInfo)
          const deleteDiv = _this.createDeleteDiv()
          cloneTemp.appendChild(deleteDiv)
          signArea.appendChild(cloneTemp)

          var y = _this.pdfHeight - 120
          _this.form.stampParam.locations.push({ // 添加坐标和页码
            x: 0,
            y: y,
            pageNum: _this.pageNum,
            name: _this.sealInfo.name,
            sealId: _this.form.sealId,
            sealInfo: JSON.parse(JSON.stringify(_this.sealInfo))
          })

          // 给签署方框加 点击/抬起/移动事件，起到拖拽效果
          cloneTemp.onmousedown = function(e) {
            document.onmousemove = function(event) {
              _this.move(event, e)
            }

            document.onmouseup = function(event) {
              document.onmousemove = null
              document.onmouseup = null
              _this.up(event, e)
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
            _this.form.stampParam.locations.splice(index, 1)

            // 重新渲染当前页，或者删除该签署域dom节点
            _this.viewToPageNum(_this.pageNum)
          }
        }
      }
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
      const deleteDiv = _this.createDeleteDiv()
      cloneTemp.appendChild(deleteDiv)
      signArea.appendChild(cloneTemp)

      var pdfBox = document.getElementsByClassName('pdfPage_1yRne')[0]
      var top = pdfBox.scrollHeight - bottom - cloneTemp.scrollHeight

      cloneTemp.style.left = left + 'px'
      cloneTemp.style.top = top + 'px'

      // 给签署方框加 点击/抬起/移动事件，起到拖拽效果
      cloneTemp.onmousedown = function(e) {
        document.onmousemove = function(event) {
          _this.move(event, e)
        }

        document.onmouseup = function(event) {
          document.onmousemove = null
          document.onmouseup = null
          _this.up(event, e)
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
        _this.form.stampParam.locations.splice(index, 1)

        // 重新渲染当前页，或者删除该签署域dom节点
        _this.viewToPageNum(_this.pageNum)
      }
    },

    move(documentEvent, divEvent) {
      var _this = this
      // var event = event || window.event;
      // var cloneTemp = document.getElementsByClassName('sign-area')[0]

      var cloneTemp = divEvent.target

      var pdfBox = document.getElementsByClassName('pdfPage_1yRne')[0]

      // pdf在浏览器的绝对位置
      var pdfTop = _this.offsetTop(pdfBox)
      var pdfBottom = pdfTop + pdfBox.scrollHeight
      var pdfLeft = _this.offsetLeft(pdfBox)
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

    up(documentEvent, divEvent) {
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

      _this.form.stampParam.stampType = 1

      var index = [].indexOf.call(document.getElementById('signArea').children, cloneTemp)
      if (index < 0) { return }

      if (_this.form.stampParam.locations.length > index) { // 已添加过，修改坐标
        var location = _this.form.stampParam.locations[index] // 点x删除也会触发这里，加一层判断防止出错
        if (location.x != undefined) { _this.form.stampParam.locations[index].x = x }

        if (location.y != undefined) { _this.form.stampParam.locations[index].y = y }
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
    },
    handleQfzSeal() {
      this.form.stampParam.qfzSealId = this.form.sealId
      this.form.stampParam.qfzSealName = this.sealInfo.name
      this.msgSuccess('设置成功')
    },
    submitForm() {
      const formData = JSON.parse(JSON.stringify(this.form))
      if (this.page_count < 2 || this.page_count > 32) {
        delete formData.stampParam.useQfz
      }
      if (formData.stampType == 1) {
        if (formData.stampParam.locations.length <= 0) {
          this.msgError('请进行拖拽配置')
          return
        }
        if (formData.stampParam.useQfz == 1 && formData.stampParam.qfzSealId == null) {
          this.msgError('请设置骑缝所用印章')
          return
        }

        const data = {
          id: formData.id,
          stampType: formData.stampType,
          dragParam: {
            param: formData.stampParam.locations,
            qfzParam: {
              useQfz: formData.stampParam.useQfz,
              qfzType: formData.stampParam.qfzType,
              sealId: formData.stampParam.qfzSealId
            }
          }
        }
        console.log(data)
        this.msgInfo('暂未上线，敬请期待')
        // pdfStamp(data).then(res => {
        //   this.msgSuccess(res.msg)
        //
        //   // 返回列表
        //   this.back()
        // })
      } else {
        this.msgError('不支持的签署方式')
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
