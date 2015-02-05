package mobisocial.stellarwallet;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

import com.lambdaworks.crypto.SCrypt;

public class WalletUtils {

	private static final int SCRYPT_N = (int) Math.pow(2, 11);
	private static final int SCRYPT_R = 8;
	private static final int SCRYPT_P = 1;
	private static final int SCRYPT_SIZE = 256;

	public static String deriveWalletId(String username, String password) {
		return "dummy";
	}

	public static byte[] deriveKey(String id, String username, String password)
			throws GeneralSecurityException, UnsupportedEncodingException {
		String credentials = id + username.toLowerCase() + password;
		// TODO Check this with stellar team about choosing salt
		byte[] salt = credentials.getBytes("UTF-8");
		return SCrypt.scrypt(salt, salt, SCRYPT_N, SCRYPT_R, SCRYPT_P,
				SCRYPT_SIZE);
	}

	public static JSONObject decryptData(String encryptedData, byte[] key)
			throws UnsupportedEncodingException, JSONException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		// Parse the base64 encoded JSON object.
		byte[] data = Base64.decode(encryptedData, Base64.DEFAULT);
		String text = new String(data, "UTF-8");
		JSONObject resultObject = new JSONObject(text);

		// Extract the cipher text from the encrypted data.
		byte[] rawCipherText = Base64.decode(
				resultObject.getString("cipherText"), Base64.DEFAULT);
		byte[] rawIV = Base64.decode(resultObject.getString("IV"),
				Base64.DEFAULT);
		byte[] cipherNameData = Base64.decode(
				resultObject.getString("cipherName"), Base64.DEFAULT);
		String cipherName = new String(cipherNameData, "UTF-8");
		byte[] modeData = Base64.decode(resultObject.getString("mode"),
				Base64.DEFAULT);
		String mode = new String(modeData, "UTF-8");

		Cipher cipher = Cipher.getInstance("AES");
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(rawIV);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] rawData = cipher.doFinal(rawCipherText);
		return new JSONObject(new String(rawData, "UTF-8"));
	}

	public static void main(String[] args) {
		System.out.println(deriveWalletId("", ""));
	}

}
