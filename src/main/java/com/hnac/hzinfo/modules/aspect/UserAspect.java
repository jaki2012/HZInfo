package com.hnac.hzinfo.modules.aspect;

import com.hnac.hzinfo.modules.sys.entity.NoticeRecord;
import com.hnac.hzinfo.modules.sys.utils.UserUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author lijiechu
 * @create on 17/8/7
 * @description 模拟用户信息\权限获取的AOP类 实际应用SpringInterceptor拦截token/session/cookie等方式实现
 */
@Aspect
@Component
public class UserAspect {

    @Pointcut("execution(public * com.hnac.hzinfo.modules.sys.service.Impl.*Impl.*(com.hnac.hzinfo.modules.sys.entity.NoticeRecord, ..))")
    public void joinPointExpression(){

    }

    /**
     * 该标签声明次方法是一个前置通知：在目标方法开始之前执行
     * @param joinPoint
     */
    @Before("joinPointExpression()")
    public void beforeAccessingNoticeRecord(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("this method "+methodName+" begin. param<"+ args+">");
    }

    @Around("joinPointExpression()")
    public int aroundAccessingNoticeRecord(ProceedingJoinPoint proceedingJoinPoint) {
        // add方法错误码为0 update方法错误码为1 如能顺利进入方法 result返回值由方法执行情况决定
        int result = 0;
        if(proceedingJoinPoint.getSignature().getName().equals("update")) {
            result = 1;
        }
        try {
            NoticeRecord noticeRecord = (NoticeRecord) proceedingJoinPoint.getArgs()[0];

            String username = noticeRecord.getSender();
            // 假如没有获取到权限
            if(!UserUtils.acquirePermission(username)) {
                return result;
            }
            //执行目标方法
            result = (Integer) proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            //异常通知
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

}
