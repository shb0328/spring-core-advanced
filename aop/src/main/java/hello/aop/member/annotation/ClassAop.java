package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE) // class 에 적용되는 annotation
@Retention(RetentionPolicy.RUNTIME) // runtime 에 살아있는 annotation
public @interface ClassAop {
}
