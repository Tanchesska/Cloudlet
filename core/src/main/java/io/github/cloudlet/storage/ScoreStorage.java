package io.github.cloudlet.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class ScoreStorage {
    private static final String FILE_NAME = "best_score.txt";
    private static final String PASSWORD = "cloudlet-super-secret-key-2026";
    private static final int TAG_LENGTH = 128;

    public static void saveBestScore(int score) {
        try {
            byte[] iv = new byte[12];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(), new GCMParameterSpec(TAG_LENGTH, iv));
            byte[] ciphertext = cipher.doFinal(ByteBuffer.allocate(4).putInt(score).array());

            ByteBuffer out = ByteBuffer.allocate(iv.length + ciphertext.length);
            out.put(iv).put(ciphertext);

            Gdx.files.local(FILE_NAME).writeString(new String(Base64Coder.encode(out.array())), false);
        } catch (Exception e) {
            Gdx.app.log("ScoreStorage", "Save failed: " + e.getMessage());
        }
    }

    public static int loadBestScore() {
        try {
            FileHandle file = Gdx.files.local(FILE_NAME);
            if (file.exists()) {
                String content = file.readString().trim();

                byte[] data = Base64Coder.decode(content);
                byte[] iv = new byte[12];
                System.arraycopy(data, 0, iv, 0, 12);

                byte[] ciphertext = new byte[data.length - 12];
                System.arraycopy(data, 12, ciphertext, 0, ciphertext.length);

                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                cipher.init(Cipher.DECRYPT_MODE, getKey(), new GCMParameterSpec(TAG_LENGTH, iv));

                byte[] plaintext = cipher.doFinal(ciphertext);
                return ByteBuffer.wrap(plaintext).getInt();
            }
        } catch (Exception e) {
            Gdx.app.log("ScoreStorage", "Old or corrupted file format. Resetting score to 0.");
            saveBestScore(0);
        }
        return 0;
    }

    private static SecretKey getKey() throws Exception {
        byte[] keyBytes = MessageDigest.getInstance("SHA-256")
            .digest(PASSWORD.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "AES");
    }
}
