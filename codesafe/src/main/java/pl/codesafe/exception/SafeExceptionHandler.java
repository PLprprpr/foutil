package pl.codesafe.exception;

/**
 * Safer的异常处理器
 * <p>处理Safer中出现的异常，按添加顺序依次处理</p>
 * <p>如果处理后没有将异常返回，处理终止；</p>
 * <p>如果全部处理器处理完毕后，最终返回了异常，Safer会将异常抛出；</p>
 * @author LiYan
 */
@FunctionalInterface
public interface SafeExceptionHandler {

    /**
     * 处理异常
     * @param throwable 异常
     * @return 如果返回的异常不为null，会交给下一个handler处理
     */
    Throwable handle(Throwable throwable);

}
