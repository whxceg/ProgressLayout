#### DownloadProgressButton
```
一种带下载状态和下载进度的按钮。
```
###### 自定义属性
```
    <attr name="psCornerRadius" format="reference|dimension" />         设置圆角
    <attr name="psStroke" format="reference|dimension" />               设置边的宽带   
    <attr name="psInitTextColor" format="reference|color" />            初始文字颜色
    <attr name="psInitStrokeColor" format="reference|color" />          初始边的颜色
    <attr name="psInitBackgroundColor" format="reference|color" />      初始背景颜色

    <attr name="psDownloadTextColor" format="reference|color" />        下载进度文字颜色
    <attr name="psDownloadStrokeColor" format="reference|color" />      下载状态边的颜色
    <attr name="psDownloadBackgroundColor" format="reference|color" />  下载状态背景的颜色
    <attr name="psDownloadProgressColor" format="reference|color" />    下载进度背景的颜色

    <attr name="psFinishTextColor" format="reference|color" />          完成文字的颜色
    <attr name="psFinishStrokeColor" format="reference|color" />        完成边的颜色
    <attr name="psFinishBackgroundColor" format="reference|color" />    完成背景的颜色

    <attr name="psInitText" format="reference|string" />                初始的提示文字
    <attr name="psDownloadText" format="reference|string" />            下载进度提示format字符串格式。如：已下载%d%%  
    <attr name="psFinishText" format="reference|string" />              完成时的提示文字

    <attr name="psInitTextSize" format="reference|dimension" />         初始的提示文字大小
    <attr name="psDownloadTextSize" format="reference|dimension" />     下载进度提示文字大小
    <attr name="psFinishTextSize" format="reference|dimension" />       完成时提示文字大小
```

###### 实例代码
```
    <com.sam.lib.progresslayout.DownloadProgressButton
        android:layout_width="match_parent"
        android:layout_height="50dp"
        app:psCornerRadius="10dp"
        app:psInitText="下载"
        app:psDownloadText="%d%%"
        app:psFinishTextSize="14sp"
        app:psInitTextSize="14sp"
        app:psStroke="0.5dp" />
```