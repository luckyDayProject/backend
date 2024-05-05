package io.swyp.luckybackend.luckyDays.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReqDto {
    private Long dtlNo;
    @Nullable
    private String review;
    @Nullable
    private String imageName;
    @Nullable
    private String imagePath;
}


