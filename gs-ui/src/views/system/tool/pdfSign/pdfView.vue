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
      />
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
      showLeft: false,
      showRight: false,
      isDrag: false,
      fileList: [],
      pdfUrl: '',
      pageNum: 1,
      fieldParam: [],
      locations: []
    }
  },
  // 监听一个值的变化,然后执行相对应的函数。
  watch: {
  },
  mounted() {
    this.$nextTick(function() {
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
    viewToPageNum(pageNum) {
      this.$refs.viewComponent.viewToPageNum(pageNum)
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
