import uniqueId from '@form-create/utils/lib/unique';

const label = '文字链接';
// elementUI的标签
const name = 'el-link';

export default {
  icon: 'el-icon-link',
  label,
  name,
  rule() {
    return {
      type: name,
      title: label,
      native: false,
      children: ['这是一段文字'],
      props: {
        href: 'https://www.baidu.com/',
        target: '_blank'
      }
    };
  },
  props() {
    return [
      {
        type: 'input',
        field: 'formCreateTitle',
        title: 'title',
      },
      {
        type: 'input',
        field: 'formCreateChild',
        title: '内容',
        props: {
          type: 'textarea'
        }
      },
      {
        type: 'input',
        field: 'href',
        title: '链接',
        props: {
          type: 'textarea'
        }
      },
      , {
        type: 'select',
        field: 'target',
        title: '打开方式',
        options: [{label: '在新窗口打开', value: '_blank'}, {label: '在当前窗口中打开', value: '_self'}]
      },
    ];
  }
};
