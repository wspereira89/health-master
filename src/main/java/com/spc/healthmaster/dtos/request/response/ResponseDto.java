package com.spc.healthmaster.dtos.request.response;

import com.spc.healthmaster.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Builder
@Data
public class ResponseDto {
    private final Status status;
    private final List<FileDto>fileDtoList;
    private final byte[] download;

    public Optional<Status> getStatus() {
        return Optional.ofNullable(status);
    }

    public Optional<List<FileDto>> getFileDtoList() {
        return Optional.ofNullable(fileDtoList);
    }

    public Optional<byte[]> getDownload() {
        return Optional.ofNullable( download);
    }
}
