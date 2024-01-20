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
      <view-pdf
        ref="viewComponent"
        :show-left="showLeft"
        :show-right="showRight"
        :is-drag="isDrag"
        :url="pdfUrl"
        @render-change-field="renderChangeField"
        @render-change-stamp="renderChangeStamp"
      >
        <template slot="left">
          <div class="doc-list-context">
            <div>
              <el-tabs v-model="activeNameLeft" :stretch="true">
                <el-tab-pane label="组件" name="1" />
              </el-tabs>
            </div>
            <template v-if="activeNameLeft == 1">
              <div class="seal-list-title">已添加文本域
                <div v-if="fieldParam != null && fieldParam.length != 0" class="list">
                  <div v-for="(item,index) in fieldParam" :key="index">
                    <el-link class="el-icon-s-check" @click="viewToPageNum(item.pageNum)">{{ item.fname }} </el-link>
                  </div>
                </div>
              </div>

              <div class="seal-list-title">已添加签署域
                <div class="list">
                  <div v-for="(item,index) in locations" :key="index">
                    <el-link class="el-icon-s-check" @click="viewToPageNum(item.pageNum)">{{ item.name }} </el-link>
                  </div>
                </div>
              </div>

            </template>
          </div>
        </template>
      </view-pdf>
    </el-col>
  </el-row>
</template>

<script>

import { getBase64, getFileType } from '@/utils/file'
import { base642blob } from '@/utils/base64/base64'

import viewPdf from '@/views/components/ViewPdfComponent'

// 2.7.542版本
const PDFJS = window['pdfjs-dist/build/pdf']
PDFJS.GlobalWorkerOptions.workerSrc = 'pdfjs-dist/build/pdf.worker'

// const PDFJS = require('pdfjs-dist')
// PDFJS.GlobalWorkerOptions.workerSrc = './pdf.worker.js'

export default {
  // name: "signFieldConfig",
  components: { viewPdf },
  data() {
    return {
      showLeft: true,
      showRight: false,
      isDrag: false,
      fileList: [],
      pdfUrl: '',
      pageNum: 1,
      activeNameLeft: '1',
      fieldParam: [{
        fx: 166, // 单位磅
        fy: 524, // 单位磅
        width: 100, // 单位磅
        height: 20, // 单位磅
        pageNum: 1,
        fname: '测试文本域'
      }], // 拖拽添加的文本域列表
      locations: [{
        pageNum: 1,
        x: 380, // 单位磅
        y: 443, // 单位磅
        width: 120, // 单位磅
        height: 120, // 单位磅
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
      }]
    }
  },
  // 监听一个值的变化,然后执行相对应的函数。
  watch: {
  },
  mounted() {
    this.$nextTick(function() {
      this.firstRender()
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
    },
    // 删除文件
    handleFileRemove(file, fileList) {
      // this.$set(this, 'fileBase64', '')
    },
    firstRender() {
      // 预览组件渲染
      this.$refs.viewComponent.renderField(this.fieldParam, this.isDrag)
      this.$refs.viewComponent.renderStampField(this.locations, this.isDrag)
    },
    viewToPageNum(pageNum) {
      this.$refs.viewComponent.viewToPageNum(pageNum)
    },
    renderChangeField(fieldParam) {
      this.fieldParam = fieldParam
    },
    renderChangeStamp(locations) {
      this.locations = locations
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
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
