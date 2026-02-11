package example.dsg_be.global.error;

import example.dsg_be.global.error.excpetion.BusinessException;
import example.dsg_be.global.error.excpetion.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice // 컨트롤러 예외 잡음
@Slf4j // log 객체 자동 생성
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        log.error("handleBusinessException: {}", exception.getMessage(), exception);
        ErrorCode errorCode = exception.getErrorCode(); // 설정한 ErrorCode 정보 가져오기

        ErrorResponse responseEntityBody = ErrorResponse.of(errorCode);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(responseEntityBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("handleMethodArgumentNotValidException: {}", exception.getMessage(), exception);
        String errorMessage = exception.getBindingResult() // 에러 메세지 가져오기
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        ErrorResponse responseEntityBody = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseEntityBody);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        log.error("파일 용량 초과: {}", exception.getMessage());

        String message = "업로드 가능한 파일 용량을 초과했습니다. (최대 1MB)";
        ErrorResponse responseEntityBody = ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseEntityBody);
    }
}
