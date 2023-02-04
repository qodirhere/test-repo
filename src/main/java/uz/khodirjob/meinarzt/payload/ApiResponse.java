package uz.khodirjob.meinarzt.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String message;
    private boolean success;

    private T object;

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ApiResponse(T data) {
        this.object = data;
        this.success = true;
    }

    public static final ApiResponse<?> SUCCESS = new ApiResponse<>(null, true);
    public static final ApiResponse<?> FAIL = new ApiResponse<>(null, false);
}