import * as qiniu from 'qiniu-js'

/**
 * // token 找后端，obj 这里指代从 el-upload 接收到的 object
 * @param {*} token 后端返回的token
 * @param {*} key 后端返回的key
 * @param {*} obj 从 el-upload 接收到的 object
 * @param {*} next 用于接收上传过程中返回的上传进度
 * @param {*} error 上传出现异常时返回
 * @param {*} complete 上传成功时返回
 */
export const upload = (token, key, obj, next, error, complete) => {
  console.log('asd')
  const {
    file
  } = obj

  // 因人而异，自行解决
  const putExtra = {
    customVars: {}
  }
  // fname: string，文件原文件名
  // params: object，用来放置自定义变量
  // mimeType: null || array，用来限制上传文件类型，为 null 时表示不对文件类型限制；限制类型放到数组里： ["image/png", "image/jpeg", "image/gif"]
  let config = {
    useCdnDomain: true,
    disableStatisticsReport: false,
    retryCount: 6,
    region: qiniu.region.z0
  }

  let observable = qiniu.upload(file, key, token, putExtra, config)
  // 调用sdk上传接口获得相应的observable，控制上传和暂停
  let subscription = observable.subscribe(next, error, complete)
  return subscription
}
