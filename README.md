#### MaxLayout
```
一种可以设置最大宽度和最大高度布局控件的实现，可以设置最大值或根据屏幕大小设置最大比例。
```
###### 自定义属性
```
 <attr name="ml_HeightRatio" format="reference|float" />
 <attr name="ml_HeightDimen" format="reference|dimension" />
 <attr name="ml_WidthRatio" format="reference|float" />
 <attr name="ml_WidthDimen" format="reference|dimension" />
```

###### 实例代码
* 设置最大高度
```
    <com.sam.lib.maxlayout.MaxLayout
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:background="@color/colorPrimary"
        app:ml_HeightDimen="800dp">
    </com.sam.lib.maxlayout.MaxLayout>
```
* 设置最大高度比例(最大值为ratio * 屏幕的宽或高)
```
    <com.sam.lib.maxlayout.MaxLayout
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:background="@color/colorPrimary"
        app:ml_HeightRatio="0.5">
    </com.sam.lib.maxlayout.MaxLayout>
```