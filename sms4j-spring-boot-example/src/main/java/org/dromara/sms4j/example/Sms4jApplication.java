package org.dromara.sms4j.example;

import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 主类
 *
 * @author handy
 */
@SpringBootApplication
public class Sms4jApplication {

    public static void main(String[] args) {
        SpringApplication.run(Sms4jApplication.class, args);
//        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.QINIU).sendMessage("135****0844", "1234");

//        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
//        linkedHashMap.put("code", "2234");
//        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.QINIU).sendMessage("135****0844", "1752130467315859456",linkedHashMap);
//        System.out.println(smsResponse);

//        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
//        linkedHashMap.put("code", "3234");
//        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.QINIU).sendMessage("135****0844", linkedHashMap);
//        System.out.println(smsResponse);

//        ArrayList<String> list = new ArrayList<>();
//        list.add("135****0844");
//        list.add("175****5952");
//
//        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.QINIU)
//                .massTexting(list, "1234");
//        System.out.println(smsResponse);


//        ArrayList<String> list = new ArrayList<>();
//        list.add("135****0844");
//        list.add("175****65952");
//
//        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
//        hashMap.put("code", "2234");
//        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.QINIU)
//                .massTexting(list,"1752130467315859456", hashMap);
//        System.out.println(smsResponse);

    }

}
