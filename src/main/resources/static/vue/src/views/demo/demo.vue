<template>
  <div>
    <h2>上传demo</h2>
    <button @click="upload()">上传文件</button>
    <button @click="uploadFolder()">上传文件夹</button>
    <global-uploader ref="globalUploader"></global-uploader>
  </div>
</template>

<script>
import globalUploader from '@/views/upload/globalUploader'

export default {
  name: 'demo',
  data () {
    return {
    }
  },
  components: {
    globalUploader
  },
  // 钩子函数：页面加载完成后执行
  mounted: function () {
  },
  // 钩子函数: 页面创建时执行
  created: function () {
  },
  // 钩子函数：页面挂载前
  beforeMount () {
  },
  methods: {
    changeData () {
      // 可以通过子组件的 ref 属性改变子组件的内容
      this.$refs.globalUploader.setAttrs({
        type: 'image/*',
        accept: 'image/*'
      })
      this.$refs.globalUploader.setOptions({
        target: 'http://127.0.0.1:8089/uploader/local-upload'
      })
    },
    upload () {
      // 调用子组件的openFileUploader方法
      this.$refs.globalUploader.openFileUploader()
    },
    uploadFolder () {
      this.$refs.globalUploader.openFolderUploader()
    }
  }
}
</script>

<style scoped lang="scss">
</style>
