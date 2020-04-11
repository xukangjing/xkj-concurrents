package com.xkj.ch2.future;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author xukangjing
 * @description
 * FutureTask实际应用场景一般在线程池中 后者一个计算复杂场景任务比较耗时，同时后面还需要前面的执行结果时的场景
 * 例如：一个文档中有文字、图片、等 可以
 * @date 2020/4/6 - 12:35
 **/
public class UseFutureDemoXkj {
    private static class UseCallable implements Callable<Integer>{



        private int sum ;
        @Override
        public Integer call() throws InterruptedException {
            System.out.println("callable子线程开始计算");
            //作用：1、在未来2000ms内不用给我cpu使用权；
            // 2触发中断异常来达到终止计算任务的目的（因为任何一个线程中抛出一个未经过捕获异常时，这个线程就会自然而然的终止，不再执行后面的代码了
            // 因为是多个线程并发执行 并且Random随机决定是否获取任务结果 所以有的没有触发中断的就继续往下执行从而打出任务执行结果）
            Thread.sleep(2000);
            //模拟业务
            for(int i = 0;i<5000;i++){
                sum = sum + i;
            }
            System.out.println("Callable子线程计算完成，结果="+sum);
            return sum;
        }

        public static void main(String[] args) throws InterruptedException, ExecutionException {
            UseCallable useCallable = new UseCallable();
            //FutureTask implements RunnableFuture,这里相当于把有返回值得Callable包
            //装成了Runnable 然后作为工作线程传给Thread中
            FutureTask<Integer> futureTask = new FutureTask<Integer>(useCallable);
            new Thread(futureTask).start();
            Random r =  new Random();
            //让main主线程挂起休眠1秒钟模拟执行自己的其他逻辑后再去执行后面的获取任务计算结果代码
            TimeUnit.SECONDS.sleep(1);
            if(r.nextBoolean()){//随机决定是获取结果还是终止任务
                System.out.println("Get UseCallable result = "+ futureTask.get());
            }else{
                System.out.println("中断计算");
                futureTask.cancel(true);
            }
        }
    }
}
