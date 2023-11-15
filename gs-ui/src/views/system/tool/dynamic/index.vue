<template>
  <div>
    <div class="mt10 mb10">示例1：默认状态</div>
    <table-component
      :table-header="tableHeader"
      :table-data="tableData"
      :selection-change="handleSelectionChange"
    />

    <div class="mt10 mb10">示例2：自定义操作栏</div>
    <table-component
      :table-header="tableHeader"
      :table-data="tableData"
      :table-action="tableAction"
      :selection-change="handleSelectionChange"
      :show-action="true"
      @function1="handleDetails"
    />
  </div>

</template>

<script>
import TableComponent from '@/views/components/TableComponent'
export default {
  name: 'Index',
  components: { TableComponent },
  data() {
    return {
      tableHeader: [
        {
          visible: true,
          englishName: 'id',
          chineseName: 'id'
        },
        {
          visible: true,
          englishName: 'date',
          chineseName: '日期'
        },
        {
          visible: true,
          englishName: 'name',
          chineseName: '姓名'
        },
        {
          visible: true,
          englishName: 'sex',
          chineseName: '性别'
        },
        {
          visible: false,
          englishName: 'address',
          chineseName: '地址'
        }
      ],
      tableData: [
        {
          id: 1,
          date: '2016-05-02',
          name: '王小虎',
          sex: 1,
          address: '上海市普陀区金沙江路 1518 弄'
        },
        {
          id: 2,
          date: '2016-05-04',
          name: '王小虎',
          sex: 1,
          address: '上海市普陀区金沙江路 1517 弄'
        },
        {
          id: 3,
          date: '2016-05-01',
          name: '王小虎',
          sex: 1,
          address: '上海市普陀区金沙江路 1519 弄'
        },
        {
          id: 4,
          date: '2016-05-03',
          name: '王小虎',
          sex: 1,
          address: '上海市普陀区金沙江路 1516 弄'
        }
      ],
      tableAction: [
        {
          name: '查看',
          functionName: 'function1'
        },
        {
          name: '查看2',
          functionName: 'function1'
        }
      ]
    }
  },
  methods: {
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id)
      this.msgInfo('已选择：' + this.ids.length + '条')
    },
    handleDetails(data) {
      console.log(data)
      this.msgInfo('查看')
    }
  }

}
</script>

<style scoped>

</style>
