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
        <template slot="right">
          <div class="doc-list-context">
            <div>
              <el-tabs v-model="activeNameRight" :stretch="true">
                <el-tab-pane label="签章方式" name="1" />
              </el-tabs>
            </div>
            <template v-if="activeNameRight == 1">
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
                  >设为骑缝章
                  </el-button>
                </div>
              </div>

              <div v-show="form.stampType === 1" class="doc-list-title">指定签署区

                <div v-if="form.stampType === 1" class="add-widget-module-list">
                  <div id="sealdivid1" draggable="true" class="added-widget" style="width:100%" @click="add">
                    <div class="added-widget-type-name">点击添加签署区
                    </div>
                  </div>
                </div>
                <div
                  v-show="form.stampType === 1 && form.stampParam.locations.length > 0"
                  class="add-widget-module-list"
                >已添加签署区
                  <div class="list">
                    <div v-for="(item,index) in form.stampParam.locations" :key="index">
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
      showRight: true,
      isDrag: true,
      activeNameLeft: '1',
      activeNameRight: '1',
      fileList: [],
      pdfUrl: '',
      pageNum: 1, //
      page_count: 0, // 总页数
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
          base64: 'iVBORw0KGgoAAAANSUhEUgAAAKAAAACgCAYAAACLz2ctAAAR+UlEQVR42u2dK7AlNRPHl1cVAkEVCARVIBAIBAKBQCAQCMQKBGIFAoFAIK5AUIVAIJAIBAKBWIFAIBAIxAoEAoFAIBAIBAKBQCBC/Zhv6p6vbyfpPGaSzER01e4958w5k/mnH/9+5JZz7taUC/nnH+d++825H35w7ttvnfviC+c+/9y5jz5y7oMPbgp/53Xkm2+Wz/3xh5traZPz3vzffzv3/ffOffaZc+++69wrrzj39NPOPfjgsiw15IknnHvtteX6n37q3I8/uv++dwLvhAD880/nvvrqGmwPP1wPaCnC9778snPvvLNo15Nry2ObUjTc++8799JL7QBnkeefd+7qyrnvvjudhjyeWf3yS+def925Rx/NB8Tjjzv37LOLpnrjDefefHPRnJoPyN95/c6dRbPyOT6f+92PPOLc7duLP3kCMB7jRggW3norXcvh8/GwARLAxUfDVNf4TX/9tVwPs//hhwuQAWeKj8km4r64vwnAzgTfiQgUEFk1ywsvOPfee4t2+f1318wXBVCAHtfAqqm5T4BMhD4B2FB++WXRCgDK8tAwkT2bs0tf9bnn4veEBsXc//yzmwDcU1hwIscY8AAd7wN0PNzRHgqBCGB85pk4EPE9MfMTgBsKJgctFvKdeO3VVxd/a0TQhXxbfNSY3/j22879+qubAKwd0X78cVjj4T/h02GWjxwtsgnRiiF/kXXCJyb4mQAslK+/du7JJ8MZBpz4WhHrSGQ6IOP+fWvDumEJJgAzhMiU1FXIvyOllbvL/7vdwP9H4jtJIYb8RFySASLmfn4MaSkfgYt5QePVMC+XoFuvv/W9afekvZ4DRNbFZ5pZN4okJgAjfF5I6xHR1trJMSDsJb57LVnDUKCGNmzFe3YNwHv3/GaE/Civb/WwpSZsAb7a16YUjHXz+YZQPBOA/xMiXC11htn45JN6dEoIdHtqwj2/i/XT2AM0JEFMR1TV/l+KH0exgLZLSf5vSam00H4h4G0JSHhBiiN8JrkTumbfL/zpJz3dxM7Emd5iZ8Yc/1ZByF7fT/5Y8w1xfTrIouzr72lRLhHclrzV5UMOgXFvH3DvjIoWKfO3xpU2+xHLmr+Hw7xHFiNmAvc0w60ib6JgqoG0Cu2GxPX2XwIPpZkA6uP29EP2joJjoGsBRnhDKmm0tYDgPxwAibg08OGX7B2J7e2H+Ux/D1yk77nghx8GgNykttikkFr5HC0DEU3rtTTJPsu0Mwj3uzl8jbt320ZdLTSPpgl7ACCC76f55jua420CDgk+/k+BaGveaaucbAr101sRBM9FI613CkzqUy1yR/F/C/haaIMtfLLQNXyasAcQas9tB4qmLsms8Xwhs9tLZUptzRcywS02nOW70HjScsETkl/uHoDQKVqGIxZw7KkRWkagW5r7mj6v5ruTMdmQLqtzIS23C9XSU4lUKxC0cDlK1lJjL8gddwtAqlo0kjnG87U2S0eUWptYI6sBZncA1IIO0mu+Xg3NH5qg29f0WtabjIlM22GaNwhKyiqZGTUhndZQe2BqMcAEZ12KKSX1SO5YBpUUtVae5pX/YVlGzw6BA7RmI3pOVY0Kvto+JxpPWjj8wYpp1PwGotwUTuq0qAmwenWPOWuqBSUV06l5ZT1SNVPJnLIrfKXxLfO0ZzLFqesqK6vJnFRqFCs3vfyY1Jo+X3lSjLydfqENRNbXU8r7ZbqukilOz/PK3UUDTGlVSixPOqmacrNbumYUKGyQL04rZpTjMqBcanavhYKUCb70ipva/rSkZsBDYZakjHCu0bc7agK/ZzButXbkhWWqrpCgtr0Rh1P6AEws2LpQdJrd/Nz2VuvFBAYZAxSMhrO9EbBtFAVFATjBd6uKj11LIKJlhx3zCTcDIJNJpdqtXbY9MyFjtXxSaCI1b2aPcfxNoFuOSNujm22Cr275f+3uOjnTh3HB1QEIvyd9vz36BSb46gBwy+8iGyJTsRQlVwUgqJaTSffSfhNQfW9ejZajDK8aAHE2pfazFpkeXY40CL1m6y1aMHEOof3iRD5nm8kcKsaY67DMrJYRMcPUqwBQnkDENPq56IvggM/NuAiAk25agoXw14FJ1Xr0oxBSsgGsiaX28QwCHyxpuoS10V/gKKydmlKGk5UDq5kJGl1kU1oCJaNHN7IKdu72a+GAwbVdca6HbjHBj9FFuflHji2VxPOM+q6d7su1OdjJlUUi+4OMgVpcnZJ8ngu8CFMeLtem8zM4mgYjFC4nAxBNJ8PqHoYK9SKSmGezznVZhCMgZMGK4Yjc//8D59Zaze8ZCwieeqqIchg6o2J5rnI8i+FckrAa9Znf3kbP7iFUe2j1dxsP7+kKfLHnKvFzdZUIwDXCi5lfa//GkYTeFw2AZ0pPxkAoLSgl/GYAEuFdEoohGx4q+TkqAOXmXIWWxaNUU5f2amsxRGSSwvV/6HCyord07MNoonGjGZxX91qsxgRZuVEjdIy/1t+X+2195loLkZtTCtzpyOCznqVnecZUyyf0Dvm732P+35kAiTMdAiCpy1HNr7XByfpMJR3D1IwoADUT47PdoR9w1DkvkuXXTnIfkY7ZYp6MjCXAVYAP9PN/OdXKMfU+IhBpObQ45hnl6MPQLanPTm7YAB+o1/ffvu2qh+ijcoRybXxC4/7RSvdzgxFK8419RHoAktN2aQHWiABkM1oAOBIdk2p2U62YbNsMBCJ6AJIT1VkBOJqZ0o6e8NExlaeHNm3hLHGbZEUV1EwQgLL8PqfJ+IgUjKxzi8lIdZOlWi4lbUnO3AtAojdZUj37HXROKyYFIyq6mitTKrTuymt7IuGlpv/yjZic0YBCv4p0I44i8GiMRxntmaD1Lu/DM8DouslmFULoUTUWJlDe+KhCTnXkgldJxXiqhm76ORHmeoiyeRnVjyaY8tHL/Y2ZtZsT7zPGK3Rbv6edX9eT3H//TetDUuAI6y9PW/Jo8+WAugpTjroUnGG0iXYyeG/Cuh8p+JPtC14AyhEcR2xCQhu++GIfQLvvvuNqvUuRbpBnlO9NqqH28MleBLqJdJl2OngL4EFcs9Z7TBvrgcLy4Oo8AFwFSmNvbfjAA+7G6QIjUisTgINpQxlkrFrP0K44AXgG2ZPAHpVQngDcicD29XzUkDNOUDAD8AxRsEW0o6hqiKE39pBijoKPzAOW8Fa15CjE/mY8oATgWRdMlqTVzOmeIejIzoSQoztSLrjm2I1ackSiuVoueI9qmN5L8X1jN2pJ4uDuQ4i5GoYy8q3qAUfpiPON3aglEM+j9AjXqnKXrQyedoXlHzJZv0V6qFcAhsZuxNJpVmF9eyw02AqAWkX05j0hNdr5ehy7cQmiI3KBW/SFJPWEaK2HqV1x1jkiPQIwNnZD5nJJ33FqQCpx3Vu/iOWE9dxnJTd1YFxvfl9wDHCjADA2duMyn8vZaKt1IK0mT4wMCT5RT+M7LBPNcp+f7AsOJDfyJyPEAJc7ZbOXsRvS32MXS0cavy4ll9xbLjgFeCkATJ6MwFwT62yYHMD1GoD4xm489NBNixDSXnI0rU886aguB1WWBCPSqgRiilvJ07Fi3fV7HZq81diNS5NLFsPqD1PQIKeDSuntxCmrhdp8OlYCc50UePQOwMce84OFXZx6Ph4mNuRTEsD03vdR6rdnzQd0mRNSU/3AUcZukEjP5ULZ7Qzj8V2bCLFXs1tjYkL2hNSUGdHWSLdnH1Abu4HpoFK6RrTKdTTusHV51lYDyn1ZJfOM6JQp+VbA9Wx+pb8GWXrvXt3fi/OtVdn0AsAa2RLZ6pA9JV9Dr+WYrhGnn7Iol5sNkCQeNZ/klEu/cKvvqkXDhFyq0PvlpF1DDjxMJxy1OpqI9dJH2aNe73JtR5qqn+LPF5+UlHJW3MgC6NCAAYJ0E8Ef4ntHOeQw1boVnxV3ltMycTUMi7OJUBdHX/LR1rTKaZlaGuVoZphFaT15Ch/0aGX60vwaB93H+bF5YvqUnPSbcVyxriGkGR5p9vGU9qR+QiOW30nvOYc5pS8hqMo8ukx/AUJWZghS86JTziH40zLjg0YsAqBWpu/LDU85t8jgIzFm8L8oR3Zg10eb4JlSGibf27q5aoTMEniQ8ULiCfJhqkDWCPZyPH1K7tnK5lsfeOoZaylFn7UrU7YWqaQwxYkUV9p8DxL2PUz0jFXuWrSZ9UGXgCmnTG2UkjaiXHpkCse6hN9A4CGHOO6dvkoxmSkALNFsW/bLjKIFZTsD2i/jyNr4m2gnlE5mL3ONUwBoeYC1AZgKpNS/t9R+siMwc6qarcxchtmth1iGgBfSgJYelpLizVITPIr/J9suC4YZ2KtHZKK5ZT7VB5wUAKbWu+UUceYCsGcNSHAqI9+Cpns72Sh9wUitf5c+YAgIlsAg1ZTW/lwPAJS9Q+DCcxBhPQCuPQ5yQWqXsLcEYE7/q1Vj5US2PUa/lJJJd6yw17ks7KbkukWlTKrptE5rsPqCpVTKqFEwjWqXvwM8FAak+aXsqzDcsYe5JimaKNZWavEFawJwBA2oDXGv0GKa/iFmpEgfoFWhQuosmtoPPmcAk1WT9QRAfDwZA1AhVcH6pX+Iji45/ZLu91m0elyRUzMqsiD5zTVSHZ/xgJsziMz3ImRBKl0//8PSFBMdHa1yumaBQm89v9ZKZ1mQUsn0lgMQQlKmYyAoczihXhl/q4/X4+8u/V2aq0XUW7mpvuwC8ICSF4KayQnNRwJf79mK0g0N5SaPtOU5b9CiW34RjaBOLcvp1YSNXDCQWg0UKsPbcLhmnQtpgx6txau91ruNDsBU2icUdFT2++oDEJMrxzJYoqWe+bARAJjKKcbex1ES0qXCz9+wFaPexShGlE4rcveuy/ZRcgpLWwCw9SZJidZ9r5HVkOAjqPQcsdUfANegRIbt/F9zXlMfWksNE3vgLQFYw5LwfLTnltBe2QcAffni0giqNzPcI9eXC0Cei0yz7ThCbpsLyzOI1x2lmeMSEIw2GLM2qVxSSraaXe3Epx0LTPZN4VjTOCm+zdEBWMO/swYcDVKq2+cRtZuEotHC+hyNdhYAWjaiFYy+59Ign7/9l/h2GmS1ljEpLYc6qpZLbYb3ZTju3NEtU6N2232+iMBE8zVI22m541SKZnSA1rjf2DXI4cr02uqbNzy7ZL8vg6LRjrLib7lVNEcJTnLm16SsB3SKxtGy9o1HMO/7hZDV2lFWmGhL6i7Wx2sxRa0qb2LRfEpraMrvxt/TrA8Zjo1J5v4AuKbttNzxWlkdK+eyVqSUZCxywWkdPLQFn6eV0fuOkiW328mks3Zf7jvKClI0h4eydMql+o0pQLIed5qzcVLvgYBCI5dXS9NR+0TbH4BfqB1ltQYonFtSMzeaO5vFmg/2gcy6OUqrmdcjILT1pJh0h9TaWABcK6tleb+cwJDSAJNS6FCjSMJaOBHTcCW+KWvIxALNoqwmt5fjwboD4GWjk+/AZ8wJJGls8npKv25tAFr905qpRdYDkxpaNzJPHXcs9vWD2KUhbYi5xr+xAjEHfKlTD0qzFjnC/UPwy54cqfVaH8gzHAAvk+RyDIic1MrOT4nkUgBo4Rhz6JRS+of7hVYJrQ2vsX6D9Gn3++PY5UTKWjR3SaQyvd/aiVejANYCwBwT7SLTyXBBfKZ2NbeAs5fhocMD8HLx19MtfYvPa5gcWP2SnZ8z92WLKPsyg8EhMKF7X+fzFYxImwC0CJNaOYFHY/Wln0hEWArGGlo0Rzh1krM3tIyR3HQUFnSQzTgHAFdhEBJtgzEgSjD2ejolmwS+E9BpjV0a8KgkyhgIPgFYmz8kEPER2ZqPxHw7fEYAyedb/G4CCbQcPh3nFof8OrmZuN9BTe3xACh9JYt51h4qeWnAQA8Eg7Zr5UgJBrgeESnAQWthVmP+nAyyuK+t3IkJwA0iZ4CE4x6KnmNC6RJgoTgC4GDyMeUAVQp/53X8MZL/fE4rfbIKvxsuFOAe7VDrwwPQ51th6lI0z95C3vvqajHNJwDdOQCo+V5oFTQWmirVXNcSvhfNCrVE+rGVLzoB2IG5RkOSK11BiU9YU1OSscGccn1SiPiEJ9NwE4A5phvyG36N4AYtRd6VLIPmA/J3XkcIFvjcybXaBOCUCcApUyYAp0wATpkyATilW/kXKpTJBc0iWzUAAAAASUVORK5CYII='
        }
      }],
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
  },
  mounted() {
    this.$nextTick(function() {
      // this.getFileInfo()
      this.firstRender()
      this.getSealList()
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
    /** 查询印章管理列表 */
    getSealList() {
      this.sealList = [
        {
          id: 1,
          name: '测试圆章',
          photo: {
            dpi: 96,
            width: 161,
            height: 161
          },
          photoDataB64: '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'
        },
        {
          id: 2,
          name: '测试方章',
          photo: {
            dpi: 96,
            width: 190,
            height: 70
          },
          photoDataB64: 'iVBORw0KGgoAAAANSUhEUgAAAL4AAABGCAYAAABytS7pAAADGklEQVR42u2dW26EMAxFZ/+bbvtZVYXYjp/JuWqkjjQDwZwYxzHw+SCEEEII3aGvnz8a7dQG+DTAB3wa4GMcGuC/fBGhgXNWwEeAD/gI8AE/9YRwPBeD77mviH5P2WZlX7o6y/Ye/7/9WWbpq35bjuvv/nZt423flKzHxgCqHBBjwP+9T4shJf3WHtsT+Fb7RIJvOV5pX6oH1rExvgV86W92wNMMygpQssC3OKDq0Gfk5HYFuOR7Hif7yR4RwHT2+FPCmxHgS0/If+GGF/jZl/ATwZdeDStD6HbgS4zk7fE7xORTwE8vH7gxnan9PwN8z+1a4/eMrM7nQAF+EvgVBVaVoQ7gFw0CKfhemZNuYVMF+KQzE9OZ3h7/aRvRJ65iDhIFfuUxA35AOtMzdRnt7QEf8FPA9ypViFrAyghDJk2eR9fqnAK+64kKSFfi8QG/bDUyYkGsy+QW8IX7e1qplWaCPMKqldfvsLDVxeNTnRkw6ZMMACv4O0vuXsVkEfUyUeB3DGmOA19bC+8N/lvaczc08Vh08yjDzgSfWh2jJ9xN3a3CKMnxS0OwiIlsBfjU6iSDb7kKWEoLtFmbiLuWIqs8pYt1hDrF4Es9rtZTe4YbUdBEpAmlZRyA32QBa4Lhq7I6HWEFfHSlAB9tAYHtAf9aL4j9Af9Y6G++QQTwLwRfe2cVlgT8o0KcjiudgI/BAR/wEeADPgJ8wEeAD/hIBS1ZHcAHfMAH/NPhl5QFY3/AvyLeB3rARwjwEQJ8hAAfIcBH6B7wpTeke5cGa9+5u5POtPZf+5iUKPsBfuABrU6o9rMUfOmg3O2Ppf+rJ0hk2Q/wEz2+9sRYPbblSmQZWBEDI8N+gF8Q6uyC+va0NM2q6+6gqwDfw36AXwy+9e3bEvBXnyXwW2Lo1cO3PMGf8PZywHfyaG/v1LX+3jvUeLvqeIEf8ZZ4wE/K6li2s8q6dI+xPcCveKob4AdPbr2zOtnga15iEb1/wG90QB55cu0+LHb0yqPv/j7KfoCPEOAjNAB8Gu2kBvg0wAd8GuBjHNqN4COEEEIInaVvoIwwMM0MjQ8AAAAASUVORK5CYII='
        }
      ]
    },
    // 接收后台返回的流文件，赋值模板地址
    sealChange(sealId) {
      // getSeal(sealId).then(res => {
      const seal = this.sealList.find(item => item.id === sealId)
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
    add() {
      // 选择印章
      if (this.form.sealId == null) {
        this.msgError('请选择印章')
        return
      }

      const width = this.sealInfo.width / this.sealInfo.dpi * 72
      const height = this.sealInfo.height / this.sealInfo.dpi * 72

      var x = 0
      var y = this.$refs.viewComponent.pdfHeight - height
      var pageNum = this.$refs.viewComponent.pageNum
      // 添加坐标和页码，默认左上角，坐标原点是左下角
      const obj = {
        x: x,
        y: y,
        width: width,
        height: height,
        pageNum: pageNum,
        name: this.sealInfo.name,
        sealId: this.form.sealId,
        sealInfo: JSON.parse(JSON.stringify(this.sealInfo))
      }
      // 预览组件渲染
      this.locations.push(obj)
      this.$refs.viewComponent.renderStampField(this.locations, this.isDrag)
      this.msgSuccess('添加成功')
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
    },
    submitForm() {
      const formData = JSON.parse(JSON.stringify(this.form))
      if (formData.stampType === 1) {
        if (this.locations.length <= 0) {
          this.msgError('请进行拖拽配置')
          return
        }

        console.log(this.fieldParam)
        console.log(this.locations)

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
