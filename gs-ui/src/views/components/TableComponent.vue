<template>
  <!--  调用示例：默认展示选择栏，不展示操作栏 <table-component :table-header="tableHeader" :table-data="tableData" />-->
  <el-table
    ref="table"
    v-loading="tableLoading"
    :data="tableData"
    border
    style="width: 100%"
    height="100%"
    :header-cell-style="{
      'background-color': '#F0F0F0',
      color: '#333',
      'font-weight': 'normal'
    }"
    @selection-change="selectionChange"
  >
    <!-- 第一列：展示选择按钮 -->
    <el-table-column v-if="showSelection" type="selection" width="50" align="center" />
    <!-- 遍历展示 -->
    <template v-for="(item, index) in tableHeader">
      <el-table-column
        v-if="item.visible"
        :key="index"
        :prop="item.englishName"
        show-overflow-tooltip
        align="center"
      >
        <template slot="header" slot-scope>
          <el-tooltip
            effect="dark"
            :content="item.chineseName"
            placement="top"
            class="table-header"
          >
            <span>{{ item.chineseName }}</span>
          </el-tooltip>
        </template>
        <template slot-scope="scope">
          <span v-if="item.englishName=='sex'" :class="{ redColor: scope.row.sex }">
            {{
              scope.row.sex ? "男" : "女"
            }}
          </span>
          <span v-else>{{ scope.row[item.englishName] }}</span>
        </template>
      </el-table-column>
    </template>
    <!-- 最后一列，展示操作栏 -->
    <el-table-column v-if="showAction" label="操作" fixed="right">
      <template slot-scope="scope">
        <el-button
          v-for="(item, index) in tableAction"
          :key="index"
          type="text"
          size="mini"
          @click="handleEvent(item.functionName, scope.row)"
        >{{ item.name }}</el-button>
      </template>
    </el-table-column>
  </el-table>

</template>

<script>
export default {
  name: 'DialogComponent',
  props: {
    tableHeader: {
      type: Array,
      required: true
    },
    tableData: {
      type: Array,
      required: true
    },
    tableAction: {
      type: Array,
      required: false
    },
    showSelection: {
      type: Boolean,
      required: false,
      default: true
    },
    showAction: {
      type: Boolean,
      required: false,
      default: false
    },
    selectionChange: Function
  },
  data() {
    return {
      tableLoading: false
    }
  },
  methods: {
    handleEvent(functionName, row) {
      this.$emit(functionName, row)
    }
  }
}
</script>

<style scoped>

</style>
