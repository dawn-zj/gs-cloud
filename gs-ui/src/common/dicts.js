export const dict = {
  sys_user_sex: [
    {"dictCode": "1", "dictValue": "男"},
    {"dictCode": "2", "dictValue": "女"},
    {"dictCode": "3", "dictValue": "未知"}
  ],
  sys_normal_disable: [
    {"dictCode": "1", "dictValue": "正常"},
    {"dictCode": "2", "dictValue": "停用"}
  ]
}


export function getDictValue(dictType, dictCode) {
  let dictValue = ''
    dict[dictType].forEach(item => {
    if (item.dictCode === dictCode){
      dictValue = item.dictValue;
    }
  })
  return dictValue
}
