package example.dsg_be.global.error.excpetion;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //teacher
    Not_Excel_File_Exception(400, ".xlsx 형식의 파일만 허용됩니다"),
    Teacher_Not_Found_Exception(404, "존재하지 않는 교직원입니다"),
    Teacher_Not_Exist_Exception(404, "교직원 정보가 비어있습니다"),
    Name_Cell_Is_Empty_Exception(400, "액셀의 이름 셀이 비어있습니다"),
    Name_Already_Exist_Exception(409, "이미 존재하는 교직원 이름입니다");

    private final int status;
    private final String message;
}
