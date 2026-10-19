<template>
  <div>
    <svg-icon icon-class="question" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'RuoYiDoc',
  data() {
    return {
      url: 'https://101.43.242.145:8443/doc/debug-all.html'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
