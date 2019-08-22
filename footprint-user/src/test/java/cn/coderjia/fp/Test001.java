package cn.coderjia.fp;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author CoderJiA
 * @Description Test001
 * @Date 21/5/19 上午10:18
 **/
public class Test001 {

    public static void main(String[] args) {

        List<String> testList = Lists.newArrayList();

        testList.stream().collect(Collectors.toSet()).forEach(t -> System.out.println(t));

    }
}
