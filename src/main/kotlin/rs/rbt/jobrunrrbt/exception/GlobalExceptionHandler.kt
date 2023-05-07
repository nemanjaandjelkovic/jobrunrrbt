package rs.rbt.jobrunrrbt.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

/** The GlobalExceptionHandler class handles exceptions for specific types and returns an appropriate
error message. */
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ApiError {
        return ApiError(HttpStatus.BAD_REQUEST.value(), ex.message ?: "Invalid request")
    }

    @ExceptionHandler(IdNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleIdNotFoundException(ex: IdNotFoundException): ApiError {
        return ApiError(HttpStatus.NOT_FOUND.value(), ex.message ?: "Invalid ID")
    }

    @ExceptionHandler(IllegalJobState::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleIllegalJobState(ex: IllegalJobState): ApiError {
        return ApiError(HttpStatus.BAD_REQUEST.value(), ex.message ?: "Invalid Job state")
    }

}
