<template>
  <div id="global-uploader">
    <uploader
      :options="simpleUploader.options"
      :key="simpleUploader.uploaderKey"
      @file-success="onFileSuccess"
      @file-complete="onFileComplete"
      @file-progress="onFileProgress"
      @file-added="onFileAdded"
      @files-added="onFilesAdded"
      @files-submitted="onFilesSubmitted"
      @file-removed="onFileRemoved"
      @file-retry="onFileRetry"
      @file-error="onFileError"
      @upload-start="onUploadStart"
      @complete="complete"
      class="uploader-example"
      ref="uploader"
    >
      <uploader-unsupport></uploader-unsupport>

      <!-- select file -->
      <uploader-btn
        class="global-uploader-btn"
        :attrs="simpleUploader.attrs"
        ref="uploadFileBtn"
      />

      <!-- select folder -->
      <uploader-btn
        class="global-uploader-btn"
        :directory="true"
        ref="uploadFolderBtn"
      />

      <uploader-list v-show="uploaderPanelShow">
        <div
          class="file-panel"
          slot-scope="props"
          :class="{'collapse': collapse}"
        >
          <div class="file-title">
            <p class="file-list-title">文件列表</p>
            <div class="operate">
              <el-button
                type="text"
                @click="operate"
                :title="collapse ? '折叠':'展开' "
              >
                <i
                  class="iconfont"
                  :class="collapse ? 'el-icon-caret-bottom': 'el-icon-caret-top'"
                ></i>
              </el-button>
              <el-button
                type="text"
                @click="close"
                title="关闭"
              >
                <i class="iconfont el-icon-close"></i>
              </el-button>
            </div>
          </div>

          <ul
            class="file-list"
            :class="collapse ? 'uploader-list-ul-show': 'uploader-list-ul-hidden'"
          >
            <li
              v-for="file in props.fileList"
              :key="file.id"
            >
              <uploader-file
                :class="'file_' + file.id"
                ref="files"
                :file="file"
                :list="true"
              ></uploader-file>
            </li>
            <div
              class="no-file"
              v-if="!props.fileList.length"
            ><i class="iconfont icon-empty-file"></i> 暂无待上传文件</div>
          </ul>

        </div>
      </uploader-list>

    </uploader>
  </div>
</template>

<script>

export default {
  data () {
    return {
      // 选择文件后，展示上传panel
      uploaderPanelShow: true,
      collapse: true,
      /**
       * 初始化组件 vue-simple-uploader
       */
      simpleUploader: {
        // 这个用来刷新组件--解决不刷新页面连续上传的缓存上传数据（注：每次上传时，强制这个值进行更改---根据自己的实际情况重新赋值）
        uploaderKey: new Date().getTime(),
        // 组件实例化时传入的配置项
        options: {
          // 目标上传 URL，可以是字符串也可以是函数，如果是函数的话，则会传入 Uploader.File 实例、当前块 Uploader.Chunk 以及是否是测试模式
          target: 'http://127.0.0.1:8089/uploader/slicing-upload',
          // 单文件上传。覆盖式，如果选择了多个会把之前的取消掉
          singleFile: false,
          // 分块时按照该值来分，最后一个上传块的大小是可能是大于等于1倍的这个值但是小于两倍的这个值大小
          chunkSize: 1 * 1024 * 1024,
          // 是否强制所有的块都是小于等于 chunkSize 的值
          forceChunkSize: false,
          // 并发上传数
          simultaneousUploads: 3,
          // 上传文件时文件的参数名
          fileParameterName: 'file',
          // 其他额外的参数，这个可以是一个对象或者是一个函数，如果是函数的话，则会传入 Uploader.File 实例、当前块 Uploader.Chunk 以及是否是测试模式
          query: {},
          // 额外的一些请求头，如果是函数的话，则会传入 Uploader.File 实例、当前块 Uploader.Chunk 以及是否是测试模式
          headers: {},
          // 标准的 CORS 请求是不会带上 cookie 的，如果想要带的话需要设置 withCredentials 为 true
          withCredentials: false,
          // 当上传的时候所使用的是方式，可选 multipart、octet
          method: 'multipart/form-data',
          // 测试的时候使用的 HTTP 方法，可以是字符串或者函数，如果是函数的话，则会传入 Uploader.File 实例、当前块 Uploader.Chunk
          testMethod: 'GET',
          // 真正上传的时候使用的 HTTP 方法，可以是字符串或者函数，如果是函数的话，则会传入 Uploader.File 实例、当前块 Uploader.Chunk
          uploadMethod: 'POST',
          // 如果说一个文件已经上传过了是否还允许再次上传。默认的话如果已经上传了，除非你移除了否则是不会再次重新上传的
          allowDuplicateUploads: false,
          // 对于文件而言是否高优先级发送第一个和最后一个块。一般用来发送到服务端，然后判断是否是合法文件；例如图片或者视频的 meta 数据一般放在文件第一部分，这样可以根据第一个块就能知道是否支持
          prioritizeFirstAndLastChunk: false,
          // 是否测试每个块是否在服务端已经上传了，主要用来实现秒传、跨浏览器上传等
          testChunks: false,
          // 服务器分片校验函数 秒传及断点续传的基础(true:不用传 false:需要传)
          // checkChunkUploadedByResponse: (chunk, message) => {
          // 这里根据实际业务来 用来判断哪些片已经上传过了 不用再重复上传了 [这里可以用来写断点续传！！！]
          // return false
          // },
          // 可选的函数，每个块在测试以及上传前会被调用，参数就是当前上传块实例 Uploader.Chunk，注意在这个函数中你需要调用当前上传块实例的 preprocessFinished 方法
          preprocess: null,
          // 可覆盖默认的生成文件唯一标示的函数
          generateUniqueIdentifier: null,
          // 最大自动失败重试上传次数，值可以是任意正整数，如果是 undefined 则代表无限次
          maxChunkRetries: 2,
          // 重试间隔，值可以是任意正整数，如果是 null 则代表立即重试
          chunkRetryInterval: null,
          // 进度回调间隔
          progressCallbacksInterval: 500,
          // 主要用于计算平均速度，值就是从 0 到 1，如果是 1 那么上传的平均速度就等于当前上传速度，如果说长时间上传的话，建议设置为 0.02，这样剩余时间预估会更精确，这个参数是需要和 progressCallbacksInterval 一起调整的
          speedSmoothingFactor: 0.1,
          // 认为响应式成功的响应码
          successStatuses: [200, 201, 202],
          // 认为是出错的响应码
          permanentErrors: [404, 415, 500, 501],
          // 初始文件 paused 状态
          initialPaused: false,
          // 用于格式化你想要剩余时间，一般可以用来做多语言
          parseTimeRemaining: function (timeRemaining, parsedTimeRemaining) {
            // timeRemaining{Number}, 剩余时间，秒为单位
            // parsedTimeRemaining{String}, 默认展示的剩余时间内容
            return parsedTimeRemaining
              .replace(/\syears?/, '年')
              .replace(/\days?/, '天')
              .replace(/\shours?/, '小时')
              .replace(/\sminutes?/, '分钟')
              .replace(/\sseconds?/, '秒')
          }
        },
        // 是否选择文件后自动开始上传
        autoStart: true,
        statusText: {
          success: '成功',
          error: '失败',
          uploading: '上传中',
          paused: '暂停',
          waiting: '等待'
        },
        // 用于转换文件上传状态文本映射对象
        fileStatusText: function (status, response) {
          // 第一个 status 为状态，第二个为响应内容
          const statusTextMap = {
            success: '成功',
            error: '失败',
            uploading: '上传中',
            paused: '暂停',
            waiting: '等待'
          }
          return statusTextMap[status]
        },
        // 添加到 input 元素上的额外属性
        attrs: {}
      }

    }
  },
  // 钩子函数：页面加载完成后执行
  mounted: function () {
  },
  methods: {
    /**
     * 触发文件上传的按钮
     */
    openFileUploader: function () {
      this.$refs.uploadFileBtn.$el.click()
    },

    /**
     * 触发文件夹上传的按钮
     */
    openFolderUploader: function () {
      this.$refs.uploadFolderBtn.$el.click()
    },

    /**
     * 折叠、展开面板动态切换
     */
    operate: function () {
      if (this.collapse === false) {
        this.collapse = true
      } else {
        this.collapse = false
      }
    },

    /**
     * 关闭折叠面板
     */
    close () {
      this.uploaderPanelShow = false
    },

    /**
     * 事件
     * 一个文件上传成功
     *
     * @param {object} rootFile 成功上传的文件所属的根 Uploader.File 对象，它应该包含或者等于成功上传文件
     * @param {object} file 当前成功的 Uploader.File 对象本身
     * @param {object} message 服务端响应内容，永远都是字符串
     * @param {object} chunk Uploader.Chunk 实例，它就是该文件的最后一个块实例，如果你想得到请求响应码的话，chunk.xhr.status 就是
     */
    onFileSuccess: function (rootFile, file, message, chunk) {
      console.log(`文件: ${file.name} 上传成功`)
    },

    /**
     * 事件
     * 一个根文件（文件夹）成功上传完成。
     */
    onFileComplete: function (rootFile) {
      console.log('触发 onFileComplete 事件')
    },

    /**
     * 事件
     * 文件上传中触发
     */
    onFileProgress (rootFile, file, chunk) {
      console.log(`上传中 ${file.name}，chunk：${chunk.startByte / 1024 / 1024} ~ ${chunk.endByte / 1024 / 1024}`)
    },

    /**
     * 事件:
     * 添加了一个文件，一般用做文件校验，如果设置 file.ignored = true 的话这个文件就会被过滤掉
     */
    onFileAdded: function (file) {
      this.uploaderPanelShow = true
    },

    /**
     * 事件
     * 和 fileAdded 一样，但是一般用作多个文件的校验。
     */
    onFilesAdded: function (files, fileList, event) {
      this.uploaderPanelShow = true
    },

    /**
     * 事件
     * 和 filesAdded 类似，但是是文件已经加入到上传列表中，一般用来开始整个的上传
     */
    onFilesSubmitted: function (files, fileList, event) {
      console.log('触发 onFilesSubmitted 事件')
    },

    /**
     * 事件
     *  一个文件（文件夹）被移除
     */
    onFileRemoved: function (file) {
      console.log(`文件: ${file.name} 删除成功`)
    },

    /**
     * 事件
     * 文件重试上传事件
     */
    onFileRetry: function (rootFile, file, chunk) {
      console.log(`文件: ${file.name} 重试上传`)
    },

    /**
     * 事件
     * 文件上传出错
     */
    onFileError: function (rootFile, file, message, chunk) {
      console.log(`文件: ${file.name} 上传出错`, message)
    },

    /**
     * 事件
     * 已经开始上传了
     */
    onUploadStart: function () {
      console.log('触发 onUploadStart 事件')
    },

    /**
     * 事件
     * 上传完毕
     */
    complete: function () {
      console.log('触发 complete 事件')
    }
  }
}
</script>

<style>
#global-uploader {
  position: fixed;
  z-index: 20;
  right: 15px;
  bottom: 15px;
  width: 550px;
}

.file-panel {
  background-color: #fff;
  border: 1px solid #e2e2e2;
  border-radius: 7px 7px 0 0;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.file-title {
  display: flex;
  height: 40px;
  line-height: 40px;
  padding: 0 15px;
  border-bottom: 1px solid #ddd;
}

.file-title {
  background-color: #e7ecf2;
}

.uploader-file-meta {
  display: none !important;
}

.operate {
  flex: 1;
  text-align: right;
}

.file-list {
  position: relative;
  height: 240px;
  overflow-x: hidden;
  overflow-y: auto;
  background-color: #fff;
  padding: 0px;
  margin: 0 auto;
  transition: all 0.5s;
}

.uploader-file-size {
  width: 15% !important;
}

.uploader-file-status {
  width: 32.5% !important;
  text-align: center !important;
}

li {
  background-color: #fff;
  list-style-type: none;
}

.no-file {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 16px;
}

/* 隐藏上传按钮 */
.global-uploader-btn {
  display: none !important;
  clip: rect(0, 0, 0, 0);
}

.file-list-title {
  line-height: 10px;
}

.uploader-file-name {
  width: 36% !important;
}

.uploader-file-actions {
  float: right !important;
}

.uploader-list-ul-hidden {
  height: 0px;
}
</style>
