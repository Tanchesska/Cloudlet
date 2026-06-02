package io.github.cloudlet.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import io.github.cloudlet.achievement.AchievementType;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.EnumSet;
import java.util.Set;

public class AchievementStorage {
    private static final String FILE_NAME = "achievements.txt";
    private static final String PASSWORD  = "cloudlet-super-secret-key-2026";
    private static final int    TAG_LEN   = 128;

    public static void saveUnlocked(Set<AchievementType> unlocked) {
        int mask = 0;
        for (AchievementType a : unlocked) {
            mask |= (1 << a.ordinal());
        }
        try {
            byte[] iv = new byte[12];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(), new GCMParameterSpec(TAG_LEN, iv));
            byte[] ciphertext = cipher.doFinal(ByteBuffer.allocate(4).putInt(mask).array());

            ByteBuffer out = ByteBuffer.allocate(iv.length + ciphertext.length);
            out.put(iv).put(ciphertext);

            Gdx.files.local(FILE_NAME)
                .writeString(new String(Base64Coder.encode(out.array())), false);
        } catch (Exception e) {
            Gdx.app.log("AchievementStorage", "Save failed: " + e.getMessage());
        }
    }

    public static Set<AchievementType> loadUnlocked() {
        Set<AchievementType> result = EnumSet.noneOf(AchievementType.class);
        try {
            FileHandle file = Gdx.files.local(FILE_NAME);
            if (!file.exists()) return result;

            byte[] data       = Base64Coder.decode(file.readString().trim());
            byte[] iv         = new byte[12];
            System.arraycopy(data, 0, iv, 0, 12);
            byte[] ciphertext = new byte[data.length - 12];
            System.arraycopy(data, 12, ciphertext, 0, ciphertext.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, getKey(), new GCMParameterSpec(TAG_LEN, iv));
            int mask = ByteBuffer.wrap(cipher.doFinal(ciphertext)).getInt();

            for (AchievementType a : AchievementType.values()) {
                if ((mask & (1 << a.ordinal())) != 0) result.add(a);
            }
        } catch (Exception e) {
            Gdx.app.log("AchievementStorage", "Load failed: " + e.getMessage());
        }
        return result;
    }

    private static SecretKey getKey() throws Exception {
        byte[] keyBytes = MessageDigest.getInstance("SHA-256")
            .digest(PASSWORD.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "AES");
    }
}
