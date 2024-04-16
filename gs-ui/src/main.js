import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
Element.Dialog.props.closeOnClickModal.default = false
Element.Dialog.props.closeOnPressEscape.default = false
import './assets/styles/element-variables.scss'

import draggable from 'vuedraggable'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import store from './store'
import router from './router'
import permission from './directive/permission'

import './assets/icons' // icon
import './permission' // permission control
import { getDictValue } from "@/common/dicts";
import { getConfigKey } from "@/api/system/config";
import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree } from "@/utils/ruoyi";
import { download, downloadBase64 } from "@/utils/file";
import Pagination from "@/components/Pagination";
//自定义表格工具扩展
import RightToolbar from "@/components/RightToolbar"

// 表单设计器
import formCreate from '@form-create/element-ui'
import FcDesigner from '@form-create/designer'
Vue.use(formCreate)
Vue.use(FcDesigner)
// 全局方法挂载
Vue.prototype.getDictValue = getDictValue
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.downloadBase64 = downloadBase64
Vue.prototype.handleTree = handleTree
Vue.prototype.publicPath = process.env.NODE_ENV === 'production' ? process.env.VUE_APP_BASE_API : ''

Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: "success" });
}

Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: "error" });
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg);
}

// 全局组件挂载，自定义组件名，用Vue.component()引入
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('draggable', draggable)

Vue.use(permission)

// 使用原始组件名，可用Vue.use()引入
import JsonViewer from 'vue-json-viewer'
Vue.use(JsonViewer)
/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false

// 正式环境清除所有console.log()
if (process.env.NODE_ENV === 'production') {
  if (window) {
    window.console.log = function () {};
  }
}

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
