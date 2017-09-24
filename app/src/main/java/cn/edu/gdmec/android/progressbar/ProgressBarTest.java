package cn.edu.gdmec.android.progressbar;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

public class ProgressBarTest extends AppCompatActivity {
    //声明ProgressBar对象
    private ProgressBar pro1;
    private ProgressBar pro2;
    private Button btn;
    protected static final int STOP_NOTIFIER=000;
    protected static final int THREADING_NOTIFIER=111;
    public int intCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        //设置窗口模式，，因为需要显示进度条在标题栏
        requestWindowFeature ( Window.FEATURE_PROGRESS );
        //设置标题栏上的进度条可见
        setProgressBarVisibility ( true );
        setContentView ( R.layout.activity_progress_bar_test );

        //取得ProgressBar
        pro1=( ProgressBar ) findViewById ( R.id.progress1 );
        pro2=( ProgressBar ) findViewById ( R.id.progress2 );
        btn=( Button ) findViewById ( R.id.button );

        //设置进度条是否自动运转，即设置其不确定模式，false表是不自动运转
        pro1.setIndeterminate ( false );
        pro2.setIndeterminate ( false );

        //当按钮按下时开始执行
        btn.setOnClickListener ( new Button.OnClickListener () {
            @Override
            public void onClick(View v) {
                //设置ProgressBar为可见状态
                pro1.setVisibility ( View.VISIBLE );
                pro2.setVisibility ( View.VISIBLE );
                //设置ProgressBar的最大值
                pro1.setMax ( 100 );
                //设置ProgressBar当前值
                pro1.setProgress ( 0 );
                pro2.setProgress ( 0 );

                //通过线程来改变ProgressBar的值
                new Thread ( new Runnable () {
                    @Override
                    public void run() {
                        for (int i=0; i < 10; i++) {
                            try {
                                intCounter=(i + 1) * 20;
                                Thread.sleep ( 1000 );
                                if (i == 4) {
                                    Message m=new Message ();
                                    m.what=ProgressBarTest.STOP_NOTIFIER;
                                    ProgressBarTest.this.myMessageHandler.sendMessage ( m );
                                    break;
                                } else {
                                    Message m=new Message ();
                                    m.what=ProgressBarTest.THREADING_NOTIFIER;
                                    ProgressBarTest.this.myMessageHandler.sendMessage ( m );
                                }
                            } catch (Exception e) {
                                e.printStackTrace ();
                            }
                        }
                    }
                } ).start ();
            }
        } );
    }

    Handler myMessageHandler=new Handler () {
        //@Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                //ProgressBar已经是对大值
                case ProgressBarTest.STOP_NOTIFIER:
                    pro1.setVisibility ( View.GONE );
                    pro2.setVisibility ( View.GONE );
                    Thread.currentThread ().interrupt ();
                    break;
                case ProgressBarTest.THREADING_NOTIFIER:
                    if (!Thread.currentThread ().isInterrupted ())
                    {
                        // 改变ProgressBar的当前值
                        pro1.setProgress ( intCounter );
                        pro2.setProgress ( intCounter );

                        // 设置标题栏中前景的一个进度条进度值
                        setProgress ( intCounter*100 );
                        // 设置标题栏中后面的一个进度条进度值
                        setSecondaryProgress ( intCounter*100 );
                    }
                    break;
            }
            super.handleMessage ( msg );
        }
    };
}
