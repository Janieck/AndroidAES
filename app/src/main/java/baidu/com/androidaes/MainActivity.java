package baidu.com.androidaes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mTvResult;
    private static final String content = "我今天中了500亿大奖";
    private static final String password = "123456";
    private String mEncrypt;
    private String mPublicKey;
    private String mPrivateKey;
    private byte[] mEncryptByPrivateKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRsaKeyPair();//生成RSA密钥对

    }

    private void initRsaKeyPair() {
        try {
            Map<String, Object> map = RSACrypt.genKeyPair();
            mPublicKey = RSACrypt.getPublicKey(map);
            mPrivateKey = RSACrypt.getPrivateKey(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    //密钥对:公钥和私钥,不能自定手动设置,必须由程序生成(支付宝支付提供工具,代码生成)
    //公钥加密,私钥解密;私钥加密,公钥解密
    //加密速度慢,不能用于加密大文件
    //公钥可以保留,私钥自己保留
    //应用场景:支付宝支付参数传递的安全
    //最高级的功能,数字签名,为了校验私钥在哪里(校验所属关系)
    //安全是相对的
    private boolean isRsa;

    public void rsa(View view) {
        if (!isRsa) {
            //加密
            try {
                mEncryptByPrivateKey = RSACrypt.encryptByPrivateKey(content.getBytes(), mPrivateKey);
                mTvResult.setText("RSA加密:" + RSACrypt.encode(mEncryptByPrivateKey));
            } catch (Exception e) {

            }
        } else {
            //解密
            try {
                byte[] bytes = RSACrypt.decryptByPublicKey(mEncryptByPrivateKey, mPublicKey);
                mTvResult.setText("RSA加密:" + new String(bytes));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        isRsa = !isRsa;
    }
}
