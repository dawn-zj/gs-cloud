<template>
  <div class="app-container">
    <el-form ref="stampForm" class="form-NTP" :model="stampForm" :rules="stampRules" label-width="140px">
      <el-form-item label="图章样式">
        <el-select v-model="stampForm.stampStyle" @change="getValue">
          <el-option
            v-for="item in stampStyleList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="图章宽度" prop="width">
        <el-input v-model="stampForm.width" />
      </el-form-item>
      <el-form-item label="图章高度" prop="height">
        <el-input v-model="stampForm.height" :disabled="disabledHeight" />
      </el-form-item>
      <el-form-item label="图章字体">
        <el-select v-model="stampForm.fontType">
          <el-option
            v-for="item in fontList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="图章名称(预览)" prop="name">
        <el-input v-model="stampForm.name" />
      </el-form-item>
      <el-form-item label="图章名称字体大小" prop="nameFontSize">
        <el-input v-model="stampForm.nameFontSize" />
      </el-form-item>
      <el-form-item label="正文名称(预览)" prop="company">
        <el-input v-model="stampForm.company" :disabled="disabledCompany" />
      </el-form-item>
      <el-form-item label="正文名称字体大小" prop="companyFontSize">
        <el-input v-model="stampForm.companyFontSize" :disabled="disabledCompany" />
      </el-form-item>
      <el-form-item label="图章编码(预览)" prop="number">
        <el-input v-model="stampForm.number" :disabled="disabledCompany" />
      </el-form-item>
      <el-form-item label="图章编码字体大小" prop="numberFontSize">
        <el-input v-model="stampForm.numberFontSize" :disabled="disabledCompany" />
      </el-form-item>
      <!--      <el-form-item label="图章文字文件">-->
      <!--        <el-upload-->
      <!--          class="upload-demo"-->
      <!--          action="#"-->
      <!--          accept=".txt"-->
      <!--          :before-remove="beforeRemove"-->
      <!--          multiple-->
      <!--          :limit="1"-->
      <!--          :http-request="uploadFile"-->
      <!--          :on-exceed="handleExceed"-->
      <!--          :file-list="fileList"-->
      <!--        >-->
      <!--          <el-button size="small" type="primary" icon="el-icon-upload">上传文字文件-->
      <!--          </el-button>-->
      <!--          <div slot="tip" class="el-upload__tip">{{ textFiletip }}</div>-->
      <!--        </el-upload>-->
      <!--      </el-form-item>-->
      <el-form-item label="实时预览">
        <el-image v-loading="loading" :src="url" fit="fill" />
      </el-form-item>
      <div class="form-footer">
        <el-button type="primary" @click="">制作图章</el-button>
      </div>
    </el-form>
  </div>

</template>

<script>
import { viewStamp } from '@/api/system/genStamp'
import { validateByteSize, validateNumber } from '@/utils/validate'
export default {
  name: 'Index',
  data() {
    return {
      loading: true,
      disabledHeight: true,
      disabledCompany: false,
      openDialog: false,
      url: '',
      fileBase64: '',
      stampPath: '',
      zipFile: '',
      title: '图章下载',
      stampForm: {
        width: '150',
        height: '100',
        fontType: '',
        name: '测试专用章',
        nameFontSize: '12',
        company: '电子签章系统测试',
        companyFontSize: '20',
        number: '1234567890123',
        numberFontSize: '13',
        stampStyle: ''
      },
      stampRules: {
        width: [
          { required: true, message: '请输入宽度', trigger: 'change' },
          { validator: validateNumber, min: 10, max: 999 }
        ],
        height: [
          { required: true, message: '请输入高度', trigger: 'change' },
          { validator: validateNumber, min: 10, max: 999 }
        ],
        name: [
          { required: true, message: '请输入图章名称', trigger: 'change' },
          { validator: validateByteSize, min: 1, max: 50 }
        ],
        nameFontSize: [
          { required: true, message: '请输入图章名称字体大小', trigger: 'change' },
          { validator: validateNumber, min: 1, max: 100 }
        ],
        company: [
          { required: true, message: '请输入正文名称', trigger: 'change' },
          { validator: validateByteSize, min: 1, max: 50 }
        ],
        companyFontSize: [
          { required: true, message: '请输入正文名称字体大小', trigger: 'change' },
          { validator: validateNumber, min: 1, max: 100 }
        ]
      },
      stampStyleList: [
        {
          label: '圆形',
          value: 1
        },
        {
          label: '椭圆形',
          value: 2
        },
        {
          label: '方形',
          value: 3
        },
        {
          label: '长方形',
          value: 4
        }
      ],
      fontList: [
        {
          label: '宋体',
          value: '宋体'
        },
        {
          label: '楷体',
          value: '楷体'
        },
        {
          label: '隶书',
          value: '隶书'
        },
        {
          label: '新宋体',
          value: '新宋体'
        },
        {
          label: '幼圆',
          value: '幼圆'
        }
      ]
    }
  },
  watch: {
    // 监听整个表单的任何一个值变化,都重新制图
    stampForm: {
      handler(val, oldVal) {
        this.viewStamp()
      },
      deep: true
    }
  },
  created() {
    this.init()
    this.viewStamp()
  },
  methods: {
    // 页面加载的初始化赋值
    init() {
      this.stampForm.stampStyle = this.stampStyleList[0].value
      this.stampForm.fontType = this.fontList[0].value
      this.textFiletip = '(txt文件格式：图章名称和正文用英文逗号分隔,每一行为一个图章,每次最多制作500个)'
    },
    // 图章预览
    viewStamp() {
      if (!this.$refs.stampForm) {
        return
      }
      this.$refs['stampForm'].validate((valid) => {
        if (valid) {
          viewStamp(this.stampForm).then(res => {
            this.loading = false
            this.url = 'data:image/png;base64,' + res.data
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    // 制作图章
    // genStamp() {
    //   this.$refs['stampForm'].validate((valid) => {
    //     if (valid) {
    //       if (this.fileBase64 == '') {
    //         this.msgError('请上传图章文字文件')
    //         return
    //       }
    //       genStamp({ StampVO: this.stampForm, fileBase64: this.fileBase64 }).then(res => {
    //         this.stampPath = res.msg
    //         this.downloadStamp()
    //         this.reset()
    //       })
    //     }
    //   })
    // },
    // // 下载zip文件
    // downloadStamp() {
    //   if (this.stampPath != '') {
    //     this.download(this.stampPath)
    //   }
    // },
    // 下拉框的选择事件
    getValue(val) {
      if (val == 1 || val == 3) {
        this.disabled = true
      } else {
        this.disabled = false
      }
      if (val == 3 || val == 4) {
        this.disabledCompany = true
        this.textFiletip = '(txt文件格式：每个图章名称为一行,每次最多制作500个)'
      } else {
        this.disabledCompany = false
        this.textFiletip = '(txt文件格式：图章名称和正文用英文逗号分隔,每一行为一个图章,每次最多制作500个)'
      }
      if (val == 1) {
        this.stampForm.width = '150'
        this.stampForm.companyFontSize = '20'
        this.stampForm.height = '100'
        this.stampForm.nameFontSize = '12'
        this.stampForm.companyFontSize = '20'
        this.stampForm.name = '测试专用章'
        this.stampForm.company = '电子签章系统测试'
        this.disabledHeight = true
      }
      if (val == 2) {
        this.stampForm.width = '150'
        this.stampForm.companyFontSize = '16'
        this.stampForm.height = '100'
        this.stampForm.nameFontSize = '12'
        this.stampForm.name = '测试专用章'
        this.stampForm.company = '电子签章系统测试'
        this.disabledHeight = false
      }
      if (val == 3) {
        this.stampForm.width = '150'
        this.stampForm.height = '100'
        this.stampForm.nameFontSize = '45'
        this.stampForm.companyFontSize = '20'
        this.stampForm.name = '测试专用章'
        this.stampForm.company = '电子签章系统测试'
        this.disabledHeight = true
      }
      if (val == 4) {
        this.stampForm.width = '150'
        this.stampForm.height = '50'
        this.stampForm.nameFontSize = '25'
        this.stampForm.companyFontSize = '20'
        this.stampForm.name = '测试专用章'
        this.stampForm.company = '电子签章系统测试'
        this.disabledHeight = false
      }
    },
    // // 表单重置
    reset() {
      this.stampForm = {
        name: '测试专用章',
        width: '150',
        height: '100',
        fontType: '',
        nameFontSize: '12',
        companyFontSize: '20',
        company: '电子签章系统测试',
        stampStyle: ''
      }
      this.fileBase64 = ''
      this.init()
      this.$set(this, 'fileList', [])
      this.resetForm('stampForm')
    }
    // handleExceed(files) {
    //   this.msgError('当前限制选择 1 个文件')
    // },
    // beforeRemove(file) {
    //   this.$confirm(`确定移除 ${file.name}？`).then(() => {
    //     this.fileBase64 = ''
    //     return true
    //   }).catch(() => {
    //     return false
    //   })
    // },
    // uploadFile(param) {
    //   const type = getFileType(param.file.name)
    //   if (type != '.txt') {
    //     this.$set(this, 'fileList', [])
    //     this.msgError('上传文件类型不匹配')
    //     return
    //   }
    //   getBase64(param.file).then(res => {
    //     this.fileBase64 = res
    //   })
    // }
  }
}
</script>

<style scoped>
  .form-footer{
    text-align: center;
    margin: 20px 0;
  }

</style>
