package com.example.csh.rxjava;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

import io.reactivex.ObservableEmitter;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    String[] arr = {"afdsa", "bfdsa", "cfda"};
    private static final String TAG = "rxandroid";

    public static final int RES_ID = R.mipmap.ic_launcher; //  drawable 动态纹理技术

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        case01();
//        case03(imageView);
//        case02_02();
//        case05();
    }


    private void case05() {
        String[] names = new String[]{"a","s","d"};
        Observable.from(names)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        Log.d(TAG, name);
                    }
                });
    }

//    private void case04() {
//        Course yuwen = new Course("语文", 1);
//        Course shuxue = new Course("数学", 2);
//        Course yingyu = new Course("英文", 3);
//        Course lishi = new Course("历史", 4);
//        Course zhengzhi = new Course("政治", 5);
//        Course xila = new Course("希腊语", 6);
//
//        ArrayList<Course> course1 = new ArrayList<>();
//        course1.add(yuwen);
//        course1.add(shuxue);
//        course1.add(yingyu);
//        course1.add(lishi);
//        course1.add(zhengzhi);
//        course1.add(xila);
//        Student zhangsan = new Student("zhangsan", course1);
//        // 在哪个线程调用subscribe 就在哪个线程生产事件;   在哪个线程生产事件就在哪个线程消费事件
//        Observable
//                .just(zhangsan) //事件产生  1:多   // 请求数据 1  id   拿着id 再去请求网络数据
//                .subscribeOn(Schedulers.io())//指定subscribe 的调用线程
//                .flatMap(new Func1<Student, Observable<Course>>() {
//                    @Override
//                    public Observable<Course> call(Student student) {
//                        Log.d(TAG, "事件的产生call: "+Thread.currentThread().getName());
//                        // Student对象   --->Observable
//                        return Observable.from(student.getCourses());  //事件的生产
//                    }
//
//                })
////        注：将观察者的方法在主线程调用
//                .observeOn(AndroidSchedulers.mainThread())//将事件的消费放在主线程
//                .subscribe(new Action1<Course>() {    Action1：只关注了onNext方法
//                    @Override
//                    public void call(Course course) {//onNext
//                        Log.d(TAG, "事件的消费call: "+Thread.currentThread().getName());
//                        Log.d(TAG, "onNext: "+course.getName());
//                    }
//                });
//    }

    private void case03(final ImageView imageView) {
        Observable
                .just(RES_ID) //事件的产生了，将参数传递过去
                .subscribeOn(Schedulers.io())//指定 subscribe 的调用线程    就在哪个线程 生产事件
                .map(new Func1<Integer, Drawable>() { //map mapping 映射  转换  1:1将id映射成drawable资源
                    @Override
                    public Drawable call(Integer integer) {
                        // id -- drawable  //耗时的操作
                        Log.d(TAG, "事件的产生  call: " + Thread.currentThread().getName());
                        return getResources().getDrawable(integer);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())// 指定观察者所在的线程
                .subscribe(new Observer<Drawable>() { //事件的消费 //在哪个线程生产事件 就在哪个线程消费事件
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        Log.d(TAG, "事件的消费  call: " + Thread.currentThread().getName());
                        imageView.setImageDrawable(drawable);
                    }
                });
    }

    private void case02_02() {
        Observable
                .from(arr) //事件的产生  传递数组、添加过滤
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) { // 多次
                        // true 表示 过滤出来了
                        return s.startsWith("a");
                    }
                })
                .subscribe(new Observer<String>() { //事件的消费
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(String s) {
                        s += " from alpha";
                        Log.d(TAG, "onNext: " + s);
                    }
                });
    }

    private void case02_01() {
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i];
            if (s.startsWith("a")) {
                s += " from Alpha";
                Log.d(TAG, "onCreate: " + s);
            }
        }
    }

    private void case01() {
        // 被观察者  ->
        Observable.create(new Observable.OnSubscribe<String>() {// 事件的产生
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        //    Subscriber  订阅者 (观察者)
                        subscriber.onNext("Hello ");
                        subscriber.onNext(", ");
                        subscriber.onNext(" Rx ");

                        //完成
                        subscriber.onCompleted();
                    }
                })
                .subscribe(new Observer<String>() { //事件的消费
                    @Override
                    public void onCompleted() {
                        //事件的完成
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        //事件出错
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(String s) {
                        //事件的消费过程
                        Log.d(TAG, "onNext: " + s);
                    }
                });
    }
}
