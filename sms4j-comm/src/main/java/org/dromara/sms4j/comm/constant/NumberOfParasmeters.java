package org.dromara.sms4j.comm.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * NumberOfParasmeters
 * <p> 重载方法的参数个数
 *
 * @author :sh1yu
 * 2023/11/01  19:33
 **/
@Getter
@AllArgsConstructor
public enum NumberOfParasmeters {
    //一个参数
    ONE(1),
    //两个参数
    TWO(2),
    //三个参数
    THREE(3);
    private final int code;

    public static NumberOfParasmeters getNumberOfParasmetersEnum(int index) {
        switch (index) {
            case 1:
                return NumberOfParasmeters.ONE;
            case 2:
                return NumberOfParasmeters.TWO;
            case 3:
                return NumberOfParasmeters.THREE;
            default:
                break;
        }
        throw new IllegalArgumentException("building enum NumberOfParasmeters error,param not match");
    }

}
