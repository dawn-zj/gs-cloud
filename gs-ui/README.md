## 开发

```bash
# 克隆项目
git clone https://github.com/dawn-zj/vue-ui.git

# 进入项目目录
cd vue-ui

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 启动服务
npm run dev
```

浏览器访问 http://localhost:80

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```

## 公共组件使用方法
### pdf预览
1. 导入
```javascript
import viewPdf from '@/views/components/ViewPdfComponent'

export default {
  components: { viewPdf }
}
```
2. 使用

```javascript
<view-pdf
        ref="viewComponent"
        :show-left="showLeft"
        :show-right="showRight"
        :is-drag="isDrag"
        @render-change="renderChange"
      >
```

| 参数       | 说明                     | 类型    | 默认值               |
| ---------- | ------------------------ | ------- | -------------------- |
| show-left  | 是否展示左侧tab页        | boolean | false                |
| show-right | 是否展示右侧tab页        | boolean | false                |
| is-drag    | 文本域、签署域是否可拖拽 | boolean | true                 |
| url        | pdf文件本地地址          | string  | 本地资源下的show.pdf |

| 方法名                              | 说明                                                         |
| ----------------------------------- | ------------------------------------------------------------ |
| render-change                       | 渲染pdf后的回调，当文本域或签署域被删除后，会进行渲染回调<br>示例：render-change(fieldParam, locations) |
| renderStampField(locations, isDrag) | 批量渲染签署域，并指定是否可以拖拽移动<br/>示例：this.$refs.viewComponent.renderStampField(locations, this.isDrag) |
| renderField(fieldParam, isDrag)     | 批量渲染文本域，并指定是否可以拖拽移动<br/>示例：this.$refs.viewComponent.renderField(fieldParam, this.isDrag) |