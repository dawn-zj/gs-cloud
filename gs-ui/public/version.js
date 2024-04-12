window.gs = {
  version: 1.0,
  urlConfig: [
    {
      describe: 'SM3摘要',
      url: 'https://101.43.242.145:8443/sm/index.html'
    },
    {
      describe: 'demo 框架示例',
      url: 'https://101.43.242.145:8443/demo/index.html'
    },
    {
      describe: '接口文档',
      url: 'https://101.43.242.145:8443/doc/debug-all.html'
    },
    {
      describe: '网络工具：csr解析验证',
      url: 'http://web.chacuo.net/netcsrdecoder'
    },
    {
      describe: '加密解密工具：rsa公钥加密解密',
      url: 'http://tool.chacuo.net/cryptrsapubkey'
    },
    {
      describe: 'form-create表单设计器',
      url: 'https://form-create.com/designer/'
    },
    {
      describe: 'jnpf 快速代码开发平台',
      url: 'https://java.jnpfsoft.com/'
    },
    {
      describe: 'vue-code-view',
      url: 'https://andurils.github.io/vue-code-view/#/home'
    },

  ],
  tabPaneConfig: [],
  tabPaneArr: [
    {
      tabLabel: 'Base64',
      row: [
        {
          col: [
            { span: 24, cardTitle: '文字编码', componentName: 'textBase64' }
          ]
        },
        {
          col: [
            { span: 24, cardTitle: '文件编码', componentName: 'fileBase64' }
          ]
        }
      ]
    },
    {
      tabLabel: '制作二维码',
      row: [
        {
          col: [
            { span: 24, cardTitle: '制作二维码', componentName: 'barcodeIndex' }
          ]
        }
      ]
    },
    {
      tabLabel: '像素尺寸转换',
      row: [
        {
          col: [
            { span: 24, cardTitle: '像素尺寸转换', componentName: 'px2cmIndex' }
          ]
        }
      ]
    },
    {
      tabLabel: '水印(外链项目)',
      row: [
        {
          col: [
            { span: 12, cardTitle: '外链项目', componentName: 'linkProject', showButton: true, buttonText: '修改', buttonFunction: 'handleEditLink' },
            { span: 12, cardTitle: '水印', componentName: 'watermark' }
          ]
        }
      ]
    },
    {
      tabLabel: '加解密',
      row: [
        {
          col: [
            { span: 12, cardTitle: '摘要', componentName: 'CryptoSm3' },
            { span: 12, cardTitle: '凯撒密码', componentName: 'CryptoCaesar' }

          ]
        }
      ]
    },
    {
      tabLabel: 'PDF预览',
      row: [{
        col: [{ span: 24, cardTitle: 'PDF预览', componentName: 'pdfView' }]
      }]
    },
    {
      tabLabel: 'PDF域预览',
      row: [{
        col: [{ span: 24, cardTitle: 'PDF域预览', componentName: 'pdfViewField' }]
      }]
    },
    {
      tabLabel: 'PDF签名',
      row: [
        {
          col: [
            { span: 24, cardTitle: 'PDF签名', componentName: 'pdfSign' }
          ]
        }
      ]
    },
    {
      tabLabel: 'PDF验签',
      row: [
        {
          col: [
            { span: 24, cardTitle: 'PDF验签', componentName: 'pdfVerify' }
          ]
        }
      ]
    },
    {
      tabLabel: '获取PDF签章数据',
      row: [
        {
          col: [
            { span: 24, cardTitle: '获取PDF签章数据', componentName: 'pdfGetStamp' }
          ]
        }
      ]
    },
    {
      tabLabel: '拖拽组件',
      row: [
        {
          col: [
            { span: 24, cardTitle: '拖拽组件', componentName: 'drag' }
          ]
        }
      ]
    },
    {
      tabLabel: '动态表格',
      row: [
        {
          col: [
            { span: 24, cardTitle: '动态表格', componentName: 'dynamicTable' }
          ]
        }
      ]
    }
  ]
}
