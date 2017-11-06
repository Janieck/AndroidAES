package baidu.com.androidaes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private TextView mTvResult;
    private static final String content = "我今天中了500亿大奖";
    private static final String password = "123456";
    private String mEncrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mTvResult = (TextView) findViewById(R.id.tv_result);
    }

    public void md5(View view) {
        //参考Eclipse 消息摘要实例代码
        String password = "我今天中了500亿大奖";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //对什么内容加密
            byte[] digest = md5.digest(password.getBytes());
            //byte ->16进制字符串
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : digest) {
                int value = b;//10->a
                String p = Integer.toHexString(value);
                stringBuilder.append(p);
            }
            mTvResult.setText(stringBuilder.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private boolean isAes;

    //应用场景:希望加密后可以还原,比如商品价格,名称,sqlite加密
    public void aes(View view) {
        //decode:解码,encode;编码
        if (!isAes) {
            //加密
            mEncrypt = Aes.encrypt(content, password);
            mTvResult.setText("AES加密:" + mEncrypt);
        } else {
            //解密
            String decrypt = Aes.decrypt(mEncrypt, password);
            mTvResult.setText("AES解密:" + decrypt);
        }
        isAes = !isAes;
    }
}
