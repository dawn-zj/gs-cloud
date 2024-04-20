<template>
  <div class="container">
    <div class="left-board">
      <drag-src />
    </div>

    <div class="center-board">
      <drag-dest />
    </div>

  </div>
</template>

<script>
import dragSrc from '@/views/system/tool/drag/drag-src'
import dragDest from '@/views/system/tool/drag/drag-desc'

export default {
  name: 'Index2',
  components: {
    dragSrc, dragDest
  },
  data() {
    return {
    }
  },
  methods: {
  }
}
</script>

<style scoped lang='scss'>
$selectedColor: #f6f7ff;
$lighterBlue: #409EFF;

.left-board {
  width: 260px;
  left: 0;
  top: 0;
  height: 100vh;
  border: 1px solid red;
  float: left;
}

.center-board {
  height: 100vh;
  width: auto;
  margin: 0 350px 0 260px;
  box-sizing: border-box;
}
</style>
