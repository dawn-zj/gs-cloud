<template>
  <div class="app-container">
    <el-form ref="stampForm" class="form" :model="stampForm" :rules="stampRules" label-width="140px">
      <el-row>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="图章样式">
            <el-select v-model="stampForm.stampStyle" style="width: 100%" @change="getValue">
              <el-option
                v-for="item in stampStyleList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="图章字体">
            <el-select v-model="stampForm.fontType" style="width: 100%">
              <el-option
                v-for="item in fontList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="图章宽度" prop="width">
            <el-input v-model="stampForm.width" />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="图章高度" prop="height">
            <el-input v-model="stampForm.height" :disabled="disabledHeight" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="单位名称(预览)" prop="company">
            <el-input v-model="stampForm.company" :disabled="disabledCompany" />
          </el-form-item>
          <el-form-item label="单位名称字体大小" prop="companyFontSize">
            <el-input-number v-model="stampForm.companyFontSize" :disabled="disabledCompany" style="width: 100%" />
          </el-form-item>
          <el-form-item label="颜色" prop="color">
            <el-color-picker
              v-model="stampForm.color"
              color-format="hex"
              :show-alpha="false"
              :predefine="predefineColors"
            />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="图章名称(预览)" prop="name">
            <el-input v-model="stampForm.name" />
          </el-form-item>
          <el-form-item label="图章名称字体大小" prop="nameFontSize">
            <el-input-number v-model="stampForm.nameFontSize" style="width: 100%" />
          </el-form-item>
          <el-form-item label="图章名称底部距离" prop="nameMarginBottom">
            <el-input-number v-model="stampForm.nameMarginBottom" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row v-if="showMore">
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="图章副名(预览)" prop="label">
            <el-input v-model="stampForm.label" />
          </el-form-item>
          <el-form-item label="图章副名字体大小" prop="labelFontSize">
            <el-input-number v-model="stampForm.labelFontSize" style="width: 100%" />
          </el-form-item>
          <el-form-item label="图章副名底部距离" prop="labelMarginBottom">
            <el-input-number v-model="stampForm.labelMarginBottom" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="24" :md="12" :lg="12">
          <el-form-item label="图章编码(预览)" prop="number">
            <el-input v-model="stampForm.number" :disabled="disabledNumber" />
          </el-form-item>
          <el-form-item label="图章编码字体大小" prop="numberFontSize">
            <el-input-number v-model="stampForm.numberFontSize" :disabled="disabledNumber" style="width: 100%" />
          </el-form-item>
          <el-form-item label="图章编码底部距离" prop="numberMarginBottom">
            <el-input-number v-model="stampForm.numberMarginBottom" :disabled="disabledNumber" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :offset="22" :span="2">
          <el-button v-if="!showMore" type="text" icon="el-icon-arrow-down" @click="()=>{ return this.showMore = !showMore}">展开</el-button>
          <el-button v-else type="text" icon="el-icon-arrow-up" @click="()=>{ return this.showMore = !showMore}">收起</el-button>
        </el-col>
      </el-row>
      <el-form-item label="实时预览">
        <template slot="label" slot-scope="{}">
          <slot-label-component label="实时预览" tip="透明背景" />
        </template>
        <el-image v-loading="loading" :src="url" fit="fill" />
      </el-form-item>
      <div class="form-footer">
        <el-button type="primary" @click="downloadFile">下载</el-button>
      </div>
    </el-form>
  </div>

</template>

<script>
import { viewStamp } from '@/api/system/genStamp'
import { validateByteSize, validateNumber } from '@/utils/validate'
import SlotLabelComponent from '@/views/components/SlotLabelComponent'
export default {
  name: 'Index',
  components: { SlotLabelComponent },
  data() {
    return {
      loading: true,
      showMore: false,
      disabledHeight: true,
      disabledCompany: false,
      disabledNumber: false,
      openDialog: false,
      url: '',
      fileBase64: '',
      stampPath: '',
      zipFile: '',
      title: '图章下载',
      predefineColors: [
        '#FF0000',
        '#000000',
        '#1e90ff'
      ],
      stampForm: {
        color: '#FF0000',
        width: '150',
        height: '150',
        fontType: '',
        name: '测试专用章',
        nameFontSize: '12',
        nameMarginBottom: '28',
        label: '(1)',
        labelFontSize: '10',
        labelMarginBottom: '15',
        company: '电子签章系统测试',
        companyFontSize: '20',
        number: '1234567890123',
        numberFontSize: '13',
        numberMarginBottom: 0,
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
        { label: '圆形', value: 1 },
        { label: '椭圆形', value: 2 },
        { label: '方形', value: 3 },
        { label: '长方形', value: 4 }
      ],
      fontList: [
        { label: '宋体', value: '宋体' },
        { label: '楷体', value: '楷体' },
        { label: '隶书', value: '隶书' },
        { label: '新宋体', value: '新宋体' },
        { label: '幼圆', value: '幼圆' }
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
            this.url = 'data:image/png;base64,' + res.data.result
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    // 下拉框的选择事件
    getValue(val) {
      if (val === 1) { // 圆
        this.disabledHeight = true
        this.stampForm.width = '150'
        this.stampForm.height = '150'

        this.stampForm.name = '测试专用章'
        this.stampForm.nameFontSize = '12'
        this.stampForm.nameMarginBottom = '28'

        this.stampForm.labelMarginBottom = '15'

        this.disabledCompany = false
        this.stampForm.company = '电子签章系统测试'
        this.stampForm.companyFontSize = '20'
      }
      if (val === 2) { // 椭圆
        this.disabledHeight = false
        this.stampForm.width = '150'
        this.stampForm.height = '100'

        this.stampForm.name = '测试专用章'
        this.stampForm.nameFontSize = '12'
        this.stampForm.nameMarginBottom = '10'

        this.stampForm.labelMarginBottom = '0'

        this.disabledCompany = false
        this.stampForm.company = '电子签章系统测试'
        this.stampForm.companyFontSize = '16'
      }
      if (val === 3) { // 正方形
        this.disabledHeight = true
        this.stampForm.width = '150'
        this.stampForm.height = '150'

        this.stampForm.name = '个人专用章'
        this.stampForm.nameFontSize = '45'
        this.stampForm.nameMarginBottom = '15'

        this.stampForm.labelFontSize = '15'
        this.stampForm.labelMarginBottom = '15'

        this.stampForm.numberFontSize = '13'
        this.stampForm.numberMarginBottom = '0'

        this.disabledCompany = true
      }
      if (val === 4) { // 长方形
        this.disabledHeight = false
        this.stampForm.width = '180'
        this.stampForm.height = '60'

        this.stampForm.name = '测试专用章'
        this.stampForm.nameFontSize = '25'
        this.stampForm.nameMarginBottom = '30'

        this.stampForm.labelFontSize = '15'
        this.stampForm.labelMarginBottom = '15'

        this.stampForm.numberFontSize = '13'
        this.stampForm.numberMarginBottom = '0'

        this.disabledCompany = true
      }
    },
    // // 表单重置
    reset() {
      this.stampForm = {
        width: '150',
        height: '150',
        fontType: '',
        name: '测试专用章',
        nameFontSize: '12',
        nameMarginBottom: '20',
        label: '(1)',
        labelFontSize: '10',
        labelMarginBottom: '10',
        company: '电子签章系统测试',
        companyFontSize: '20',
        number: '1234567890123',
        numberFontSize: '13',
        numberMarginBottom: 0,
        stampStyle: ''
      }
      this.fileBase64 = ''
      this.init()
      this.$set(this, 'fileList', [])
      this.resetForm('stampForm')
    },
    downloadFile() {
      this.$refs['stampForm'].validate((valid) => {
        if (valid) {
          viewStamp(this.stampForm).then(res => {
            this.loading = false
            this.downloadBase64(res.data.result, 'seal.png')
          }).catch(() => {
            this.loading = false
          })
        }
      })
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
  /*在使用min-width时，小的在前面，大的在后面；同理，如果使用max-width时，就是大的在前面，小的在后面*/
  @media screen and (max-width:1200px){
    .form{
      width: 70%;
      margin: 20px auto;
    }
  }
  @media screen{
    .form{
      width: 90%;
      margin: 20px auto;
    }
  }
</style>
