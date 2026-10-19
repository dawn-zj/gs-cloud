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
        target: '_blank',
        type: '',
        disabled: false,
        underline: false
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
      {
        type: 'select',
        field: 'type',
        title: '链接类型',
        options: [
          { label: '默认链接', value: '' },
          { label: '主要链接', value: 'primary' },
          { label: '成功链接', value: 'success' },
          { label: '警告链接', value: 'warning' },
          { label: '危险链接', value: 'danger' },
          { label: '信息链接', value: 'info' }
        ]
      },
      {
        type: 'select',
        field: 'target',
        title: '打开方式',
        options: [
          {label: '在新窗口打开', value: '_blank'},
          {label: '在当前窗口中打开', value: '_self'}
        ]
      },
      { type: 'switch', field: 'disabled', title: '是否禁用状态' },
      { type: 'switch', field: 'underline', title: '是否有下划线' },
      {
        type: 'input',
        field: 'icon',
        title: '图标类名'
      }
    ];
  }
};
