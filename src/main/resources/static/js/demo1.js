let count = 0;
// 初始化上传控件
let uploader = $.uploaderInit({
    server: 'uploader/demo1',
    pick: {
        id: '#picker',
        multiple: true
    },
    chunked: true,
    fileQueued: (file) => {
        count++;
        $("#thelist table>tbody").append(`
        <tr id="${file.id}" class="item" flag=0>
            <td class="index">${count}</td>
            <td class="info">${file.name}</td>
            <td class="size">${WebUploader.Base.formatSize(file.size)}</td>
            <td class="state">等待上传...</td>
            <td class="percentage"></td>
            <td class="operate">
                <button name="upload" data-type="start" data-fid="${file.id}" class="btn btn-warning up-start">开始</button>
                <button name="delete" data-fid="${file.id}" class="btn btn-error">删除</button>
            </td>
        </tr>`);
        // 绑定事件之前先解除绑定
        $("button[name=upload]").unbind('click');
        $("button[name=delete]").unbind('click');
        $("button[name=upload]").on('click', function () {
            let state = $(this).data('type');
            console.log(new Date().getTime());
            switch (state) {
                case 'start':
                    $(this).data('type', 'stop');
                    $(this).text('暂停');
                    uploader.upload(uploader.getFile($(this).data('fid'), true))
                    break;
                case 'stop':
                    $(this).data('type', 'retry')
                    $(this).text('开始');
                    uploader.stop(true);
                    break;
                case 'retry':
                    $(this).data('type', 'stop');
                    $(this).text('暂停');
                    uploader.upload(uploader.getFile($(this).data('fid'), true).id);
                    break;
            }
            return false;
        })
        $("button[name=delete]").on('click', function () {
            uploader.removeFile(uploader.getFile($(this).data('fid'), true));
            console.log($(this).data('fid'))
            $("#" + $(this).data('fid')).remove();
        })
    },
    uploadBeforeSend: (object, data, headers) => {
        let file = object.file;
        data.md5 = file.md5 || '';
        data.uid = file.uid;
    },
    uploadProgress: (file, percentage) => {
        $('#' + file.id).find('td.percentage').text(
            '上传中 '
            + Math.round(percentage * 100) + '%'
            + '(' + WebUploader.Base.formatSize(file.uploadRate) + '/s)');
    },
    uploadSuccess: (file, response) => {
        $('#' + file.id).find('td.state').text('已上传');
        console.log(response);
    },
    uploadError: (file, reason) => {
        $('#' + file.id).find('td.state').text('上传出错');
        console.error(reason)
    },
    beforeInit: () => {
        // 这个必须要写在实例化前面
        WebUploader.Uploader.register({
            'before-send-file': 'beforeSendFile',
            'before-send': 'beforeSend'
        }, {
            // 时间点1：所有分块进行上传之前调用此函数
            beforeSendFile: function (file) {
                let deferred = WebUploader.Deferred();
                (new WebUploader.Uploader()).md5File(file, 0, 5242880).progress(function (percentage) {
                    // 显示计算进度
                    console.log('计算md5进度:', percentage);
                    $('#' + file.id).find("td.state").text("校验MD5中...");
                }).then(function (val) {
                    file.md5 = val;
                    file.uid = WebUploader.Base.guid();
                    // 进行md5判断
                    $.ajax({
                        url: 'uploader/checkFile',
                        type: 'GET',
                        showError: false,
                        global: false,
                        data: {
                            fileName: file.name,
                            md5: file.md5
                        },
                        success: (data) => {
                            console.log(data);
                            let status = data.code;
                            deferred.resolve();
                            switch (status) {
                                case "200":
                                    // 忽略上传过程，直接标识上传成功；
                                    uploader.skipFile(file);
                                    file.pass = true;
                                    break;
                                case "206":
                                    // 部分已经上传到服务器了，但是差几个模块。
                                    file.missChunks = data.data;
                                    console.log(file.missChunks);
                                    break;
                                default:
                                    break;
                            }
                        }
                    })
                })
                return deferred.promise();
            },
            // 时间点2：如果有分块上传，则每个分块上传之前调用此函数
            beforeSend: function (block) {
                let deferred = WebUploader.Deferred();
                // 当前未上传分块
                let missChunks = block.file.missChunks;
                // 当前分块
                let blockChunk = block.chunk;
                if (missChunks !== null && missChunks !== undefined && missChunks !== '') {
                    let flag = true;
                    for (let i = 0; i < missChunks.length; i++) {
                        if (blockChunk === parseInt(missChunks[i])) {
                            // 存在还未上传的分块
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        deferred.reject();
                    } else {
                        deferred.resolve();
                    }
                } else {
                    deferred.resolve();
                }
                return deferred.promise();
            }
        });
    }
});