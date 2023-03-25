package com.zomuhtech.cn.features.advft;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class AppSignHelper extends ContextWrapper {
    public static final String TAG = AppSignHelper.class.getSimpleName();
    private static final String HASH_TYPE = "SHA-256";
    public static final int NUM_HASHED_BYTES = 9;
    public static final int NUM_BASE64_CHAR = 11;

    public AppSignHelper(Context context) {
        super(context);
    }

    /*
     * Get all the app signatures for current package
     *
     * @return
     * */

    public ArrayList<String> getAppSignatues() {
        ArrayList<String> appCodes = new ArrayList<>();

        try {

            String pkgName = getPackageName();
            PackageManager pkgManager = getPackageManager();

            Signature[] signatures = pkgManager.getPackageInfo(pkgName,
                    PackageManager.GET_SIGNATURES).signatures;

            //for each signature create a compatible hash
            for (Signature signature : signatures) {
                String hash = hash(pkgName, signature.toCharsString());

                if (hash != null) {
                    appCodes.add(String.format("%s", hash));
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("PkgErr ", "Unable to find package to obtain hash");
        }

        return appCodes;
    }

    private static String hash(String pkgName, String signature) {
        String appInfo = pkgName + " " + signature;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_TYPE);

            messageDigest.update(appInfo.getBytes(StandardCharsets.UTF_8));

            byte[] hashSign = messageDigest.digest();

            //truncated into NUM_HASHED_BYTES
            hashSign = Arrays.copyOfRange(hashSign, 0, NUM_HASHED_BYTES);

            //Encode to base64
            String base64Hash = Base64.encodeToString(hashSign, Base64.NO_PADDING | Base64.NO_WRAP);

            base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR);

            Log.e("SMS Sample Test ", String.format("pkg: %s -- hash: %s", pkgName, base64Hash));

            return base64Hash;
        } catch (NoSuchAlgorithmException e) {
            Log.v(TAG + "SMS Sample Test", "hash:NoSuchAlgorithm", e);
        }
        return null;
    }
}
