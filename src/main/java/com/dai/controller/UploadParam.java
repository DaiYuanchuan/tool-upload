package com.dai.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>分片上传参数</p>
 * <p>2021-02-02 10:27</p>
 *
 * @author Dan
 **/
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ApiModel(value = "分片上传参数")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadParam {

    @ApiModelProperty(value = "当前块的次序，第一个块是 1，注意不是从 0 开始的")
    private Integer chunkNumber;
    @ApiModelProperty(value = "分块大小，根据 totalSize 和这个值你就可以计算出总共的块数。注意最后一块的大小可能会比这个要大")
    private Long chunkSize;
    @ApiModelProperty(value = "当前块的大小，实际大小")
    private Integer currentChunkSize;
    @ApiModelProperty(value = "文件总大小。")
    private Long totalSize;
    @ApiModelProperty(value = "每个文件的唯一标示")
    private String identifier;
    @ApiModelProperty(value = "文件名")
    private String filename;
    @ApiModelProperty(value = "文件夹上传的时候文件的相对路径属性(如果上传的是文件夹)")
    private String relativePath;
    @ApiModelProperty(value = "文件被分成块的总数")
    private Integer totalChunks;
    @ApiModelProperty(value = "当前文件的分片对象", required = true)
    private MultipartFile file;

}
