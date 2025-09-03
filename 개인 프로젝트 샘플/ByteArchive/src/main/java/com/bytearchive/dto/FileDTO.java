package com.bytearchive.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class FileDTO {

    private String uuid;
    private Integer order;

    @ToString.Exclude
    private byte[] data;

    @ToString.Exclude
    private MultipartFile multipartFile;

    private String mimeType;
    private String extension;

    @Override
    public String toString() {
        return "FileDTO(uuid=" + uuid + ", order=" + order + ", mimeType=" + mimeType + ", extension=" + extension + ")";
    }

}
