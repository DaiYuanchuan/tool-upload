<template>
  <div id="">
    <uploader
      :key="uploader_key"
      :options="options"
      class="uploader-app"
      @file-success="onFileSuccess"
    >
      <uploader-unsupport></uploader-unsupport>
      <uploader-btn
        id="global-uploader-btn"
        :attrs="attrs"
        ref="uploadBtn"
      >选择文件</uploader-btn>
      <uploader-list v-show="panelShow">
        <!-- <div
          class="file-panel"
          slot-scope="props"
          :class="{'collapse': collapse}"
        >
          <div class="file-title">
            <h2>文件列表</h2>
            <div class="operate">
              <el-button
                @click="fileListShow"
                type="text"
                :title="collapse ? '展开':'折叠' "
              >
                <i
                  class="iconfont el-icon-minus"
                  :class="collapse ? 'inuc-fullscreen': 'inuc-minus-round'"
                ></i>
              </el-button>
              <el-button
                @click="close"
                type="text"
                title="关闭"
              >
                <i class="iconfont el-icon-close"></i>
              </el-button>
            </div>
          </div>

          <ul class="file-list">
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
        </div> -->
      </uploader-list>
    </uploader>
  </div>
</template>

<script>
import { upload } from '@/api/qiniu'

export default {
  name: 'upload',
  data () {
    return {
      // 这个用来刷新组件--解决不刷新页面连续上传的缓存上传数据（注：每次上传时，强制这个值进行更改---根据自己的实际情况重新赋值）
      uploader_key: new Date().getTime(),
      options: {
        // SpringBoot后台接收文件夹数据的接口
        target: 'http://127.0.0.1:8089/uploader/slicing-upload',
        // 是否分片-不分片
        testChunks: false,
        query: this.query,
        headers: {
          Authorization: 'authorization'
        },
        // 真正上传的时候使用的 HTTP 方法
        uploadMethod: 'POST'
      },
      attrs: {
        // 接受的文件类型，形如['.png', '.jpg', '.jpeg', '.gif', '.bmp'...] 这里我封装了一下
        accept: ['.png', '.jpg', '.jpeg', '.gif', '.bmp']
      },
      panelShow: true,
      collapse: true
    }
  },
  // 钩子函数：页面加载完成后执行
  mounted: function () {
    console.log('asd')
  },
  methods: {
    myOss () {
      console.log('===================开始上传========================')
      upload(
        'token',
        'key',
        this.$refs.uploader.files[0],
        next => {
          console.log('next')
          let total = next.total
          console.log(total)
          // 设置进度条百分比
          this.progress = total.percent
        },
        error => {
          console.log('error')
          console.log(error)
        },
        complete => {
          console.log('complete')
        }
      )
    },
    // 其他额外的参数
    query: function (file, chunk) {
      console.log('query-file', file)
      console.log('query-chunk', chunk)
      console.log()
      return {
        token: 'asd'
      }
    },
    onFileSuccess: function (rootFile, file, response, chunk) {
      // console.log('onFileSuccess', rootFile, file, response, chunk)
      // 这里可以根据response（接口）返回的数据处理自己的实际问题（如：从response拿到后台返回的想要的数据进行组装并显示）
      // 注，这里从文件夹每上传成功一个文件会调用一次这个方法
    },

    fileListShow () {
      if (this.collapse === false) {
        console.log('上滑')
        this.collapse = true
      } else {
        console.log('下滑')
        this.collapse = false
      }
    },
    close () {
      this.uploader.cancel()
      this.panelShow = false
    }
  }
}
</script>

<style scoped lang="scss">
#global-uploader {
  position: fixed;
  z-index: 20;
  right: 15px;
  bottom: 15px;
  .uploader-app {
    width: 520px;
  }
  .file-panel {
    background-color: #fff;
    border: 1px solid #e2e2e2;
    border-radius: 7px 7px 0 0;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
    .file-title {
      display: flex;
      height: 40px;
      line-height: 40px;
      padding: 0 15px;
      border-bottom: 1px solid #ddd;
      .operate {
        flex: 1;
        text-align: right;
      }
    }
    .file-list {
      position: relative;
      height: 240px;
      overflow-x: hidden;
      overflow-y: auto;
      background-color: #fff;
      > li {
        background-color: #fff;
      }
    }
    &.collapse {
      .file-title {
        background-color: #e7ecf2;
      }
    }
  }
  .no-file {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 16px;
  }
  // /deep/.uploader-file-icon {
  //   &:before {
  //     content: '' !important;
  //   }
  //   &[icon='image'] {
  //     background: url(https://file.jiugell.com/w3/upload/images/image-icon.png);
  //   }
  //   &[icon='video'] {
  //     background: url(https://file.jiugell.com/w3/upload/images/video-icon.png);
  //   }
  //   &[icon='document'] {
  //     background: url(https://file.jiugell.com/w3/upload/images/text-icon.png);
  //   }
  // }
  /deep/.uploader-file-actions > span {
    margin-right: 6px;
  }
}
/* 隐藏上传按钮 */
// #global-uploader-btn {
//   position: absolute;
//   clip: rect(0, 0, 0, 0);
// }
</style>
