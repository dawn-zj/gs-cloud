<template>
  <div class="app-container">
    <el-table :data="tabPaneArr" :default-sort="{prop: 'createTime', order: 'descending'}">
      <el-table-column label="编号" align="center" prop="index" sortable />
      <el-table-column label="tab名称" align="left" prop="tabLabel" />
      <el-table-column label="是否上架" align="left" prop="visiable">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.visiable == 1" type="success" size="mini">已上架</el-tag>
          <el-tag v-else size="mini">未上架</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160" sortable>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="修改时间" align="center" prop="updateTime" width="160" sortable>
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="320">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEditForm(scope)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope)">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope)">预览</el-button>
          <el-button size="mini" type="text" icon="el-icon-upload2" :disabled="scope.row.visiable === 1" @click="handleShow(scope)">上架</el-button>
          <el-button size="mini" type="text" icon="el-icon-download" :disabled="scope.row.visiable === 0" @click="handleUnShow(scope)">下架</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="list" />

    <dialog-component v-if="showEdit" title="修改" :open.sync="showEdit" width="1200px">
      <form-designer ref="myDesigner" @save="handleSave" />
    </dialog-component>
    <dialog-component v-if="showView" title="预览" :open.sync="showView">
      <form-view :data="currentData" />
    </dialog-component>
  </div>
</template>

<script>
import FormView from '@/views/tool/buildForm/formView'
import DialogComponent from '@/views/components/DialogComponent'
import FormDesigner from '@/views/tool/buildForm/formDesigner'
import { list, update, del } from '@/api/tool/tool'
export default {
  components: { DialogComponent, FormView, FormDesigner },
  data() {
    return {
      showView: false,
      showEdit: false,
      activeName: '0',
      showButton: true,
      buttonText: '修改',
      // 总条数
      total: 0,
      queryParams: {
        pageNum: 1
      },
      tabPaneArr: [],
      currentIndex: 0,
      currentData: {}
    }
  },
  watch: {
  },
  created() {
    this.list()
  },
  methods: {
    list() {
      list().then(res => {
        this.tabPaneArr = res.data
        if (res.data !== undefined) {
          this.total = res.data.length
          for (var i = 0; i < this.total; i++) {
            this.tabPaneArr[i].index = i
          }
        }
      })
    },
    handleEditForm(scope) {
      this.showEdit = true
      this.currentData = scope.row
      this.currentIndex = scope.row.index
      this.$nextTick(() => {
        this.$refs.myDesigner.setRule(scope.row.rule)
        this.$refs.myDesigner.setOption(scope.row.options)
      })
    },
    handleSave(data) {
      data.tabLabel = this.currentData.tabLabel
      data.visiable = 0
      data.formData = {}
      const timestamp = new Date().getTime()
      data.updateTime = timestamp
      update(this.currentIndex, data).then(res => {
        this.msgSuccess('发布成功')
        this.showEdit = false
        this.list()
      })
    },
    handleView(scope) {
      this.currentData = scope.row
      this.showView = true
    },
    handleShow(scope) {
      const data = scope.row
      data.visiable = 1
      const timestamp = new Date().getTime()
      data.updateTime = timestamp
      update(scope.row.index, data).then(res => {
        this.msgSuccess('上架成功')
        this.list()
      })
    },
    handleUnShow(scope) {
      const data = scope.row
      data.visiable = 0
      const timestamp = new Date().getTime()
      data.updateTime = timestamp
      update(scope.row.index, data).then(res => {
        this.msgSuccess('下架成功')
        this.list()
      })
    },
    handleDelete(scope) {
      del(scope.row.index).then(res => {
        this.msgSuccess('删除成功')
        this.list()
      })
    }
  }
}
</script>

<style scoped>

</style>
